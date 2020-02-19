package com.marketzone.app.Cart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.marketzone.app.CheckOut;
import com.marketzone.app.Products.ProductListAdapter;
import com.marketzone.app.Products.ProductListItem;
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

import static java.lang.Float.parseFloat;

public class Cart extends AppCompatActivity {
    ArrayList<String> items_count = CustomListViewAdapter.items_count;
    List<CustomList> cartList = new ArrayList<CustomList>();
    CustomListViewAdapter adapter;
    String UID;
    final String apiURL = "http://10.40.46.65:3000/project/";
    String[] apiCall = {"getProductsCart?UID=","getProduct?PID="};
    ListView listView;
    EditText comments;
    boolean display = true;
    List <String> tmp = new ArrayList<String>();
    List <Integer> qt = new ArrayList<Integer>();
    int j = 0;
    int index =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        comments = (EditText) findViewById(R.id.comments);

        Intent intent = getIntent();
        UID = intent.getStringExtra("UID");

        new apiConnect().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,apiURL + apiCall[0] + UID, apiURL + apiCall[1]);


    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
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


                            while (j < apiIn.length()) {
                                msg = apiIn.getJSONObject(j++);


                                tmp.add(index, msg.getString("PID"));
                                qt.add(index, msg.getInt("Quantity"));
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

            if(apiIn.length()>0 && display) {
                cartList = new ArrayList<CustomList>();
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

                                cartList.add(new CustomList(msg.getString("img"), msg.getString("Name"), msg.getString("Price"), msg.getInt("PID"), qt.get(i), UID));


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
        protected void onPostExecute(Void arg) {
            listView = (ListView) findViewById(R.id.first);
            adapter = new CustomListViewAdapter(getBaseContext(), R.layout.product_list_item, cartList);
            listView.setAdapter(adapter);

            final Button clickButton = (Button) findViewById(R.id.check_out);
            clickButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getBaseContext(), CheckOut.class);
                    boolean no_error = true;
                    comments.requestFocus();

                    float sum = 0;
                    for (int j = 0; j < cartList.size(); j++) {
                        sum = sum + (parseFloat(cartList.get(j).getPrice()) * cartList.get(j).getQuantity());
                    }

                    i.putExtra("sum", sum);
                    i.putExtra("UID", UID);
                    startActivity(i);
                }
            });
        }
    }
}
