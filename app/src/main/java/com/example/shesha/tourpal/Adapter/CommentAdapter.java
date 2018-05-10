package com.example.shesha.tourpal.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shesha.tourpal.Model.Comment;
import com.example.shesha.tourpal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    public List<Comment> commentsList;
    public Context context;
    private String email;
    private FirebaseFirestore firebaseFirestore;

    public CommentAdapter(List<Comment> commentsList,String email){

        this.commentsList = commentsList;
        this.email = email;

    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
        context = parent.getContext();
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentAdapter.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        firebaseFirestore = FirebaseFirestore.getInstance();

        String commentMessage = commentsList.get(position).getMessage();
        holder.setComment_message(commentMessage);
        String imageuri = commentsList.get(position).getImageuri();
        Picasso.with(context).load(imageuri).placeholder(R.drawable.com_facebook_profile_picture_blank_square).into(holder.comment_image);
        firebaseFirestore.collection(email).document("Details").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    String username = task.getResult().get("username").toString();
                    holder.cmt_username.setText(username);
                }else{
                    holder.cmt_username.setText("Username");
                }


            }
        });

    }


    @Override
    public int getItemCount() {

        if(commentsList != null) {

            return commentsList.size();

        } else {

            return 0;

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView comment_message;
        private CircleImageView comment_image;
        private TextView cmt_username;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            comment_image = mView.findViewById(R.id.comment_image);
            cmt_username = mView.findViewById(R.id.comment_username);
        }

        public void setComment_message(String message){

            comment_message = mView.findViewById(R.id.comment_message);
            comment_message.setText(message);

        }


    }

}
