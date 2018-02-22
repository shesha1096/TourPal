package com.example.shesha.tourpal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.shesha.tourpal.Adapter.MyAdapter;
import com.example.shesha.tourpal.Model.ItineraryRow;

import java.util.ArrayList;
import java.util.List;

public class Itinerary extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ItineraryRow> listItems;
    private String[] placename;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItems = new ArrayList<>();
        placename = new String[]{"Cubbon Park","Lalbagh","Town Hall","Museum"};
        for(int i = 0;i<4; i++)
        {
            ItineraryRow row = new ItineraryRow(placename[i]);
            listItems.add(row);
        }
        adapter = new MyAdapter(this,listItems);
        recyclerView.setAdapter(adapter);
    }
}
