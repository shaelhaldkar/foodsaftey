package com.itgc.foodsafety.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.itgc.foodsafety.Capture;
import com.itgc.foodsafety.MainActivity;
import com.itgc.foodsafety.MySingleton;
import com.itgc.foodsafety.R;
import com.itgc.foodsafety.dao.Answers;
import com.itgc.foodsafety.db.DBHelper;
import com.itgc.foodsafety.db.DbManager;
import com.itgc.foodsafety.utils.AppPrefrences;
import com.itgc.foodsafety.utils.AppUtils;
import com.itgc.foodsafety.utils.Vars;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 4/11/15.
 */
public class Submit_report extends Fragment implements View.OnClickListener {
    public static final int SIGNATURE_ACTIVITY = 4;
    private Context ctx;
    private EditText auditerNameEditText, auditerContactNumberEditText;
    private Button signatureButton, submitReport, signatureButton1, expiry_btn;
    private String auditerName, auditerId, auditerContactNumber, Store_name, audit_sign;
    private ArrayList<Answers> answersArrayList;
    private Bundle b;
    private int Cat_id, Store_id;
    private ArrayList<ArrayList<Answers>> lists;
    private ProgressDialog pd;
    private ImageView img_back;
    private String expiry = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = getArguments();

        Store_id = b.getInt("Store_id");
        Store_name = b.getString("Store_name");


