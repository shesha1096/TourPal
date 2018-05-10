package com.example.shesha.tourpal.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shesha.tourpal.ChatActivity;
import com.example.shesha.tourpal.Model.User;
import com.example.shesha.tourpal.Model.Users;
import com.example.shesha.tourpal.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {
    private List<User> usersList;
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    public ChatsAdapter(List users,Context context){
        this.context = context;
        usersList = users;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String user_name = usersList.get(position).getUsername();
        String imageuri = usersList.get(position).getImage();
        final String email = usersList.get(position).getEmail();
        holder.usernameView.setText(user_name);
        Picasso.with(context).load(imageuri).placeholder(R.drawable.com_facebook_profile_picture_blank_square).into(holder.userImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]{"Open Profile","Send Message"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Select One");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 1){
                            Intent chatIntent = new Intent(context, ChatActivity.class);
                            chatIntent.putExtra("username",holder.usernameView.getText().toString());
                            chatIntent.putExtra("email",email);
                            context.startActivity(chatIntent);
                        }

                    }
                });
                builder.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView userImage;
        private TextView usernameView;
        public ViewHolder(View itemView) {
            super(itemView);
            userImage = (CircleImageView) itemView.findViewById(R.id.chat_profile_image);
            usernameView = (TextView) itemView.findViewById(R.id.chat_username);

        }
    }
}
