package com.example.shesha.tourpal.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.INotificationSideChannel;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shesha.tourpal.Model.ItineraryRow;
import com.example.shesha.tourpal.PlaceDescription;
import com.example.shesha.tourpal.R;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Shesha on 29-01-2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>
{
    private Context context;
    private List<ItineraryRow> listItems;

    public MyAdapter(Context context, List<ItineraryRow> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itinerary_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        ItineraryRow row = listItems.get(position);


    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView placename;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            placename = (TextView) itemView.findViewById(R.id.event_name);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ItineraryRow row = listItems.get(position);
            Intent intent = new Intent(context, PlaceDescription.class);
            intent.putExtra("Place Name",row.getPlaceName());
            context.startActivity(intent);

        }
    }
}
