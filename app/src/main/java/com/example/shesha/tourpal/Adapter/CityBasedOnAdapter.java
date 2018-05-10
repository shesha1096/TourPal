package com.example.shesha.tourpal.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shesha.tourpal.MapsActivity;
import com.example.shesha.tourpal.Model.CityBasedOn;
import com.example.shesha.tourpal.Model.CityRow;
import com.example.shesha.tourpal.R;

import java.util.List;

public class CityBasedOnAdapter extends RecyclerView.Adapter<CityBasedOnAdapter.ViewHolder> {
    private Context ctx;
    private List<CityBasedOn> cityBasedOnList;
    public CityBasedOnAdapter(Context context, List listitem){
        ctx = context;
        cityBasedOnList = listitem;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_based_on_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CityBasedOnAdapter.ViewHolder holder, int position) {
        final CityBasedOn cityBasedOn = cityBasedOnList.get(position);
        holder.cityname.setText(cityBasedOn.getCityname());
        holder.cityname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = cityBasedOn.getCityname();
                Intent mapsIntent = new Intent(ctx,MapsActivity.class);
                mapsIntent.putExtra("City Name",name);
                ctx.startActivity(mapsIntent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return cityBasedOnList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView cityname;
       public ViewHolder(View itemView) {
            super(itemView);
            cityname =(TextView) itemView.findViewById(R.id.citybasedonID);
        }

    }
}
