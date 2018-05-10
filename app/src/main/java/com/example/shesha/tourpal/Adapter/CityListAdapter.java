package com.example.shesha.tourpal.Adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shesha.tourpal.MapsActivity;
import com.example.shesha.tourpal.Model.CityRow;
import com.example.shesha.tourpal.R;

import java.util.List;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {
    private Context ctx;
    private List<CityRow> listitems;
    public CityListAdapter(Context context, List listitem){
        ctx = context;
        this.listitems = listitem;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CityListAdapter.ViewHolder holder, int position) {
        CityRow row = listitems.get(position);
        holder.cityName.setText(row.getCityName());


    }

    @Override
    public int getItemCount() {
        return listitems.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView cityName;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            cityName = (TextView)itemView.findViewById(R.id.cityNameID);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            CityRow cityRow = listitems.get(position);
            String name = cityRow.getCityName();
            Intent mapsIntent = new Intent(ctx,MapsActivity.class);
            mapsIntent.putExtra("City Name",name);
            ctx.startActivity(mapsIntent);

        }
    }
}
