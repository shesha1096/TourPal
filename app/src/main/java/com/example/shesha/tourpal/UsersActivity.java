package com.example.shesha.tourpal;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class UsersActivity extends AppCompatActivity {
    private BottomNavigationView blogBottom;
    private ChatsFragment chatsFragment;
    private StatusFragment statusFragment;
    private AllUsersFragment allUsersFragment;
    private Bundle extras;
    private String email;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        blogBottom = (BottomNavigationView) findViewById(R.id.chat_bottom_nav);

        extras = getIntent().getExtras();
        email = extras.getString("email");
        Bundle args = new Bundle();
        args.putString("email",email);
        chatsFragment = new ChatsFragment();
        statusFragment = new StatusFragment();
        allUsersFragment = new AllUsersFragment();
        allUsersFragment.setArguments(args);
        blogBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_chat_chats:
                        replaceFragment(chatsFragment);
                        return true;
                    case R.id.bottom_chat_allusers:
                        replaceFragment(allUsersFragment);
                        return  true;
                    case R.id.bottom_chat_status:
                        replaceFragment(statusFragment);
                        return true;
                        default: return false;
                }

            }
        });
        firebaseFirestore = FirebaseFirestore.getInstance();



    }
    private void replaceFragment(android.support.v4.app.Fragment fragment){
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.users_container,fragment);
        fragmentTransaction.commit();



    }

    @Override
    protected void onStart() {
        super.onStart();
        Map<String,String> onlineMap = new HashMap<>();
        onlineMap.put("online","true");
        firebaseFirestore.collection("Users").document(email).set(onlineMap, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(UsersActivity.this,"You are online", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(UsersActivity.this,"Error",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Map<String,String> offlineMap = new HashMap<>();
        offlineMap.put("online","false");
        firebaseFirestore.collection("Users").document(email).set(offlineMap, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(UsersActivity.this,"You are offline", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(UsersActivity.this,"Error",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
