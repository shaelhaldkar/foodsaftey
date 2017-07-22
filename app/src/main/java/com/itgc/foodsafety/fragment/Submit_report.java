package com.itgc.foodsafety.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 4/11/15.
 */
public class Submit_report extends Fragment implements View.OnClickListener {
    public static final int SIGNATURE_ACTIVITY = 4;
    private Context ctx;
    private EditText auditerNameEditText, auditerContactNumberEditText;
    private Button signatureButton, submitReport, signatureButton1, expiry_btn,saveLocally;
    private String auditerName, auditerId, auditerContactNumber, Store_name, audit_sign,data="",startTime,endTime;
    private ArrayList<Answers> answersArrayList;
    private Bundle b;
    private int Cat_id, Store_id;
    private ArrayList<ArrayList<Answers>> lists;
    private ProgressDialog pd;
    private ImageView img_back;
    private String expiry = "";

    private String auditId="";
    private String categoryId="";
    private String storeId="";
    private ArrayList<String> categoryIsList=new ArrayList<>();
    private int idPosition=0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        b = getArguments();
        Store_id = b.getInt("Store_id");
        storeId=String.valueOf(Store_id);
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
//      Log.e("Json", AuditJson.getObject().toString());
//        try {
//            data=AuditJson.getObject().getJSONArray("data").toString();
//            Cat_id= Integer.parseInt(AuditJson.getObject().getString("cat_id"));
//            startTime= AuditJson.getObject().getString("startdateTime");
//            endTime= AuditJson.getObject().getString("enddatetime");
            getStoreSignature(String.valueOf(Store_id),String.valueOf(Cat_id));
//            Log.e("Data", data);
//            Log.e("Cat ID", Store_id+"");
//            Log.e("Store ID", Cat_id +"");
//            Log.e("Start_End time", startTime + "   " + endTime);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        getAllCompleteAudits();
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
        saveLocally = (Button) view.findViewById(R.id.submitLocally);

