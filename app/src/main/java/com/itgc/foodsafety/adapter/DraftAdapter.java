package com.itgc.foodsafety.adapter;

/**
 * Created by root on 24/10/15.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itgc.foodsafety.R;
import com.itgc.foodsafety.dao.Drafts;

import java.util.ArrayList;

public class DraftAdapter extends RecyclerView.Adapter<DraftAdapter.DataObjectHolder>
{
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<Drafts> mDataset;
    private static MyClickListener myClickListener;
    private Context context;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView txt_surveyid, txt_store, txt_date;
        ImageView imgarr, img_task;

        public DataObjectHolder(View itemView)
        {
            super(itemView);
            txt_surveyid = (TextView) itemView.findViewById(R.id.txt_surveyid);
            txt_store = (TextView) itemView.findViewById(R.id.txt_store);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            imgarr = (ImageView) itemView.findViewById(R.id.imgarr);
            img_task = (ImageView) itemView.findViewById(R.id.img_task);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            myClickListener.onItemClick(getPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener)
    {
        this.myClickListener = myClickListener;
    }

    public DraftAdapter(ArrayList<Drafts> myDataset, Context context)
    {
        mDataset = myDataset;
        this.context = context;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.draft_row_view, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position)
    {
        holder.txt_surveyid.setText(String.valueOf(mDataset.get(position).getDraft_surveyid()));
        holder.txt_store.setText(Html.fromHtml(mDataset.get(position).getDraft_storename() + "<small>" + "<font color='#AEAEAE'> (" + mDataset.get(position).getDraft_storeregion() + ") </font></small>"));
        holder.txt_date.setText(mDataset.get(position).getDraft_date());

        if (mDataset.get(position).getDraft_status().equals("Complete"))
            holder.img_task.setImageDrawable(context.getResources().getDrawable(R.mipmap.check));
        else
            holder.img_task.setImageDrawable(context.getResources().getDrawable(R.mipmap.pause));
    }

    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }

    public interface MyClickListener
    {
        public void onItemClick(int position, View v);
    }
}