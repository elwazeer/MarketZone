package com.marketzone.app.Products;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ProductListAdapter extends ArrayAdapter<ProductListItem> {

    List<ProductListItem> items;
    Context context;
    public static ArrayList added_items = new ArrayList();
    public static Integer clicks = 0;
    apiConnect api1;
    DownloadImageTask api2;

    public ProductListAdapter(Context context, int resourceId, List<ProductListItem> items) {
        super(context, resourceId, items);
        this.context = context;
        this.items = items;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView price;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final ProductListAdapter.ViewHolder holder;
        final ProductListItem rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.product_list_item, null);
            holder = new ProductListAdapter.ViewHolder();

            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);

            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);

            convertView.setTag(holder);
        } else
            holder = (ProductListAdapter.ViewHolder) convertView.getTag();

        Button remove_btn = (Button) convertView.findViewById(R.id.add_btn);
        remove_btn.setTag(position);

        remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View position) {
                clicks++;

                api1 = new apiConnect();
                api1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, rowItem.getUID(), Integer.toString(rowItem.getPID()));

            }
        });

        holder.txtTitle.setText(rowItem.getTitle());
        api2 = new DownloadImageTask(holder.imageView);
        api2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,rowItem.getImageId());
        holder.price.setText(rowItem.getPrice());

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

    private class apiConnect extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("UID", strings[0]) // A sample POST field
                    .add("PID", strings[1]) // Another sample POST field
                    .build();
            Request request = new Request.Builder()
                    .url("http://10.40.46.65:3000/project/addCart") // The URL to send the data to
                    .post(formBody)
                    .build();
            try {
                client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.v("okhttp", "works");
            return null;

        }

        @Override
        protected void onPostExecute(Void arg) {
            super.onPostExecute(arg);
            Toast toast = Toast.makeText(getContext(), "Added to Cart!", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
