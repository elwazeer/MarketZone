package com.marketzone.app.ui.orders;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.marketzone.app.PostLog;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.app.PendingIntent.getActivity;

public class ordersAdapter extends ArrayAdapter<ordersList>{

    List<ordersList> items;
    Context context;
    public static ArrayList added_items = new ArrayList();
    public static Integer clicks = 0;
    public PostLog mainActivity;
    boolean Reorder = false;
    URL url;
    HttpURLConnection http;
    JSONArray apiIn;
    JSONObject msg;
    String oid;


    public ordersAdapter(Context context, int resourceId, List<ordersList> items) {
        super(context, resourceId, items);
        this.context = context;
        this.items = items;

    }

    private class ViewHolder {
        TextView OrderId;
        TextView Status;
        TextView time;
        Button btn;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final ordersAdapter.ViewHolder holder;
        final ordersList rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.orders_list, null);
            holder = new ordersAdapter.ViewHolder();

            holder.Status = (TextView) convertView.findViewById(R.id.Status);
            holder.OrderId = (TextView) convertView.findViewById(R.id.OrderNo);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.btn = (Button)convertView.findViewById(R.id.orders_btn);

            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View position) {

                    Reorder = true;
                    new button_api().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, rowItem.getUserid(), rowItem.getAddress(), rowItem.getOrderId());

                }
            });

            convertView.setTag(holder);
        } else
            holder = (ordersAdapter.ViewHolder) convertView.getTag();





        holder.OrderId.setText(rowItem.getOrderId());
        holder.Status.setText(rowItem.getStatus());
        if(holder.Status.getText().equals("Delivered"))
            holder.btn.setText("Reorder");
        if(holder.Status.getText().equals("Preparing") || holder.Status.getText().equals("Cancelled"))
            holder.btn.setVisibility(View.GONE);



        holder.time.setText(rowItem.getTime());

        return convertView;
    }



    private class button_api extends AsyncTask<String, Void, Void> {


        protected Void doInBackground(String... strings) {



            {

                if (Reorder) {

                    try {
                        OkHttpClient client = new OkHttpClient();
                        RequestBody formBody = new FormBody.Builder()
                                // A sample POST field
                                .add("PaymentMethod", "0")
                                .add("UID", strings[0])
                                .add("Status", "0")
                                .add("StreetAdd", strings[1])
                                .build();
                        Request request = new Request.Builder()
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
                        url = new URL("http://10.40.46.65:3000/project/getOrderlatest?UID="+strings[0]);

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
                        url = new URL("http://10.40.46.65:3000/project/CartReOrder?OrderId=" + oid + "&UID=" +strings[0] + "&old=" + strings[2] );

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
        }

        @Override
        protected void onProgressUpdate(Void... arg) {
            super.onProgressUpdate();



        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast toast = Toast.makeText(getContext(), "New order is added", Toast.LENGTH_LONG);
            toast.show();



        }
    }



}
