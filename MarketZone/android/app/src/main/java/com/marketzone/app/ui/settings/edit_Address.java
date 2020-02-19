package com.marketzone.app.ui.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

public class edit_Address extends AppCompatActivity {

    EditText addr;
    String uid ;
    final String apiURL = "http://10.40.46.65:3000/project/";
    String[] apiCall = {"editAddress?UID="};
    HttpURLConnection http;
    JSONArray apiIn;
    JSONObject msg;
    URL url;
    String Addr, call1;
    boolean btnpress = false;
    Intent ext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addr = (EditText) findViewById(R.id.newaddr_disp);

        ext = this.getIntent();
        if(ext!=null)
        {


            uid = ext.getStringExtra( "UID");
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

    public void submitbuttonhandler(View view)
    {


        if(view.getId() == R.id.address_submit_btn && addr.getText().toString().length() > 0)
        {
          btnpress = true;
          Addr =  addr.getText().toString();
          addr.setText("");

            call1 = apiCall[0]+uid+"&Address=" + Addr;

            new editAddress_api().execute(apiURL + call1);
            btnpress = true;
            call1 ="";

        }


    }




    private class editAddress_api extends AsyncTask<String, Void, Void> {


        protected Void doInBackground(String... strings) {


                if (btnpress) {

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
                }

                return null;

            }



        @Override
        protected void onProgressUpdate(Void... arg) {
            super.onProgressUpdate();
            Toast.makeText(edit_Address.this,"Address Changed Successfully",Toast.LENGTH_LONG).show();



        }


    }


}

