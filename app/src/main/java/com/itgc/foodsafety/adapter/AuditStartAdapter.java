package com.itgc.foodsafety.adapter;

/**
 * Created by root on 24/10/15.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itgc.foodsafety.R;
import com.itgc.foodsafety.dao.Sample_Audit;

import java.util.ArrayList;

public class AuditStartAdapter extends RecyclerView
        .Adapter<AuditStartAdapter
        .DataObjectHolder> {
    private ArrayList<Sample_Audit> sampleAudits;
    private static MyClickListener myClickListener;
    private Context context;
    private int size;
    private float rate_x;
    private float rate;
    int progressChanged = 0;
    float trueprogress = 0;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        private final int width;
        TextView txt_sample, txt_min, txt_max, textView;
        EditText edt_rate;
        SeekBar seekBar;
        View view_state;
        Button btn_rate;

        public DataObjectHolder(View itemView) {
            super(itemView);

            txt_sample = (TextView) itemView.findViewById(R.id.txt_sample);
            txt_min = (TextView) itemView.findViewById(R.id.txt_min);
            txt_max = (TextView) itemView.findViewById(R.id.txt_max);
            textView = (TextView) itemView.findViewById(R.id.progress_state);

            edt_rate = (EditText) itemView.findViewById(R.id.edt_rate);

            seekBar = (SeekBar) itemView.findViewById(R.id.seek_bar);

            btn_rate = (Button) itemView.findViewById(R.id.btn_rate);


            View info_win = (View) itemView.findViewById(R.id.layout_state);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            info_win.setLayoutParams(params);

            info_win.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            width = info_win.getMeasuredWidth();

            view_state = (View) itemView.findViewById(R.id.layout_state);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                myClickListener.onItemClick(getPosition(), v);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public AuditStartAdapter(Context c, ArrayList<Sample_Audit> sampleAudits, int size) {
        this.sampleAudits = sampleAudits;
        this.context = c;
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lin_sample, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        final int pos = position + 1;
        holder.txt_sample.setText(context.getResources().getText(R.string.audit_sample) + " " + pos);

        try {
            int progress = (int) sampleAudits.get(position).getSample_current_rate();
            holder.seekBar.setOnSeekBarChangeListener(null);
            holder.seekBar.setProgress(progress * 2);

            holder.txt_max.setText("" + sampleAudits.get(position).getSample_current_rate());
            holder.edt_rate.setText("" + sampleAudits.get(position).getSample_current_rate());

            float trans = sampleAudits.get(position).getRate_x() - holder.width / 2;
            if (sampleAudits.get(position).getRate_x() > 0)
                holder.view_state.setTranslationX(trans);
            else
                holder.view_state.setTranslationX(0);

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.btn_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.edt_rate.getText().toString() != null && !holder.edt_rate.getText().toString().isEmpty()) {
                    try {
                        rate = Float.valueOf(holder.edt_rate.getText().toString());
                        if (rate > 10) {
                            Toast.makeText(context, "Please rate it between 1 to 10", Toast.LENGTH_LONG).show();
                            holder.edt_rate.setText("");
                            holder.seekBar.setProgress(0);
                            holder.txt_max.setText("");
                            holder.view_state.setTranslationX(0);
                        } else if (rate == 0) {
                            Toast.makeText(context, "Please enter rate properly", Toast.LENGTH_LONG).show();
                        } else {
                            final int progress = (int) rate;
                            holder.seekBar.setProgress(progress * 2);
                            holder.txt_max.setText("" + rate);

                            Sample_Audit sample_audit = new Sample_Audit();
                            sample_audit.setSample_count(pos);
                            sample_audit.setSample_current_rate(rate);
                            sample_audit.setRate_x(rate_x);

                            try {
                                sampleAudits.set(position, sample_audit);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        holder.seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                rate_x = motionEvent.getX();
                float trans = motionEvent.getX() - holder.seekBar.getWidth() / 2;
                float limit = holder.seekBar.getWidth();

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if (trans >= 0 && trans < limit) {
                            holder.view_state.setTranslationX(trans);
                            break;
                        }
                    case MotionEvent.ACTION_DOWN:
                        if (trans >= 0 && trans < limit) {
                            holder.view_state.setTranslationX(trans);
                            break;
                        }
                    case MotionEvent.ACTION_UP:
                        if (trans >= 0 && trans < limit) {
                            holder.view_state.setTranslationX(trans);
                            break;
                        }
                }
                return false;

            }
        });

        holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
                float j, k;
                j = (float) progress;
                k = 2;
                trueprogress = (j / k);

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }


            public void onStopTrackingTouch(SeekBar seekBar) {
                Sample_Audit sample_audit = new Sample_Audit();
                sample_audit.setSample_count(pos);
                sample_audit.setSample_current_rate(trueprogress);
                sample_audit.setRate_x(rate_x);

                try {
                    sampleAudits.set(position, sample_audit);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                notifyItemChanged(position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return size;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}