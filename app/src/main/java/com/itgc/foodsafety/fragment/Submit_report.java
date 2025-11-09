package com.itgc.foodsafety.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.google.android.gms.maps.model.LatLng;
import com.itgc.foodsafety.Capture;
import com.itgc.foodsafety.MainActivity;
import com.itgc.foodsafety.MySingleton;
import com.itgc.foodsafety.R;
import com.itgc.foodsafety.dao.Answers;
import com.itgc.foodsafety.db.DBHelper;
import com.itgc.foodsafety.db.DbManager;
import com.itgc.foodsafety.utils.AppPrefrences;
import com.itgc.foodsafety.utils.AppUtils;
import com.itgc.foodsafety.utils.S3FileUploadHelper;
import com.itgc.foodsafety.utils.TrackGPS;
import com.itgc.foodsafety.utils.Vars;

import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by root on 4/11/15.
 */
public class Submit_report extends Fragment implements View.OnClickListener {
    public static final int SIGNATURE_ACTIVITY = 4;
    private static final org.apache.commons.logging.Log log = LogFactory.getLog(Submit_report.class);
    private Context ctx;
    private EditText auditerNameEditText, auditerContactNumberEditText;
    private Button signatureButton, submitReport, signatureButton1, saveLocally, submitimagebutton;
    private String auditerName, auditocde, auditerContactNumber, Store_name, audit_sign, data = "", startTime, endTime, storeStartTime = "";
    private ArrayList<Answers> answersArrayList;
    private Bundle b;
    private int Cat_id, Store_id;
    private ArrayList<ArrayList<Answers>> lists;
    private ProgressDialog pd;
    private ImageView img_back;
    private String expiry = null;
    private ArrayList<String> imagepath;
    private ArrayList<String> imagename;


    private String auditId = "";
    String auditor_filename = "", storefilename = "";
    private String categoryId = "";
    private String storeId = "";
    private ArrayList<String> categoryIsList = new ArrayList<>();
    private int idPosition = 0;
    JSONObject o;
    TextView imagecount;
    int image_counter = 1;
    String image_progressdialog = "Please wait...";

    int a = 0, bb = 0;

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
        storeId = String.valueOf(Store_id);
        Store_name = b.getString("Store_name");
        lists = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.i("++inside", "onCreateView: Submit_report");
        View view = inflater.inflate(R.layout.submit_report, container, false);
        restoreToolbar();
        setUpView(view);
//      Log.e("Json", AuditJson.getObject().toString());
//        try {
//            data=AuditJson.getObject().getJSONArray("data").toString();
//            Cat_id= Integer.parseInt(AuditJson.getObject().getString("cat_id"));
//            startTime= AuditJson.getObject().getString("startdateTime");
//            endTime= AuditJson.getObject().getString("enddatetime");
        getStoreSignature(String.valueOf(Store_id), String.valueOf(Cat_id));
//            Log.e("Data", data);
//            Log.e("Cat ID", Store_id+"");
//            Log.e("Store ID", Cat_id +"");
//            Log.e("Start_End time", startTime + "   " + endTime);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        getAllCompleteAudits();
        getStoreStartTime();

        if (isGpsEnabled()) {
            new GetCurrentLatLong().execute();
        } else {
            Log.e("GPS ", "NOT ENABLE");
        }

