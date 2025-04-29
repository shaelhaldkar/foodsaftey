package com.itgc.foodsafety.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.itgc.foodsafety.MainActivity;
import com.itgc.foodsafety.MySingleton;
import com.itgc.foodsafety.R;
import com.itgc.foodsafety.db.DBHelper;
import com.itgc.foodsafety.db.DbManager;
import com.itgc.foodsafety.utils.AppPrefrences;
import com.itgc.foodsafety.utils.AppUtils;
import com.itgc.foodsafety.utils.Vars;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 11/12/15.
 */
public class Store_DetailsFragment extends Fragment implements View.OnClickListener,DatePickerDialog.OnDateSetListener {

    private Context context;
    private EditText account_name,store_chiller_no, store_freezer_no, store_vendor_chiller_no, store_vendor_freezer_no, store_rodent_no,
            store_flycatcher_no, store_aircutter_no, store_thermo_no, store_manager_name, store_manager_email;
    private Button btn_submit;
    private String store_name,submit_date="";
    private int store_id;
    private Bundle b;
    private ProgressDialog pd;
    private TextView fssai_lic;
    LinearLayout select_date;

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
        getStoreDetails();
        return view;
    }

    private void setUpView(View view) {
        account_name=(EditText)view.findViewById(R.id.account_name);
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

        select_date=(LinearLayout)view.findViewById(R.id.fssai_lic_ll);
        fssai_lic=(TextView) view.findViewById(R.id.fssai_lic);

        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c=Calendar.getInstance();
                int year=c.get(Calendar.YEAR);
                int month=c.get(Calendar.MONTH);
                int day=c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog=DatePickerDialog.newInstance(Store_DetailsFragment.this,year,month,day);
                datePickerDialog.setMinDate(c);
                datePickerDialog.show(getActivity().getFragmentManager(),"show");
            }
        });

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
                saveStoreDetailsLocally();
                sendStoreDetails();
                break;

//                if (store_manager_name.getText().toString() != null && !store_manager_name.getText().toString().isEmpty()) {
//                    if (store_manager_email.getText().toString() != null && !store_manager_email.getText().toString().isEmpty()) {
//                        if (store_chiller_no.getText().toString() != null && !store_chiller_no.getText().toString().isEmpty()) {
//                            if (store_freezer_no.getText().toString() != null && !store_freezer_no.getText().toString().isEmpty()) {
//                                if (store_vendor_chiller_no.getText().toString() != null && !store_vendor_chiller_no.getText().toString().isEmpty()) {
//                                    if (store_vendor_freezer_no.getText().toString() != null && !store_vendor_freezer_no.getText().toString().isEmpty()) {
//                                        if (store_rodent_no.getText().toString() != null && !store_rodent_no.getText().toString().isEmpty()) {
//                                            if (store_flycatcher_no.getText().toString() != null && !store_flycatcher_no.getText().toString().isEmpty()) {
//                                                if (store_aircutter_no.getText().toString() != null && !store_aircutter_no.getText().toString().isEmpty()) {
//                                                    if (store_thermo_no.getText().toString() != null && !store_thermo_no.getText().toString().isEmpty()) {
//                                                        saveStoreDetailsLocally();
//                                                        sendStoreDetails();
//                                                    } else
//                                                        Toast.makeText(context, "Please enter No. of Thermometers", Toast.LENGTH_LONG).show();
//                                                } else
//                                                    Toast.makeText(context, "Please enter No. of Air Cutters", Toast.LENGTH_LONG).show();
//                                            } else
//                                                Toast.makeText(context, "Please enter No. of Flycatchers", Toast.LENGTH_LONG).show();
//                                        } else
//                                            Toast.makeText(context, "Please enter No. of Rodent Boxes", Toast.LENGTH_LONG).show();
//                                    } else
//                                        Toast.makeText(context, "Please enter No. of Freezers from Vendor", Toast.LENGTH_LONG).show();
//                                } else
//                                    Toast.makeText(context, "Please enter No. of Chillers from Vendor", Toast.LENGTH_LONG).show();
//                            } else
//                                Toast.makeText(context, "Please enter No. of Freezers", Toast.LENGTH_LONG).show();
//                        } else
//                            Toast.makeText(context, "Please enter No. of Chillers", Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(context, "Please enter Manager Email", Toast.LENGTH_LONG).show();
//                    }
//                } else {
//                    Toast.makeText(context, "Please enter Manager Name", Toast.LENGTH_LONG).show();
//                }
        }
    }

    private void sendStoreDetails() {
        pd = new ProgressDialog(context);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();

        JSONObject params=new JSONObject();
        try {


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
            params.put("accountName", account_name.getText().toString());
            params.put("fssaiLicence_date", submit_date);
        }catch (Exception e){}

        String URL=Vars.BASE_URL+Vars.STORE_INFO;
        JsonObjectRequest req = new JsonObjectRequest(1,URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if (pd != null && pd.isShowing())
                            pd.dismiss();
                        try {
                            JSONArray jsonArray=new JSONArray( response.getString("store_informationResult"));
                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                            String message=jsonObject.getString("Message");
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(context,MainActivity.class));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                error.printStackTrace();
                Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
            }
        });


