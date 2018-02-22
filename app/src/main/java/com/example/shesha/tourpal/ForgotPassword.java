package com.example.shesha.tourpal;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
    private EditText enterednumber;
    private Button confirmbtn;
    private AlertDialog alertDialog;
    private AlertDialog.Builder dialog;
    private String mo;
    private SmsManager sms;
    private int otp;
    private EditText code;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        enterednumber = (EditText) findViewById(R.id.phonenoID);
        confirmbtn = (Button) findViewById(R.id.otpButtonID);
        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp = generateOTP();
                PendingIntent sentPI;
                String SENT = "SMS_SENT";

                String  DELIVERED = "SMS_DELIVERED";
                PendingIntent deliveredPI = PendingIntent.getBroadcast(ForgotPassword.this, 0,
                        new Intent(DELIVERED), 0);


                mo = "+91"+enterednumber.getText().toString();
                sentPI = PendingIntent.getBroadcast(ForgotPassword.this, 0,new Intent(SENT), 0);
                registerReceiver(new BroadcastReceiver(){
                    @Override
                    public void onReceive(Context arg0, Intent arg1) {
                        switch (getResultCode())
                        {
                            case Activity.RESULT_OK:
                                Toast.makeText(getBaseContext(), "SMS sent",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                Toast.makeText(getBaseContext(), "Generic failure",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_NO_SERVICE:
                                Toast.makeText(getBaseContext(), "No service",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_NULL_PDU:
                                Toast.makeText(getBaseContext(), "Null PDU",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_RADIO_OFF:
                                Toast.makeText(getBaseContext(), "Radio off",
                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }, new IntentFilter(SENT));
                registerReceiver(new BroadcastReceiver(){
                    @Override
                    public void onReceive(Context  arg0, Intent arg1) {
                        switch (getResultCode())
                        {
                            case Activity.RESULT_OK:
                                Toast.makeText(getBaseContext(), "SMS delivered",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case Activity.RESULT_CANCELED:
                                Toast.makeText(getBaseContext(), "SMS not delivered",
                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }, new IntentFilter(DELIVERED));
                String message = "Your OTP Is "+String.valueOf(otp);

                sms = SmsManager.getDefault();
                sms.sendTextMessage(mo,null,message,sentPI,deliveredPI);

                dialog = new AlertDialog.Builder(ForgotPassword.this);
                dialog.setTitle(getResources().getString(R.string.otp));
                dialog.setCancelable(false);
                View view = getLayoutInflater().inflate(R.layout.popup,null);
                dialog.setView(view);
                code = (EditText) view.findViewById(R.id.enteredCode);
                final String otpString = String.valueOf(otp);
                final String enteredcode = code.getText().toString();
                dialog.setPositiveButton(getResources().getString(R.string.verify), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(enteredcode.equals(otpString))
                        {
                            startActivity(new Intent(ForgotPassword.this,ResetPassword.class));
                        }
                        else
                        {
                            Toast.makeText(ForgotPassword.this,"Wrong OTP"+enteredcode,Toast.LENGTH_SHORT).show();
                            Log.d("OTP",otpString);
                            Log.d("OTP",enteredcode);

                        }


                    }
                });
                dialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                    alertDialog = dialog.create();
                    alertDialog.show();


            }
        });
    }

    private int generateOTP() {
        return new Random().nextInt(1000);
    }
}
