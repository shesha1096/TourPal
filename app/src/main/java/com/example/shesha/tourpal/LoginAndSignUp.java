package com.example.shesha.tourpal;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

public class LoginAndSignUp extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "Login And Sign Up";
    private static final int RC_SIGN_IN = 1;
    private Button loginButton;
    private Button forgotpwdButton;
    private Button signupbutton;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText emailID;
    private EditText password;
    private static final int MAX = 9999;
    private CallbackManager mCallbackManager;
    private Button fbButton;
    private SignInButton gmailButton;
    private GoogleSignInClient mGoogleSignInClient;
    private EditText phoneno;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_sign_up);
        setTitle("Login");
        loginButton = (Button) findViewById(R.id.loginbuttonID);
        forgotpwdButton = (Button) findViewById(R.id.forgotpwdID);
        signupbutton = (Button) findViewById(R.id.signupButton);
        emailID = (EditText) findViewById(R.id.enteremailID);
        password = (EditText) findViewById(R.id.enterPassword);
        fbButton = (Button) findViewById(R.id.facebookButtonID);
        gmailButton = (SignInButton) findViewById(R.id.gmailButtonID);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(LoginAndSignUp.this, gso);



        loginButton.setOnClickListener(this);
        forgotpwdButton.setOnClickListener(this);
        signupbutton.setOnClickListener(this);
        fbButton.setOnClickListener(this);
        gmailButton.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user!=null)
                {
                    Toast.makeText(LoginAndSignUp.this,"Signed In",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(LoginAndSignUp.this," Not Signed In",Toast.LENGTH_LONG).show();
                }
            }
        };


    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.loginbuttonID: {
                //startActivity(new Intent(LoginAndSignUp.this, HomePage.class));
                    final String email = emailID.getText().toString();
                    final String pwd = password.getText().toString();
                    if(email.equals("") || pwd.equals(""))
                    {
                        Toast.makeText(LoginAndSignUp.this,"Please Enter the right Details",Toast.LENGTH_SHORT).show();
                    }else{
                mAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(LoginAndSignUp.this,"Enter a valid email ID and Password",Toast.LENGTH_LONG).show();
                        }else{
                             Intent intent = new Intent(LoginAndSignUp.this,HomePage.class);

                             startActivity(intent);
                        }
                    }
                });

            }}
            break;
            case R.id.forgotpwdID: {
                if(emailID.getText().toString().isEmpty()){
                    Toast.makeText(LoginAndSignUp.this,"Please Enter Your Email Address",Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(LoginAndSignUp.this, ForgotPassword.class));
                }



    }
            break;
            case R.id.signupButton: {
                        signupbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(LoginAndSignUp.this,SignUpPage.class));
                            }
                        });
            }
            break;
            case R.id.facebookButtonID: {
                mCallbackManager = CallbackManager.Factory.create();
                LoginManager.getInstance().logInWithReadPermissions(LoginAndSignUp.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                        // ...
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError", error);
                        // ...
                    }
                });





            }
            break;
            case R.id.gmailButtonID: signIn(); break;

        }
    }




    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }

        // Pass the activity result back to the Facebook SDK

    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user!=null)
                            startActivity(new Intent(LoginAndSignUp.this,HomePage.class));
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginAndSignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                        // ...
                    }
                });

    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginAndSignUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user!=null)
                            {
                                startActivity(new Intent(LoginAndSignUp.this,HomePage.class));
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

}
