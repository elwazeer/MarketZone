package com.marketzone.app.ui.settings;

import androidx.appcompat.app.AppCompatActivity;

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

public class Change_name extends AppCompatActivity {

    Button btn;
    EditText firstnametext;
    EditText lnametext;
    String fname_value;
    String lname_value;
    String uid;
    final String apiURL = "http://10.40.46.65:3000/project/";
     String[] apiCall = {"changeName?UID="};
    boolean btnpress = false;
    HttpURLConnection http;
    JSONArray apiIn;
    JSONObject[] msg;
    URL url;
    Intent ext;
    String call1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firstnametext = (EditText)findViewById(R.id.First_name);
        lnametext = (EditText)findViewById(R.id.Last_name);

        btn = (Button)findViewById(R.id.namesubmit_button);

        ext = this.getIntent();
        if(ext!=null)
        {


            uid = ext.getStringExtra( "UID");
        }





    }



    private class Changename_api extends AsyncTask<String, Void, Void> {





        protected Void doInBackground(String... strings) {

            while (true) {

                if(btnpress) {
                    try {


                        url = new URL(strings[0]);
                        Log.v("URL", strings[0]);
                        http = (HttpURLConnection) url.openConnection();
                        http.setRequestMethod("GET");
                        http.connect();
                        if (http.getResponseCode() == 200) {
                            Log.v("API changeName Call:", "Succeeded");
                        } else
                            Log.v("API changeName Call:", "Failed");


                        btnpress = false;
                        http.disconnect();
                        publishProgress();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return null;
                }

            }
        }

        @Override
        protected void onProgressUpdate(Void... arg) {
            super.onProgressUpdate();
            Toast.makeText(Change_name.this,"Name Changed Successfully",Toast.LENGTH_LONG).show();
            finish();

        }


    }



    public void nameclickHandler (View v) {

        if (v.getId() == R.id.namesubmit_button && firstnametext.getText().toString().length() > 0) {

            fname_value = firstnametext.getText().toString();
            firstnametext.setText("");
            lname_value = lnametext.getText().toString();
            lnametext.setText("");
            call1= apiCall[0]+uid+"&FirstName=" + fname_value +"&LastName=" +lname_value;

            new Changename_api().execute(apiURL +call1);
            btnpress = true;
            call1 = "";
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
}
