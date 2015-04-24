package edu.wpi.cs.cs403xproj4.wpifreebies.services;

import android.app.Service;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Tyler on 4/24/2015.
 */
public abstract class ManageModelService extends Service {
    public String doGet(String request) {
        String resp = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(request);
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

    public String doPost(String request, MultipartEntity formData) {
        String resp = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(request);
//            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//            reqEntity.addPart("uploaded", bab);
//            reqEntity.addPart("photoCaption", new StringBody("sfsdfsdf"));
            postRequest.setEntity(formData);
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

    public String doPut(String request, MultipartEntity formData) {
        String resp = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPut putRequest = new HttpPut(request);
//            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//            reqEntity.addPart("uploaded", bab);
//            reqEntity.addPart("photoCaption", new StringBody("sfsdfsdf"));
            putRequest.setEntity(formData);
            HttpResponse response = httpClient.execute(putRequest);
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

    public String doDelete(String request) {
        String resp = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPut deleteRequest = new HttpPut(request);
            HttpResponse response = httpClient.execute(deleteRequest);
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
}
