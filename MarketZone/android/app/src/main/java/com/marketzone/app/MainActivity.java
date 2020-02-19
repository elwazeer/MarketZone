package com.marketzone.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.marketzone.app.Cart.Cart;
import com.marketzone.app.Products.Filter;

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

public class MainActivity extends AppCompatActivity {
    HttpURLConnection http;
    JSONArray apiIn = new JSONArray();
    JSONObject msg = new JSONObject();
    URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                EditText username = (EditText)findViewById(R.id.login_username);
                EditText password = (EditText)findViewById(R.id.login_pw);
                if(TextUtils.isEmpty(username.getText()) || TextUtils.isEmpty(password.getText())) {
                    Toast.makeText(getApplicationContext(), "Please enter a username and password", Toast.LENGTH_LONG).show();
                }
                else{
                    if (view.getId() == R.id.login) {
                        new filterApi().execute(username.getText(), password.getText());
                    }
                }
            }
        });
    }

    public void myClickHandler(View target) {
        switch(target.getId()){
            case R.id.registerText:
                Intent intent = new Intent(getBaseContext(), signup.class);
                startActivity(intent);
                break;
        }
    }

    private class filterApi extends AsyncTask<Editable, Void, Void> {

        protected Void doInBackground(Editable... strings) {
            try {
                url = new URL("http://10.40.46.65:3000/project/login?UID=" + strings[0] + "&Password=" + strings[1]);
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
                    Log.v("Length", Integer.toString(apiIn.length()));
                    if (apiIn.length() > 0) {
                        msg = apiIn.getJSONObject(0);
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
                            }
                        });
                        cancel(true);
                    }
                    Log.v("Login Read", "Succeed");
                } else
                    Log.v("Login Read", "Failed");

                http.disconnect();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void arg) {
            Intent intent;
            try {
                if(msg.getInt("type") == 0) {
                    intent = new Intent(getBaseContext(), PostLog.class);
                    intent.putExtra("User JSON", msg.toString());
                }
                else{
                    intent = new Intent(getBaseContext(), Admin.class);
                }
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            cancel(true);
            finish();
        }
    }
    @Override
    public void onBackPressed() { }
}
