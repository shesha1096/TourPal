package com.example.shesha.tourpal;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.shesha.tourpal.language.LanguageCodeHelper;
import com.example.shesha.tourpal.language.Translator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class LanguageTranslator extends AppCompatActivity {
    private Spinner mySpinner;
    private EditText input;
    private TextView transoutput;
    private Button showresult;
    private String result ="";
    private Translate translator;
    // TODO: If you have your own Premium account credentials, put them down here:
    private static final String CLIENT_ID = "FREE_TRIAL_ACCOUNT";
    private static final String CLIENT_SECRET = "PUBLIC_SECRET";
    private static final String ENDPOINT = "http://api.whatsmate.net/v1/translation/translate";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_translator);
        mySpinner = (Spinner) findViewById(R.id.trans_spinner);
        input = (EditText) findViewById(R.id.trans_input);
        transoutput = (TextView) findViewById(R.id.trans_result);
        showresult = (Button) findViewById(R.id.trans_button);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LanguageTranslator.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.translate));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(arrayAdapter);
        showresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromLang = "en";
                String selectedLang = mySpinner.getSelectedItem().toString();
                String toLang = "";
                switch (selectedLang){
                    case "English":
                        toLang = "en";
                        break;
                    case "Marathi":
                        toLang = "mr";
                        break;
                    case "Hindi":
                        toLang = "hi";
                        break;
                    case "Kannada":
                        toLang = "kn";
                        break;
                    case "Tamil":
                        toLang = "ta";
                        break;
                    case "Telugu":
                        toLang = "te";
                        break;
                    case "Malayalam":
                        toLang = "ml";
                        break;

                }

                String text = input.getText().toString();
                try {
                     translator = new Translate(transoutput);
                     translator.execute(fromLang,toLang,text);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        Log.d("Resultant",result);
        transoutput.setText(result);

    }


        /**
         * Entry Point
         */
        // TODO: Specify your translation requirements here:


        /**
         * Sends out a WhatsApp message via WhatsMate WA Gateway.
         */
       /* public static String translate (String fromLang, String toLang, String text) throws Exception
        {
            // TODO: Should have used a 3rd party library to make a JSON string from an object
            String jsonPayload = new StringBuilder()
                    .append("{")
                    .append("\"fromLang\":\"")
                    .append(fromLang)
                    .append("\",")
                    .append("\"toLang\":\"")
                    .append(toLang)
                    .append("\",")
                    .append("\"text\":\"")
                    .append(text)
                    .append("\"")
                    .append("}")
                    .toString();

            URL url = new URL(ENDPOINT);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("X-WM-CLIENT-ID", CLIENT_ID);
            conn.setRequestProperty("X-WM-CLIENT-SECRET", CLIENT_SECRET);
            conn.setRequestProperty("Content-Type", "application/json");

            OutputStream os = conn.getOutputStream();
            os.write(jsonPayload.getBytes());
            os.flush();
            os.close();

            int statusCode = conn.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (statusCode == 200) ? conn.getInputStream() : conn.getErrorStream()
            ));
            String output;
            String res="";
            while ((output = br.readLine()) != null) {
                res+=output;



            }
            conn.disconnect();
            return res;
        }*/
        public class Translate extends AsyncTask<String,String,String>{
            public TextView textView;
            public Translate(TextView textView){
                this.textView = textView;
            }

            @Override
            protected String doInBackground(String... strings) {
                String fromLang = (String) strings[0];
                String toLang = (String) strings[1];
                String text = (String) strings[2];
                String jsonPayload = new StringBuilder()
                        .append("{")
                        .append("\"fromLang\":\"")
                        .append(fromLang)
                        .append("\",")
                        .append("\"toLang\":\"")
                        .append(toLang)
                        .append("\",")
                        .append("\"text\":\"")
                        .append(text)
                        .append("\"")
                        .append("}")
                        .toString();

                URL url = null;
                try {
                    url = new URL(ENDPOINT);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                conn.setDoOutput(true);
                try {
                    conn.setRequestMethod("POST");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }
                conn.setRequestProperty("X-WM-CLIENT-ID", CLIENT_ID);
                conn.setRequestProperty("X-WM-CLIENT-SECRET", CLIENT_SECRET);
                conn.setRequestProperty("Content-Type", "application/json");

                OutputStream os = null;
                try {
                    os = conn.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    os.write(jsonPayload.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int statusCode = 0;
                try {
                    statusCode = conn.getResponseCode();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(
                            (statusCode == 200) ? conn.getInputStream() : conn.getErrorStream()
                    ));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String output;
                String res="";
                try {
                    while ((output = br.readLine()) != null) {
                        res += output;


                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                conn.disconnect();
                Log.d("ResultTrans",res);
                result = res;
                return res;



            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                textView.setText(s);
            }
        }


}