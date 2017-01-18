package com.itgc.foodsafety.adapter;

/**
 * Created by root on 24/10/15.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.itgc.foodsafety.R;
import com.itgc.foodsafety.dao.Reports;

import java.util.ArrayList;

public class ReportAdapter extends RecyclerView
        .Adapter<ReportAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<Reports> mDataset;
    private static MyClickListener myClickListener;
    private Context context;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView nametextView1, datetextView1, statustextView1;
        Button downloadReportButton;

        public DataObjectHolder(View itemView) {
            super(itemView);
            nametextView1 = (TextView) itemView.findViewById(R.id.nametextView1);
            datetextView1 = (TextView) itemView.findViewById(R.id.datetextView1);
            statustextView1 = (TextView) itemView.findViewById(R.id.statustextView1);

            downloadReportButton = (Button) itemView.findViewById(R.id.downloadReportButton);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try{
                myClickListener.onItemClick(getPosition(), v);
            }
            catch (Exception e){

            }
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public ReportAdapter(ArrayList<Reports> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reportview, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {
        holder.nametextView1.setText(mDataset.get(position).getReport_id());
        holder.datetextView1.setText(mDataset.get(position).getReport_date());
        holder.statustextView1.setText(mDataset.get(position).getReport_status());

        holder.downloadReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataset.get(position).getReport_link().isEmpty()) {
                    Toast.makeText(context, "Report has not been generated currently", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mDataset.get(position).getReport_link()));
                        context.startActivity(browserIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}