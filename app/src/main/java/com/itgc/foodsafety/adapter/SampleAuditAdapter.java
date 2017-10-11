package com.itgc.foodsafety.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itgc.foodsafety.R;
import com.itgc.foodsafety.dao.SampleDetails;
import com.itgc.foodsafety.db.DBHelper;
import com.itgc.foodsafety.db.DbManager;
import com.itgc.foodsafety.fragment.AuditStartFragment;

import java.util.ArrayList;

/**
 * Created by Farhan on 5/29/17.
 */

public class SampleAuditAdapter extends RecyclerView.Adapter<SampleAuditAdapter.Samples>
{
    ArrayList<SampleDetails> samples;
    AuditStartFragment fragment;
    Context c;
    int progressChanged = 0;
    float trueprogress = 0;
    int storeId,categoryId,questionId;

    public SampleAuditAdapter(ArrayList<SampleDetails> samples, Context c, int storeId, int categoryId, int questionId, AuditStartFragment auditStartFragment) {
        this.samples = samples;
        this.c = c;
        this.storeId = storeId;
        this.categoryId = categoryId;
        this.questionId = questionId;
        this.fragment=auditStartFragment;
    }

    @Override
    public Samples onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(c).inflate(R.layout.lin_sample,parent,false);
        return new Samples(v);
    }

    @Override
    public void onBindViewHolder(Samples holder, int position)
    {
        holder.txt_sample.setText("Sample " + (position+1));
//        String query="SELECT * FROM " + DBHelper.AUDIT_SAMPLE_TBL_NAME +" WHERE " + DBHelper.STORE_ID +"=" + storeId + " AND " + DBHelper.CATEGORY_ID +"=" + categoryId + " AND " + DBHelper.QUESTION_ID +"=" + questionId + " AND " + DBHelper.SAMPLE_POS +"=" + position;
//        Cursor c=DbManager.getInstance().getDetails(query);
//        Log.e("Sample Audit ",query + " Result " +c.getCount());
//        if(c.getCount()>0)
//        {
//            //holder.edt_rate.setText(c.getString(c.getColumnIndex(DBHelper.STORE_ID)));
//            holder.progressBar.setProgress(0);
//        }else
//        {
//            holder.edt_rate.setText("0.0");
//            holder.progressBar.setProgress(0);
//        }
        holder.edt_rate.setText(samples.get(position).getSampleCurrentRate()+"");
        holder.progressBar.setProgress(samples.get(position).getSampleCurrentRate()*2);

    }

    @Override
    public int getItemCount() {
        return samples.size();
    }

    public class Samples extends RecyclerView.ViewHolder
    {
        TextView txt_sample;
        EditText edt_rate;
        SeekBar progressBar;
        Button btn_rate;
        public Samples(View itemView)
        {
            super(itemView);
            edt_rate=(EditText)itemView.findViewById(R.id.edt_rate);
            txt_sample=(TextView)itemView.findViewById(R.id.txt_sample);
            progressBar=(SeekBar)itemView.findViewById(R.id.seek_bar);
            btn_rate=(Button)itemView.findViewById(R.id.btn_rate);
            btn_rate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int rate=Double.valueOf(edt_rate.getText().toString()).intValue();
                    if(rate<=10){
                        progressBar.setProgress(rate*2);
                        fragment.updateSamples(getAdapterPosition(),rate);
                    }else {
                        Toast.makeText(c, "Value should not more than 10", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b)
                {
                    progressChanged = i;
                    float j, k;
                    j = (float) i;
                    k = 2;
                    trueprogress = (j / k);
                    //Log.e("Progress Position ", getAdapterPosition() + " Progress" +trueprogress+"   " +i);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    edt_rate.setText(trueprogress+"");
                    fragment.updateSamples(getAdapterPosition(),(int)trueprogress);
                }
            });

//            progressBar.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent)
//                {
//                    fragment.updateSamplesRateX(getAdapterPosition(),(int)motionEvent.getX());
//                    float trans = motionEvent.getX() - holder.seekBar.getWidth() / 2;
//                    float limit = holder.seekBar.getWidth();
//
//                    switch (motionEvent.getAction()) {
//                        case MotionEvent.ACTION_MOVE:
//                            if (trans >= 0 && trans < limit) {
//                                holder.view_state.setTranslationX(trans);
//                                break;
//                            }
//                        case MotionEvent.ACTION_DOWN:
//                            if (trans >= 0 && trans < limit) {
//                                holder.view_state.setTranslationX(trans);
//                                break;
//                            }
//                        case MotionEvent.ACTION_UP:
//                            if (trans >= 0 && trans < limit) {
//                                holder.view_state.setTranslationX(trans);
//                                break;
//                            }
//                    }
//                    return false;
//                }
//            });

        }
    }
}
