package com.itgc.foodsafety.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itgc.foodsafety.R;
import com.itgc.foodsafety.dao.StartAudit;

import java.util.ArrayList;

/**
 * Created by root on 14/10/15.
 */
public class AuditPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    private TextView audit_date, txt_sample_count, question_head, question;
    private RadioButton rg_yes, rg_no, rg_partial;
    private LinearLayout btn_previous, btn_camera, btn_next;
    private ListView sample;
    private ArrayList<String> rate;
    private ArrayList<StartAudit> startAudit;
    private AuditStartAdapter startAdapter;

    public AuditPagerAdapter(Context context, ArrayList<String> rate, ArrayList<StartAudit> startAudit) {
        this.rate = rate;
        mContext = context;
        this.startAudit = startAudit;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.audit_viewpager, container, false);

        question_head = (TextView) itemView.findViewById(R.id.question_head);
        question = (TextView) itemView.findViewById(R.id.question);

        rg_yes = (RadioButton) itemView.findViewById(R.id.rdt_yes);
        rg_no = (RadioButton) itemView.findViewById(R.id.rdt_no);
        rg_partial = (RadioButton) itemView.findViewById(R.id.rdt_partial);

        sample = (ListView) itemView.findViewById(R.id.sample);
        setAdapter();

        container.addView(itemView);

        return itemView;
    }

    private void setAdapter() {
        /*startAdapter = new AuditStartAdapter(mContext, startAudit);
        sample.setAdapter(startAdapter);*/
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

}
