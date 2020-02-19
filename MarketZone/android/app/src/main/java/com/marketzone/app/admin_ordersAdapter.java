package com.marketzone.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

public class admin_ordersAdapter extends ArrayAdapter<orderListAdmin>{

    List<orderListAdmin> items;
    Context context;


    public admin_ordersAdapter(Context context, int resourceId, List<orderListAdmin> items) {
        super(context, resourceId, items);
        this.context = context;
        this.items = items;
    }

    private class ViewHolder {
        TextView Address;
        TextView Orderno;
        TextView time;
        TextView user;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final admin_ordersAdapter.ViewHolder holder;
        orderListAdmin rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.admin_orders, null);
            holder = new admin_ordersAdapter.ViewHolder();

            holder.Address = (TextView) convertView.findViewById(R.id.Status1);
            holder.Orderno = (TextView) convertView.findViewById(R.id.OrderNo_admin);
            holder.time = (TextView) convertView.findViewById(R.id.time1);
            holder.user = (TextView) convertView.findViewById(R.id.price_txt);

            convertView.setTag(holder);
        } else
            holder = (admin_ordersAdapter.ViewHolder) convertView.getTag();


        holder.Address.setText(rowItem.getAddress());
        holder.Orderno.setText(rowItem.getOrderno());
        holder.time.setText(rowItem.getTime());
        holder.user.setText(rowItem.getUser());




        return convertView;
    }


}

