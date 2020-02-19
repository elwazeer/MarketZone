package com.marketzone.app.Cart;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.marketzone.app.Products.ProductListAdapter;
import com.marketzone.app.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CustomListViewAdapter extends ArrayAdapter<CustomList> {

    List<CustomList> items;
    Context context;
    public static ArrayList items_count = new ArrayList();
    public static Integer exist_items = ProductListAdapter.clicks; // According to cart array

    public CustomListViewAdapter(Context context, int resourceId, List<CustomList> items) {
        super(context, resourceId, items);
        this.context = context;
        this.items = items;

        for (int i = 0; i < 10; i++) items_count.add("");
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView price;
        EditText int_qt;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        CustomList rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.itemlistrow, null);
            holder = new ViewHolder();

            holder.int_qt = (EditText) convertView.findViewById(R.id.int_qt);

            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);

            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        Button remove_btn = (Button) convertView.findViewById(R.id.remove_btn);
        remove_btn.setTag(position);

        remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View position) {
                int pos = (int)position.getTag();
                holder.int_qt.clearFocus();

                exist_items = exist_items - 1;
                items_count.remove(pos);

                items.remove(pos);
                CustomListViewAdapter.this.notifyDataSetChanged();
            }
        });

        holder.int_qt.setText(Integer.toString(rowItem.getQuantity()));
        holder.int_qt.setId(position);

        holder.int_qt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    final int position = v.getId();
                    final EditText Caption = (EditText) v;
                    items_count.set(position, Caption.getText().toString());
                }
            }
        });

        holder.txtTitle.setText(rowItem.getTitle());
        new DownloadImageTask(holder.imageView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,rowItem.getImageId());
        holder.price.setText(rowItem.getPrice() + " EGP");

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