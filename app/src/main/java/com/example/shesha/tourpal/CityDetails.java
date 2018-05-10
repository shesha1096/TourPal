package com.example.shesha.tourpal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CityDetails extends AppCompatActivity {
    private TextView city;
    private Button itineraryButton;
    private FirebaseFirestore firebaseFirestore;
    private Bundle extras;
    String cityname;
    private TextView citydetails;
    private String namelist[],response,imgurl;
    private ImageView cityView;
    private FirebaseFirestore firebaseFirestore1;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_details);
        city = (TextView) findViewById(R.id.citytitle);
        citydetails = (TextView) findViewById(R.id.citydescription);
        itineraryButton = (Button) findViewById(R.id.createItineraryID);
        firebaseFirestore1 = FirebaseFirestore.getInstance();
        cityView = (ImageView) findViewById(R.id.citypic);
         extras =getIntent().getExtras();
        cityname = extras.getString("City");
        email = extras.getString("email");
        firebaseFirestore = FirebaseFirestore.getInstance();
        final ProgressDialog progressDialog = new ProgressDialog(CityDetails.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        firebaseFirestore.collection(cityname).document("citydesc").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if(!task.getResult().exists()){
                        response = "There is no description available for this place currently.";

                    }
                    progressDialog.dismiss();
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        response = documentSnapshot.getString("description");
                        imgurl = documentSnapshot.getString("image");
                        Log.d("Text", response);
                    }
                }
                citydetails.setText(response);
                Picasso.with(CityDetails.this).load(imgurl).placeholder(R.drawable.common_google_signin_btn_icon_dark).into(cityView);
            }
        });


                    String size = extras.getString("Size");
        int n = Integer.parseInt(size);
        namelist = new String[n+10];
        namelist = extras.getStringArray("Place List");
        Log.d("Names List",namelist[1]);


        city.setText(cityname);

        itineraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> visitMap = new HashMap<>();
                visitMap.put("placesvisited",cityname);
                firebaseFirestore1.collection(email).document("Details").set(visitMap,SetOptions.merge()).addOnCompleteListener(CityDetails.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent intent=new  Intent(CityDetails.this,Itinerary.class);
                            intent.putExtra("City List",namelist);
                            intent.putExtra("City Name",cityname);
                            startActivity(intent);

                        }else{
                            Toast.makeText(CityDetails.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

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
