package com.example.shesha.tourpal;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shesha.tourpal.Adapter.BlogAdapter;
import com.example.shesha.tourpal.Model.BlogPost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private RecyclerView bloglistView;
    private List<BlogPost> blogPosts;
    private FirebaseFirestore firebaseFirestore;
    private BlogAdapter blogAdapter;
    private String emailId;


    public HomeFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        String email = (String) getArguments().get("email");
        blogPosts = new ArrayList<>();
        blogAdapter = new BlogAdapter(blogPosts,getContext());
        bloglistView =(RecyclerView) view.findViewById(R.id.blog_post_view);
        bloglistView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bloglistView.setAdapter(blogAdapter);
        Query firstQuery = firebaseFirestore.collection("Blogs").orderBy("timestamp",Query.Direction.DESCENDING);
        firstQuery.addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for(DocumentChange documentChange: documentSnapshots.getDocumentChanges()){
                    if(documentChange.getType() == DocumentChange.Type.ADDED){
                        String blogid = documentChange.getDocument().getId();

                        BlogPost blogPost = documentChange.getDocument().toObject(BlogPost.class).withId(blogid);
                        blogPosts.add(blogPost);
                        blogAdapter.notifyDataSetChanged();
                    }
                }

            }
        });

        // Inflate the layout for this fragment
        return view;

    }

}