        return view;
    }

    private void setUpView(View view) {

        auditerNameEditText = (EditText) view.findViewById(R.id.auditerNameEditText);
        auditerContactNumberEditText = (EditText) view.findViewById(R.id.auditerContactNumberEditText);

        auditerNameEditText.setText(AppPrefrences.getUserName(ctx));
        auditerContactNumberEditText.setText(AppPrefrences.getMobileNo(ctx));
        submitimagebutton = (Button) view.findViewById(R.id.submitimage);
        imagecount = (TextView) view.findViewById(R.id.remainingimagecount);
        img_back = (ImageView) view.findViewById(R.id.img_back);

        signatureButton = (Button) view.findViewById(R.id.signatureButton);
        signatureButton1 = (Button) view.findViewById(R.id.signatureButton1);
        submitReport = (Button) view.findViewById(R.id.submitReport);
        //  expiry_btn = (Button) view.findViewById(R.id.expiry_btn);
        saveLocally = (Button) view.findViewById(R.id.submitLocally);

        signatureButton.setOnClickListener(this);
        signatureButton1.setOnClickListener(this);
        submitReport.setOnClickListener(this);
        img_back.setOnClickListener(this);
        //   expiry_btn.setOnClickListener(this);
        saveLocally.setOnClickListener(this);
        submitimagebutton.setOnClickListener(this);
    }

    private void restoreToolbar() {
        MainActivity.mToolbar.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitLocally:
                String error_message = "";
                if (AppUtils.encodedimageAuditor.equals("")) {
                    error_message = "Please get Auditor sign.";
                    Toast.makeText(ctx, error_message, Toast.LENGTH_LONG).show();
                } else if (AppUtils.encodedstoreimage.equals("")) {
                    error_message = "Please get Store Manager sign.";
                    Toast.makeText(ctx, error_message, Toast.LENGTH_LONG).show();
                } else if (expiry.equalsIgnoreCase("")) {
                    error_message = "Please Add Expiry Status";
                    Toast.makeText(ctx, error_message, Toast.LENGTH_LONG).show();
                } else {
                    saveSignature(1);
                }
                break;

            case R.id.signatureButton:
                openSignatureDialog("auditor");
                break;

            case R.id.signatureButton1:
                openSignatureDialog("store");
                break;

            case R.id.submitimage:
                SharedPreferences mSharedPreference1 = PreferenceManager.getDefaultSharedPreferences(ctx);
                if (mSharedPreference1.getInt("Status_size", 0) > 0) {
                    uploadimage();
                } else {
                    Toast.makeText(ctx, "No Image Found", Toast.LENGTH_SHORT).show();
                }
            break;

            case R.id.img_back:
                getFragmentManager().popBackStack();
                AppUtils.encodedimageAuditor = "";
                break;

            case R.id.submitReport:
                String message = "";
                auditerName = auditerNameEditText.getText().toString();
                auditerContactNumber = auditerContactNumberEditText.getText()
                        .toString();

                if (auditerName.isEmpty() || auditerName.equals("")) {
                    message = "Please enter the Auditer name.";
                    Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
                } else if (auditerContactNumber.isEmpty() || auditerContactNumber.equals("")) {
                    message = "Please enter the Auditer Contact Number.";
                    Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
                } else if (auditerContactNumber.length() > 10 || auditerContactNumber.length() < 10) {
                    message = "Please enter the Auditer Contact Number properly.";
                    Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
                } else if (AppUtils.encodedimageAuditor.equals("")) {
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

    private void saveSignature(int from) {
        saveStoreSignature(String.valueOf(Store_id), String.valueOf(Cat_id), AppUtils.encodedstoreimage, AppUtils.encodedimageAuditor, from);
    }

    private void submitReport() {
        getSignature();
        saveSignature(2);


        imagepath = new ArrayList<>();
        imagename = new ArrayList<>();
        getdata();

    }

    private void submitfinalData(final String data1) {
        Log.d("", "data1 " + data1);
        pd = new ProgressDialog(ctx);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();

        JSONObject jsonObject = new JSONObject();

        imagecount.setText("Submitting final data....");

        try {
            JSONArray jsonArray = new JSONArray();
            jsonObject.put("marchent_id", AppPrefrences.getMerchatId(ctx));
            jsonObject.put("store_id", storeId);
            jsonObject.put("audit_code",auditocde );
            jsonObject.put("startdateTime", storeStartTime);
            jsonObject.put("store_sign", storefilename);
            jsonObject.put("audit_sign", auditor_filename);
            jsonObject.put("cat_id", 0);
            jsonObject.put("auditor_id", AppPrefrences.getUserId(ctx));
            jsonObject.put("lat", AppPrefrences.getLatitude(ctx));
            jsonObject.put("longs", AppPrefrences.getLongitude(ctx));
            jsonObject.put("final_submit", "true");
            jsonObject.put("enddatetime", getDateTime());
            jsonObject.put("data", jsonArray.toString());


        } catch (Exception e) {
        }

        String URL = Vars.BASE_URL + Vars.SUBMIT_REPORT;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                if (pd != null && pd.isShowing())
                    pd.dismiss();

                try {
                    JSONArray jsonArray = response.getJSONArray("submit_reportResult");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    boolean status = jsonObject.getBoolean("Status");

                    if (status) {
                        String msg = jsonObject.getString("Message");
                        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();

                        try {
                            if (imagepath.size() > 0) {
                                saveArray();

                                imagecount.setText("Please click on image button");
                                submitimagebutton.setVisibility(View.VISIBLE);
                                submitReport.setClickable(false);

                            } else {
                                AppUtils.encodedimageAuditor = "";
                                AppUtils.encodedstoreimage = "";
                                deleteSubmittedData(String.valueOf(Store_id), String.valueOf(Cat_id));
                                Toast.makeText(ctx, "ALL DATA SUBMITTED SUCCESSFULLY", Toast.LENGTH_LONG).show();
                                openHomePage();


                            }


//                            Intent intent = new Intent("DraftsCount");
//                            ctx.sendBroadcast(intent);
//                           getFragmentManager().beginTransaction().replace(R.id.container_body, new Store_Fragement()).addToBackStack("Store").commit();
                        } catch (Exception e) {
                            Log.e("+++exception", e.toString());
                        }

                    } else {
                        Toast.makeText(ctx, "Failed 1. Please try after some time", Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                Toast.makeText(ctx, "Failed 2. Please try after some time", Toast.LENGTH_LONG).show();
            }
        });


        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(ctx).addToRequestQueue(jsonObjectRequest);
    }

    private void openHomePage() {
        if (ctx != null){
            ctx.startActivity(new Intent(ctx, MainActivity.class));
        }
    }

    private String getDateTime() {
        String formattedDate = new SimpleDateFormat(
                "dd MMM yyyy kk:mm",
                Locale.ENGLISH          // force English month names
        ).format(Calendar.getInstance().getTime());
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

    private void saveStoreSignature(String storeId, String categoryId, String storeSign, String auditSign, int from) {
        DbManager.getInstance().deleteDetails(DBHelper.STORE_SIGNATURE_TBL_NAME, DBHelper.STORE_ID + "=" + storeId);// + " AND " + DBHelper.CATEGORY_ID + "=" + categoryId);

        ContentValues cv = new ContentValues();
        cv.put(DBHelper.STORE_ID, storeId);
        //cv.put(DBHelper.CATEGORY_ID, categoryId);
        cv.put(DBHelper.STORE_SIGNATURE_IMAGE, storeSign);
        cv.put(DBHelper.AUDIOTR_SIGNATURE_IMAGE, auditSign);
        cv.put(DBHelper.EXPIRY_QUESTION, expiry);

        DbManager.getInstance().insertDetails(cv, DBHelper.STORE_SIGNATURE_TBL_NAME);

        if (from == 1) {
            Toast.makeText(ctx, "Details saved", Toast.LENGTH_SHORT).show();
            openHomePage();
            getActivity().finish();
        }
    }




    private void getStoreSignature(String storeId, String categoryId) {

        String query = "SELECT * FROM " + DBHelper.STORE_SIGNATURE_TBL_NAME + " WHERE " + DBHelper.STORE_ID + "=" + storeId;// + " AND " + DBHelper.CATEGORY_ID + "=" + categoryId;
        DbManager.getInstance().openDatabase();
        Cursor cursor = DbManager.getInstance().getDetails(query);
        Log.e("Signature Count", cursor.getCount() + "   " + query);
        try {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                AppUtils.encodedstoreimage = cursor.getString(2);
                AppUtils.encodedimageAuditor = cursor.getString(3);
                expiry = cursor.getString(4);
            }
        } catch (Exception e) {
            Log.e("Store Image Error", e.getMessage());
        }


    }

    private void submitFirstStep() {

        final ProgressDialog pd = new ProgressDialog(ctx);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();

        imagecount.setText("Uploading data....");


        JSONObject jsonObject = new JSONObject();

        try {
            data = o.getJSONArray("data").toString();
            categoryId = String.valueOf(Integer.parseInt(o.getString("cat_id")));
            startTime = o.getString("startdateTime");
            endTime = o.getString("enddatetime");

            jsonObject.put("marchent_id", AppPrefrences.getMerchatId(ctx));
            jsonObject.put("store_id", storeId);
            jsonObject.put("audit_code", auditocde);
            jsonObject.put("startdateTime", storeStartTime);
            jsonObject.put("store_sign", "");
            jsonObject.put("audit_sign", "");
            jsonObject.put("cat_id", categoryId);
            jsonObject.put("auditor_id", AppPrefrences.getUserId(ctx));
            jsonObject.put("lat", AppPrefrences.getLatitude(ctx));
            jsonObject.put("longs", AppPrefrences.getLongitude(ctx));
            jsonObject.put("final_submit", "false");
            jsonObject.put("enddatetime", getDateTime());
            jsonObject.put("data", o.getJSONArray("data").toString());


        } catch (Exception e) {
            e.printStackTrace();
        }
       // settImaeToSign();

        String URL = Vars.BASE_URL + Vars.SUBMIT_REPORT;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.i("TAG", "submitFirstStep: request"+jsonObject);
                Log.i("TAG", "submitFirstStep: request"+ URL);
                Log.i("TAG", "submitFirstStep: "+response);
                if (pd != null && pd.isShowing())
                    pd.dismiss();

                try {
                    JSONArray jsonArray = response.getJSONArray("submit_reportResult");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    boolean status = jsonObject.getBoolean("Status");

                    if (status) {
                        String msg = jsonObject.getString("Message");
                        idPosition = idPosition + 1;
                        if (idPosition < categoryIsList.size()) {

                            getdata();
                        }
//
                        if (idPosition == categoryIsList.size()) {
                            submitsignature();
                        }
                    } else {

                        Toast.makeText(ctx, "Failed 3. Please try after some time", Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    Log.i("TAG", "onResponse errroq: "+e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i("TAG", "onResponse errroq: "+error.toString());
                if (pd != null && pd.isShowing())
                    pd.dismiss();

                Toast.makeText(ctx, "Failed 4. Please try after some time", Toast.LENGTH_LONG).show();
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                120000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjectRequest.setShouldCache(false);
        MySingleton.getInstance(ctx).addToRequestQueue(jsonObjectRequest);
    }

    private void deleteSubmittedData(String storeId, String categoryId) {
        //Delete Store Manager & Auditor Singature
        DbManager.getInstance().deleteDetails(DBHelper.STORE_SIGNATURE_TBL_NAME, DBHelper.STORE_ID + "=" + storeId);// + " AND " + DBHelper.CATEGORY_ID + "=" + categoryId);

        //Delete Data From Answer Table
        DbManager.getInstance().deleteDetails(DBHelper.ANSWER_TBL_NAME, DBHelper.STORE_ID + "=" + storeId);// + " AND " + DBHelper.CATEGORY_ID + "=" + categoryId);

        //Delete Data From Answer Image Table
        DbManager.getInstance().deleteDetails(DBHelper.ANSWER_IMAGE_TBL_NAME, DBHelper.STORE_ID + "=" + storeId);// + " AND " + DBHelper.CATEGORY_ID + "=" + categoryId);

        //Delete Data From Sample Audit Table
        DbManager.getInstance().deleteDetails(DBHelper.AUDIT_SAMPLE_TBL_NAME, DBHelper.STORE_ID + "=" + storeId);// + " AND " + DBHelper.CATEGORY_ID + "=" + categoryId);

        //Delete Data From StartDateTime Table
        DbManager.getInstance().deleteDetails(DBHelper.STORE_START_TIME_TABLE, DBHelper.STORE_ID + "=" + storeId);// + " AND " + DBHelper.CATEGORY_ID + "=" + categoryId);

        //Update Category Status
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.CATEGORY_STATUS, "NULL");
        contentValues.put(DBHelper.CATEGORY_START_DATE, "");
        contentValues.put(DBHelper.CATEGORY_END_DATE, "");
        DbManager.getInstance().deleteStore(storeId);
        DbManager.getInstance().updateDetails(contentValues, DBHelper.CATEGORY_TBL_NAME, DBHelper.STORE_ID + "=" + storeId + " AND " + DBHelper.CATEGORY_STATUS + "='Complete'");
    }

    private void getAllCompleteAudits() {
        categoryIsList.clear();
        String completeAudit = "SELECT * FROM " + DBHelper.CATEGORY_TBL_NAME + " WHERE " + DBHelper.STORE_ID + "=" + Store_id + " AND " + DBHelper.CATEGORY_STATUS + "='Complete'";
        Cursor completed = DbManager.getInstance().getDetails(completeAudit);

        if (completed.getCount() > 0) {
            completed.moveToFirst();
            do {
                categoryId = completed.getString(completed.getColumnIndex(DBHelper.CATEGORY_ID));
                categoryIsList.add(categoryId);
                Log.e("Category Id", categoryId);
            } while (completed.moveToNext());
            Log.e("Total Category ", categoryIsList.size() + "");
        }
    }

    private JSONObject getLocalSavedData(String storeId, String categoryId) {
        DbManager.getInstance().openDatabase();
        Cursor c = DbManager.getInstance().getDetails("SELECT * FROM " + DBHelper.ANSWER_TBL_NAME + " WHERE " + DBHelper.STORE_ID + "=" + storeId + " AND " + DBHelper.CATEGORY_ID + "=" + categoryId);
        //Log.e("Category Count", c.getCount() + "");
        JSONObject storeObject = new JSONObject();
        JSONArray array = new JSONArray();

        JSONObject o;
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                o = new JSONObject();
                try {
                    o.put("store_id", c.getInt(c.getColumnIndex(DBHelper.STORE_ID)));
                    o.put("cat_id", c.getInt(c.getColumnIndex(DBHelper.CATEGORY_ID)));
                    o.put("subcat_id", c.getInt(c.getColumnIndex(DBHelper.ANSWER_SUBCAT_ID)));
                    o.put("question_id", c.getInt(c.getColumnIndex(DBHelper.QUESTION_ID)));
                    int quesId = c.getInt(c.getColumnIndex(DBHelper.QUESTION_ID));
                    int catId = c.getInt(c.getColumnIndex(DBHelper.QUESTION_ID));
                    int strId = c.getInt(c.getColumnIndex(DBHelper.QUESTION_ID));

                    // For Audit Images
                    ArrayList<StringBuilder> stringBuilders = new ArrayList<>();
                    JSONArray imageArray = new JSONArray();
                    Cursor imageCursor = DbManager.getInstance().getDetails("SELECT answerImage FROM " + DBHelper.ANSWER_IMAGE_TBL_NAME + " WHERE " + DBHelper.STORE_ID + "=" + storeId + " AND " +
                            DBHelper.CATEGORY_ID + "=" + categoryId + " AND " +
                            DBHelper.QUESTION_ID + "=" + quesId);
                    //Log.e("Image Count", imageCursor.getCount() + "");
                    if (imageCursor.getCount() > 0) {
                        imageCursor.moveToFirst();
                        do {

                            String iamge = (imageCursor.getString(imageCursor.getColumnIndex(DBHelper.ANSWER_IMAGE)));
                            String[] parts = iamge.split(">>");
                            String part1 = parts[0]; // 004
                            String part2 = parts[1];
                            imagename.add(part1);
                            imagepath.add(part2);
                            imageArray.put(part1);
                        } while (imageCursor.moveToNext());

                    }
                    imageCursor.close();
                    o.put("image", imageArray);

//                    Cursor pathCursor = DbManager.getInstance().getDetails("SELECT answerpath FROM " + DBHelper.ANSWER_IMAGE_TBL_PATH + " WHERE " + DBHelper.STORE_ID + "=" + storeId + " AND " +
//                            DBHelper.CATEGORY_ID +"=" + categoryId + " AND " +
//                            DBHelper.QUESTION_ID +"=" + quesId);
//                    //Log.e("Image Count", imageCursor.getCount() + "");
//                    if(pathCursor.getCount()>0)
//                    {
//                        pathCursor.moveToFirst();
//                        do {
//
//                            String iamge=(pathCursor.getString(pathCursor.getColumnIndex(DBHelper.ANSWER_PATH)));
//
//                        }while (imageCursor.moveToNext());
//
//                    }
//                    pathCursor.close();

                    String sampleQuery = "SELECT * FROM " + DBHelper.AUDIT_SAMPLE_TBL_NAME + " WHERE " + DBHelper.STORE_ID + "=" + storeId + " AND " +
                            DBHelper.CATEGORY_ID + "=" + categoryId + " AND " +
                            DBHelper.QUESTION_ID + "=" + quesId;
                    Cursor samplesCursor = DbManager.getInstance().getDetails(sampleQuery);
                    //Log.e("Total Samples",samplesCursor.getCount()+"");
                    if (samplesCursor.getCount() > 0) {
                        samplesCursor.moveToFirst();
                        do {
                            StringBuilder sb = new StringBuilder();
                            sb.append(samplesCursor.getInt(samplesCursor.getColumnIndex(DBHelper.IS_SAMPLE_CLICKED))).append(",");//1
                            sb.append(samplesCursor.getString(samplesCursor.getColumnIndex(DBHelper.TEMPERATURE))).append(",");  //2
                            sb.append(samplesCursor.getString(samplesCursor.getColumnIndex(DBHelper.NO_SAMPLE_PRODUCT))).append(",");//3
                            sb.append(samplesCursor.getInt(samplesCursor.getColumnIndex(DBHelper.SAMPLE_CURRENT_RATE))).append(",");  //4
                            sb.append(samplesCursor.getString(samplesCursor.getColumnIndex(DBHelper.PRODUCCT_NAME))).append(",");  //5
                            sb.append(samplesCursor.getString(samplesCursor.getColumnIndex(DBHelper.BRAND_NAME))).append(",");  //6
                            sb.append(samplesCursor.getString(samplesCursor.getColumnIndex(DBHelper.SELFLIFE))).append(",");  //7
                            sb.append(samplesCursor.getString(samplesCursor.getColumnIndex(DBHelper.MFDPKD))).append(",");    //8
                            sb.append(samplesCursor.getString(samplesCursor.getColumnIndex(DBHelper.MFDDATA))).append(",");  ///9
                            sb.append(samplesCursor.getString(samplesCursor.getColumnIndex(DBHelper.BB_EXP))).append(",");  //10
                            sb.append(samplesCursor.getString(samplesCursor.getColumnIndex(DBHelper.BBEXPDATA))); //11

                            stringBuilders.add(sb);
//                            JSONObject auditObject=new JSONObject();
//                            auditObject.put("isclicked",Boolean.parseBoolean(samplesCursor.getString(samplesCursor.getColumnIndex(DBHelper.SAMPLE_IS_CLICKED))));
//                           // auditObject.put("rate_x",samplesCursor.getInt(samplesCursor.getColumnIndex(DBHelper.SAMPLE_RATE_X)));
//                            auditObject.put("sample_count",samplesCursor.getInt(samplesCursor.getColumnIndex(DBHelper.SAMPLE_COUNT)));
//                            auditObject.put("sample_current_rate",samplesCursor.getInt(samplesCursor.getColumnIndex(DBHelper.SAMPLE_CURRENT_RATE)));
//                            auditObject.put("sample_pos",samplesCursor.getInt(samplesCursor.getColumnIndex(DBHelper.SAMPLE_POS)));
//                            auditSamples.put(auditObject);
                        } while (samplesCursor.moveToNext());

                    }
                    samplesCursor.close();
                    StringBuilder str;
                    JSONArray jsonArray = new JSONArray();

                    if (stringBuilders.size() > 0) {
                        for (int q = 0; q < stringBuilders.size(); q++) {
                            str = new StringBuilder();
                            str.append(stringBuilders.get(q).toString());
                            jsonArray.put(q, str.toString());

                        }
                        o.put("sampleAudits", jsonArray);
                    } else {
                        o.put("sampleAudits", jsonArray);
                    }

                    o.put("comment", c.getString(c.getColumnIndex(DBHelper.ANSWER_COMMENT)));
                    o.put("remark", c.getString(c.getColumnIndex(DBHelper.ANSWER_REMARK)));
                    o.put("actions", c.getString(c.getColumnIndex(DBHelper.ANSWER_ACTION)));
                    o.put("answer_type", c.getInt(c.getColumnIndex(DBHelper.ANSWER_TYPE)));
                    o.put("cat_skip", c.getString(c.getColumnIndex(DBHelper.ANSWER_CAT_SKIP)));
                    o.put("sec_exst", c.getString(c.getColumnIndex(DBHelper.SEC_EXISTS)));
                    o.put("question_fail", c.getString(c.getColumnIndex(DBHelper.QUESTION_FAIL)));
                    //   o.put("isSeen",Boolean.parseBoolean(c.getString(c.getColumnIndex(DBHelper.ANSWER_IS_SEEN))));
                    o.put("max_no", c.getInt(c.getColumnIndex(DBHelper.ANSWER_MAX_NO)));
                    o.put("max_sample", c.getInt(c.getColumnIndex(DBHelper.ANSWER_MAX_SAMPLE)));
                    o.put("no_sample", c.getInt(c.getColumnIndex(DBHelper.ANSWER_NO_SAMPLE)));
                    // o.put("ques_skip",c.getString(c.getColumnIndex(DBHelper.ANSWER_QUES_SKIP)));
                    if (c.getString(c.getColumnIndex(DBHelper.ANSWER_QUES_SKIP)).equalsIgnoreCase("no")) {
                        o.put("ques_skip", "0");
                    } else {
                        o.put("ques_skip", "1");
                    }
                    o.put("type", c.getInt(c.getColumnIndex(DBHelper.ANSWER_CAT_TYPE)));
                    o.put("answerDateTime", c.getString(c.getColumnIndex(DBHelper.ANSWER_DATETIME)));
                    array.put(o);

                    String categoryQuery = "SELECT * FROM " + DBHelper.CATEGORY_TBL_NAME + " WHERE " + DBHelper.STORE_ID + "=" + storeId + " AND " + DBHelper.CATEGORY_ID + "=" + categoryId;
                    Cursor catCursor = DbManager.getInstance().getDetails(categoryQuery);
                    String startDateTime = "", endDateTime = "";
                    if (catCursor.getCount() > 0) {
                        catCursor.moveToFirst();
                        startDateTime = catCursor.getString(catCursor.getColumnIndex(DBHelper.CATEGORY_START_DATE));
                        endDateTime = catCursor.getString(catCursor.getColumnIndex(DBHelper.CATEGORY_END_DATE));
                        auditocde=catCursor.getString(catCursor.getColumnIndex(DBHelper.AUDIT_CODE_IN_CATOERY));

                    }
                    catCursor.close();

                    storeObject.put("marchent_id", AppPrefrences.getMerchatId(ctx));
                    storeObject.put("store_id", storeId);
                    storeObject.put("cat_id", categoryId);
                    //       storeObject.put("audit_id",auditId);
                    //   storeObject.put("expiry_question","0");
                    storeObject.put("startdateTime", startDateTime);
                    storeObject.put("enddatetime", endDateTime);
                    storeObject.put("data", array);
                    storeObject.put("store_sign", "");
                    storeObject.put("audit_sign", "");
                    //    storeObject.put("audit_contact","");
                    storeObject.put("final_submit", "false");
                    storeObject.put("auditor_id", AppPrefrences.getUserId(ctx));
                    storeObject.put("lat", AppPrefrences.getLatitude(ctx));
                    storeObject.put("long", AppPrefrences.getLongitude(ctx));


                } catch (JSONException e) {
                    Log.e("Data Binding Error ", e.getMessage());
                }
            } while (c.moveToNext());

            //Log.e("Json Data",storeObject.toString());
            //AuditJson.setObject(storeObject);
        }
        c.close();
        return storeObject;
    }

    private void getStoreStartTime() {
        DbManager.getInstance().openDatabase();
        String checkStartDateTime = "SELECT " + DBHelper.DATE_TIME + " FROM " + DBHelper.STORE_START_TIME_TABLE + " WHERE " + DBHelper.STORE_ID + "=" + Store_id;
        Cursor checkStartDate = DbManager.getInstance().getDetails(checkStartDateTime);
        if (checkStartDate.getCount() > 0) {
            checkStartDate.moveToFirst();
            storeStartTime = checkStartDate.getString(checkStartDate.getColumnIndex(DBHelper.DATE_TIME));
            Log.e("StoreStartTime", storeStartTime);
        }
    }

    int totalLocFetchTryCount = 0;

    public class GetCurrentLatLong extends AsyncTask<Void, Void, LatLng> {

        TrackGPS trackGPS;

        @Override
        protected void onPreExecute() {
            trackGPS = new TrackGPS(ctx);
        }

        @Override
        protected LatLng doInBackground(Void... voids) {
            double lat = trackGPS.getLatitude();
            double lang = trackGPS.getLongitude();
            LatLng latLng = new LatLng(lat, lang);
            if (latLng.latitude != 0 && latLng.longitude != 0) {
                return latLng;
            }
            return null;
        }

        @Override
        protected void onPostExecute(LatLng latLng) {
            if (latLng == null) {
                totalLocFetchTryCount = totalLocFetchTryCount + 1;
                new GetCurrentLatLong().execute();
            } else {
                Log.e("Submit Report:> ", "Location: " + latLng.toString());
                AppPrefrences.setLatitude(ctx, latLng.latitude + "");
                AppPrefrences.setLongiTude(ctx, latLng.longitude + "");
            }
        }
    }


    private boolean isGpsEnabled() {
        LocationManager service = (LocationManager) ctx.getSystemService(LOCATION_SERVICE);
        return service.isProviderEnabled(LocationManager.GPS_PROVIDER) && service.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void submitsignature() {

        pd = new ProgressDialog(ctx);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();

        JSONObject jsonObject = new JSONObject();

        try {
            JSONArray jsonArray = new JSONArray();
            jsonObject.put("marchent_id", AppPrefrences.getMerchatId(ctx));
            jsonObject.put("store_id", storeId);
            jsonObject.put("audit_code", AppPrefrences.getAuditCODE(ctx));
            if (a == 0) {
                jsonObject.put("FileString", AppUtils.encodedimageAuditor);
            } else {
                jsonObject.put("FileString", AppUtils.encodedstoreimage);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = Vars.BASE_URL + "upladfile";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            /*    Log.i('+++insideupladfile1', "a="+a);
                Log.i('+++insideupladfile2', jsonObject);
                Log.i('+++insideupladfile3', response);*/

                if (pd != null && pd.isShowing())
                    pd.dismiss();

                try {
                    JSONArray jsonArray = response.getJSONArray("upladfileResult");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    Log.i("+++++upladfile", "onResponse: "+response);
                    boolean status = jsonObject.getBoolean("Status");

                    if (status) {

                        JSONArray jsonArray1 = jsonObject.getJSONArray("Payload");
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(0);

                        if (a == 0) {
                            auditor_filename = jsonObject1.getString("FileName");
                            a = 1;
                            Log.i("+++++auditor_filename", ""+auditor_filename);
                          new  Handler().postDelayed(new Runnable() {
                              @Override
                              public void run() {
                                  submitsignature();
                              }
                          },1000);

                        } else {
                            storefilename = jsonObject1.getString("FileName");
                            Log.i("+++++storefilename", ""+storefilename);
                            submitfinalData("");
                        }



                    } else {
                        Log.e("+++++Failed 5", "onResponse: "+status);
                        Toast.makeText(ctx, "Failed 5. Please try after some time", Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    Log.e("+++++Failed 6", "exp: "+e.toString());
                    Toast.makeText(ctx, "Failed 6. Please try after some time", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                Toast.makeText(ctx, "Failed 7. Please try after some time", Toast.LENGTH_LONG).show();
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjectRequest.setShouldCache(false);
        MySingleton.getInstance(ctx).addToRequestQueue(jsonObjectRequest);

    }

    private void uploadimage() {
        final ProgressDialog pdImageUpload = new ProgressDialog(ctx);
        image_counter = (AppPrefrences.getimageuploadcount(ctx) + 1);
        final SharedPreferences mSharedPreference1 = PreferenceManager.getDefaultSharedPreferences(ctx);
        image_progressdialog = (String.valueOf(image_counter) + "/" + String.valueOf(mSharedPreference1.getInt("Status_size", 0)) + " image uploading...");
        pdImageUpload.setMessage(image_progressdialog);
        pdImageUpload.setCancelable(false);
        pdImageUpload.show();

        S3FileUploadHelper transferHelper = new S3FileUploadHelper(ctx);

        transferHelper.upload(mSharedPreference1.getString("imagepath" + AppPrefrences.getimageuploadcount(ctx), null), mSharedPreference1.getString("imagename" + AppPrefrences.getimageuploadcount(ctx), null));///

        transferHelper.setFileTransferListener(new S3FileUploadHelper.FileTransferListener() {
            @Override
            public void onSuccess(int id, TransferState state, String fileName) {
                if (pdImageUpload != null && pdImageUpload.isShowing()) pdImageUpload.dismiss();
                image_counter = image_counter + 1;
                if (bb == mSharedPreference1.getInt("Status_size", 0) - 1) {
                    AppUtils.encodedimageAuditor = "";
                    AppUtils.encodedstoreimage = "";
                    deleteSubmittedData(String.valueOf(Store_id), String.valueOf(Cat_id));
                    Toast.makeText(ctx, "ALL DATA SUBMITTED SUCCESSFULLY", Toast.LENGTH_LONG).show();
                    openHomePage();
                } else {
                    imagecount.setText((bb + 1) + "/" + mSharedPreference1.getInt("Status_size", 0) + " image uploaded");
                    bb = bb + 1;

                    AppPrefrences.setimageuploadcount(ctx, bb);
                    uploadimage();
                }
                // AppLogger.d("S3FileUpload", String.format("%s uploaded successfully!", fileName));
                // profileImage = fileName;
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int) percentDonef;

                Log.d("S3FileUpload", "ID:" + id + " bytesCurrent: " + bytesCurrent
                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                if (pd != null && pd.isShowing())
                    pd.dismiss();

                Toast.makeText(ctx, "Error in uploading image. Please try after some time..", Toast.LENGTH_SHORT).show();

            }
        });


    }


    public void getdata() {
        o = null;
        o = getLocalSavedData(storeId, categoryIsList.get(idPosition));

        submitFirstStep();

    }


    public void saveArray() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor i_path = sp.edit();
        SharedPreferences.Editor i_name = sp.edit();
        SharedPreferences.Editor image_size = sp.edit();
        image_size.putInt("Status_size", imagepath.size());
        /* sKey is an array */

        for (int i = 0; i < imagepath.size(); i++) {
            i_path.putString("imagepath" + i, imagepath.get(i));
            i_name.putString("imagename" + i, imagename.get(i));
        }

        i_path.commit();
        i_name.commit();
        image_size.commit();
    }




}
