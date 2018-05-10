package com.example.shesha.tourpal;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shesha.tourpal.Adapter.ChatsAdapter;
import com.example.shesha.tourpal.Model.User;
import com.example.shesha.tourpal.Model.Users;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {
    private RecyclerView usersList;
    private FirebaseFirestore firebaseFirestore;
    private List<User> users;
    private RecyclerView.Adapter allUsersAdapter;


    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        users = new ArrayList<>();
        firebaseFirestore.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String blogid = documentChange.getDocument().getId();

                        User user = documentChange.getDocument().toObject(User.class);
                        users.add(user);
                        allUsersAdapter.notifyDataSetChanged();
                    }

                }
            }
        });
        allUsersAdapter = new ChatsAdapter(users,getContext());

        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        usersList = (RecyclerView) view.findViewById(R.id.users_recyclerView);
        usersList.setLayoutManager(new LinearLayoutManager(getContext()));
        usersList.setAdapter(allUsersAdapter);
        usersList.setHasFixedSize(true);
        // Inflate the layout for this fragment
        return view;

    }

}
