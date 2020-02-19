package com.marketzone.app.Products;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Filter extends AppCompatActivity {
    final String apiURL = "http://10.40.46.6586:3000/project/";
    final String[] apiCall = {"getCategories/", "getBrands/"};
    int val = 10000;
    filterApi apiFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        apiFilter = new filterApi();
        apiFilter.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,apiURL + apiCall[0], apiURL + apiCall[1]);

        SeekBar sk = (SeekBar) findViewById(R.id.seekBar);
        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                TextView t=(TextView)findViewById(R.id.seekText);
                val = i;
                t.setText("<" + String.valueOf(i) + " EGP");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
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

    public void onButtonClick(View view){
        Intent intent = new Intent();
        Spinner catSpinner = (Spinner)findViewById(R.id.catSpinner);
        Spinner brandSpinner = (Spinner)findViewById(R.id.brandSpinner);
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
        RadioGroup group = (RadioGroup)findViewById(R.id.sortRadio);

        if(group.getCheckedRadioButtonId() == R.id.priceLHRadio)
            intent.putExtra("Order", "price asc");
        else if(group.getCheckedRadioButtonId() == R.id.priceHLRadio)
            intent.putExtra("Order", "price desc");
        else if(group.getCheckedRadioButtonId() == R.id.nameARadio)
            intent.putExtra("Order", "name asc");
        else if(group.getCheckedRadioButtonId() == R.id.nameDRadio)
            intent.putExtra("Order", "name desc");
        else
            intent.putExtra("Order", "name asc");

        if(catSpinner.getSelectedItemPosition() == 0)
            intent.putExtra("Category", "Category");
        else
            intent.putExtra("Category", "\"" + catSpinner.getSelectedItem().toString() + "\"");

        if(brandSpinner.getSelectedItemPosition() == 0)
            intent.putExtra("Brand", "Brand");
        else
            intent.putExtra("Brand", "\"" + brandSpinner.getSelectedItem().toString() + "\"");

        intent.putExtra("Price", Integer.toString(val));
        setResult(RESULT_OK, intent);
        finish();
    }

    private class filterApi extends AsyncTask<String, Void, Void> {
        HttpURLConnection http;
        JSONArray apiIn;
        JSONObject msg = new JSONObject();
        ArrayList<String> catArray = new ArrayList<String>();
        ArrayList<String> brandArray = new ArrayList<String>();
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
                    catArray.add("All Categories");
                    if (apiIn.length() > 0) {
                        for(int i = 0; i < apiIn.length(); i++) {
                            msg = apiIn.getJSONObject(i);
                            catArray.add(msg.getString("Category"));
                        }
                    }
                    Log.v("JSON Read2", "Succeed");
                }
                else
                    Log.v("JSON Read1", "Failed");

                http.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            try {
                url = new URL(strings[1]);
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
                    brandArray.add("All Brands");
                    if (apiIn.length() > 0) {
                        for(int i = 0; i < apiIn.length(); i++) {
                            msg = apiIn.getJSONObject(i);
                            brandArray.add(msg.getString("Brand"));
                        }
                    }
                    Log.v("JSON Read2", "Succeed");
                }
                else
                    Log.v("JSON Read2", "Failed");

                http.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void arg) {
            Spinner catSpinner = (Spinner)findViewById(R.id.catSpinner);
            Spinner brandSpinner = (Spinner)findViewById(R.id.brandSpinner);

            ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, catArray);
            ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, brandArray);
            catSpinner.setAdapter(catAdapter);
            brandSpinner.setAdapter(brandAdapter);

        }


    }

}
