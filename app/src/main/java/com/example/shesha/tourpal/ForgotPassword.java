package com.example.shesha.tourpal;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class ForgotPassword extends AppCompatActivity {
    private EditText email;
    private Button forgot_btn;
    private String emailstring;
    private EditText input;
    int num = 0;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        email = (EditText) findViewById(R.id.forgot_emailID);
        forgot_btn = (Button) findViewById(R.id.otpButtonID);
        final AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
        forgot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailstring = email.getText().toString();
                builder.setTitle("Forgot Password");
                builder.setMessage("Enter the OTP here");

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            GmailSender sender = new GmailSender("shesha1096@gmail.com",
                                    "shesha1096shankar");
                            sender.sendMail("Forgot Password:", "Your OTP is:"+randomString(),
                                    "shesha1096@gmail.com", emailstring);
                        } catch (Exception e) {
                            Log.e("SendMail", e.getMessage(), e);
                        }
                    }
            }).start();
                input = new EditText(ForgotPassword.this);
                builder.setView(input);
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String otp = input.getText().toString();
                        if(Integer.parseInt(otp) == num){
                            startActivity(new Intent(ForgotPassword.this,ResetPassword.class));

                        }else{
                            Toast.makeText(ForgotPassword.this,"Wrong OTP",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                 dialog = builder.create();
                dialog.show();

            }
        });

    }

    private int randomString() {
       Random random = new Random();
        num = random.nextInt(1000);
       return num;
    }
}