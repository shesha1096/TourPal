package com.example.shesha.tourpal;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shesha on 15-02-2018.
 */

public class DataParser {
    private HashMap<String,String> getDuration(JSONArray googleDirectionsJson){
        HashMap<String,String> googleDirectionsMap = new HashMap<>();
        String duration = "";
        String distance = "";
        try {
            //duration = googleDirectionsJson.getJSONObject(0).getJSONObject("duration").getString("text");
            distance = googleDirectionsJson.getJSONObject(0).getJSONObject("distance").getString("text");
            Log.d("Distance",distance);
            googleDirectionsMap.put("Duration","duration");
            googleDirectionsMap.put("Distance",distance);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return googleDirectionsMap;

    }

    private HashMap<String,String> getPlace(JSONObject googlePlaceJSON){
        HashMap<String,String> googleplacemap = new HashMap<>();
        String placename = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";
        try{
            if(!googlePlaceJSON.isNull("name")){

                placename = googlePlaceJSON.getString("name");
            }
            if(!googlePlaceJSON.isNull("icinity")){
                vicinity = googlePlaceJSON.getString("vicinity");
            }
            latitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = googlePlaceJSON.getString("reference");
            googleplacemap.put("place_name",placename);
            googleplacemap.put("vicinity",vicinity);
            googleplacemap.put("lat",latitude);
            googleplacemap.put("lng",longitude);
            googleplacemap.put("reference",reference);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return googleplacemap;
    }
    private List<HashMap<String,String>> getPlaces(JSONArray jsonArray){
        int count = jsonArray.length();
        List<HashMap<String,String>> placesList = new ArrayList<>();
        HashMap<String,String> placeMap = null;
        for(int i = 0; i<count; i++){
            try {
                placeMap = getPlace((JSONObject)jsonArray.get(i));
                placesList.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placesList;
    }
    public List<HashMap<String,String>> parse(String jsonData){
        JSONArray jsonArray = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }
    public HashMap<String,String> parseDirections(String jsonData){
        JSONArray jsonArray = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getDuration(jsonArray);

    }
}
