package edu.wpi.cs.cs403xproj4.wpifreebies.services;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import edu.wpi.cs.cs403xproj4.wpifreebies.models.Category;

/**
 * Created by Tyler on 4/24/2015.
 */
public class CategoryManagerService extends ManageModelService {
    private final static String TAG = "Categories";
    private final static String CATEGORY_API_URL = "http://cs403x-final-host.herokuapp.com/api/categories/";

    // Binder given to clients
    private final IBinder mBinder = new CategoryManagerBinder();
    private boolean initialized = false;
    LinkedList<Category> categories = new LinkedList<>();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class CategoryManagerBinder extends Binder {
        public CategoryManagerService getService() {
            // Return this instance so clients can call public methods
            return CategoryManagerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private List<Category> initialize() {
        String result = this.doGet(CATEGORY_API_URL);
        Log.d(TAG, result);
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i=0; i < jArray.length(); i++) {

                JSONObject jObject = jArray.getJSONObject(i);

                String _id = jObject.getString("_id");
                String name = jObject.getString("name");
                String color = jObject.getString("color");

                categories.add(new Category(_id, name, color));
            }
        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
        }

        return categories;
    }

    public List<Category> getCategories() {
        if (initialized) {
            return categories;
        } else {
            return this.initialize();
        }
    }
}
