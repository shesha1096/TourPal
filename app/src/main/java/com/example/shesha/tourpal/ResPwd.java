package com.example.shesha.tourpal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ResPwd extends AppCompatActivity {
    private EditText resfirst;
    private EditText ressecond;
    private Button resConfirm;
    private FirebaseFirestore firebaseFirestore;
    private Bundle extras;
    private String emailID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_pwd);
        resfirst = (EditText) findViewById(R.id.resEnter);
        ressecond = (EditText) findViewById(R.id.resReenter);
        resConfirm = (Button) findViewById(R.id.res_confirm);
        extras = getIntent().getExtras();
        emailID = extras.getString("email");
        firebaseFirestore = FirebaseFirestore.getInstance();
        resConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!resfirst.getText().toString().isEmpty() && !ressecond.getText().toString().isEmpty()){
                    Map<String,String> pwdMap = new HashMap<>();
                    pwdMap.put("password",resfirst.getText().toString());
                    firebaseFirestore.collection(emailID).document("Details").set(pwdMap, SetOptions.merge()).addOnCompleteListener(ResPwd.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent logIntent = new Intent(ResPwd.this, LoginAndSignUp.class);
                                startActivity(logIntent);
                            }else{
                                Toast.makeText(ResPwd.this,"Please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

    }
}
