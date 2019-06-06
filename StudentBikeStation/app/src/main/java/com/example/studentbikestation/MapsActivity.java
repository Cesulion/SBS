package com.example.studentbikestation;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    final static int PERMISSION_ALL = 1;
    final static String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    private GoogleMap mMap;
    private DatabaseReference ref;
    private FirebaseDatabase database;
    Location deviceLocation;
    MarkerOptions mo;
    Marker deviceMarker;

    private LocationManager deviceLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Put markers and setup database
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("stations");
        setMarkers();

        //Setup device location
        deviceLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted()) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        } else getDeviceLocation();

        if (!isLocationEnabled()) {
            showMessage(0);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng horsens = new LatLng(55.86066, 9.85034);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(horsens, 12.0f));

        /* Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/


    }

    @Override
    public void onLocationChanged(Location location) {
        deviceLocation = location;
        if (deviceMarker != null) {
            deviceMarker.remove();

        }
        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        mo = new MarkerOptions();
        mo.position(latlng);
        mo.title("Current location");
        mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        deviceMarker = mMap.addMarker(mo);
        Log.d("Current position : ", latlng.toString());

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    //This function returns the coordinates from a human-understandable address
    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    //This function is to be called in the onCreate and gets the "location" child property of the stations to create a marker at the location
    private void setMarkers() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String location = ds.child("location").getValue().toString();
                    LatLng markerpos = getLocationFromAddress(getApplicationContext(), location);
                    mMap.addMarker(new MarkerOptions().position(markerpos).title(ds.child("name").getValue().toString()));
                    Log.d("Station :", location);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private void getDeviceLocation() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        String provider = deviceLocationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        deviceLocationManager.requestLocationUpdates(provider, 0, 0, this);

    }
    private boolean isLocationEnabled()
    {
        return deviceLocationManager.isProviderEnabled(deviceLocationManager.GPS_PROVIDER)
                || deviceLocationManager.isProviderEnabled(deviceLocationManager.NETWORK_PROVIDER);
    }

    private boolean isPermissionGranted()
    {
        if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        == PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED)
        {
            Log.d("Status of permission : ", "granted");
            return true;
        }
        else
        {
            Log.d("Status of permission : ", "not granted");
            return false;
        }
    }

    private void showMessage(final int status)
    {
        String message, title, btnText;
        if(status == 0)
        {
            title = "Device location unavailable";
            message = "Please enable location permissions";
            btnText = "Go to device settings";
        }
        else
        {
            title = "Enable permission access";
            message = "Click to allow the app to access your location";
            btnText = "Allow device location";
        }
        final AlertDialog.Builder messageBox = new AlertDialog.Builder(this);
        messageBox.setCancelable(false);
        messageBox.setTitle(title).setMessage(message).setPositiveButton(btnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface diParameters, int paramInt) {
                if (status == 1){
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
                else
                    requestPermissions(PERMISSIONS, PERMISSION_ALL);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
    }



}