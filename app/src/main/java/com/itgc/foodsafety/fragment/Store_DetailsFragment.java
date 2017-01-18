package com.itgc.foodsafety.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.itgc.foodsafety.MainActivity;
import com.itgc.foodsafety.MySingleton;
import com.itgc.foodsafety.R;
import com.itgc.foodsafety.utils.AppPrefrences;
import com.itgc.foodsafety.utils.AppUtils;
import com.itgc.foodsafety.utils.Vars;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 11/12/15.
 */
public class Store_DetailsFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private EditText store_chiller_no, store_freezer_no, store_vendor_chiller_no, store_vendor_freezer_no, store_rodent_no,
            store_flycatcher_no, store_aircutter_no, store_thermo_no, store_manager_name, store_manager_email;
    private Button btn_submit;
    private String store_name;
    private int store_id;
    private Bundle b;
    private ProgressDialog pd;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (Activity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = getArguments();

        store_name = b.getString("Store_name");
        store_id = b.getInt("Store_id");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        restoreToolbar();
        View view = inflater.inflate(R.layout.store_details, container, false);
        setUpView(view);

        return view;
    }

    private void setUpView(View view) {
        store_chiller_no = (EditText) view.findViewById(R.id.store_chiller_no);
        store_freezer_no = (EditText) view.findViewById(R.id.store_freezer_no);
        store_vendor_chiller_no = (EditText) view.findViewById(R.id.store_vendor_chiller_no);
        store_vendor_freezer_no = (EditText) view.findViewById(R.id.store_vendor_freezer_no);
        store_rodent_no = (EditText) view.findViewById(R.id.store_rodent_no);
        store_flycatcher_no = (EditText) view.findViewById(R.id.store_flycatcher_no);
        store_aircutter_no = (EditText) view.findViewById(R.id.store_aircutter_no);
        store_thermo_no = (EditText) view.findViewById(R.id.store_thermo_no);
        store_manager_name = (EditText) view.findViewById(R.id.store_manager_name);
        store_manager_email = (EditText) view.findViewById(R.id.store_manager_email);

        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
    }

    private void restoreToolbar() {
        Toolbar toolbar = MainActivity.mToolbar;
        toolbar.setTitle(store_name);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitleTextAppearance(context, R.style.MyTitleTextApperance);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyBoard(getActivity(), v);
                getFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (store_manager_name.getText().toString() != null && !store_manager_name.getText().toString().isEmpty()) {
                    if (store_manager_email.getText().toString() != null && !store_manager_email.getText().toString().isEmpty()) {
                        if (store_chiller_no.getText().toString() != null && !store_chiller_no.getText().toString().isEmpty()) {
                            if (store_freezer_no.getText().toString() != null && !store_freezer_no.getText().toString().isEmpty()) {
                                if (store_vendor_chiller_no.getText().toString() != null && !store_vendor_chiller_no.getText().toString().isEmpty()) {
                                    if (store_vendor_freezer_no.getText().toString() != null && !store_vendor_freezer_no.getText().toString().isEmpty()) {
                                        if (store_rodent_no.getText().toString() != null && !store_rodent_no.getText().toString().isEmpty()) {
                                            if (store_flycatcher_no.getText().toString() != null && !store_flycatcher_no.getText().toString().isEmpty()) {
                                                if (store_aircutter_no.getText().toString() != null && !store_aircutter_no.getText().toString().isEmpty()) {
                                                    if (store_thermo_no.getText().toString() != null && !store_thermo_no.getText().toString().isEmpty()) {
                                                        sendStoreDetails();
                                                    } else
                                                        Toast.makeText(context, "Please enter No. of Thermometers", Toast.LENGTH_LONG).show();
                                                } else
                                                    Toast.makeText(context, "Please enter No. of Air Cutters", Toast.LENGTH_LONG).show();
                                            } else
                                                Toast.makeText(context, "Please enter No. of Flycatchers", Toast.LENGTH_LONG).show();
                                        } else
                                            Toast.makeText(context, "Please enter No. of Rodent Boxes", Toast.LENGTH_LONG).show();
                                    } else
                                        Toast.makeText(context, "Please enter No. of Freezers from Vendor", Toast.LENGTH_LONG).show();
                                } else
                                    Toast.makeText(context, "Please enter No. of Chillers from Vendor", Toast.LENGTH_LONG).show();
                            } else
                                Toast.makeText(context, "Please enter No. of Freezers", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(context, "Please enter No. of Chillers", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Please enter Manager Email", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Please enter Manager Name", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void sendStoreDetails() {
        pd = new ProgressDialog(context);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();

        StringRequest str = new StringRequest(Request.Method.POST,
                Vars.BASE_URL + Vars.STORE_INFO, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String msg = jsonObject.getString("Message");

                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

                        store_chiller_no.setText("");
                        store_freezer_no.setText("");
                        store_vendor_chiller_no.setText("");
                        store_vendor_freezer_no.setText("");
                        store_rodent_no.setText("");
                        store_flycatcher_no.setText("");
                        store_aircutter_no.setText("");
                        store_thermo_no.setText("");
                        store_manager_name.setText("");
                        store_manager_email.setText("");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError v) {
                v.printStackTrace();
                Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("auditor_id", AppPrefrences.getUserId(context));
                params.put("storeId", String.valueOf(store_id));
                params.put("chillers", store_chiller_no.getText().toString());
                params.put("freezers", store_freezer_no.getText().toString());
                params.put("chillers_from_vendors", store_vendor_chiller_no.getText().toString());
                params.put("freezers_from_vendors", store_vendor_freezer_no.getText().toString());
                params.put("rodent_boxes", store_rodent_no.getText().toString());
                params.put("flyCatchers", store_flycatcher_no.getText().toString());
                params.put("airCutters", store_aircutter_no.getText().toString());
                params.put("thermometers", store_thermo_no.getText().toString());
                params.put("manager_name", store_manager_name.getText().toString());
                params.put("manager_email", store_manager_email.getText().toString());
                return params;
            }
        };
        str.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(str);

    }
}
