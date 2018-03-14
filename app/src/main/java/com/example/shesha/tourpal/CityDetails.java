package com.example.shesha.tourpal;

import android.content.Intent;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CityDetails extends AppCompatActivity {
    private TextView city;
    private Button itineraryButton;
    private static final String encoding = "UTF-8";
    private Bundle extras;
    String cityname;
    private TextView citydetails;
    private String namelist[],response = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_details);
        city = (TextView) findViewById(R.id.citytitle);
        citydetails = (TextView) findViewById(R.id.citydescription);
        itineraryButton = (Button) findViewById(R.id.createItineraryID);
         extras =getIntent().getExtras();
        cityname = extras.getString("City");

        String size = extras.getString("Size");
        int n = Integer.parseInt(size);
        namelist = new String[n+10];
        namelist = extras.getStringArray("Place List");
        Log.d("Names List",namelist[1]);
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {



                    String wikipediaApiJSON = "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&titles="+cityname;

                    HttpURLConnection httpcon = (HttpURLConnection) new URL(wikipediaApiJSON).openConnection();
                    httpcon.addRequestProperty("User-Agent", "Mozilla/5.0");
                    InputStream in = new BufferedInputStream(httpcon.getInputStream());
                    response = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
                    JSONObject object= new JSONObject(response);
                    JSONObject pages=object.getJSONObject("batchcomplete");
                    Iterator keys = pages.keys();
                    while(keys.hasNext()) {
                        String name = (String)keys.next();
                        JSONObject jsonObject=object.getJSONObject(name);

                        String pageid=jsonObject.getString("pageid");
                        String ns=jsonObject.getString("ns");
                        String title=jsonObject.getString("title");
                        String extract=jsonObject.getString("extract");
                        Log.d("Response",extract);
                    }
                    in.close();
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


        city.setText(cityname);
        citydetails.setText(response);
        itineraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new  Intent(CityDetails.this,Itinerary.class);
                intent.putExtra("City List",namelist);
                intent.putExtra("City Name",cityname);
                startActivity(intent);
            }
        });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        super.onBackPressed();
        Intent i=new Intent(Intent.ACTION_MAIN);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
    }

}