        lists = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.submit_report, container, false);
        restoreToolbar();
        setUpView(view);

        return view;
    }

    private void setUpView(View view) {

        auditerNameEditText = (EditText) view.findViewById(R.id.auditerNameEditText);
        auditerContactNumberEditText = (EditText) view.findViewById(R.id.auditerContactNumberEditText);

        auditerNameEditText.setText(AppPrefrences.getUserName(ctx));
        auditerContactNumberEditText.setText(AppPrefrences.getMobileNo(ctx));

        img_back = (ImageView) view.findViewById(R.id.img_back);

        signatureButton = (Button) view.findViewById(R.id.signatureButton);
        signatureButton1 = (Button) view.findViewById(R.id.signatureButton1);
        submitReport = (Button) view.findViewById(R.id.submitReport);
        expiry_btn = (Button) view.findViewById(R.id.expiry_btn);

        signatureButton.setOnClickListener(this);
        signatureButton1.setOnClickListener(this);
        submitReport.setOnClickListener(this);
        img_back.setOnClickListener(this);
        expiry_btn.setOnClickListener(this);

    }

    private void restoreToolbar() {
        MainActivity.mToolbar.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signatureButton:
                openSignatureDialog("auditor");
                break;
            case R.id.signatureButton1:
                openSignatureDialog("store");
                break;
            case R.id.img_back:
                getFragmentManager().popBackStack();
                AppUtils.encodedimage = "";
                break;
            case R.id.submitReport:
                String message = "";
                auditerName = auditerNameEditText.getText().toString();
                auditerContactNumber = auditerContactNumberEditText.getText()
                        .toString();

                if (auditerName.isEmpty() || auditerName.equals("")) {
                    message = "Please enter the Auditer name.";
                    Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
                } else if (auditerContactNumber.isEmpty()
                        || auditerContactNumber.equals("")) {
                    message = "Please enter the Auditer Contact Number.";
                    Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
                } else if (auditerContactNumber.length() > 10 || auditerContactNumber.length() < 10) {
                    message = "Please enter the Auditer Contact Number properly.";
                    Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
                } else if (AppUtils.encodedimage.equals("")) {
                    message = "Please get Auditor sign.";
                    Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
                } else if (AppUtils.encodedstoreimage.equals("")) {
                    message = "Please get Store Manager sign.";
                    Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
                } else {
                    submitReport();
                }
                break;
            case R.id.expiry_btn:
                ExpiryDialog();
                break;
        }
    }

    private void submitReport() {
        getSignature();
        if (expiry.equalsIgnoreCase("")) {
            Toast.makeText(ctx, "Please Add Expiry Status", Toast.LENGTH_LONG).show();
        } else
            submitData("");


    }

    private void submitData(final String data1) {
        Log.d("", "data1 " + data1);

        pd = new ProgressDialog(ctx);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();

        StringRequest str = new StringRequest(Request.Method.POST,
                Vars.BASE_URL + Vars.SUBMIT_REPORT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("SUBMIT_FINAL---", response);
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                if (response != null) {


                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String msg = jsonObject.getString("Message");

                        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();

                        AppUtils.encodedimage = "";
                        AppPrefrences.setStartTime(ctx, "");
                        deleteData();
                        try {
                            Intent intent = new Intent("DraftsCount");
                            ctx.sendBroadcast(intent);
                            getFragmentManager().beginTransaction().replace(R.id.container_body, new Store_Fragement()).
                                    addToBackStack("Store")
                                    .commit();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        if (pd != null && pd.isShowing())
                            pd.dismiss();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError v) {
                v.printStackTrace();
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                Toast.makeText(ctx, "Failed", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

             /* params.put("data", data1);
                params.put("audit_by", auditerName);
                params.put("audit_id", AppPrefrences.getUserId(ctx));
                params.put("audit_no", auditerContactNumber);
                params.put("audit_sign", AppUtils.encodedimage);
                params.put("lat", String.valueOf(latitude));
                params.put("long", String.valueOf(longitude));
                params.put("startdateTime", AppPrefrences.getStartTime(ctx));
                params.put("enddatetime", getDateTime());
                params.put("store_id", String.valueOf(Store_id));*/

                params.put("data", "");
                params.put("audit_id", AppPrefrences.getAuditId(ctx));
                params.put("final_submit", "true");
                params.put("audit_sign", AppUtils.encodedimage);
                params.put("store_sign", AppUtils.encodedstoreimage);
                params.put("cat_id", Cat_id + "");
                params.put("audit_contact", auditerContactNumberEditText.getText().toString());
                params.put("auditor_id", AppPrefrences.getUserId(ctx));
                params.put("lat", AppPrefrences.getLatitude(ctx));
                params.put("long", AppPrefrences.getLongitude(ctx));
                params.put("startdateTime", AppPrefrences.getStartTime(ctx));
                params.put("enddatetime", getDateTime());
                params.put("store_id", String.valueOf(Store_id));
                params.put("expiry_question", expiry);
                Log.e("Expiry", expiry);

                Log.e("Submit Report Params ",params.toString());

                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(
                120000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(ctx).addToRequestQueue(str);
    }

    private String getDateTime() {
        String formattedDate = new SimpleDateFormat("dd MMM yyyy kk:mm").format(Calendar.getInstance().getTime());
        return formattedDate;
    }

    private void deleteData() {
        DbManager.getInstance().deleteDetails(DBHelper.ANSWER_Tbl_NAME, DBHelper.ANSWER_Store_id + "='" + Store_id + "' and " +
                DBHelper.ANSWER_Status + "= 'Complete' ");
        DbManager.getInstance().deleteDetails(DBHelper.ANSWER_Tbl_NAME, DBHelper.ANSWER_Store_id + "='" + Store_id + "' and " +
                DBHelper.ANSWER_Status + "= 'Skipped' ");
        Fragment fragment = new Store_Fragement();
        getFragmentManager().beginTransaction().replace(R.id.container_body, fragment)
                .addToBackStack("Audit").commit();
    }

    private void getSignature() {
        Bitmap bm = BitmapFactory.decodeFile(Capture.mypath.toString());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        audit_sign = Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Object deserializeObject(byte[] b) {
        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(b));
            Object object = in.readObject();
            in.close();

            return object;
        } catch (ClassNotFoundException cnfe) {
            Log.e("deserializeObject", "class not found error", cnfe);
            return null;
        } catch (IOException ioe) {
            Log.e("deserializeObject", "io error", ioe);
            return null;
        }

    }

    public void openSignatureDialog(String type) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(ctx, Capture.class);
        intent.putExtra("type", type);
        startActivityForResult(intent, SIGNATURE_ACTIVITY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGNATURE_ACTIVITY) {
        }
    }

    public void ExpiryDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx);
        builder1.setMessage("Expiry Found ?");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        expiry = "1";
                        dialog.cancel();

                    }
                });
        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        expiry = "0";
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }
}
