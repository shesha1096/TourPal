package com.example.shesha.tourpal;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shesha.tourpal.Adapter.CommentAdapter;
import com.example.shesha.tourpal.Model.BlogPost;
import com.example.shesha.tourpal.Model.Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentsActivity extends AppCompatActivity {
    private EditText comment;
    private ImageView add;
    private String blogpostID;
    private Bundle extras;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    public String cuurentuserid;
    private RecyclerView commentList;
    private List<Comment> commentsList;
    private CommentAdapter commentAdapter;
    private FirebaseFirestore firebaseFirestore1;
    private String uri = "";
    private String emailID;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        add = (ImageView) findViewById(R.id.blog_comment_send);
        comment = (EditText) findViewById(R.id.blog_add_comment);
        commentList = (RecyclerView) findViewById(R.id.blogRecyclerView);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore1 = FirebaseFirestore.getInstance();
        cuurentuserid = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        extras = getIntent().getExtras();
        blogpostID = extras.getString("blogid");
        emailID = extras.getString("email");
        firebaseFirestore1.collection(emailID).document("Details").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                     uri =task.getResult().get("image uri").toString();
                     username = task.getResult().get("username").toString();
                    Log.d("Image uri",uri);
                }else{
                    Toast.makeText(CommentsActivity.this,"Could not fetch uri",Toast.LENGTH_SHORT).show();
                }

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = comment.getText().toString();
                if(!comment.getText().toString().isEmpty()){
                    Map<String,Object> commentsMap = new HashMap<>();
                    commentsMap.put("message",msg);
                    commentsMap.put("userid",cuurentuserid);
                    commentsMap.put("timestamp", FieldValue.serverTimestamp());
                    commentsMap.put("imageuri",uri);
                    firebaseFirestore.collection("Blogs/"+ blogpostID+"/Comments").add(commentsMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(CommentsActivity.this,"Please Try again",Toast.LENGTH_SHORT).show();
                            }else{
                                comment.setText("");
                            }

                        }
                    });

                }
            }
        });
        commentsList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentsList,emailID);
        commentList.setHasFixedSize(true);
        commentList.setLayoutManager(new LinearLayoutManager(this));
        commentList.setAdapter(commentAdapter);

        firebaseFirestore.collection("Blogs/"+ blogpostID + "/Comments").addSnapshotListener(CommentsActivity.this,new  EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(!documentSnapshots.isEmpty()) {
                    for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {

                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            String commentid = documentChange.getDocument().getId();

                            Comment comment = documentChange.getDocument().toObject(Comment.class);
                            commentsList.add(comment);
                            commentAdapter.notifyDataSetChanged();

                        }
                    }
                }


            }
        });
    }
}
