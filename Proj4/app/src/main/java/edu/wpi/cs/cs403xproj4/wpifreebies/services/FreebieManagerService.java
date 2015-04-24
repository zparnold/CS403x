package edu.wpi.cs.cs403xproj4.wpifreebies.services;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.LinkedList;
import java.util.List;

import edu.wpi.cs.cs403xproj4.wpifreebies.models.Category;

/**
 * Created by Tyler on 4/24/2015.
 */
public class FreebieManagerService extends ManageModelService {
    private final static String FREEBIE_API_URL = "http://cs403x-final-host.herokuapp.com/api/freebies/";

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    LinkedList<Category> categories = new LinkedList<>();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        FreebieManagerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return FreebieManagerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public List<Category> getFreebies() {
        return categories;
    }
}
