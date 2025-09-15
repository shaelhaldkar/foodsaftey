package com.itgc.foodsafety.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.itgc.foodsafety.R;
import com.itgc.foodsafety.dao.Stores;
import com.itgc.foodsafety.fragment.StartAuditFragment;
import com.itgc.foodsafety.fragment.Store_DetailsFragment;
import com.itgc.foodsafety.utils.AppPrefrences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
            holder.txt_mercahnt_name=convertView.findViewById(R.id.txt_mercahnt_name);
            holder.txt_store_name = (TextView) convertView.findViewById(R.id.txt_store_name);
            holder.txt_address=(TextView)convertView.findViewById(R.id.txt_address);
            holder.txt_store_region = (TextView) convertView.findViewById(R.id.txt_store_region);
            holder.txt_audit_date=convertView.findViewById(R.id.txt_audit_date);
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

        String complete_store_with_address=stores.get(position).getStore_name();
        String[] parts = complete_store_with_address.split("Address");

        String store_name=parts[0];
        String Address=parts[1];
        holder.txt_store_name.setText(store_name);
        holder.txt_address.setText(Address);
        holder.txt_store_region.setText(stores.get(position).getStore_loc());
        holder.txt_mercahnt_name.setText(stores.get(position).getMerchant_name());
        holder.txt_audit_date.setText("Audit date: "+stores.get(position).getAudit_date());

        return convertView;
    }

    private String getDateTime() {
        String formattedDate = new SimpleDateFormat(
                "dd MMM yyyy kk:mm",
                Locale.ENGLISH          // force English month names
        ).format(Calendar.getInstance().getTime());
      //  String formattedDate = new SimpleDateFormat("dd MMM yyyy kk:mm").format(Calendar.getInstance().getTime());
        return formattedDate;
    }

    public static class ViewHolder {
        TextView txt_mercahnt_name,txt_store_name,txt_address, txt_store_region,txt_audit_date, txt_details;
        LinearLayout lin_store;

    }

}
