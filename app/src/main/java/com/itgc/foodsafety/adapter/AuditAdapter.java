package com.itgc.foodsafety.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itgc.foodsafety.R;
import com.itgc.foodsafety.dao.Categories;

import java.util.ArrayList;

/**
 * Created by root on 9/10/15.
 */
public class AuditAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Categories> audit;
    private LayoutInflater mInflater;
    private int store_id;

    public AuditAdapter(Context c, ArrayList<Categories> audit, int store_id) {
        context = c;
        this.audit = audit;
        this.store_id = store_id;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return audit.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            convertView = mInflater.inflate(R.layout.audit_row_view, null);
            holder.txt_cat = (TextView) convertView.findViewById(R.id.txt_cat);
            holder.img_task = (ImageView) convertView.findViewById(R.id.img_task);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_cat.setText(audit.get(position).getCategoryName());

        try {
            if (audit.get(position).getCategoryStatus().equalsIgnoreCase("Complete"))
                holder.img_task.setImageDrawable(context.getResources().getDrawable(R.mipmap.check
                ));
            else if (audit.get(position).getCategoryStatus().equalsIgnoreCase("Incomplete"))
                holder.img_task.setImageDrawable(context.getResources().getDrawable(R.mipmap.pause
                ));
            else
            {
                Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
                holder.img_task.setImageDrawable(transparentDrawable);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public static class ViewHolder {
        TextView txt_cat;
        ImageView img_task;
    }

}
