package edu.wpi.cs.cs403xproj4.wpifreebies.services;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import edu.wpi.cs.cs403xproj4.wpifreebies.models.Freebie;

/**
 * Created by Tyler on 4/24/2015.
 */
public class FreebieManager {
    private final static String TAG = "FREEBIES/Freebies";
    private final static String FREEBIE_API_URL = "http://cs403x-final-host.herokuapp.com/api/freebies/";

    private boolean initialized = false;
    LinkedList<FreebieListener> listeners = new LinkedList<>();
    LinkedList<Freebie> freebies = new LinkedList<>();

    private final static FreebieManager manager = new FreebieManager();

    public FreebieManager() {
        new GetFreebiesTask().execute(new RequestParams(FREEBIE_API_URL));
    }

    public static FreebieManager getInstance() {
        return manager;
    }

    public void addListener(FreebieListener listener) {
        listeners.add(listener);
    }


    public boolean isInitialized() {
        return initialized;
    }

    public List<Freebie> getFreebies() {
        return freebies;
    }

    public void removeFreebieByID(String id) {
        for (int i = 0; i < freebies.size(); i++) {
            if (freebies.get(i).get_id().equals(id)) {
                freebies.remove(i);
            }
        }
    }

    public Freebie getFreebieByID(String id) {
        for (int i = 0; i < freebies.size(); i++) {
            Freebie current = freebies.get(i);
            if (current.get_id().equals(id)) {
                return current;
            }
        }
    }

    public void refreshFreebies() {
        new GetFreebiesTask().execute(new RequestParams(FREEBIE_API_URL));
    }

    public void addFreebie(Freebie freebie) {
        try {
            BasicNameValuePair params[] = {
                    new BasicNameValuePair("name", freebie.getName()),
                    new BasicNameValuePair("description", freebie.getDescription()),
                    new BasicNameValuePair("latitude", String.valueOf(freebie.getLatitude())),
                    new BasicNameValuePair("longitude", String.valueOf(freebie.getLongitude())),
                    new BasicNameValuePair("color", freebie.getColor())
            };
            UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(Arrays.asList(params));

            new CreateFreebieTask().execute(new RequestParams(FREEBIE_API_URL, reqEntity));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void upVote(Freebie freebie) {
        new VoteFreebieTask().execute(new RequestParams(FREEBIE_API_URL + freebie.get_id() + "/upvote"));
    }

    public void downVote (Freebie freebie) {
        new VoteFreebieTask().execute(new RequestParams(FREEBIE_API_URL + freebie.get_id() + "/downvote"));
    }

    private Freebie parseFreebie(JSONObject jObject) {
        try {
            Freebie newFreebie = new Freebie();

            newFreebie.set_id(jObject.getString("_id"));
            newFreebie.setName(jObject.getString("name"));

            if (jObject.has("description")) {
                newFreebie.setDescription(jObject.getString("description"));
            }

            if (jObject.has("color")) {
                newFreebie.setColor(jObject.getString("color"));
            }

            newFreebie.setUpVotes(jObject.getInt("upVotes"));
            newFreebie.setDownVotes(jObject.getInt("downVotes"));
            newFreebie.setLatitude(jObject.getDouble("latitude"));
            newFreebie.setLongitude(jObject.getDouble("longitude"));

            String postDate = jObject.getString("postDate");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));

            try {
                Date date = format.parse(postDate);
                newFreebie.setPostDate(date);
            } catch (ParseException e) {
                Log.e(TAG, "Could not parse date");
            }

            return newFreebie;
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    private class GetFreebiesTask extends AsyncTask<RequestParams, Void, String> {
        protected String doInBackground(RequestParams... params) {
            String resp = null;
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet getRequest = new HttpGet(params[0].getUrl());
                HttpResponse response = httpClient.execute(getRequest);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String sResponse;
                StringBuilder s = new StringBuilder();
                while ((sResponse = reader.readLine()) != null) {
                    s = s.append(sResponse);
                }

                resp = s.toString();
            } catch (Exception e) {
                Log.e(e.getClass().getName(), e.getMessage());
            }

            return resp;
        }

        protected void onPostExecute(String result) {
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i=0; i < jArray.length(); i++) {

                    JSONObject jObject = jArray.getJSONObject(i);
                    Freebie newFreebie = parseFreebie(jObject);

                    if (newFreebie != null) {
                        freebies.add(newFreebie);
                    }
                }

                initialized = true;
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).onFreebieUpdate();
                }
            } catch (JSONException e) {
                Log.e(TAG, "Error: " + e.toString());
            }
        }
    }

    private class VoteFreebieTask extends AsyncTask<RequestParams, Void, String> {
        protected String doInBackground(RequestParams... params) {
            String resp = null;
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet getRequest = new HttpGet(params[0].getUrl());
                HttpResponse response = httpClient.execute(getRequest);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String sResponse;
                StringBuilder s = new StringBuilder();
                while ((sResponse = reader.readLine()) != null) {
                    s = s.append(sResponse);
                }

                resp = s.toString();
            } catch (Exception e) {
                Log.e(e.getClass().getName(), e.getMessage());
            }

            return resp;
        }

        protected void onPostExecute(String result) {
            try {
                JSONObject successObject = new JSONObject(result);
                if (successObject.has("success")) {
                    JSONObject jObject = successObject.getJSONObject("success");
                    Freebie newFreebie = parseFreebie(jObject);

                    if (newFreebie != null) {
                        removeFreebieByID(newFreebie.get_id());
                        freebies.add(newFreebie);
                    }

                } else if (successObject.has("deleted")) {
                    String id = successObject.getString("deleted");
                    removeFreebieByID(id);
                }

                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).onFreebieUpdate();
                }
            } catch (JSONException e) {
                Log.e(TAG, "Error: " + e.toString());
                Log.e(TAG, result);
            }
        }
    }

    private class CreateFreebieTask extends AsyncTask<RequestParams, Void, String> {
        protected String doInBackground(RequestParams... params) {
            String resp = null;
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost postRequest = new HttpPost(params[0].getUrl());
                postRequest.setEntity(params[0].getFormData());
                postRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
                HttpResponse response = httpClient.execute(postRequest);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String sResponse;
                StringBuilder s = new StringBuilder();
                while ((sResponse = reader.readLine()) != null) {
                    s = s.append(sResponse);
                }

                resp = s.toString();
            } catch (Exception e) {
                Log.e(e.getClass().getName(), e.getMessage());
            }

            return resp;
        }

        protected void onPostExecute(String result) {
            try {
                JSONObject successObject = new JSONObject(result);
                JSONObject jObject = successObject.getJSONObject("success");
                Freebie newFreebie = parseFreebie(jObject);

                if (newFreebie != null) {
                    freebies.add(newFreebie);
                }

                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).onFreebieUpdate();
                }
            } catch (JSONException e) {
                Log.e(TAG, "Error: " + e.toString());
                Log.e(TAG, result);
            }
        }
    }
}
