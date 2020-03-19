package com.fynd.ardemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LandActivity extends AppCompatActivity {

    // Declarations.
    private FusedLocationProviderClient fusedLocationProviderClient ;

    ListView listView;
    TextView locationTextView;
    Button getlocationButton;
    Geocoder geocoder;
    ImageView pinIcon;
    TextView restaurantName;
    List<Address> addresses;

    Double demoLatitude = 22.828782;
    Double demoLongitude = 75.943789;
    Double currentLatitude = 0.0;
    Double currentLongitude = 0.0;
    String fullAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land);

        // Request location on app install and first startup.
        requestLocationPermission();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        listView = (ListView) findViewById(R.id.listView);
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        getlocationButton = (Button) findViewById(R.id.getLocationButton);
        pinIcon =  (ImageView)findViewById(R.id.pinIcon);
        restaurantName = (TextView) findViewById(R.id.restaurantNameTextView);

        geocoder = new Geocoder(this, Locale.getDefault());

        // Adapter for the listView.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(LandActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.menu_items));

        // Set the listView adapter and set it's visibility to GONE.
        listView.setAdapter(adapter);
        listView.setVisibility(View.GONE);
        restaurantName.setVisibility(View.GONE);
        pinIcon.setVisibility(View.GONE);

        // Button to start the location retrieval.
        getlocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return if permission not granted.
                if(ActivityCompat.checkSelfPermission(LandActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    return;
                }
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(LandActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null){
                            // Get address from actual coordinates provided by the Geocoder.
                            try {
                                String locationString = location.toString();
                                // Convert latitude and longitude substrings into double.
                                currentLatitude =  Double.valueOf(locationString.substring(15, 23));
                                currentLongitude = Double.valueOf(locationString.substring(25, 30));
                                addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1);
                                fullAddress = addresses.get(0).getAddressLine(0);
                                pinIcon.setVisibility(View.VISIBLE);
                                locationTextView.setText(fullAddress);
                                restaurantName.setVisibility(View.VISIBLE);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                listView.setVisibility(View.VISIBLE);
                getlocationButton.setText(R.string.againFindPrompt);
            }
        });

        // Set the onItemClick to start the corresponding activity.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

                String selectedItem = (String) parent.getItemAtPosition(position);
                if(selectedItem.equals("Pizza")){
                    Intent intentPizza = new Intent(LandActivity.this, AugmentedActivity.class);
                    startActivity(intentPizza);
                }
                if(selectedItem.equals("Coffee")) {
                    Intent intentCoffee = new Intent(LandActivity.this, AugmentedActivityCoffee.class);
                    startActivity(intentCoffee);
                }
                if(selectedItem.equals("Burger")) {
                    Intent intentBurger = new Intent(LandActivity.this, AugmentedActivityBurger.class);
                    startActivity(intentBurger);
                }
                if(selectedItem.equals("Pasta")){
                    Intent intentPasta = new Intent(LandActivity.this, AugmentedActivityPasta.class);
                    startActivity(intentPasta);
                }
                else{
                    Toast.makeText(LandActivity.this, "Item not yet ready", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Location access method.
    private void requestLocationPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }
}
