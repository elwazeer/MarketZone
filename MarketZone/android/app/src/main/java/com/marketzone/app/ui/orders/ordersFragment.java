package com.marketzone.app.ui.orders;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.marketzone.app.Cart.Cart;
import com.marketzone.app.PostLog;
import com.marketzone.app.R;
import com.marketzone.app.orderListAdmin;
import com.marketzone.app.order_details;
import com.marketzone.app.order_details_user;

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

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ordersFragment extends Fragment {
    List<ordersList> my_orders = new ArrayList<ordersList>();

    ListView listView;
    private ordersViewModel ovm1;
    public String uid;
    public String addr;

    public PostLog mainActivity;
    boolean Reorder = false;






    final String apiURL = "http://10.40.46.65:3000/project/";
    String[] apiCall = {"getOrders?UID="};
    HttpURLConnection http;
    JSONArray apiIn;
    JSONObject msg, temp;
    Intent intent;
    URL url;
    String call1;
     ordersAdapter adapter;
     Button ordering;


    int j = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ovm1 = ViewModelProviders.of(this).get(ordersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_myorders, container, false);


        mainActivity = (PostLog)getActivity();
        uid = mainActivity.UID;

        listView = (ListView) root.findViewById(R.id.first);
       adapter = new ordersAdapter(getActivity(), R.layout.orders_list, my_orders);
       new myorders_api().execute(apiURL + apiCall[0] +uid);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), order_details_user.class);
                intent.putExtra("orderId", my_orders.get(position).getOrderId());
                intent.putExtra("userid", uid);
                startActivity(intent);

            }
        });






        return root;
    }






    private class myorders_api extends AsyncTask<String, Void, Void> {


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
                            String realSt ="";

                            String ts = msg.getString("ts");
                            ts = ts.substring(0, ts.indexOf('T'));
                            String tempSt = msg.getString("Status");
                                    if(tempSt.equals("1"))
                                        realSt= "Delivered";
                                     if(tempSt.equals("0"))
                                        realSt= "Preparing";
                                    if(tempSt.equals("2"))
                                        realSt= "Cancelled";


                         my_orders.add(new ordersList(msg.getString("OrderId"),realSt,
                                ts, msg.getString("UID"), msg.getString("StreetAdd")));


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
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();



        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

}