package com.example.shesha.tourpal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CityDetails extends AppCompatActivity {
    private TextView city;
    private Button itineraryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_details);
        city = (TextView) findViewById(R.id.citytitle);
        itineraryButton = (Button) findViewById(R.id.createItineraryID);
        Bundle extras =getIntent().getExtras();
        String cityname = extras.getString("City");

        city.setText(cityname);
        itineraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CityDetails.this,Itinerary.class));
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
