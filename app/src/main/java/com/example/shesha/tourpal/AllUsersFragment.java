package com.example.shesha.tourpal;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.shesha.tourpal.Adapter.AllUsersAdapter;
import com.example.shesha.tourpal.Model.BlogPost;
import com.example.shesha.tourpal.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllUsersFragment extends Fragment {
    private RecyclerView mUsersList;
    private Button changeBtn;
    private String email;
    private List<Users> users;
    private RecyclerView.Adapter allUsersAdapter;
    private FirebaseFirestore firebaseFirestore;


    public AllUsersFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =   inflater.inflate(R.layout.fragment_all_users, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        users = new ArrayList<>();
        // Inflate the layout for this fragment
        mUsersList = (RecyclerView) view.findViewById(R.id.chat_all_usersID);
        changeBtn = (Button) view.findViewById(R.id.changeStatusBtn);
        email = (String) getArguments().get("email");
        allUsersAdapter = new AllUsersAdapter(users,getContext());
        mUsersList.setLayoutManager(new LinearLayoutManager(getContext()));
        mUsersList.setAdapter(allUsersAdapter);
        mUsersList.setHasFixedSize(true);
        firebaseFirestore.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                                      @Override
                                                                      public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                                                                          for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                                                                              if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                                                                  String blogid = documentChange.getDocument().getId();

                                                                                  Users user = documentChange.getDocument().toObject(Users.class);
                                                                                  users.add(user);
                                                                                  allUsersAdapter.notifyDataSetChanged();
                                                                              }

                                                                          }
                                                                      }
                                                                  });




        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent statusIntent = new Intent(getContext(),ChangeStatus.class);
                statusIntent.putExtra("email",email);
                startActivity(statusIntent);
            }
        });
        return view;
    }

}
