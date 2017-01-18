package com.itgc.foodsafety.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itgc.foodsafety.R;
import com.itgc.foodsafety.dao.Stores;
import com.itgc.foodsafety.fragment.StartAuditFragment;
import com.itgc.foodsafety.fragment.Store_DetailsFragment;
import com.itgc.foodsafety.utils.AppPrefrences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by root on 9/10/15.
 */
public class StoreAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Stores> stores;
    private LayoutInflater mInflater;
    private FragmentTransaction ft;

    public StoreAdapter(Context c, ArrayList<Stores> stores) {
        context = c;
        this.stores = stores;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return stores.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();

            convertView = mInflater.inflate(R.layout.store_view, null);
            holder.txt_store = (TextView) convertView.findViewById(R.id.txt_store);
            holder.txt_store_loc = (TextView) convertView.findViewById(R.id.txt_store_loc);
            holder.txt_details = (TextView) convertView.findViewById(R.id.txt_details);
            holder.lin_store = (LinearLayout) convertView.findViewById(R.id.lin_store);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.lin_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppPrefrences.getStartTime(context).equals(""))
                    AppPrefrences.setStartTime(context, getDateTime());

                AppPrefrences.setAuditId(context, "0");
                Fragment fragment = new StartAuditFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("Store_id", stores.get(position).getStore_id());
                bundle.putString("Store_name", stores.get(position).getStore_name());
                bundle.putString("Store_region", stores.get(position).getStore_loc());
                bundle.putString("merchant_id", stores.get(position).getMerchant_id());
                fragment.setArguments(bundle);
                ft.replace(R.id.container_body, fragment).addToBackStack("Store").commit();
            }
        });

        holder.txt_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Store_DetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("Store_id", stores.get(position).getStore_id());
                bundle.putString("Store_name", stores.get(position).getStore_name());
                bundle.putString("merchant_id", stores.get(position).getMerchant_id());
                fragment.setArguments(bundle);
                ft.replace(R.id.container_body, fragment).addToBackStack("Store").commit();
            }
        });

        holder.txt_store.setText(stores.get(position).getStore_name());
        holder.txt_store_loc.setText(stores.get(position).getStore_loc());

        return convertView;
    }

    private String getDateTime() {
        String formattedDate = new SimpleDateFormat("dd MMM yyyy kk:mm").format(Calendar.getInstance().getTime());
        return formattedDate;
    }

    public static class ViewHolder {
        TextView txt_store, txt_store_loc, txt_details;
        LinearLayout lin_store;

    }

}
