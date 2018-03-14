package com.example.shesha.tourpal.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.INotificationSideChannel;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shesha.tourpal.Itinerary;
import com.example.shesha.tourpal.Model.ItineraryRow;
import com.example.shesha.tourpal.PlaceDescription;
import com.example.shesha.tourpal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Shesha on 29-01-2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>
{
    private Context context;
    private List<ItineraryRow> listItems;
    private String name;
    private FirebaseFirestore firebaseFirestore;
    private String desc;
    ItineraryRow row;

    public MyAdapter(Context context, List<ItineraryRow> listItems,String name) {
        this.context = context;
        this.listItems = listItems;
        this.name = name;


    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itinerary_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        ItineraryRow row = listItems.get(position);
        holder.placename.setText(row.getPlaceName());


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
            firebaseFirestore = FirebaseFirestore.getInstance();
             row = listItems.get(position);
            String row_name = row.getPlaceName();
            Log.d("Row Name",row_name);
            firebaseFirestore.collection(name).document(row_name).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot!=null && documentSnapshot.exists()) {
                            desc = documentSnapshot.getString("description");
                            Log.d("Text", desc);
                        }else{
                            desc = "There is no description available for this place currently.";
                        }

                        Intent intent = new Intent(context, PlaceDescription.class);
                            intent.putExtra("Place Name",row.getPlaceName());
                            intent.putExtra("Place Description",desc);
                            context.startActivity(intent);



                    }else{
                        Toast.makeText(context,"Could not set text",Toast.LENGTH_SHORT).show();
                    }

                }
            });


        }
    }
}
