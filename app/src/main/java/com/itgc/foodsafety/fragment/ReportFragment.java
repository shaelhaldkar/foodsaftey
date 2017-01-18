package com.itgc.foodsafety.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.itgc.foodsafety.MySingleton;
import com.itgc.foodsafety.R;
import com.itgc.foodsafety.adapter.ReportAdapter;
import com.itgc.foodsafety.dao.Reports;
import com.itgc.foodsafety.ui.DividerItemDecoration;
import com.itgc.foodsafety.utils.AppPrefrences;
import com.itgc.foodsafety.utils.Vars;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 9/10/15.
 */
public class ReportFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context;
    private ArrayList<Reports> reportses;
    private ProgressDialog pd;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (Activity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.reportfragment, container, false);
        setUpView(view);

        getReport();
        return view;
    }

    private void getReport() {
        pd = new ProgressDialog(context);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();

        StringRequest str = new StringRequest(Request.Method.POST,
                Vars.BASE_URL + Vars.REPORT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        boolean Status = jsonObject.getBoolean("Status");
                        String msg = jsonObject.getString("Message");

                        if (Status) {
                            JSONArray payload = jsonObject.getJSONArray("Payload");
                            reportses = new ArrayList<>();

                            if (payload.length() > 0){
                                for (int i = 0; i < payload.length(); i++) {
                                    Reports reports = new Reports();
                                    JSONObject obj = payload.getJSONObject(i);
                                    reports.setReport_id(obj.getString("ReportId"));
                                    reports.setReport_date(obj.getString("ReportDate"));
                                    reports.setReport_status(obj.getString("Status"));
                                    reports.setReport_link(obj.getString("ReportUrl"));

                                    reportses.add(reports);
                                }
                            }
                            setAdapter(reportses);

                        } else {
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError v) {
                v.printStackTrace();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("auditor_id", AppPrefrences.getUserId(context));
                return params;
            }
        };
        str.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(str);
    }

    private void setUpView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(context, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

    }

    private void setAdapter(ArrayList<Reports> reportses) {
        AppPrefrences.setReportCount(context, reportses.size());
        Intent intent = new Intent("DraftsCount");
        context.sendBroadcast(intent);
        mAdapter = new ReportAdapter(reportses, context);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
