package com.example.shesha.tourpal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.CancelledKeyException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shesha on 24-01-2018.
 */

public class QuickGuide extends Fragment {
    private EditText weather;
    private EditText distance;
    private EditText duration;
    private EditText month;
    private Button help;
    private String weatherString;
    private String monthString;
    private String rainfall;
    private int intMonth,userMonth;
    private Double temp,rain;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.quick_guide,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        weather = (EditText) view.findViewById(R.id.weather);
        distance = (EditText) view.findViewById(R.id.distance);
        duration = (EditText) view.findViewById(R.id.duration);
        month = (EditText) view.findViewById(R.id.month_travel);
        help = (Button) view.findViewById(R.id.helpmeBtn);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    weatherString = weather.getText().toString();
                    monthString = month.getText().toString();
                    userMonth = Integer.valueOf(monthString);
                    Log.d("Month",monthString);
                    rainfall = duration.getText().toString();
                    Log.d("Rainfall",rainfall);
                    Log.d("Weather",weatherString);
                    getData();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });



    }

    private void getData() throws IOException {
        List<Climate> climateList = new ArrayList<>();
        List<Climate> climateList1 = new ArrayList<>();
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
                Double abs = Double.parseDouble(rainfall) * rain + Double.parseDouble(weatherString) * temp + userMonth * intMonth;
                Double denom1 = Math.sqrt(Math.pow(Double.parseDouble(rainfall), 2) + Math.pow(Double.parseDouble(weatherString), 2) + Math.pow(userMonth, 2));
                Double denom2 = Math.sqrt(Math.pow(temp, 2) + Math.pow(rain, 2) + Math.pow(intMonth, 2));
                Double denom = denom1 * denom2;
                Double cosine_similarity = abs / denom;
                Log.d("Climate", cosine_similarity.toString());
                Log.d("Place",climate.getStationname());

            }
        }
    }


}
