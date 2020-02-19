package com.marketzone.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

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
import java.util.ArrayList;
import java.util.List;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class order_details extends AppCompatActivity {


    ListView list;
    List<productList> products = new ArrayList<productList>();

    String uid ;
    final String apiURL = "http://10.40.46.65:3000/project/";
    String[] apiCall = {"getProductsAdmin?OrderId=","getProduct?PID=", "addNotification","updateOrder?OrderId=" };
    HttpURLConnection http;
    JSONArray apiIn;
    JSONObject msg;
    List <String> tmp = new ArrayList<String>();
    String oid;
    Intent ext;
    boolean once = true;
    int tmplength;
    boolean display = false;
    Button notify, deliver, cancel;
    boolean send_notif = false, isDelivered = false, isCancelled =false;
    URL url;
    String call1;
    order_detailsAdapter adapter;

    int j = 0;
    int index =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list = (ListView) findViewById(R.id.product_list_admin);
        notify = (Button) findViewById(R.id.admin_notify);
        cancel = (Button) findViewById(R.id.cancelled_btn);
        deliver = (Button) findViewById(R.id.delivered_btn);

        adapter = new order_detailsAdapter(this, R.layout.order_details_item, products);

        ext = this.getIntent();
        if(ext!=null)
        {

            oid = ext.getStringExtra( "orderId");
            uid = ext.getStringExtra( "userid");
        }



        new Admin_products_api().execute(apiURL + apiCall[0] + oid, apiURL + apiCall[1]);





    }

    public void AdminClickHandler(View v)
    {
        if(v.getId()== R.id.admin_notify)
        {
                send_notif = true;
               new button_api().execute( apiURL + apiCall[2]);

        }

        if(v.getId()== R.id.cancelled_btn)
        {
            isCancelled = true;
           new  button_api().execute(apiURL + apiCall[3]+oid+"&Status=2");
        }

        if(v.getId()== R.id.delivered_btn)
        {
            isDelivered = true;
           new button_api().execute(apiURL + apiCall[3]+oid+"&Status=1");

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


    private class Admin_products_api extends AsyncTask<String, Void, Void> {


        protected Void doInBackground(String... strings) {



                if (once) {
                    try {
                        once = false;
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


                                    tmp.add(index, msg.getString("PID"));
                                    index++;


                                }

                                display = true;
                            }
                        } else
                            Log.v("JSON Read", "Failed");

                        http.disconnect();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (apiIn.length() > 0 && display) {

                    for (int i = 0; i < index; i++) {
                        try {

                            url = new URL(strings[1] + tmp.get(i));
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


                                    products.add(new productList(msg.getString("PID"), msg.getString("Price"),
                                            msg.getString("img"), msg.getString("Name")));


                                }


                            } else
                                Log.v("JSON Read", "Failed");

                            http.disconnect();
                            publishProgress();

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }


                return null;



        }

        @Override
        protected void onProgressUpdate(Void... arg) {
            super.onProgressUpdate();
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }
    }





private class button_api extends AsyncTask<String, Void, Void> {


    protected Void doInBackground(String... strings) {



            {


                if (send_notif) {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody formBody = new FormBody.Builder()
                            // A sample POST field
                            .add("msg", "Your order is on the way!")
                            .add("UID", uid)// Another sample POST field
                            .build();
                    Request request = new Request.Builder()
                            .url(strings[0]) // The URL to send the data to
                            .post(formBody)
                            .build();
                    try {
                        client.newCall(request).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.v("okhttp", "works");

                }

                if (isDelivered) {
                    try {
                        url = new URL(strings[0]);
                        Log.v("URL", strings[0]);
                        http = (HttpURLConnection) url.openConnection();
                        http.setRequestMethod("GET");
                        http.connect();
                        if (http.getResponseCode() == 200) {
                            Log.v("API Delivered Call:", "Succeeded");
                        } else
                            Log.v("API Delivered Call:", "Failed");

                        http.disconnect();
                        publishProgress();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (isCancelled) {
                    try {
                        url = new URL(strings[0]);
                        Log.v("URL", strings[0]);
                        http = (HttpURLConnection) url.openConnection();
                        http.setRequestMethod("GET");
                        http.connect();
                        if (http.getResponseCode() == 200) {
                            Log.v("API Cancelled Call:", "Succeeded");
                        } else
                            Log.v("API Cancelled Call:", "Failed");

                        http.disconnect();
                        publishProgress();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

                return null;



        }
    }

    @Override
    protected void onProgressUpdate(Void... arg) {
        super.onProgressUpdate();
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);


    }
}
}






