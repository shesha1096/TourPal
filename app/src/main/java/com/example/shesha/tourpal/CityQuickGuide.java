package com.example.shesha.tourpal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CityQuickGuide extends AppCompatActivity implements Serializable {
    private TextView weather;
    private TextView duration;
    private EditText month;
    private Button help;
    private String weatherString;
    private String monthString;
    private String rainfall;
    private int intMonth, userMonth, intRainfall, intWeather;
    private Double temp, rain;
    private SeekBar weatherValue;
    private SeekBar rainValue;
    List<Climate> climateList, climateList1;
    private String[] citynames;
    private Bundle extras;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_quick_guide);
        weather = (TextView) findViewById(R.id.weather);
        duration = (TextView) findViewById(R.id.duration);
        month = (EditText) findViewById(R.id.month_travel);
        help = (Button) findViewById(R.id.helpmeBtn);
        extras = getIntent().getExtras();
        email = extras.getString("email");
        weatherValue = (SeekBar) findViewById(R.id.weatherSeek);
        rainValue = (SeekBar) findViewById(R.id.rainfallSeek);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    weatherValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {


                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            intWeather = seekBar.getProgress();
                            weatherString = String.valueOf(intWeather);
                            Log.d("Weather", weatherString);

                        }
                    });
                    rainValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            intRainfall = seekBar.getProgress();
                            rainfall = String.valueOf(intRainfall);
                            Log.d("Rainfall", rainfall);

                        }
                    });
                    monthString = month.getText().toString();
                    userMonth = Integer.valueOf(monthString);
                    Log.d("Month", monthString);
                    int flag =getData();
                    if(flag==1) {
                        if(!weatherString.isEmpty() && !rainfall.isEmpty() && !monthString.isEmpty()) {
                            Intent cityIntent = new Intent(CityQuickGuide.this, CityList.class);
                            cityIntent.putExtra("City List Names", citynames);
                            startActivity(cityIntent);
                        }else{
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CityQuickGuide.this);
                            alertDialog.setTitle("Error");
                            alertDialog.setMessage("Please enter all the values");
                            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            });
                            alertDialog.show();
                        }
                    }else{
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CityQuickGuide.this);
                        alertDialog.setTitle("Error");
                        alertDialog.setMessage("Could not find any cities. Try entering different inputs?");
                        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });



    }

    private int getData() throws IOException {
        climateList = new ArrayList<>();
        climateList1 = new ArrayList<>();
        InputStream is = getResources().openRawResource(R.raw.climate_change_upto_2000_1);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line;
        bufferedReader.readLine();
        while ((line = bufferedReader.readLine()) != null) {
            String[] tokens = line.split(",");
            Climate climate = new Climate();
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
            if (intMonth == userMonth) {
                climate.setStationname(tokens[0]);
                climate.setPeriod(tokens[2]);
                climate.setNoofyears(tokens[3]);
                climate.setMeantempmax(Double.parseDouble(tokens[4]));
                climate.setMeantempmin(Double.parseDouble(tokens[5]));
                climate.setRainfall(Double.parseDouble(tokens[6]));
                climateList.add(climate);
                rain = climate.getRainfall();
                Double maxtemp = climate.getMeantempmax();
                Double mintemp = climate.getMeantempmin();
                temp = maxtemp - mintemp;
                Double abs = intRainfall * rain + intWeather * temp + userMonth * intMonth;
                Double denom1 = Math.sqrt(Math.pow((double) intRainfall, 2) + Math.pow((double) intWeather, 2) + Math.pow(userMonth, 2));
                Double denom2 = Math.sqrt(Math.pow(temp, 2) + Math.pow(rain, 2) + Math.pow(intMonth, 2));
                Double denom = denom1 * denom2;
                Double cosine_similarity = abs / denom;
                if (cosine_similarity >= 0.80) {
                    climateList1.add(climate);
                    Log.d("Selected", climate.getStationname());
                }
                Log.d("Climate", cosine_similarity.toString());
                Log.d("Place", climate.getStationname());


            }
        }
        citynames = new String[climateList1.size() + 4];
        for (int i = 0; i < climateList1.size(); i++) {
            citynames[i] = climateList1.get(i).getStationname();

        }
        if(climateList1.size()>=1){
            return 1;
        }
        else
            return  0;



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent homeIntent = new Intent(CityQuickGuide.this,HomePage.class);
        homeIntent.putExtra("email ID",email);
        startActivity(homeIntent);
    }
}
