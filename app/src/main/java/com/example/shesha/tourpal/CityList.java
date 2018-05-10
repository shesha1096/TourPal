package com.example.shesha.tourpal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.shesha.tourpal.Adapter.CityListAdapter;
import com.example.shesha.tourpal.Adapter.MyAdapter;
import com.example.shesha.tourpal.Model.CityRow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CityList extends AppCompatActivity implements Serializable {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<CityRow> cityRows;
    private Bundle extras;
    private String[] cities;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        extras = getIntent().getExtras();
        cities = extras.getStringArray("City List Names");

        recyclerView = (RecyclerView) findViewById(R.id.cityRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CityList.this));
        cityRows = new ArrayList<>();
        for(int i = 0; i<cities.length; i++){
            CityRow row = new CityRow(cities[i]);
            cityRows.add(row);
        }
        adapter = new CityListAdapter(this,cityRows);
        recyclerView.setAdapter(adapter);

    }
}
