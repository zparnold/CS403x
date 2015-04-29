package edu.wpi.cs.cs403xproj4.wpifreebies;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Date;

import edu.wpi.cs.cs403xproj4.wpifreebies.models.Category;
import edu.wpi.cs.cs403xproj4.wpifreebies.models.Freebie;


public class CreateFreebie extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_freebie);

        //Setup the freebie spinner
        Spinner spinner = (Spinner) findViewById(R.id.create_freebie_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.freebie_choices, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_freebie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Send the creation data and end the current activity, returning to main screen
    public void sendFreebie(View view) {
        ///TODO Validate form
        //get the freebie from the view and send it
        Date when = new Date();
        String name;
        String description;
        String categoryString;
        String color = "";
        double lat,lng;
        Category category = null;

        if (view != null) {
            TextView nameText = (TextView) view.findViewById(R.id.create_freebie_title);
            name = nameText.getText().toString();
            TextView descText = (TextView) view.findViewById(R.id.editText);
            description = descText.getText().toString();
            Spinner spinner = (Spinner)findViewById(R.id.create_freebie_spinner);
            categoryString = spinner.getSelectedItem().toString();


            switch (categoryString){
                case "Food":
                    color = "RED";
                    break;
                case "Clothing":
                    color = "GREEN";
                    break;
                case "Other":
                    color = "BLUE";
                    break;
                default:
                    break;
            }
            category = new Category(categoryString,color);

            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);
            // Getting Current Location
            Location location = locationManager.getLastKnownLocation(provider);

            lat = location.getLatitude();
            lng = location.getLongitude();

            Freebie mFreebie = new Freebie(name,description,when,lng,lat,0,0,category);

            //send the freebie information here

            //TODO make HTTP request either here, or farm out to new method.
        }





        finish();
    }
}
