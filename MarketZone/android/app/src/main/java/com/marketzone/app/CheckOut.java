package com.marketzone.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class CheckOut extends AppCompatActivity {
    final String apiURL = "http://10.40.46.65:3000/project/";
    final String[] apiCall = {"addNotification/"};//, "getBrands/"};
    double lat,lan;
    String UID;
    HttpURLConnection http;
    JSONArray apiIn;
    JSONObject msg;
    URL url;
    String oid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        Bundle bundle = getIntent().getExtras();
        float sum = bundle.getFloat("sum");
        UID = bundle.getString("UID");

        TextView total = (TextView) findViewById(R.id.total);
        total.setText(String.valueOf(sum));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getSupportActionBar().setHomeButtonEnabled(true);

        final Button address = (Button) findViewById(R.id.location);
        address.setEnabled(false);

        final Spinner branches = (Spinner) findViewById(R.id.branch);
        branches.setEnabled(false);

        List<String> list = new ArrayList<String>();
        list.add("Pick up from a branch");
        list.add("Deliver to my saved address");
        list.add("Deliver to [Select from Map]");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, list);
        final ListView listView = (ListView) findViewById(R.id.pick_delv);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setAdapter(adapter);
        ////////////////////////////////////////////////////////////////
        List<String> list2 = new ArrayList<String>();
        list2.add("Cash on Delivery");
        list2.add("Visa on Delivery");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, list2);
        final ListView listView2 = (ListView) findViewById(R.id.pay);
        listView2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView2.setAdapter(adapter2);
        listView2.setEnabled(false);

        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        address.setEnabled(false);
                        branches.setEnabled(true);
                        listView2.setEnabled(false);
                        listView2.clearChoices();
                        listView2.requestLayout();
                        //Inform database
                        break;
                    case 1:
                        //expand address and inform database
                        address.setEnabled(false);
                        branches.setEnabled(false);
                        listView2.setEnabled(true);
                        break;
                    case 2:
                        address.setEnabled(true);
                        branches.setEnabled(false);
                        listView2.setEnabled(true);
                        break;
                }
            }
        });

        listView2.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //inform database
                        break;
                    case 1:
                        //inform database
                        break;
                }
            }
        });

        Button clickButton = (Button) findViewById(R.id.confirm);
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNot();
                addOrder();

                // inflate the layout of the popup window
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.activity_popup, null);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(findViewById(android.R.id.content).getRootView(), Gravity.CENTER, 0, 0);

                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        });
        /////////////////////////////////////////////// Map
        Button button = (Button) findViewById(R.id.location);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPointOnMap();
            }
        });
    }

    private void addNot() {
        String url = "http://10.40.46.65:3000/project/addNotification/";
        Map<String, String> params = new HashMap();
        params.put("msg", "Order Placed");
        params.put("uid", UID);

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //TODO: handle success
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //TODO: handle failure
            }
        });

        Volley.newRequestQueue(this).add(jsonRequest);
    }

    private void addOrder() {


       new order_api().execute();





    }

    static final int PICK_MAP_POINT_REQUEST = 999;  // The request code
    private void pickPointOnMap() {
        Intent pickPointIntent = new Intent(this, MapsActivity.class);
        startActivityForResult(pickPointIntent, PICK_MAP_POINT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_MAP_POINT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                LatLng latLng = (LatLng) data.getParcelableExtra("picked_point");
                //Toast.makeText(this, "Point Chosen: " + latLng.latitude + " " + latLng.longitude, Toast.LENGTH_LONG).show();

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.activity_popup, null);
                TextView popText = (TextView) popupView.findViewById(R.id.popText);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popText.setText("Point Chosen: " + latLng.latitude + " " + latLng.longitude);
                lat = latLng.latitude;
                lan = latLng.longitude;

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(findViewById(android.R.id.content).getRootView(), Gravity.CENTER, 0, 0);

                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }



    private class order_api extends AsyncTask<String, Void, Void> {


        protected Void doInBackground(String... strings) {



            {


                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody formBody = new FormBody.Builder()
                            // A sample POST field
                            .add("PaymentMethod", "0")
                            .add("UID", UID)
                            .add("Status", "0")
                            .add("StreetAdd", "https://www.google.com/maps/@" + lat + "," + lan + "," + "15z")
                            .build();
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url("http://10.40.46.65:3000/project/addOrder") // The URL to send the data to
                            .post(formBody)
                            .build();
                    try {
                        client.newCall(request).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.v("okhttp", "works");

                } catch (Exception e) {
                    e.printStackTrace();
                }



                    try {
                        url = new URL("http://10.40.46.65:3000/project/getOrderlatest?UID="+UID);

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
                                oid = msg.getString("OrderId");

                            } else
                                Log.v("JSON Read", "Failed");

                            http.disconnect();
                            publishProgress();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    try {
                        url = new URL("http://10.40.46.65:3000/project/CartOrder?OrderId=" + oid + "&UID=" +UID);

                        http = (HttpURLConnection) url.openConnection();
                        http.setRequestMethod("GET");
                        http.connect();
                        if (http.getResponseCode() == 200) {
                            Log.v("API sentSMS Call:", "Succeeded");
                        } else
                            Log.v("API sentSMS Call:", "Failed");

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



        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);




        }
    }


}
