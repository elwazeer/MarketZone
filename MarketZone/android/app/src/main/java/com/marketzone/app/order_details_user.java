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

public class order_details_user extends AppCompatActivity {


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
  boolean Reorder =false;
  URL url;
  String call1;
  order_detailsAdapter adapter;

  int j = 0;
  int index =0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order_details_user);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    list = (ListView) findViewById(R.id.product_list_user);

    adapter = new order_detailsAdapter(this, R.layout.order_details_item, products);

    ext = this.getIntent();
    if(ext!=null)
    {

      oid = ext.getStringExtra( "orderId");
      uid = ext.getStringExtra( "userid");
    }



    new User_products_api().execute(apiURL + apiCall[0] + oid, apiURL + apiCall[1]);





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


  private class User_products_api extends AsyncTask<String, Void, Void> {


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






}






