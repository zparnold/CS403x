package edu.wpi.cs.cs403xproj4.wpifreebies;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import java.util.Locale;
import javax.xml.datatype.Duration;

import edu.wpi.cs.cs403xproj4.wpifreebies.models.Freebie;
import edu.wpi.cs.cs403xproj4.wpifreebies.services.FreebieListener;
import edu.wpi.cs.cs403xproj4.wpifreebies.services.FreebieManager;

public class FreebieLocator extends FragmentActivity implements FreebieListener, LocationListener {
    private static final String TAG = "WPIFreebiesMain";

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public final static String EXTRA_MESSAGE = "edu.wpi.cs.cs403xproj4.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freebie_locator);
        FreebieManager.getInstance().addListener(this);

        FreebieManager.getInstance().addFreebie(new Freebie("test from phone", "test", 40.0, 40.0));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onFreebieUpdate() {
        Toast.makeText(getApplicationContext(), "FreebieManager retrieved data", Toast.LENGTH_SHORT).show();
        // add all map markers
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
        locationManager.requestLocationUpdates(provider,60000,0, this);
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
}
