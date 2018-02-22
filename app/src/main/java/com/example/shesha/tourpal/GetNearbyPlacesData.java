package com.example.shesha.tourpal;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
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
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shesha on 15-02-2018.
 */

public class GetNearbyPlacesData extends AsyncTask<Object,String,String> {
    String googleplacesdata;
    GoogleMap mMap;
    String url;
    DataParser parser = new DataParser();
    private double distancematrix[][];
    String parsedDistance;
    String response;

    String distance2;

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];
        DownloadUrl downloadURL = new DownloadUrl();
        googleplacesdata = downloadURL.readurl(url);
        return googleplacesdata;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String,String>> nearbyplaceList = null;
        nearbyplaceList = parser.parse(s);
        showNearbyPlaces(nearbyplaceList);
        mapData(nearbyplaceList);

    }

    private void mapData(List<HashMap<String, String>> nearbyplaceList) {
        distancematrix = new double[nearbyplaceList.size()][nearbyplaceList.size()];

        for(int i = 0; i<nearbyplaceList.size(); i++) {
            HashMap<String,String> googleplace = nearbyplaceList.get(i);
            double lat = Double.parseDouble(googleplace.get("lat"));
            double lng = Double.parseDouble(googleplace.get("lng"));
            for(int j=0; j<nearbyplaceList.size(); j++){
                HashMap<String,String> gplace = nearbyplaceList.get(j);
                double lat1 = Double.parseDouble(gplace.get("lat"));
                double lng1 = Double.parseDouble(gplace.get("lng"));
                String parsedDistance = getDistance(lat, lng, lat1, lng1);
                distancematrix[i][j] = Double.valueOf(parsedDistance).doubleValue();
                Log.d("Distance",String.valueOf(distancematrix[i][j]));

            }

        }
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


