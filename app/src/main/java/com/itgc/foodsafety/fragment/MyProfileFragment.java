package com.itgc.foodsafety.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by root on 9/10/15.
 */
public class MyProfileFragment extends Fragment implements View.OnClickListener {

    private EditText txt_contact, edt_ownername, edt_phone1;
    private TextView txt_change_password;
    private Button btn_submit;
    private Context context;
    private ProgressDialog pd;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
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
        View view = inflater.inflate(R.layout.myprofilefragment, container, false);
        setUpView(view);

        return view;
    }

    private void setUpView(View view) {
        txt_contact = (EditText) view.findViewById(R.id.txt_contact);
        edt_ownername = (EditText) view.findViewById(R.id.edt_ownername);
        edt_phone1 = (EditText) view.findViewById(R.id.edt_phone1);

        txt_change_password = (TextView) view.findViewById(R.id.txt_change_password);

        btn_submit = (Button) view.findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(this);
        txt_change_password.setOnClickListener(this);

        setData();
    }

    private void setData() {
        txt_contact.setText(AppPrefrences.getEmail(context));
        edt_ownername.setText(AppPrefrences.getUserName(context));
        edt_phone1.setText(AppPrefrences.getMobileNo(context));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_change_password:
                getFragmentManager().beginTransaction().replace(R.id.container_body, new Change_password())
                        .addToBackStack("Change Password").commit();
                break;
            case R.id.btn_submit:
                checkDetails();
                break;
        }
    }

    private void checkDetails() {
        if (edt_ownername.getText().toString() != null && !edt_ownername.getText().toString().isEmpty()) {
            if (edt_phone1.getText().toString() != null && !edt_phone1.getText().toString().isEmpty()) {
                updateProfile();
            } else {
                Toast.makeText(context, "Please enter Phone Number.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "Please enter Owner Name.", Toast.LENGTH_LONG).show();
        }
    }

    private void updateProfile() {
        pd = new ProgressDialog(context);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();

        StringRequest str = new StringRequest(Request.Method.POST,
                Vars.BASE_URL + Vars.UPDATE_PROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        boolean Status = jsonObject.getBoolean("Status");
                        String msg = jsonObject.getString("Message");

                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
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
                params.put("name", edt_ownername.getText().toString());
                params.put("number", edt_phone1.getText().toString());
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(str);
    }

    private void restoreToolbar() {
        Toolbar toolbar = MainActivity.mToolbar;
        toolbar.setTitle("My Profile");
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitleTextAppearance(context, R.style.MyTitleTextApperance);
        toolbar.setNavigationIcon(R.mipmap.revealicon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    hideKeyboard(v);
                } catch (Exception e) {

                }
                if (FragmentDrawer.mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
                    FragmentDrawer.mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    FragmentDrawer.mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }
    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
