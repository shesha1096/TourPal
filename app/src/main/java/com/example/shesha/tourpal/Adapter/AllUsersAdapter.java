package com.example.shesha.tourpal.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shesha.tourpal.Model.Users;
import com.example.shesha.tourpal.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.ViewHolder> {
    private List<Users> usersList;
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    public AllUsersAdapter(List users,Context context){
        this.context = context;
        usersList = users;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_single_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String user_name = usersList.get(position).getUsername();
        String emailID = usersList.get(position).getStatus();
        String imageuri = usersList.get(position).getImage();
        holder.usernameView.setText(user_name);
        holder.emailView.setText(emailID);
        Picasso.with(context).load(imageuri).placeholder(R.drawable.com_facebook_profile_picture_blank_square).into(holder.userImage);


    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView userImage;
        private TextView emailView;
        private TextView usernameView;
        public ViewHolder(View itemView) {
            super(itemView);
            userImage = (CircleImageView) itemView.findViewById(R.id.user_single_image);
            emailView = (TextView) itemView.findViewById(R.id.user_single_status);
            usernameView = (TextView) itemView.findViewById(R.id.user_single_name);

        }
    }
}
