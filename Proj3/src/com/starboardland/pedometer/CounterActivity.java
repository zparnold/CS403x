package com.starboardland.pedometer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.*;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class CounterActivity extends FragmentActivity implements SensorEventListener,LocationListener {

    private SensorManager sensorManager;
    private TextView countTotal;
    private TextView minCount;
    private TextView[] countSegment = new TextView[8];
    private int countSegmentVal = 0;
    private int countTotalVal = 0;

    boolean countRunning;
    boolean activityRunning;
    private int minuteCount;
    private static final int ONE_MINUTE = 60000;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private Handler resetHandler;
    private SQLiteDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        resetHandler = new Handler();

        //initialize text views
        countSegment[0] = (TextView) findViewById(R.id.count1);
        countSegment[1] = (TextView) findViewById(R.id.count2);
        countSegment[2] = (TextView) findViewById(R.id.count3);
        countSegment[3] = (TextView) findViewById(R.id.count4);
        countSegment[4] = (TextView) findViewById(R.id.count5);
        countSegment[5] = (TextView) findViewById(R.id.count6);
        countSegment[6] = (TextView) findViewById(R.id.count7);
        countSegment[7] = (TextView) findViewById(R.id.count8);
        countTotal = (TextView) findViewById(R.id.countTotalText);

        //setup new recurring task each minute
        setMinuteCount(0);
        countRunning = true;
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (countRunning) {
                    if (minuteCount == 0) {
                        minuteCount++;
                    } else {
                        resetHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                displayAndReset();
                            }
                        });
                    }

                    if (minuteCount >= 8) {
                        countRunning = false;
                    }
                }
            }
        },new Date(),ONE_MINUTE);

        //setMinCount((TextView) findViewById(R.id.minCount));
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        setUpMapIfNeeded();

        db = openOrCreateDatabase("StepCountDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS step_count(minutes int NOT NULL, steps int, PRIMARY KEY (minutes))");
        db.execSQL("INSERT OR REPLACE INTO step_count (minutes, steps) VALUES (1,0)");
        db.execSQL("INSERT OR REPLACE INTO step_count (minutes, steps) VALUES (2,0)");
        db.execSQL("INSERT OR REPLACE INTO step_count (minutes, steps) VALUES (3,0)");
        db.execSQL("INSERT OR REPLACE INTO step_count (minutes, steps) VALUES (4,0)");
        db.execSQL("INSERT OR REPLACE INTO step_count (minutes, steps) VALUES (5,0)");
        db.execSQL("INSERT OR REPLACE INTO step_count (minutes, steps) VALUES (6,0)");
        db.execSQL("INSERT OR REPLACE INTO step_count (minutes, steps) VALUES (7,0)");
        db.execSQL("INSERT OR REPLACE INTO step_count (minutes, steps) VALUES (8,0)");
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(getApplicationContext(), "Count sensor not available!", Toast.LENGTH_LONG).show();
        }
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link com.google.android.gms.maps.SupportMapFragment} (and
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
     * mMap.addMarker(new MarkerOptions().position(new LatLng(mMap.getMyLocation().getLatitude(),
     mMap.getMyLocation().getLongitude())).title("Marker"));
     */
    protected void setUpMap() {
        mMap.setMyLocationEnabled(true);
        // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);

        if(location!=null){
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(provider,60000,0,this);
    }
    @Override
    public void onLocationChanged(Location location) {

        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Showing the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        setUpMapIfNeeded();
    }

    @Override
    public void onProviderDisabled(String s) {

    }


    @Override
    protected void onPause() {
        super.onPause();
        activityRunning = false;
        // if you unregister the last listener, the hardware will stop detecting step events
//        sensorManager.unregisterListener(this); 
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (activityRunning && countRunning) {
            countSegmentVal += 1;
            countTotalVal += 1;

            countSegment[minuteCount - 1].setText(String.valueOf(countSegmentVal));
            countTotal.setText(String.valueOf(countTotalVal));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public int getMinuteCount() {
        return minuteCount;
    }

    public void setMinuteCount(int minuteCount) {
        this.minuteCount = minuteCount;
    }

    public TextView getMinCount() {
        return minCount;
    }

    public void setMinCount(TextView minCount) {
        this.minCount = minCount;
    }

    public void displayAndReset() {
        Toast.makeText(getApplicationContext(), "You took " + String.valueOf(countSegmentVal) + " steps in Segment " + String.valueOf(minuteCount), Toast.LENGTH_LONG).show();
        db.execSQL("UPDATE step_count SET steps=" + countSegmentVal + " WHERE minutes=" + minuteCount);
        minuteCount += 1;
        countSegmentVal = 0;
    }
}
