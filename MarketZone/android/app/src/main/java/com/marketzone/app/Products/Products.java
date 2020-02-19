package com.marketzone.app.Products;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.marketzone.app.Cart.Cart;
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
import java.util.List;

public class Products extends AppCompatActivity {
    List<ProductListItem> cartList;
    private static final int FILTER_REQUEST_CODE = 0;
    String category = "Category", brand = "Brand", order = "name%20asc";
    String price = "10000";
    ListView listView;
    final String apiURL = "http://10.40.46.65:3000/project/";
    final String[] apiCall = {"filterSearch"};
    ProductListAdapter adapter;
    apiConnect api, api2;
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        UID = intent.getStringExtra("UID");
        SearchView searchView = (SearchView) findViewById(R.id.productSearch);
        searchView.onActionViewExpanded();
        searchView.clearFocus();
        api = new apiConnect();
        api.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,apiURL + apiCall[0] + "?category=" + category + "&brand=" + brand + "&price=" + price + "&order=" + order);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        else if(id == R.id.shoppingcart){
            Intent intent = new Intent(this, Cart.class);
            intent.putExtra("UID", UID);
            startActivity(intent);
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void myClickHandler(View target) {
        switch(target.getId()){
            case R.id.FilterBtn:
                Intent intent = new Intent(this, Filter.class);
                startActivityForResult(intent, FILTER_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that it is the SecondActivity with an OK result
        if (requestCode == FILTER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.v("Entered ", "onActivityResult");
                category = data.getStringExtra("Category").replaceAll(" ", "%20");
                Log.v("category ", category);
                brand = data.getStringExtra("Brand").replaceAll(" ", "%20");
                Log.v("brand ", brand);
                order = data.getStringExtra("Order").replaceAll(" ", "%20");
                Log.v("order ", order);
                price = data.getStringExtra("Price");
                Log.v("Api call", apiURL + apiCall[0] + "?category=" + category + "&brand=" + brand + "&price=" + price + "&order=" + order);
                api2 = new apiConnect();
                api2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,apiURL + apiCall[0] + "?category=" + category + "&brand=" + brand + "&price=" + price + "&order=" + order);
            }
        }
    }

    private class apiConnect extends AsyncTask<String, Void, Void> {
        HttpURLConnection http;
        JSONArray apiIn;
        JSONObject msg = new JSONObject();
        ArrayList<String> prodArray = new ArrayList<String>();
        ArrayList<String> priceArray = new ArrayList<String>();
        ArrayList<String> imgArray = new ArrayList<String>();
        ArrayList<Integer> pidArray = new ArrayList<Integer>();
        URL url;

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
                        for (int i = 0; i < apiIn.length(); i++) {
                            msg = apiIn.getJSONObject(i);
                            prodArray.add(msg.getString("Name"));
                            priceArray.add(Integer.toString(msg.getInt("Price")));
                            imgArray.add(msg.getString("img"));
                            pidArray.add(msg.getInt("PID"));
                        }
                    }
                    Log.v("JSON Read", "Succeed");
                } else
                    Log.v("JSON Read", "Failed");

                http.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void arg) {
            cartList = new ArrayList<ProductListItem>();
            for (int i = 0; i < prodArray.size(); i++) {
                ProductListItem item = new ProductListItem(imgArray.get(i), prodArray.get(i), priceArray.get(i), pidArray.get(i), UID);
                cartList.add(item);
            }

            listView = (ListView) findViewById(R.id.first);
            adapter = new ProductListAdapter(getBaseContext(), R.layout.product_list_item, cartList);
            listView.setAdapter(adapter);
        }
    }
}