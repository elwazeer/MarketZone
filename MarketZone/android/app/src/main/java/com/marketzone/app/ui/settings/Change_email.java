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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Change_email extends AppCompatActivity {
    Button btn;
    EditText text;
    String email,call1;


    String uid ;
    final String apiURL = "http://10.40.46.65:3000/project/";
    String[] apiCall = {"changeEmail?UID="};
    boolean btnpress = false;
    HttpURLConnection http;

    URL url;
    Intent ext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        text = (EditText)findViewById(R.id.email);

        btn = (Button)findViewById(R.id.submit_button);
        ext = this.getIntent();
        if(ext!=null)
        {


            uid = ext.getStringExtra( "UID");
        }







    }


    public void clickHandler (View v)
    {

            if(v.getId() == R.id.submit_button)
              email   = text.getText().toString();

        call1 = apiCall[0]+uid+"&Email=" + email;

        new Changeemail_api().execute(apiURL + call1);
        btnpress = true;
        text.setText("");
            text.setText("");
            call1 ="";
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



    private class Changeemail_api extends AsyncTask<String, Void, Void> {





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
                            Log.v("API changeEmailCall:", "Succeeded");
                        } else
                            Log.v("API changeEmail Call:", "Failed");


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
            Toast.makeText(Change_email.this,"Email Changed Successfully",Toast.LENGTH_LONG).show();
            finish();


        }


    }
}
