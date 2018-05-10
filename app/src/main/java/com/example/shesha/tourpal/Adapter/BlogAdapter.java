package com.example.shesha.tourpal.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shesha.tourpal.CommentsActivity;
import com.example.shesha.tourpal.Model.BlogPost;
import com.example.shesha.tourpal.ProfileActivity;
import com.example.shesha.tourpal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {
    private List<BlogPost> blogPosts;
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public BlogAdapter(List<BlogPost> blog_list,Context ctx){
        blogPosts = blog_list;
        context = ctx;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_list_item,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String blogPostID = blogPosts.get(position).BlogPostID;
        final String currentuserId = firebaseAuth.getCurrentUser().getUid();
        String descData = blogPosts.get(position).getDescription();
        holder.descView.setText(descData);
        final String email = blogPosts.get(position).getEmailid();
        String timestamp = blogPosts.get(position).getTimestamp();
        String url = blogPosts.get(position).getBlogimage();
        String blogurl = blogPosts.get(position).getImageuri();
        holder.email.setText(email);
        holder.time.setText(timestamp);
        Picasso.with(context).load(url).placeholder(R.drawable.com_facebook_profile_picture_blank_square).into(holder.blogImage);
        Picasso.with(context).load(blogurl).placeholder(R.drawable.com_facebook_profile_picture_blank_square).into(holder.bloguser);
        firebaseFirestore.collection("Blogs").document(blogPostID).collection("Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(!documentSnapshots.isEmpty()){
                    int likes = documentSnapshots.size();
                    holder.updateLikescount(likes);

                }else{
                    holder.updateLikescount(0);

                }

            }
        });
        firebaseFirestore.collection("Blogs").document(blogPostID).collection("Likes").document(currentuserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    holder.blogLikeImage.setImageDrawable(context.getDrawable(R.mipmap.action_like_accent));
                }else{
                    holder.blogLikeImage.setImageDrawable(context.getDrawable(R.mipmap.action_like_gray));

                }

            }
        });
        holder.blogLikeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Blogs").document(blogPostID).collection("Likes").document(currentuserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){
                            firebaseFirestore.collection("Blogs").document(blogPostID).collection("Likes").document(currentuserId).delete();
                            Toast.makeText(context,"You disliked this post",Toast.LENGTH_SHORT).show();

                        }else{
                            Map<String,Object> likesMap = new HashMap<>();
                            likesMap.put("timestamp", FieldValue.serverTimestamp());

                            firebaseFirestore.collection("Blogs").document(blogPostID).collection("Likes").document(currentuserId).set(likesMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(context,"You successfully liked this post",Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(context,"Please Try Again",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }

                    }
                });

            }
        });
        holder.addCmtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                String userString = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                Intent commentIntent = new Intent(context, CommentsActivity.class);
                commentIntent.putExtra("blogid",blogPostID);
                commentIntent.putExtra("email",userString);
                context.startActivity(commentIntent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return blogPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView descView;
        private TextView email;
        private TextView time;
        private ImageView blogImage;
        private ImageView bloguser;
        private ImageView blogLikeImage;
        private TextView blogLikeCount;
        private ImageView addCmtBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            descView = itemView.findViewById(R.id.blog_post_desc);
            email = itemView.findViewById(R.id.blog_user_emailid);
            time = itemView.findViewById(R.id.blog_user_time);
            blogImage = itemView.findViewById(R.id.blog_post_image);
            bloguser = itemView.findViewById(R.id.blog_user_id);
            blogLikeImage = itemView.findViewById(R.id.blog_like_button);
            blogLikeCount = itemView.findViewById(R.id.blog_like_counts);
            addCmtBtn = itemView.findViewById(R.id.addComment);

        }
        public void updateLikescount(int count){
            blogLikeCount.setText(count+" Likes");

        }

    }

}
