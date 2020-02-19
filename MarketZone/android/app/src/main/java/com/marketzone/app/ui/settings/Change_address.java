package com.marketzone.app.ui.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.marketzone.app.PostLog;
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
import java.util.ArrayList;

public class Change_address extends AppCompatActivity {

    TextView addr;
    String uid ;
    final String apiURL = "http://10.40.46.65:3000/project/";
    String[] apiCall = {"getAddress?UID="};
    HttpURLConnection http;
    JSONArray apiIn;
    JSONObject msg;
    URL url;
    String Addr;
    Intent ext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);
        addr = (TextView) findViewById(R.id.addr_disp);

        ext = this.getIntent();
        if(ext!=null)
        {


            uid = ext.getStringExtra( "UID");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new getAddress().execute(apiURL + apiCall[0] +uid);


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


    public void buttonhandler(View view)
    {


        if(view.getId() == R.id.edit_btn)
        {
            Intent intent = new Intent(view.getContext(), edit_Address.class);
           intent.putExtra("UID",uid);

            startActivity(intent);
            finish();
        }


    }




    private class getAddress extends AsyncTask<String, Void, Void> {


        protected Void doInBackground(String... strings) {

            while(true) {
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
                  Addr = msg.getString("Address");
              }
          } else
              Log.v("JSON Read", "Failed");

          http.disconnect();

          publishProgress();
         } catch (IOException | JSONException e) {
             e.printStackTrace();
      }


      return null;


  }
        }

        @Override
        protected void onProgressUpdate(Void... arg) {
            super.onProgressUpdate();
            addr.setText(Addr);



        }


    }


}
