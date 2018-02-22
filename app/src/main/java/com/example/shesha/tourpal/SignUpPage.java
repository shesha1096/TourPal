package com.example.shesha.tourpal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpPage extends AppCompatActivity {
    private EditText name;
    private EditText email;
    private EditText pwd;
    private EditText reenterpwd;
    private Button signUp;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        name = (EditText) findViewById(R.id.enterNameID);
        email = (EditText) findViewById(R.id.enterEmailID);
        pwd = (EditText) findViewById(R.id.createpwdID);
        reenterpwd = (EditText) findViewById(R.id.pwdreentryID);
        signUp = (Button) findViewById(R.id.signupID);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        databaseReference = firebaseDatabase.getReference();
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 final String username = name.getText().toString().trim();
                 final String emailID = email.getText().toString().trim();
                String password = pwd.getText().toString().trim();
                String reentry = reenterpwd.getText().toString().trim();
                if(!Patterns.EMAIL_ADDRESS.matcher(emailID).matches()){
                    Toast.makeText(SignUpPage.this,"Please Enter a Valid Email Address",Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(emailID) || TextUtils.isEmpty(password) || TextUtils.isEmpty(reentry)) {
                    Toast.makeText(SignUpPage.this, "Please Fill In All Fields", Toast.LENGTH_SHORT).show();
                }
                else if (!password.equals(reentry)) {
                    Toast.makeText(SignUpPage.this, "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
                } else {


                    mAuth.createUserWithEmailAndPassword(emailID, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(SignUpPage.this,"Account Created Successfully",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpPage.this, HomePage.class);
                                // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignUpPage.this, "Failed to Sign In", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                            }
                        }
                    });

                }
            }









