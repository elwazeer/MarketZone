package com.marketzone.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.marketzone.app.ui.orders.ordersList;
import com.marketzone.app.ui.settings.Change_name;

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
import java.util.List;

public class Admin extends AppCompatActivity {


    ListView list;
    List<orderListAdmin> orders = new ArrayList<orderListAdmin>();

    String uid ;
    final String apiURL = "http://10.40.46.65:3000/project/";
    String[] apiCall = {"getOrdersAdmin"};
    HttpURLConnection http;
    JSONArray apiIn;
    JSONObject msg, temp;
Intent intent;
    URL url;
    String call1;
    admin_ordersAdapter adapter;

    int j = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        list = (ListView) findViewById(R.id.admin_orders);


        adapter = new admin_ordersAdapter(this, R.layout.admin_orders, orders);


        new Admin_api().execute(apiURL + apiCall[0]);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), order_details.class);
                intent.putExtra("orderId", orders.get(position).getOrderno());
                intent.putExtra("userid", orders.get(position).getUser());
                startActivity(intent);

            }
        });

    }

    @Override
    public void onBackPressed() { }


    public void logoutHandler(View v) {
        if (v.getId() == R.id.admin_logout) {
            intent = new Intent(v.getContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }



    private class Admin_api extends AsyncTask<String, Void, Void> {


        protected Void doInBackground(String... strings) {


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


                        while (j < apiIn.length()) {
                            msg = apiIn.getJSONObject(j++);

                            String ts = msg.getString("ts");
                            ts = ts.substring(0, ts.indexOf('T'));

                            orders.add(new orderListAdmin(msg.getString("OrderId"), msg.getString("StreetAdd"),
                                    ts, msg.getString("UID")));


                        }


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

        @Override
        protected void onProgressUpdate(Void... arg) {
            super.onProgressUpdate();
            list.setAdapter(adapter);


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();

        }
    }
}



