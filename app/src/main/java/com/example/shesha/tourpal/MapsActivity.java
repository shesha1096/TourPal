package com.example.shesha.tourpal;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,View.OnClickListener {

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastlocation;
    private Marker currentlocationmarker;
    public static final int PERMISSION_REQUEST_LOCATION_CODE = 99;
    private Button searchBtn;
   // private EditText searchtext;
    Object datatransfer[] = new Object[2];
    GetNearbyPlacesData getNearbyPlacesData;
    private Button confirmBtn;
    private Bundle extras;
    private String cityname;
    int PROXIMITY_RADIUS = 10000;
    double latitude,longitude;
    double end_latitude, end_longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        searchBtn = (Button) findViewById(R.id.B_search);
        confirmBtn = (Button) findViewById(R.id.itineraryID);
        //searchtext = (EditText) findViewById(R.id.TF_location);
        extras = getIntent().getExtras();
         cityname = extras.getString("City Name");
        searchBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (client == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


    }

    protected synchronized void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        client.connect();

    }

    @Override
    public void onLocationChanged(Location location) {
        lastlocation = location;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        if (currentlocationmarker != null) {
            currentlocationmarker.remove();

        }
        LatLng latLng = new LatLng(latitude,longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentlocationmarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        if (client != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);

        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION_CODE);
            }
            return false;
        } else
            return true;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.B_search:



                mMap.clear();
                String URL = getURL(latitude, longitude, cityname);
                datatransfer[0] = mMap;
                datatransfer[1] = URL;

                getNearbyPlacesData = new GetNearbyPlacesData();
                getNearbyPlacesData.execute(datatransfer);
                Toast.makeText(MapsActivity.this, "Showing Tourist Locations", Toast.LENGTH_SHORT).show();
                break;
            case R.id.itineraryID:
                Intent intent = new Intent(MapsActivity.this,CityDetails.class);
                intent.putExtra("City",cityname);
                startActivity(intent);


                break;
           /* case  R.id.B_hopistals:
                mMap.clear();
                String hostpital = "hospital";
                String URL =  getURL(latitude,longitude,hostpital);
                datatransfer[0] = mMap;
                datatransfer[1] = URL;
                getNearbyPlacesData = new GetNearbyPlacesData();
                getNearbyPlacesData.execute(datatransfer);
                Toast.makeText(MapsActivity.this,"Showing Nearby Hospitals",Toast.LENGTH_SHORT).show();
                break;

            case R.id.B_restaurants :
                mMap.clear();
                String restaurant = "restaurant";
                URL =  getURL(latitude,longitude,restaurant);

                datatransfer[0] = mMap;
                datatransfer[1] = URL;
                getNearbyPlacesData = new GetNearbyPlacesData();
                getNearbyPlacesData.execute(datatransfer);
                Toast.makeText(MapsActivity.this,"Showing Nearby Restaurants",Toast.LENGTH_SHORT).show();
                break;
            case  R.id.B_schools:
                mMap.clear();
                String school = "school";
                URL =  getURL(latitude,longitude,school);

                datatransfer[0] = mMap;
                datatransfer[1] = URL;
                getNearbyPlacesData = new GetNearbyPlacesData();
                getNearbyPlacesData.execute(datatransfer);
                Toast.makeText(MapsActivity.this,"Showing Nearby Schools",Toast.LENGTH_SHORT).show();
                break;
        }*/
        }

    }

    private String getURL(double latitude, double longitude, String nearbyPlace){

        //StringBuilder googleplaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json");
        //googleplaceUrl.append("?location="+latitude+","+longitude);
        //googleplaceUrl.append("&radius="+PROXIMITY_RADIUS);
        //googleplaceUrl.append("&type="+nearbyPlace);
        //googleplaceUrl.append("&sensor=true");
        //googleplaceUrl.append("&key=AIzaSyDrBnWvFcPXlfot0WOTfD6v3ZjUJyC5fjI");
        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+nearbyPlace+"+point+of+interest&language=en&key=AIzaSyDrBnWvFcPXlfot0WOTfD6v3ZjUJyC5fjI";
        return url;
    }
}
