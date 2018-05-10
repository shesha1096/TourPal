package com.example.shesha.tourpal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.shesha.tourpal.Adapter.CityBasedOnAdapter;
import com.example.shesha.tourpal.Model.CityBasedOn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CityIntermediate extends AppCompatActivity {
    private Bundle extras;
    private String email;
    private int j;
    private FirebaseFirestore firebaseFirestore;
    private List<Climate> c1List, c2List;
    private Climate climate,climate1;
    private int intMonth;
    private Double avgTemp=0.0,totmaxtemp=0.0,totrainfall=0.0,totmintemp=0.0;
    private List<String> nameslist;
    private RecyclerView recyclerView;
    private List<CityBasedOn> cityBasedOnList;
    private RecyclerView.Adapter cityBasedOnAdapter;
    private String cityname;
    private ProgressDialog progressDialog;
    private Button troubleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_intermediate);
        recyclerView = (RecyclerView) findViewById(R.id.cityListRecyclerView);
        troubleBtn = (Button) findViewById(R.id.stillTroubleID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(CityIntermediate.this));
        extras = getIntent().getExtras();
        cityBasedOnList = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        email = extras.getString("email");
        Log.d("Email",email);
        troubleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent quickIntent = new Intent(CityIntermediate.this,CityQuickGuide.class);
                quickIntent.putExtra("email",email);
                startActivity(quickIntent);
            }
        });
        progressDialog = new ProgressDialog(CityIntermediate.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();



        j=0;
        firebaseFirestore.collection(email).document("Details").get().addOnCompleteListener(CityIntermediate.this, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                   if(!task.getResult().contains("placesvisited")){
                       progressDialog.dismiss();
                       Intent guideIntent = new Intent(CityIntermediate.this,CityQuickGuide.class);
                       guideIntent.putExtra("email",email);
                       startActivity(guideIntent);
                   }else {
                       progressDialog.dismiss();
                       c1List = new ArrayList<>();
                       c2List = new ArrayList<>();
                       cityname = task.getResult().get("placesvisited").toString();
                       Log.d("City Name",cityname);


                       InputStream is = getResources().openRawResource(R.raw.climate_change_upto_2000_1);
                       BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                       String line;
                       try {
                           try {
                               bufferedReader.readLine();
                           } catch (IOException e) {
                               e.printStackTrace();
                           }
                           while ((line = bufferedReader.readLine()) != null) {
                               String[] tokens = line.split(",");
                               climate = new Climate();
                               climate.setStationname(tokens[0]);
                               if (cityname.equals(climate.getStationname())) {
                                   climate.setMonth(tokens[1]);
                                   switch (climate.getMonth()) {
                                       case "January":
                                           intMonth = 1;
                                           break;
                                       case "February":
                                           intMonth = 2;
                                           break;
                                       case "March":
                                           intMonth = 3;
                                           break;
                                       case "April":
                                           intMonth = 4;
                                           break;
                                       case "May":
                                           intMonth = 5;
                                           break;
                                       case "June":
                                           intMonth = 6;
                                           break;
                                       case "July":
                                           intMonth = 7;
                                           break;
                                       case "August":
                                           intMonth = 8;
                                           break;
                                       case "September":
                                           intMonth = 9;
                                           break;
                                       case "October":
                                           intMonth = 10;
                                           break;
                                       case "November":
                                           intMonth = 11;
                                           break;
                                       case "December":
                                           intMonth = 12;
                                           break;

                                   }
                                   climate.setRainfall(Double.parseDouble(tokens[6]));
                                   Log.d("Rainfall",String.valueOf(climate.getRainfall()));
                                   totrainfall += climate.getRainfall();
                                   climate.setMeantempmax(Double.parseDouble(tokens[4]));
                                   totmaxtemp += climate.getMeantempmax();
                                   climate.setMeantempmin(Double.parseDouble(tokens[5]));
                                   totmintemp += climate.getMeantempmin();
                                   climate.setPeriod(tokens[2]);
                                   climate.setNoofyears(tokens[3]);
                                   c1List.add(climate);
                                   Log.d("Size", String.valueOf(c1List.size()));

                               }

                           }
                           avgTemp = (totmaxtemp + totmintemp) / 2;
                           Log.d("Average",String.valueOf(avgTemp));

                       } catch (Exception e) {
                           e.printStackTrace();
                       }
                       InputStream inputStream = getResources().openRawResource(R.raw.climate_change_upto_2000_1);
                       BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                       String line1;
                       try {
                           try {
                               bufferedReader1.readLine();
                               while ((line1 = bufferedReader1.readLine()) != null) {
                                   String[] tokens = line1.split(",");
                                   climate1 = new Climate();
                                   climate1.setStationname(tokens[0]);
                                   climate1.setPeriod(tokens[2]);
                                   climate1.setNoofyears(tokens[3]);
                                   climate1.setMeantempmax(Double.parseDouble(tokens[4]));
                                   climate1.setMeantempmin(Double.parseDouble(tokens[5]));
                                   climate1.setRainfall(Double.parseDouble(tokens[6]));
                                   c2List.add(climate1);
                               }
                               nameslist = new ArrayList<>();
                           } catch (IOException e) {
                               e.printStackTrace();
                           }
                           Log.d("C2List", c2List.get(2).getStationname());


                           for (int i = 0; i < c2List.size(); i++) {
                               Double rain = c2List.get(i).getRainfall();
                               Double maxtemp = c2List.get(i).getMeantempmax();
                               Double mintemp = c2List.get(i).getMeantempmin();
                               Double avg = (mintemp + maxtemp) / 2;
                               Double abs = rain * totrainfall + avgTemp * avg;
                               Log.d("ABs", String.valueOf(abs));
                               Double denom1 = Math.sqrt(Math.pow((double) totrainfall, 2) + Math.pow((double) avgTemp, 2));
                               Double denom2 = Math.sqrt(Math.pow(rain, 2) + Math.pow(avg, 2));
                               Log.d("Denom1", String.valueOf(denom1));
                               Log.d("Denom2", String.valueOf(denom2));
                               Double denom = denom1 * denom2;
                               Double cosine_similarity = abs / denom;
                               Log.d("Cos", String.valueOf(cosine_similarity));
                               if (cosine_similarity >= 0.80) {

                                   nameslist.add(c2List.get(i).getStationname());


                               }

                           }
                       } catch (Exception e) {
                           e.printStackTrace();
                       }
                       Set<String> hs = new HashSet<>();
                       hs.addAll(nameslist);
                       nameslist.clear();
                       nameslist.addAll(hs);

                       for (int k = 0; k < nameslist.size(); k++) {
                           Log.d("List", nameslist.get(k).toString());
                       }
                       for (int a = 0; a < nameslist.size(); a++) {
                           CityBasedOn cityBasedOn = new CityBasedOn(nameslist.get(a).toString());
                           cityBasedOnList.add(cityBasedOn);
                       }
                       cityBasedOnAdapter = new CityBasedOnAdapter(CityIntermediate.this, cityBasedOnList);
                       recyclerView.setAdapter(cityBasedOnAdapter);



                   }


                }else{
                    Toast.makeText(CityIntermediate.this,"Unsuccessful",Toast.LENGTH_SHORT).show();
                }


            }


        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent homeIntent = new Intent(CityIntermediate.this,HomePage.class);
        homeIntent.putExtra("email ID",email);
        startActivity(homeIntent);
    }
}
