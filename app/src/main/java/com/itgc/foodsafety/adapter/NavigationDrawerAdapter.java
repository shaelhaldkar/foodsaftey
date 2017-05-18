package com.itgc.foodsafety.adapter;

/**
 * Created by Ravi on 29/07/15.
 */

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itgc.foodsafety.R;
import com.itgc.foodsafety.dao.NavDrawerItem;
import com.itgc.foodsafety.db.DbManager;
import com.itgc.foodsafety.utils.AppPrefrences;

import java.util.Collections;
import java.util.List;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
        if (current.getTitle().equals("Drafts") || current.getTitle().equals("Reports")) {
            holder.count.setVisibility(View.VISIBLE);
            if (current.getTitle().equals("Drafts"))
                holder.count.setText(String.valueOf(getCount()));
            else if (current.getTitle().equals("Reports"))
                holder.count.setText(String.valueOf(AppPrefrences.getReportCount(context)));
        } else
            holder.count.setVisibility(View.GONE);

    }

    public int getCount() {
        int count = 0;

        Cursor curs = null;
        String query = "";

        try {
            query = "SELECT * FROM answer where ans_status = 'Complete' OR ans_status = 'Incomplete' ";

            DbManager.getInstance().openDatabase();
            curs = DbManager.getInstance().getDetails(query);

            count = curs.getCount();
            Log.d("Curs_count", "" + count);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, count;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            count = (TextView) itemView.findViewById(R.id.count);
        }
    }
}
