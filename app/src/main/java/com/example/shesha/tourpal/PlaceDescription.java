package com.example.shesha.tourpal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PlaceDescription extends AppCompatActivity {
    private TextView placetitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_description);
        placetitle = (TextView) findViewById(R.id.placeTitleID);
        Bundle extras = getIntent().getExtras();
        placetitle.setText(extras.getString("Place Name").toString());
    }
}
