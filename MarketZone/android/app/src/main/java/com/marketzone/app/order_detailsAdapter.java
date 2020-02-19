package com.marketzone.app;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;


public class order_detailsAdapter extends ArrayAdapter<productList>{

    List<productList> items;
    Context context;
    public static ArrayList added_items = new ArrayList();
    public static Integer clicks = 0;

    DownloadImageTask api2;


    public order_detailsAdapter(Context context, int resourceId, List<productList> items) {
        super(context, resourceId, items);
        this.context = context;
        this.items = items;
    }

    private class ViewHolder {
        TextView PID;
        TextView name;
        TextView price;
        ImageView img;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final order_detailsAdapter.ViewHolder holder;
        productList rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.order_details_item, null);
            holder = new order_detailsAdapter.ViewHolder();

            holder.PID = (TextView) convertView.findViewById(R.id.PID_admin);
            holder.name = (TextView) convertView.findViewById(R.id.pname_info);
            holder.price = (TextView) convertView.findViewById(R.id.price_txt);
            holder.img = (ImageView) convertView.findViewById(R.id.produc_pic);

            convertView.setTag(holder);
        } else
            holder = (order_detailsAdapter.ViewHolder) convertView.getTag();


        holder.PID.setText(rowItem.getPID());
        holder.name.setText(rowItem.getName());
        holder.price.setText(rowItem.getPrice()+ " L.E");
        api2 = new DownloadImageTask(holder.img);
        api2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,rowItem.getImg());





        return convertView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            Log.v("img", "works");
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


}