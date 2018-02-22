package com.example.shesha.tourpal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ResetPassword extends AppCompatActivity {
    private EditText pwd;
    private EditText repwd;
    private Button resetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        pwd = (EditText) findViewById(R.id.newpwd);
        repwd = (EditText) findViewById(R.id.newpwdsecond);
        resetBtn = (Button) findViewById(R.id.resetBtnID);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newpwd = pwd.getText().toString();
                String newpwdsecond = repwd.getText().toString();
                if(newpwd.equals(newpwdsecond)){
                    Toast.makeText(ResetPassword.this,"New Password has been set",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ResetPassword.this,HomePage.class));
                }
                else{
                    Toast.makeText(ResetPassword.this,"Please Set a Proper Password",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}