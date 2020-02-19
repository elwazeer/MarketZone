package com.marketzone.app.ui.notify;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.marketzone.app.R;

import java.util.List;


public class notifications_list_adapter extends ArrayAdapter<notifyListItem>  {
    List<notifyListItem> items;
    Context context;

    public notifications_list_adapter(Context context, int resourceId, List<notifyListItem> items) {
        super(context, resourceId, items);
        this.context = context;
        this.items = items;
    }

    private class ViewHolder {
        TextView txtTitle;
        TextView price;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final notifications_list_adapter.ViewHolder holder;
        notifyListItem rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.notify_list_item, null);
            holder = new notifications_list_adapter.ViewHolder();

            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);

            convertView.setTag(holder);
        } else
            holder = (notifications_list_adapter.ViewHolder) convertView.getTag();

        holder.txtTitle.setText(rowItem.getTitle());
        holder.price.setText(rowItem.getPrice());

        return convertView;
    }
}