        signatureButton.setOnClickListener(this);
        signatureButton1.setOnClickListener(this);
        submitReport.setOnClickListener(this);
        img_back.setOnClickListener(this);
        expiry_btn.setOnClickListener(this);
        saveLocally.setOnClickListener(this);
    }

    private void restoreToolbar() {
        MainActivity.mToolbar.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.submitLocally:
                String error_message = "";
             if (AppUtils.encodedimage.equals(""))
            {
                error_message = "Please get Auditor sign.";
                Toast.makeText(ctx, error_message, Toast.LENGTH_LONG).show();
            } else if (AppUtils.encodedstoreimage.equals(""))
            {
                error_message = "Please get Store Manager sign.";
                Toast.makeText(ctx, error_message, Toast.LENGTH_LONG).show();
            } else if (expiry.equalsIgnoreCase("")) 
            {
                 error_message = "Please Add Expiry Status";
                 Toast.makeText(ctx, error_message, Toast.LENGTH_LONG).show();
             } else
             {
                saveSignature(1);
             }
                break;

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

                if (auditerName.isEmpty() || auditerName.equals(""))
                {
                    message = "Please enter the Auditer name.";
                    Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
                } else if (auditerContactNumber.isEmpty() || auditerContactNumber.equals(""))
                {
                    message = "Please enter the Auditer Contact Number.";
                    Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
                } else if (auditerContactNumber.length() > 10 || auditerContactNumber.length() < 10)
                {
                    message = "Please enter the Auditer Contact Number properly.";
                    Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
                } else if (AppUtils.encodedimage.equals(""))
                {
                    message = "Please get Auditor sign.";
                    Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
                } else if (AppUtils.encodedstoreimage.equals(""))
                {
                    message = "Please get Store Manager sign.";
                    Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
                } else
                    {
                    submitReport();
                }
                break;

            case R.id.expiry_btn:
                ExpiryDialog();
                break;
        }
    }

    private void saveSignature(int from)
    {
        saveStoreSignature(String.valueOf(Store_id),String.valueOf(Cat_id),AppUtils.encodedstoreimage,AppUtils.encodedimage,from);
    }

    private void submitReport() {
        //getSignature();
        if (expiry.equalsIgnoreCase("")) {
            Toast.makeText(ctx, "Please Add Expiry Status", Toast.LENGTH_LONG).show();
        } else
            saveSignature(2);
            submitFirstStep();
    }

    private void submitData(final String data1)
    {
        Log.d("", "data1 " + data1);
        pd = new ProgressDialog(ctx);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();
        StringRequest str = new StringRequest(Request.Method.POST,Vars.BASE_URL + Vars.SUBMIT_REPORT, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.e("Final Submit Response ", response);
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                if (response != null)
                {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(response);
                        String msg = jsonObject.getString("Message");
                        if(msg.contains("success"))
                        {
                            AppUtils.encodedimage = "";
                            AppUtils.encodedstoreimage="";
                            deleteSubmittedData(String.valueOf(Store_id),String.valueOf(Cat_id));

                        }
                        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
                        try
                        {
                            Intent intent = new Intent("DraftsCount");
                            ctx.sendBroadcast(intent);
                            getFragmentManager().beginTransaction().replace(R.id.container_body, new Store_Fragement()).addToBackStack("Store").commit();
                        } catch (Exception e) {}

                    } catch (Exception e)
                    {
                        if (pd != null && pd.isShowing())
                            pd.dismiss();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError v)
            {
                v.printStackTrace();
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                Toast.makeText(ctx, "Failed", Toast.LENGTH_LONG).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("data", "");
                params.put("audit_id", auditId);
                params.put("final_submit", "true");
                params.put("audit_sign",AppUtils.encodedimage);
                params.put("store_sign",AppUtils.encodedstoreimage);
                params.put("cat_id", "0");//categoryId);
                params.put("audit_contact", auditerContactNumberEditText.getText().toString());
                params.put("auditor_id", AppPrefrences.getUserId(ctx));
                params.put("lat", AppPrefrences.getLatitude(ctx));
                params.put("long", AppPrefrences.getLongitude(ctx));
                params.put("startdateTime", startTime);
                params.put("enddatetime", endTime);
                params.put("store_id", storeId);
                params.put("expiry_question", expiry);
                Log.e("Final Post Data ",params.toString());
                return params;
            }
        };
        str.setRetryPolicy(new DefaultRetryPolicy(120000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGNATURE_ACTIVITY)
        {
            
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

    private void saveStoreSignature(String storeId,String categoryId,String storeSign,String auditSign,int from)
    {
        DbManager.getInstance().deleteDetails(DBHelper.STORE_SIGNATURE_TBL_NAME,DBHelper.STORE_ID+"="+storeId);// + " AND " + DBHelper.CATEGORY_ID + "=" + categoryId);
        
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.STORE_ID, storeId);
        //cv.put(DBHelper.CATEGORY_ID, categoryId);
        cv.put(DBHelper.STORE_SIGNATURE_IMAGE, storeSign);
        cv.put(DBHelper.AUDIOTR_SIGNATURE_IMAGE, auditSign);
        cv.put(DBHelper.EXPIRY_QUESTION, expiry);
        DbManager.getInstance().insertDetails(cv, DBHelper.STORE_SIGNATURE_TBL_NAME);
        if(from==1)
        {
            Toast.makeText(ctx, "Details saved", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ctx,MainActivity.class));
            getActivity().finish();
        }
    }

    private void getStoreSignature(String storeId,String categoryId)
    {
        String query="SELECT * FROM "+ DBHelper.STORE_SIGNATURE_TBL_NAME + " WHERE " + DBHelper.STORE_ID + "=" + storeId;// + " AND " + DBHelper.CATEGORY_ID + "=" + categoryId;
        DbManager.getInstance().openDatabase();
        Cursor cursor = DbManager.getInstance().getDetails(query);
        Log.e("Signature Count", cursor.getCount()+ "   " + query);
        try
        {
            if(cursor.getCount()>0)
            {
                cursor.moveToFirst();
                AppUtils.encodedstoreimage=cursor.getString(2);
                AppUtils.encodedimage=cursor.getString(3);
                expiry=cursor.getString(4);
            }
        }catch (Exception e)
        {
            Log.e("Store Image Error",e.getMessage());
        }

    }

    private void submitFirstStep()
    {
        final ProgressDialog pd = new ProgressDialog(ctx);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();

        StringRequest str = new StringRequest(Request.Method.POST,
                Vars.BASE_URL + Vars.SUBMIT_REPORT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response)
            {
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                Log.e("First Submit Response", response);
                if (response != null)
                {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject payload = jsonObject.getJSONObject("Payload");
                        String msg = jsonObject.getString("Message");
                        //Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
                        //AppUtils.encodedimage = "";

                        String Code = jsonObject.getString("Code");
                        if (Code.equalsIgnoreCase("ok"))
                        {
                            AppPrefrences.setAuditId(ctx, payload.getString("audit_id"));
                            auditId=payload.getString("audit_id");
                            idPosition=idPosition+1;
                            if(idPosition<categoryIsList.size())
                            {
                                submitFirstStep();
                            }

                            if(idPosition==categoryIsList.size())
                            {
                                submitData("");
                            }
                        }

//                        idPosition=idPosition+1;
//                        if(idPosition<categoryIsList.size())
//                        {
//                            submitFirstStep();
//                        }
//                        // deleteData();
//                        try {
//
//                            Intent intent = new Intent("DraftsCount");
//                            ctx.sendBroadcast(intent);
//
//                            Fragment fragment = new StartAuditFragment();
//                            Bundle bundle = new Bundle();
//                            bundle.putInt("Store_id", Store_id);
//                            bundle.putString("Store_name", Store_name);
//                            fragment.setArguments(bundle);
//                            getFragmentManager().beginTransaction().replace(R.id.container_body, fragment).addToBackStack("Store").commit();

                        } catch (Exception e) {

                        }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError v)
            {
                v.printStackTrace();
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                Toast.makeText(ctx, "Failed", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                JSONObject o=getLocalSavedData(storeId,categoryIsList.get(idPosition));
                try {
                    data=o.getJSONArray("data").toString();
                    categoryId= String.valueOf(Integer.parseInt(o.getString("cat_id")));
                    startTime= o.getString("startdateTime");
                    endTime= o.getString("enddatetime");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                params.put("data", data);
                params.put("audit_id", auditId);
                params.put("cat_id", categoryId);
                params.put("audit_sign", "");
                params.put("final_submit", "false");
                params.put("store_sign", "");
                params.put("audit_contact", "");
                params.put("auditor_id", AppPrefrences.getUserId(ctx));
                params.put("lat", AppPrefrences.getLatitude(ctx));
                params.put("long", AppPrefrences.getLongitude(ctx));
                params.put("startdateTime", startTime);
                params.put("enddatetime", endTime);
                params.put("store_id", storeId);
                params.put("expiry_question", "0");

                Log.e("First Post Data", params.toString());

                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(
                120000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        str.setShouldCache(false);
        MySingleton.getInstance(ctx).addToRequestQueue(str);
    }

    private void deleteSubmittedData(String storeId,String categoryId)
    {
        //Delete Store Manager & Auditor Singature
        DbManager.getInstance().deleteDetails(DBHelper.STORE_SIGNATURE_TBL_NAME, DBHelper.STORE_ID + "=" + storeId);// + " AND " + DBHelper.CATEGORY_ID + "=" + categoryId);

        //Delete Data From Answer Table
        DbManager.getInstance().deleteDetails(DBHelper.ANSWER_TBL_NAME, DBHelper.STORE_ID + "=" + storeId);// + " AND " + DBHelper.CATEGORY_ID + "=" + categoryId);

        //Delete Data From Answer Image Table
        DbManager.getInstance().deleteDetails(DBHelper.ANSWER_IMAGE_TBL_NAME, DBHelper.STORE_ID + "=" + storeId);// + " AND " + DBHelper.CATEGORY_ID + "=" + categoryId);

        //Delete Data From Sample Audit Table
        DbManager.getInstance().deleteDetails(DBHelper.AUDIT_SAMPLE_TBL_NAME, DBHelper.STORE_ID + "=" + storeId);// + " AND " + DBHelper.CATEGORY_ID + "=" + categoryId);

        //Update Category Status
        ContentValues contentValues=new ContentValues();
        contentValues.put(DBHelper.CATEGORY_STATUS,"NULL");
        contentValues.put(DBHelper.CATEGORY_START_DATE,"");
        contentValues.put(DBHelper.CATEGORY_END_DATE,"");
        DbManager.getInstance().updateDetails(contentValues,DBHelper.CATEGORY_TBL_NAME,DBHelper.STORE_ID +"=" + storeId + " AND " + DBHelper.CATEGORY_STATUS +"='Complete'");
    }

    private void getAllCompleteAudits()
    {
        categoryIsList.clear();
        String completeAudit="SELECT * FROM " + DBHelper.CATEGORY_TBL_NAME + " WHERE " + DBHelper.STORE_ID + "=" + Store_id + " AND " + DBHelper.CATEGORY_STATUS + "='Complete'";
        Cursor completed=DbManager.getInstance().getDetails(completeAudit);

        if(completed.getCount()>0)
        {
            completed.moveToFirst();
            do
            {
                categoryId=completed.getString(completed.getColumnIndex(DBHelper.CATEGORY_ID));
                categoryIsList.add(categoryId);
                Log.e("Category Id",categoryId);
            }while (completed.moveToNext());
            Log.e("Total Category ",categoryIsList.size()+"");
        }
    }

    private JSONObject getLocalSavedData(String storeId,String categoryId)
    {
        DbManager.getInstance().openDatabase();
        Cursor c = DbManager.getInstance().getDetails("SELECT * FROM " + DBHelper.ANSWER_TBL_NAME + " WHERE " + DBHelper.STORE_ID + "=" + storeId + " AND " + DBHelper.CATEGORY_ID +"=" + categoryId);
        //Log.e("Category Count", c.getCount() + "");
        JSONObject storeObject=new JSONObject();
        JSONArray array=new JSONArray();
        JSONObject o;
        if (c.getCount()>0)
        {
            c.moveToFirst();
            do
            {
                o=new JSONObject();
                try
                {
                    o.put("store_id",c.getInt(c.getColumnIndex(DBHelper.STORE_ID)));
                    o.put("cat_id",c.getInt(c.getColumnIndex(DBHelper.CATEGORY_ID)));
                    o.put("subcat_id",c.getInt(c.getColumnIndex(DBHelper.ANSWER_SUBCAT_ID)));
                    o.put("question_id",c.getInt(c.getColumnIndex(DBHelper.QUESTION_ID)));
                    int quesId=c.getInt(c.getColumnIndex(DBHelper.QUESTION_ID));
                    int catId=c.getInt(c.getColumnIndex(DBHelper.QUESTION_ID));
                    int strId=c.getInt(c.getColumnIndex(DBHelper.QUESTION_ID));

                    // For Audit Images
                    JSONArray imageArray=new JSONArray();
                    Cursor imageCursor = DbManager.getInstance().getDetails("SELECT answerImage FROM " + DBHelper.ANSWER_IMAGE_TBL_NAME + " WHERE " + DBHelper.STORE_ID + "=" + storeId + " AND " +
                            DBHelper.CATEGORY_ID +"=" + categoryId + " AND " +
                            DBHelper.QUESTION_ID +"=" + quesId);
                    //Log.e("Image Count", imageCursor.getCount() + "");
                    if(imageCursor.getCount()>0)
                    {
                        imageCursor.moveToFirst();
                        do {
                            imageArray.put(imageCursor.getString(imageCursor.getColumnIndex(DBHelper.ANSWER_IMAGE)));
                        }while (imageCursor.moveToNext());
                        imageCursor.close();
                    }
                    o.put("image",imageArray);

                    // For Sample Audits
                    JSONArray auditSamples=new JSONArray();
                    String sampleQuery="SELECT * FROM " + DBHelper.AUDIT_SAMPLE_TBL_NAME + " WHERE " + DBHelper.STORE_ID + "=" + storeId + " AND " +
                            DBHelper.CATEGORY_ID + "=" + categoryId + " AND " +
                            DBHelper.QUESTION_ID + "=" +quesId;
                    Cursor samplesCursor=DbManager.getInstance().getDetails(sampleQuery);
                    //Log.e("Total Samples",samplesCursor.getCount()+"");
                    if(samplesCursor.getCount()>0)
                    {
                        samplesCursor.moveToFirst();
                        do
                        {
                            JSONObject auditObject=new JSONObject();
                            auditObject.put("isclicked",Boolean.parseBoolean(samplesCursor.getString(samplesCursor.getColumnIndex(DBHelper.SAMPLE_IS_CLICKED))));
                            auditObject.put("rate_x",samplesCursor.getInt(samplesCursor.getColumnIndex(DBHelper.SAMPLE_RATE_X)));
                            auditObject.put("sample_count",samplesCursor.getInt(samplesCursor.getColumnIndex(DBHelper.SAMPLE_COUNT)));
                            auditObject.put("sample_current_rate",samplesCursor.getInt(samplesCursor.getColumnIndex(DBHelper.SAMPLE_CURRENT_RATE)));
                            auditObject.put("sample_pos",samplesCursor.getInt(samplesCursor.getColumnIndex(DBHelper.SAMPLE_POS)));
                            auditSamples.put(auditObject);
                        }while (samplesCursor.moveToNext());
                        samplesCursor.close();
                    }
                    o.put("sampleAudits",auditSamples);

                    o.put("comment",c.getString(c.getColumnIndex(DBHelper.ANSWER_COMMENT)));
                    o.put("remark",c.getString(c.getColumnIndex(DBHelper.ANSWER_REMARK)));
                    o.put("actions",c.getString(c.getColumnIndex(DBHelper.ANSWER_ACTION)));
                    o.put("answer_type",c.getInt(c.getColumnIndex(DBHelper.ANSWER_TYPE)));
                    o.put("cat_skip",c.getString(c.getColumnIndex(DBHelper.ANSWER_CAT_SKIP)));
                    o.put("isSeen",Boolean.parseBoolean(c.getString(c.getColumnIndex(DBHelper.ANSWER_IS_SEEN))));
                    o.put("max_no",c.getInt(c.getColumnIndex(DBHelper.ANSWER_MAX_NO)));
                    o.put("max_sample",c.getInt(c.getColumnIndex(DBHelper.ANSWER_MAX_SAMPLE)));
                    o.put("no_sample",c.getInt(c.getColumnIndex(DBHelper.ANSWER_NO_SAMPLE)));
                    o.put("ques_skip",c.getString(c.getColumnIndex(DBHelper.ANSWER_QUES_SKIP)));
                    o.put("type",c.getInt(c.getColumnIndex(DBHelper.ANSWER_CAT_TYPE)));
                    o.put("answerDateTime",c.getString(c.getColumnIndex(DBHelper.ANSWER_DATETIME)));
                    array.put(o);

                    String categoryQuery="SELECT * FROM " + DBHelper.CATEGORY_TBL_NAME + " WHERE " + DBHelper.STORE_ID +"=" + storeId + " AND " + DBHelper.CATEGORY_ID +"=" + categoryId;
                    Cursor catCursor=DbManager.getInstance().getDetails(categoryQuery);
                    String startDateTime="",endDateTime="";
                    if(catCursor.getCount()>0)
                    {
                        catCursor.moveToFirst();
                        startDateTime=catCursor.getString(catCursor.getColumnIndex(DBHelper.CATEGORY_START_DATE));
                        endDateTime=catCursor.getString(catCursor.getColumnIndex(DBHelper.CATEGORY_END_DATE));
                    }

                    storeObject.put("store_id",storeId);
                    storeObject.put("cat_id",categoryId);
                    storeObject.put("audit_id",auditId);
                    storeObject.put("expiry_question","0");
                    storeObject.put("startdateTime",startDateTime);
                    storeObject.put("enddatetime",endDateTime);
                    storeObject.put("data",array);
                    storeObject.put("store_sign","");
                    storeObject.put("audit_sign","");
                    storeObject.put("audit_contact","");
                    storeObject.put("final_submit","false");
                    storeObject.put("auditor_id",AppPrefrences.getUserId(ctx));
                    storeObject.put("lat",AppPrefrences.getLatitude(ctx));
                    storeObject.put("long",AppPrefrences.getLongitude(ctx));

                } catch (JSONException e)
                {
                    Log.e("Data Binding Error ",e.getMessage());
                }
            } while (c.moveToNext());
            c.close();
            //Log.e("Json Data",storeObject.toString());
            //AuditJson.setObject(storeObject);
        }
        return storeObject;
    }

}
