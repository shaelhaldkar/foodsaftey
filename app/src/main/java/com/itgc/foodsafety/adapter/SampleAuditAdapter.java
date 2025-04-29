package com.itgc.foodsafety.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.itgc.foodsafety.R;
import com.itgc.foodsafety.dao.SampleDetails;
import com.itgc.foodsafety.db.DBHelper;
import com.itgc.foodsafety.db.DbManager;
import com.itgc.foodsafety.fragment.AuditStartFragment;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

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
    String mfdpkd, mfd_date=null,bb_exp,bb_expdate=null;
    String no_samples_product="",product_name="",brand_name="",temperature="0";
    int Selflife_value=0,sample_fail_value=0;

    public SampleAuditAdapter(ArrayList<SampleDetails> samples, Context c, int storeId, int categoryId, int questionId, AuditStartFragment auditStartFragment) {
        this.samples = samples;
        this.c = c;
        this.fragment=auditStartFragment;
    }

    @Override
    public Samples onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(c).inflate(R.layout.lin_sample,parent,false);
        return new Samples(v);
    }

    @Override
    public void onBindViewHolder(final Samples holder, int position)
    {
        if (samples.get(0).isIs_temp_visible())
        {
            holder.temperatire.setVisibility(View.VISIBLE);
            holder.recycle_view_sample.setHint("No of Chillers/Freezers");
        }else
        {
            holder.temperatire.setVisibility(View.GONE);
            holder.recycle_view_sample.setHint("No of Sample");
        }
        holder.txt_sample.setText("Sample " + (position+1));

        holder.edt_rate.setText(samples.get(position).getSampleCurrentRate()+"");
        holder.edit_product_name.setText(samples.get(position).getProduct_name());
        holder.edit_brand_name.setText(samples.get(position).getBrand_name());
        holder.txt_dateexp.setText(samples.get(position).getBb_exp_date());
        holder.txt_datemfd.setText(samples.get(position).getMfd_date());
        holder.recycle_view_sample.setText(samples.get(position).getNo_sample_product());
        holder.progressBar.setProgress((int)(samples.get(position).getSampleCurrentRate()*2));
        int vaolue=samples.get(position).getShellife_value();
        if(vaolue==0)
        {
            holder.rdt_none.setChecked(true);
        }
        else if(vaolue==1)
        {
            holder.rdt_upto30.setChecked(true);
        }
        else if(vaolue==2)
        {
            holder.rdt_1month6mont.setChecked(true);
        }
        else
        {
            holder.rdt_more6month.setChecked(true);
        }



        holder.calender_mfd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = ((Activity) c).getFragmentManager();
                Calendar calendar=Calendar.getInstance();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                calendar.setTimeZone(TimeZone.getTimeZone("IST"));

                DatePickerDialog datePickerDialog=DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        String day= String.valueOf(dayOfMonth); String month=String.valueOf(monthOfYear);
                        if(dayOfMonth<10)
                        {
                            day="0"+dayOfMonth;
                        }
                        if (monthOfYear<9)
                        {
                            month="0"+ (monthOfYear+1);
                        }
                        else {
                            month=String.valueOf(monthOfYear+1);
                        }

                        holder.txt_datemfd.setText(day+"-"+month+"-"+year);
                    }
                }, year, month, day);
                datePickerDialog.show(manager,"show");

                datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        holder.txt_datemfd.setText("");
                    }
                });
            }
        });

        holder.calender_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager manager = ((Activity) c).getFragmentManager();
                Calendar calendar=Calendar.getInstance();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);

                calendar.setTimeZone(TimeZone.getTimeZone("IST"));

                DatePickerDialog datePickerDialog=DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        String day= String.valueOf(dayOfMonth); String month=String.valueOf(monthOfYear);
                        if(dayOfMonth<10)
                        {
                            day="0"+dayOfMonth;
                        }
                        if (monthOfYear<9)
                        {
                            month="0"+ (monthOfYear+1);
                        }
                        else {
                            month=String.valueOf(monthOfYear+1);
                        }
                        holder.txt_dateexp.setText(day+"-"+month+"-"+year);
                        bb_expdate=year+"-"+month+"-"+day;
                    }

                }, year, month, day);
                datePickerDialog.show(manager,"show");

                datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        holder.txt_dateexp.setText("");
                    }
                });
            }
        });



    }

    @Override
    public int getItemCount() {
        return samples.size();
    }

    public class Samples extends RecyclerView.ViewHolder
    {

        TextView txt_sample;
        EditText recycle_view_sample,edit_product_name,edit_brand_name,edt_rate,temperatire,txt_datemfd,txt_dateexp;
        SeekBar progressBar;
        ImageView calender_mfd,calender_exp;
        RadioButton my_recycler_view_sample_fail,rdt_none,rdt_upto30,rdt_1month6mont,rdt_more6month;
        Button btn_rate;
        Spinner mfd_spinner,bb_spinner;
        public Samples(View itemView)
        {
            super(itemView);
            recycle_view_sample=(EditText)itemView.findViewById(R.id.my_recycler_view_sample);
            edit_product_name=(EditText)itemView.findViewById(R.id.edt_product_name);
            edit_brand_name=(EditText)itemView.findViewById(R.id.edt_brand_name);
            my_recycler_view_sample_fail=(RadioButton)itemView.findViewById(R.id.my_recycler_view_sample_fail);
            rdt_none=(RadioButton)itemView.findViewById(R.id.rdt_none);
            rdt_upto30=(RadioButton)itemView.findViewById(R.id.rdt_upto30);
            rdt_1month6mont=(RadioButton)itemView.findViewById(R.id.rdt_1monthto6month);
            rdt_more6month=(RadioButton)itemView.findViewById(R.id.rdt_more6months);
            temperatire=(EditText)itemView.findViewById(R.id.my_recycler_view_temp);
            calender_exp=itemView.findViewById(R.id.calender_exp);
            calender_mfd=itemView.findViewById(R.id.calender_mfd);



            edt_rate=(EditText)itemView.findViewById(R.id.edt_rate);
            txt_sample=(TextView)itemView.findViewById(R.id.txt_sample);
            progressBar=(SeekBar)itemView.findViewById(R.id.seek_bar);
            btn_rate=(Button)itemView.findViewById(R.id.btn_rate);

            mfd_spinner=(Spinner)itemView.findViewById(R.id.list_samplemfd);
            bb_spinner=(Spinner)itemView.findViewById(R.id.list_sampleexp);

            txt_datemfd=(EditText)itemView.findViewById(R.id.txt_datemfd);
            txt_dateexp=(EditText)itemView.findViewById(R.id.txt_dateexp);

            mfd_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    mfdpkd= adapterView.getItemAtPosition(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            bb_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    bb_exp=adapterView.getItemAtPosition(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            btn_rate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   double rate=Double.valueOf(edt_rate.getText().toString());
                    if(rate<=10){
                        progressBar.setProgress(((int)rate)*2);
                        if (rdt_none.isChecked())
                        {
                            Selflife_value=0;
                        }
                        else if(rdt_upto30.isChecked())
                        {
                            Selflife_value=1;
                        }
                        else if(rdt_1month6mont.isChecked())
                        {
                            Selflife_value=2;
                        }
                        else
                        {
                            Selflife_value=3;
                        }

                        if (samples.get(0).isIs_temp_visible())
                        {
                            temperature=(temperatire.getText().toString());
                        }


                        mfd_date=txt_datemfd.getText().toString();
                        bb_expdate=txt_dateexp.getText().toString();
                        no_samples_product=recycle_view_sample.getText().toString();
                        brand_name=edit_brand_name.getText().toString();
                        product_name=edit_product_name.getText().toString();
                        fragment.updateSamples(getAdapterPosition(),rate,no_samples_product,product_name,brand_name,
                                mfdpkd,mfd_date,bb_exp,bb_expdate,Selflife_value,temperature,sample_fail_value);
                    }else {
                        Toast.makeText(c, "Value should not more than 10", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            my_recycler_view_sample_fail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(sample_fail_value==0)
                    {
                        sample_fail_value=1;
                        my_recycler_view_sample_fail.setChecked(true);
                    }
                    else
                    {
                        sample_fail_value=0;
                        my_recycler_view_sample_fail.setChecked(false);
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
                    if (rdt_none.isChecked())
                    {
                        Selflife_value=0;
                    }
                    else if(rdt_upto30.isChecked())
                    {
                        Selflife_value=1;
                    }
                    else if(rdt_1month6mont.isChecked())
                    {
                        Selflife_value=2;
                    }
                    else
                    {
                        Selflife_value=3;
                    }
                    if (samples.get(0).isIs_temp_visible())
                    {
                        temperature=(temperatire.getText().toString());
                    }

                    mfd_date=txt_datemfd.getText().toString();
                    bb_expdate=txt_dateexp.getText().toString();
                    no_samples_product=recycle_view_sample.getText().toString();
                    brand_name=edit_brand_name.getText().toString();
                    product_name=edit_product_name.getText().toString();
                    edt_rate.setText(trueprogress+"");
                    fragment.updateSamples(getAdapterPosition(),(int)trueprogress,no_samples_product,product_name,brand_name,
                            mfdpkd,mfd_date,bb_exp,bb_expdate,Selflife_value,temperature,sample_fail_value);
                }
            });

        }
    }


}
