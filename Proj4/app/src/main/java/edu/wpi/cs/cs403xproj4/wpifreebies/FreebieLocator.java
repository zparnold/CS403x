package edu.wpi.cs.cs403xproj4.wpifreebies;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.wpi.cs.cs403xproj4.wpifreebies.services.CategoryManagerService;
import edu.wpi.cs.cs403xproj4.wpifreebies.services.FreebieManagerService;

public class FreebieLocator extends FragmentActivity {
    private static final String TAG = "WPIFreebiesMain";

    CategoryManagerService categories;
    private boolean categoriesBound = false;

    FreebieManagerService freebies;
    private boolean freebiesBound = false;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public final static String EXTRA_MESSAGE = "edu.wpi.cs.cs403xproj4.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freebie_locator);
        Intent intent = new Intent(this, CategoryManagerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        Log.v(TAG, "connected to category service: " + categoriesBound);

        setUpMapIfNeeded();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "connected to category service: " + categoriesBound);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (categoriesBound) {
            unbindService(mConnection);
            categoriesBound = false;
        }
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            CategoryManagerService.CategoryManagerBinder binder = (CategoryManagerService.CategoryManagerBinder) service;
            categories = binder.getService();
            categoriesBound = true;
            Toast.makeText(FreebieLocator.this, "Connected", Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            categoriesBound = false;
        }
    };

    /**
     * Start the "create a freebie" activity
     */
    public void startFreebieMaker(View view) {
        Intent intent = new Intent(this, CreateFreebie.class);
        //get lattitude and longitude
        //loc
        //intent.putExtra(EXTRA_MESSAGE, loc);
        startActivity(intent);
    }
}