//        StringRequest str = new StringRequest(Request.Method.POST,
//                Vars.BASE_URL + Vars.STORE_INFO, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                if (pd != null && pd.isShowing())
//                    pd.dismiss();
//                if (response != null) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        String msg = jsonObject.getString("Message");
//                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
////                        store_chiller_no.setText("");
////                        store_freezer_no.setText("");
////                        store_vendor_chiller_no.setText("");
////                        store_vendor_freezer_no.setText("");
////                        store_rodent_no.setText("");
////                        store_flycatcher_no.setText("");
////                        store_aircutter_no.setText("");
////                        store_thermo_no.setText("");
////                        store_manager_name.setText("");
////                        store_manager_email.setText("");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError v) {
//                v.printStackTrace();
//                Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("auditor_id", AppPrefrences.getUserId(context));
//                params.put("storeId", String.valueOf(store_id));
//                params.put("chillers", store_chiller_no.getText().toString());
//                params.put("freezers", store_freezer_no.getText().toString());
//                params.put("chillers_from_vendors", store_vendor_chiller_no.getText().toString());
//                params.put("freezers_from_vendors", store_vendor_freezer_no.getText().toString());
//                params.put("rodent_boxes", store_rodent_no.getText().toString());
//                params.put("flyCatchers", store_flycatcher_no.getText().toString());
//                params.put("airCutters", store_aircutter_no.getText().toString());
//                params.put("thermometers", store_thermo_no.getText().toString());
//                params.put("manager_name", store_manager_name.getText().toString());
//                params.put("manager_email", store_manager_email.getText().toString());
//                return params;
//            }
//        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(req);
    }

    private void saveStoreDetailsLocally()
    {
        DbManager.getInstance().deleteDetails(DBHelper.STORE_DETAILS_TBL_NAME1,DBHelper.STORE_ID+"="+String.valueOf(store_id));

        ContentValues cv = new ContentValues();
        cv.put(DBHelper.STORE_ID, String.valueOf(store_id));
        cv.put(DBHelper.ACCOUNT_NAME, AppPrefrences.getUserId(context));
        cv.put(DBHelper.CHILLERS, store_chiller_no.getText().toString());
        cv.put(DBHelper.FREZERS, store_freezer_no.getText().toString());
        cv.put(DBHelper.VENDOR_CHILLERS, store_vendor_chiller_no.getText().toString());
        cv.put(DBHelper.VENDOR_FREZEERS, store_vendor_freezer_no.getText().toString());
        cv.put(DBHelper.REDANT_BOXEX, store_rodent_no.getText().toString());
        cv.put(DBHelper.FLY_CATCHERS, store_flycatcher_no.getText().toString());
        cv.put(DBHelper.AIR_CUTTERS, store_aircutter_no.getText().toString());
        cv.put(DBHelper.THERMOMETERS, store_thermo_no.getText().toString());
        cv.put(DBHelper.MANAGER_NAME, store_manager_name.getText().toString());
        cv.put(DBHelper.FSSAI_LIC,fssai_lic.getText().toString());
        cv.put(DBHelper.MANAGER_EMAIL, store_manager_email.getText().toString());
        DbManager.getInstance().insertDetails(cv, DBHelper.STORE_DETAILS_TBL_NAME1);
    }

    private void getStoreDetails()
    {
        String query = "SELECT * FROM " + DBHelper.STORE_DETAILS_TBL_NAME1 + " WHERE " + DBHelper.STORE_ID + "=" + store_id;
        DbManager.getInstance().openDatabase();
        Cursor cursor = DbManager.getInstance().getDetails(query);
        Log.e("StoreDetails", cursor.getCount() + "   " + query);
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            account_name.setText(cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNT_NAME)));
            store_chiller_no.setText(cursor.getString(cursor.getColumnIndex(DBHelper.CHILLERS)));
            store_freezer_no.setText(cursor.getString(cursor.getColumnIndex(DBHelper.FREZERS)));
            store_vendor_chiller_no.setText(cursor.getString(cursor.getColumnIndex(DBHelper.VENDOR_CHILLERS)));
            store_vendor_freezer_no.setText(cursor.getString(cursor.getColumnIndex(DBHelper.VENDOR_FREZEERS)));
            store_rodent_no.setText(cursor.getString(cursor.getColumnIndex(DBHelper.REDANT_BOXEX)));
            store_flycatcher_no.setText(cursor.getString(cursor.getColumnIndex(DBHelper.FLY_CATCHERS)));
            store_aircutter_no.setText(cursor.getString(cursor.getColumnIndex(DBHelper.AIR_CUTTERS)));
            store_thermo_no.setText(cursor.getString(cursor.getColumnIndex(DBHelper.THERMOMETERS)));
            store_manager_name.setText(cursor.getString(cursor.getColumnIndex(DBHelper.MANAGER_NAME)));
            store_manager_email.setText(cursor.getString(cursor.getColumnIndex(DBHelper.MANAGER_EMAIL)));
            fssai_lic.setText(cursor.getString(cursor.getColumnIndex(DBHelper.FSSAI_LIC)));
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)
    {
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

        submit_date=month+"-"+day+"-"+year;
        fssai_lic.setText(month+"-"+day+"-"+year);

    }

    //17100612062580N1

}
