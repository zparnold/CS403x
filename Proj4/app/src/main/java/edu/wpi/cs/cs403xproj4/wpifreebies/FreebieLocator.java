package edu.wpi.cs.cs403xproj4.wpifreebies;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

import edu.wpi.cs.cs403xproj4.wpifreebies.models.Freebie;
import edu.wpi.cs.cs403xproj4.wpifreebies.services.FreebieListener;
import edu.wpi.cs.cs403xproj4.wpifreebies.services.FreebieManager;

public class FreebieLocator extends FragmentActivity implements FreebieListener, LocationListener {
    private static final String TAG = "WPIFreebiesMain";

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private boolean firstLocationUpdate = false;
    private HashMap<Marker, Freebie> freebieMarkerMap = new HashMap<>();

    public final static String EXTRA_MESSAGE = "edu.wpi.cs.cs403xproj4.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freebie_locator);
        FreebieManager.getInstance().addListener(this);
        initializeInfoTab();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        initializeInfoTab();
    }

    @Override
    public void onFreebieUpdate() {

        mMap.clear();
        freebieMarkerMap.clear();

        LinkedList<Freebie> freebies = (LinkedList<Freebie>) FreebieManager.getInstance().getFreebies();
        for (int i = 0; i < freebies.size(); i++) {
            Freebie currentFreebie = freebies.get(i);
            MarkerOptions options = new MarkerOptions();

            options.title(currentFreebie.getName());
            options.position(new LatLng(currentFreebie.getLatitude(), currentFreebie.getLongitude()));
            Marker currentMarker = mMap.addMarker(options);

            freebieMarkerMap.put(currentMarker, currentFreebie);
        }
    }

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
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                freebie = freebieMarkerMap.get(marker);
                updateInfoTab();
                return true;
            }
        });
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
        locationManager.requestLocationUpdates(provider,30000,0, this);
    }
    @Override
    public void onLocationChanged(Location location) {
        if (!(firstLocationUpdate)) {
            // Getting latitude of the current location
            double latitude = location.getLatitude();
            // Getting longitude of the current location
            double longitude = location.getLongitude();
            // Creating a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);
            // Showing the current location in Google Map
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            // Zoom in the Google Map
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
            firstLocationUpdate = true;
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        setUpMapIfNeeded();
    }

    @Override
    public void onProviderDisabled(String s) {}

    public void initializeInfoTab(){
        if(!FreebieManager.getInstance().getFreebies().isEmpty() && freebie == null) {
            freebie = FreebieManager.getInstance().getFreebies().get(0);
            updateInfoTab();
        }
        else if (freebie != null) {
            updateInfoTab();
        }
        else {
            ((TextView) findViewById(R.id.info_freebie_title)).setText("No Freebies exist at this time");
            ((TextView) findViewById(R.id.info_freebie_description)).setText("There are no freebie that are currently active. Please try again later.");
            ((TextView) findViewById(R.id.info_freebie_upvotes)).setText("0");
            ((TextView) findViewById(R.id.info_freebie_downvotes)).setText("0");
            ((TextView) findViewById(R.id.info_freebie_date)).setText(getDateAsString());
        }

    }

    private void updateInfoTab() {
        if(freebie == null) { return; }
        ((TextView) findViewById(R.id.info_freebie_title)).setText(freebie.getName());
        ((TextView) findViewById(R.id.info_freebie_description)).setText(freebie.getDescription());
        ((TextView) findViewById(R.id.info_freebie_upvotes)).setText(Integer.toString(freebie.getUpVotes()));
        ((TextView) findViewById(R.id.info_freebie_downvotes)).setText(Integer.toString(freebie.getDownVotes()));
        ((TextView) findViewById(R.id.info_freebie_date)).setText(getDateAsString());
    }

    //upvote the currently selected
    public void upVote(View view) {
        if(freebie == null) {
            Toast.makeText(getApplicationContext(), "There is not a Freebie to upvote", Toast.LENGTH_SHORT).show();
            return;
        }
        FreebieManager.getInstance().upVote(freebie);
        updateInfoTab();
    }
    //downvote the currently selected
    public void downVote(View view) {
        if(freebie == null) {
            Toast.makeText(getApplicationContext(), "There is not a Freebie to downvote", Toast.LENGTH_SHORT).show();
            return;
        }
        FreebieManager.getInstance().downVote(freebie);
        updateInfoTab();
    }

    public String getDateAsString() {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        return df.format(c.getTime());
    }
}
