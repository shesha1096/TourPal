package com.example.shesha.tourpal;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.shesha.tourpal.Adapter.MessageAdapter;
import com.example.shesha.tourpal.Model.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private Bundle extras;
    private String username;
    private FirebaseAuth auth;
    private DatabaseReference mRootref;
    private String email,emailID;
    private String user1,user2;
    private EditText messageView;
    private RelativeLayout sendBtn;
    private RecyclerView msgList;
    private List<Message> messageList;
    private LinearLayoutManager mLinearLayout;
    private MessageAdapter messageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        messageView = (EditText) findViewById(R.id.editTextchat);
        sendBtn = (RelativeLayout) findViewById(R.id.addBtnchat);
        msgList = (RecyclerView) findViewById(R.id.recyclerViewchat);
        messageList = new ArrayList<>();
        mLinearLayout = new LinearLayoutManager(ChatActivity.this);
        msgList.setHasFixedSize(true);
        msgList.setLayoutManager(mLinearLayout);
        messageAdapter = new MessageAdapter(messageList);
        msgList.setAdapter(messageAdapter);

        extras = getIntent().getExtras();
        username = extras.getString("username");
        getSupportActionBar().setTitle(username);
        auth = FirebaseAuth.getInstance();
        email = auth.getCurrentUser().getEmail();
        emailID = extras.getString("email");
        int index1 = email.indexOf('@');
        user1 = email.substring(0,index1);
        int index2 = emailID.indexOf('@');
        user2 = emailID.substring(0,index2);
        mRootref = FirebaseDatabase.getInstance().getReference();
        mRootref.child("chats").child(user1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(user2)){
                    Map chatAddMap = new HashMap();
                    chatAddMap.put("timestamp",ServerValue.TIMESTAMP);
                    Map chatUserMap = new HashMap();
                    chatUserMap.put("chats/"+user1+"/"+user2,chatAddMap);
                    chatUserMap.put("chats/"+user2+"/"+user1,chatAddMap);
                    mRootref.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError!=null){
                                Log.d("Chat Error",databaseError.getMessage());
                            }

                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        loadMessages();



    }


    private void sendMessage() {
        String message = messageView.getText().toString();
        if(!TextUtils.isEmpty(message)){
            String currentUserref = "messages/" + user1 + "/" + user2 ;
            String chatUserref = "messages/" + user2 + "/" + user1;
            DatabaseReference userMessagePush = mRootref.child("messages").child(user1).child(user2).push();
            String pushID = userMessagePush.getKey();
            Map messageMap = new HashMap();
            messageMap.put("message",message);
            messageMap.put("seen",false);
            messageMap.put("type","text");
            messageMap.put("timestamp",ServerValue.TIMESTAMP);
            messageMap.put("from",user1);

            Map messageUsermap = new HashMap();
            messageUsermap.put(currentUserref + "/" + pushID,messageMap);
            messageUsermap.put(chatUserref+ "/" + pushID,messageMap);
            mRootref.updateChildren(messageUsermap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError!= null){
                        Log.d("Chat Error",databaseError.getMessage());
                    }

                }
            });
        }
    }
    private void loadMessages() {
        DatabaseReference databaseReference = mRootref.child("messages").child(user1).child(user2);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                messageList.add(message);
                messageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


}
