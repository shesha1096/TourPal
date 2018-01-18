package com.example.shesha.tourpal;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class LoginAndSignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView forgotpassword = (TextView) findViewById(R.id.forgotPasswordID);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_sign_up);
        forgotpassword.setPaintFlags(forgotpassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

    }
}
