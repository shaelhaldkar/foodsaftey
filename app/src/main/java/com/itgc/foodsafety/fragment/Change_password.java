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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.itgc.foodsafety.MainActivity;
import com.itgc.foodsafety.MySingleton;
import com.itgc.foodsafety.R;
import com.itgc.foodsafety.utils.AppPrefrences;
import com.itgc.foodsafety.utils.AppUtils;
import com.itgc.foodsafety.utils.Vars;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 11/12/15.
 */
public class Change_password extends Fragment implements View.OnClickListener {

    private Context context;
    private EditText edt_password, edt_confpassword;
    private Button btn_submit;
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
        restoreToolbar();
        View view = inflater.inflate(R.layout.change_password, container, false);
        setUpView(view);

        return view;
    }

    private void setUpView(View view) {
        btn_submit = (Button) view.findViewById(R.id.btn_submit);

        edt_password = (EditText) view.findViewById(R.id.edt_password);
        edt_confpassword = (EditText) view.findViewById(R.id.edt_confpassword);

        btn_submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (edt_password.getText().toString() != null && !edt_password.getText().toString().isEmpty()) {
                    if (edt_confpassword.getText().toString() != null && !edt_confpassword.getText().toString().isEmpty()) {
                        if (edt_password.getText().toString().equals(edt_confpassword.getText().toString())) {
                            changePassword();
                        } else
                            Toast.makeText(context, "Password does not match. Please enter properly", Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(context, "Please confirm the new password", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(context, "Please enter new password", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void changePassword() {
        pd = new ProgressDialog(context);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();

        String URL=Vars.BASE_URL+Vars.CHANGE_PASSWORD;

        JSONObject jsonObject=new JSONObject();

        try{
            jsonObject.put("auditor_id", AppPrefrences.getUserId(context));
            jsonObject.put("password", edt_password.getText().toString());
        }catch(Exception e){}

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (pd != null && pd.isShowing())
                            pd.dismiss();
                        try {
                            JSONArray jsonArray1 = response.getJSONArray("PasswordchangeResult");
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);

                            boolean Status = jsonObject1.getBoolean("Status");

                            if(Status) {
                                String msg = jsonObject1.getString("Message");
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                            }

                            else
                            {
                                Toast.makeText(context, "Request Failed!!", Toast.LENGTH_LONG).show();
                            }


                        }catch (Exception e)
                        {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                Toast.makeText(context, "Request Failed!!", Toast.LENGTH_LONG).show();
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private void restoreToolbar() {
        Toolbar toolbar = MainActivity.mToolbar;
        toolbar.setTitle("Change Password");
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

}
