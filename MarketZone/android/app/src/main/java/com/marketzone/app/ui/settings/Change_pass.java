package com.marketzone.app.ui.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.marketzone.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class Change_pass extends AppCompatActivity {


    Button btn;
    EditText text, text2;
    String oldPass,newPass,dbPass;
    String call1, call2;

    String uid ;
    final String apiURL = "http://10.40.46.65:3000/project/";
    final String[] apiCall = {"getPassword?UID=" ,"changePassword?UID="};
    boolean btnpress = false;
    HttpURLConnection http;
    JSONArray apiIn;
    JSONObject msg;
    URL url;
    boolean success = false;
    Intent ext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        text = (EditText)findViewById(R.id.pass);
        text2 =(EditText)findViewById(R.id.new_pass);

        btn = (Button)findViewById(R.id.pass_submit_button);

        ext = this.getIntent();
        if(ext!=null)
        {


            uid = ext.getStringExtra( "UID");
        }







    }


    public void passclickHandler (View v)
    {

        if(v.getId() == R.id.pass_submit_button) {
            if(text.getText().toString().trim().length()>0 && text2.getText().toString().trim().length()>0) {


                btnpress = true;
                oldPass = text.getText().toString();


                newPass = text2.getText().toString();
                call1 = apiCall[0]+uid;

                call2 = apiCall[1]+uid+"&Password=" + newPass;
                new Passwordapi().execute(apiURL + call1,apiURL + call2);
                call1 = "";
                call2= "";

                text.setText("");
                text2.setText("");

            }
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private class Passwordapi extends AsyncTask<String, Void, Void> {

        protected Void doInBackground(String... strings) {

            if (btnpress) {
                try {
                    url = new URL(strings[0]);
                    http = (HttpURLConnection) url.openConnection();
                    http.setRequestMethod("GET");
                    http.connect();
                    if (http.getResponseCode() == 200) {
                        InputStream stream = new BufferedInputStream(http.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                        StringBuilder apiStream = new StringBuilder();

                        String inputString;
                        while ((inputString = bufferedReader.readLine()) != null) {
                            apiStream.append(inputString);
                        }

                        apiIn = new JSONArray(apiStream.toString());
                        if (apiIn.length() > 0) {
                            msg = apiIn.getJSONObject(0);
                            dbPass = msg.getString("Password");
                        }
                    } else
                        Log.v("JSON Read", "Failed");

                    http.disconnect();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                if (apiIn.length() > 0 && dbPass.equals(oldPass))
                {

                    try {

                        url = new URL(strings[1]);
                        Log.v("URL", strings[1]);
                        http = (HttpURLConnection) url.openConnection();
                        http.setRequestMethod("GET");
                        http.connect();
                        if (http.getResponseCode() == 200) {
                            Log.v("API changepass Call:", "Succeeded");
                        } else
                            Log.v("API changepass Call:", "Failed");
                        success = true;
                        http.disconnect();
                        publishProgress();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    publishProgress();
                }
            }

                return null;


        }


        @Override
        protected void onProgressUpdate(Void... arg) {
            super.onProgressUpdate();
            if(success) {
                Toast.makeText(Change_pass.this, "Password changed successfully.", Toast.LENGTH_LONG).show();
                finish();
            }
            else
                Toast.makeText(Change_pass.this,"You have entered an in-correct password",Toast.LENGTH_LONG).show();


        }


    }
}

