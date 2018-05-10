package com.example.shesha.tourpal;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    public ProgressDialog progressDialog;
    private int flag=0;
    private String email;

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
        extras = getIntent().getExtras();
         cityname = extras.getString("City Name");
         email = extras.getString("email");
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


                flag = 1;
                mMap.clear();
                String URL = getURL(latitude, longitude, cityname);
                datatransfer[0] = mMap;
                datatransfer[1] = URL;


                getNearbyPlacesData = new GetNearbyPlacesData(MapsActivity.this);
                getNearbyPlacesData.execute(datatransfer);




                break;
            case R.id.itineraryID:
                if(flag==0){
                    Toast.makeText(MapsActivity.this,"Please Search for Nearby Places First",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(MapsActivity.this, CityDetails.class);

                    intent.putExtra("City", cityname);
                    intent.putExtra("Place List", getNearbyPlacesData.names);
                    intent.putExtra("Size", String.valueOf(getNearbyPlacesData.n));
                    intent.putExtra("email",email);
                    startActivity(intent);
                }


                break;

        }

    }

    private String getURL(double latitude, double longitude, String nearbyPlace){

        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+nearbyPlace+"+tourist+places+&language=en&radius=10000&key=AIzaSyDrBnWvFcPXlfot0WOTfD6v3ZjUJyC5fjI";
        return url;

    }
    private static class GetNearbyPlacesData extends AsyncTask<Object,String,String>{
        String googleplacesdata;
        GoogleMap mMap;
        String url;
        DataParser parser = new DataParser();
        double distancematrix[][];
        String parsedDistance;
        String response;
        double cost=0.0;
        int n;
        int completed[],completed2[];
        int z=0;
        String distance2;
        String names[];
        private ProgressDialog progressDialog;
        private Activity activity;

        public GetNearbyPlacesData(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected String doInBackground(Object... objects) {
            mMap = (GoogleMap) objects[0];
            url = (String) objects[1];
            DownloadUrl downloadURL = new DownloadUrl();
            googleplacesdata = downloadURL.readurl(url);

            return googleplacesdata;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setTitle("Please Wait");
            progressDialog.setMessage("Showing Tourist Locations");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            List<HashMap<String,String>> nearbyplaceList = null;
            nearbyplaceList = parser.parse(s);
            showNearbyPlaces(nearbyplaceList);
            mapData(nearbyplaceList);
            progressDialog.dismiss();

        }

        private void mapData(List<HashMap<String, String>> nearbyplaceList) {
            n=nearbyplaceList.size();


            completed=new int[n+2];    //won't give index out of bound exception if we save some extra space
            completed2=new int[n+2];

            distancematrix = new double[nearbyplaceList.size()][nearbyplaceList.size()];
            names = new String[n+2];

            for(int i = 0; i<nearbyplaceList.size(); i++) {
                HashMap<String,String> googleplace = nearbyplaceList.get(i);
                Iterator iterator = googleplace.entrySet().iterator();
                Map.Entry pair = (Map.Entry) iterator.next();
                names[i] = googleplace.get(pair.getKey());



                double lat = Double.parseDouble(googleplace.get("lat"));
                double lng = Double.parseDouble(googleplace.get("lng"));
                for(int j=0; j<nearbyplaceList.size(); j++){
                    HashMap<String,String> gplace = nearbyplaceList.get(j);
                    double lat1 = Double.parseDouble(gplace.get("lat"));
                    double lng1 = Double.parseDouble(gplace.get("lng"));
                    String parsedDistance = getDistance(lat, lng, lat1, lng1);

                        distancematrix[i][j] = Double.valueOf(parsedDistance).doubleValue();
                        //Log.d("Distance",String.valueOf(distancematrix[i][j]));


                }


            }
            for (int i = 0; i < n; i++) {
                completed[i]=0;
                completed2[i]=0;
            }

            mincost(0);
             for(int i = 0;i<n;i++){
            Log.d("Place name",names[i]);
             Log.d("Reorder",names[completed2[i]]);
             }


        }

        private void mincost(int city) {
            completed[city]=1;
            completed2[z]=city;

            Log.d("Route",String.valueOf(completed2[z]));   //completed2[] holds the order in which places should be visited
            z++;
            int i,ncity;
            ncity=least(city);
            if(ncity==999)
            {
                ncity=0;

                cost+=distancematrix[city][ncity];

                return;
            }
            mincost(ncity);

        }

        private int least(int c) {
            int i,nc=999;
            double min=999,kmin=0.0;

            for(i=0;i < n;i++)
            {
                if((distancematrix[c][i]!=0)&&(completed[i]==0))
                    if(distancematrix[c][i]+distancematrix[i][c] < min)
                    {
                        min=distancematrix[i][0]+distancematrix[c][i];
                        kmin=distancematrix[c][i];
                        nc=i;
                    }
            }

            if(min!=999)
                cost+=kmin;

            return nc;

        }


        private String getDistance(final double lat, final double lng, final double lat1, final double lng1) {



            Thread thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    try {


                        URL url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + lat + "," + lng + "&destination=" + lat1 + "," + lng1 + "&sensor=false&units=metric&mode=driving");
                        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        InputStream in = new BufferedInputStream(conn.getInputStream());
                        response = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
                        Log.d("Map Route",response);

                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray("routes");
                        JSONObject routes = array.getJSONObject(0);
                        JSONArray legs = routes.getJSONArray("legs");
                        JSONObject steps = legs.getJSONObject(0);
                        JSONObject distance = steps.getJSONObject("distance");
                        parsedDistance=distance.getString("text");
                        char ch = parsedDistance.charAt(0);
                        distance2 = parsedDistance.substring(parsedDistance.indexOf(ch),parsedDistance.indexOf(" "));
                        Log.d("Distance",distance2);





                        // Log.d("numberOnly",numberOnly);
                        //Log.d("Distance",parsedDistance);

                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return distance2;

        }


        private void showNearbyPlaces(List<HashMap<String,String>> nearbyPlaceList){
            for(int i = 0; i<nearbyPlaceList.size(); i++){
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String,String> googleplace = nearbyPlaceList.get(i);
                String placename = googleplace.get("place_name");
                String vicinity = googleplace.get("vicinity");
                double lat = Double.parseDouble(googleplace.get("lat"));
                double lng = Double.parseDouble(googleplace.get("lng"));
                LatLng latLng = new LatLng(lat,lng);
                markerOptions.position(latLng);
                markerOptions.title(placename + ":" +vicinity);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));

            }
        }


    }




}
