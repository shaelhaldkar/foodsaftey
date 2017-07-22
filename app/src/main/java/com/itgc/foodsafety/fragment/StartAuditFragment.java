package com.itgc.foodsafety.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.itgc.foodsafety.MainActivity;
import com.itgc.foodsafety.R;
import com.itgc.foodsafety.adapter.AuditAdapter;
import com.itgc.foodsafety.dao.AuditJson;
import com.itgc.foodsafety.dao.Categories;
import com.itgc.foodsafety.db.DBHelper;
import com.itgc.foodsafety.db.DbManager;
import com.itgc.foodsafety.ui.ExpandableHeightListView;
import com.itgc.foodsafety.utils.AppPrefrences;
import com.itgc.foodsafety.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by root on 9/10/15.
 */
public class StartAuditFragment extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private Context ctx;
    private TextView audit_date;
    private ExpandableHeightListView audit_list;
    private Button btn_submit;
    private AuditAdapter adapter;
    private ArrayList<Categories> categoriesArrayList = new ArrayList<>();
    private Fragment fragment;
    private Bundle b;
    private String store_name, store_loc, merchant_id;
    private int store_id, listPosition = 0;
    private ArrayList<String> strings = new ArrayList<>();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private double latitude, longitude;
    private ProgressDialog pd;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        ctx = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = getArguments();
        store_name = b.getString("Store_name");
        store_id = b.getInt("Store_id");
        store_loc = b.getString("Store_region");
        merchant_id = b.getString("merchant_id");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        restoreToolbar();
        View view = inflater.inflate(R.layout.auditfragment, container, false);
        setUpView(view);
        getAllCategories(store_id + "");
        return view;
    }

    private void setUpView(View view)
    {
        pd = new ProgressDialog(ctx);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        AppUtils.isDraft = false;
        audit_date = (TextView) view.findViewById(R.id.audit_date);
        String formattedDate = new SimpleDateFormat("dd MMM yyyy HH:mm a").format(Calendar.getInstance().getTime());
        audit_date.setText(getResources().getText(R.string.audit_title) + " " + formattedDate);

        audit_list = (ExpandableHeightListView) view.findViewById(R.id.audit_list);
        audit_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                checkStatus(position, Integer.parseInt(categoriesArrayList.get(position).getCategoryType()));
            }
        });

        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        //btn_submit.setVisibility(View.INVISIBLE);
        btn_submit.setOnClickListener(this);

        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }
    }

    private void checkStatus(int position, int type) {

        if (categoriesArrayList.get(position).getCategoryStatus().equalsIgnoreCase("Complete"))
        {
            Toast.makeText(ctx, "Please submit the current Audit Survey", Toast.LENGTH_LONG).show();
            listPosition=position;
            //new loadLocalData().execute();
            //getLocalSavedData(String.valueOf(store_id),String.valueOf(categoriesArrayList.get(position).getCategoryId()));
        }
        else
            {

            if (type == 0)
            {
                fragment = new AuditStartFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("Cat_id", Integer.parseInt(categoriesArrayList.get(position).getCategoryId()));
                bundle.putString("Cat_name", categoriesArrayList.get(position).getCategoryName());
                bundle.putInt("Type", Integer.parseInt(categoriesArrayList.get(position).getCategoryType()));
                bundle.putInt("Store_id", store_id);
                bundle.putString("Store_name", store_name);
                bundle.putString("Store_region", store_loc);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container_body, fragment).addToBackStack("Audit").commit();
            }
            else
            {
                fragment = new CheckAuditStatus();
                Bundle bundle = new Bundle();
                bundle.putInt("Cat_id", Integer.parseInt(categoriesArrayList.get(position).getCategoryId()));
                bundle.putString("Cat_name", categoriesArrayList.get(position).getCategoryName());
                bundle.putInt("Type", Integer.parseInt(categoriesArrayList.get(position).getCategoryType()));
                bundle.putInt("Store_id", store_id);
                bundle.putString("Store_name", store_name);
                bundle.putString("Store_region", store_loc);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container_body, fragment).addToBackStack("Audit").commit();
            }
        }

    }

    private void setAdapter(ArrayList<Categories> categoriesArrayList) {
        adapter = new AuditAdapter(ctx, categoriesArrayList, store_id);
        audit_list.setAdapter(adapter);
        audit_list.setExpanded(true);
        audit_list.setFocusable(false);
    }

    private void restoreToolbar() {
        MainActivity.mToolbar.setVisibility(View.VISIBLE);
        Toolbar toolbar = MainActivity.mToolbar;
        toolbar.setTitle(store_name);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitleTextAppearance(ctx, R.style.MyTitleTextApperance);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Store_Fragement();
                getFragmentManager().beginTransaction().replace(R.id.container_body, fragment).
                        addToBackStack("Store")
                        .commit();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                String completeAudit="SELECT * FROM " + DBHelper.CATEGORY_TBL_NAME + " WHERE " + DBHelper.STORE_ID + "=" + store_id + " AND " + DBHelper.CATEGORY_STATUS + "='Complete'";
                Cursor completed=DbManager.getInstance().getDetails(completeAudit);

                if(completed.getCount()>0)
                {
                    Fragment fragment = new Submit_report();
                    Bundle bundle = new Bundle();
                    bundle.putInt("Store_id", store_id);
                    bundle.putString("Store_name", store_name);
                    fragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.container_body, fragment)
                            .addToBackStack("Submit Report")
                            .commit();
                } else
                {
                    Toast.makeText(ctx, "No Audit has been completed", Toast.LENGTH_LONG).show();
                }
                Log.d("Status", "CLicked");
//                try {
//                    Cursor curs = null;
//                    String query = "";
//
//                    try {
//                        query = "SELECT * FROM answer where ans_storeid = '" + store_id + "' AND ans_status = 'Skipped' OR " +
//                                "ans_status = 'Complete' ";
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    DbManager.getInstance().openDatabase();
//                    curs = DbManager.getInstance().getDetails(query);
//                    curs.getCount();
//                    if (curs != null)
//                    {
//                        if (curs.moveToFirst())
//                        {
//                            Fragment fragment = new Submit_report();
//                            Bundle bundle = new Bundle();
//                            bundle.putInt("Store_id", store_id);
//                            bundle.putString("Store_name", store_name);
//                            fragment.setArguments(bundle);
//                            getFragmentManager().beginTransaction().replace(R.id.container_body, fragment)
//                                    .addToBackStack("Submit Report")
//                                    .commit();
//                        } else
//                        {
//                            Toast.makeText(ctx, "No Audit has been completed", Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResume() {
        super.onResume();
        checkPlayServices();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(ctx)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(ctx);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(ctx,
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
            }
            return false;
        }
        return true;
    }

    private void displayLocation() {

        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        } else {
            latitude = 0;
            longitude = 0;
        }

        Log.d("LatLong", latitude + ", " + longitude);
    }

    private void getAllCategories(String storeId)
    {
        DbManager.getInstance().openDatabase();
        Cursor c = DbManager.getInstance().getDetails("SELECT * FROM " + DBHelper.CATEGORY_TBL_NAME + " WHERE " + DBHelper.STORE_ID + "=" + storeId);
        Log.e("Category Count", c.getCount() + "");

        if (c != null) {
            categoriesArrayList.clear();
            c.moveToFirst();
            do {
                Categories categories = new Categories();
                categories.setCategoryId(c.getString(c.getColumnIndex(DBHelper.CATEGORY_ID)));
                categories.setCategoryName(c.getString(c.getColumnIndex(DBHelper.CATEGORY_NAME)));
                categories.setCategoryType(c.getString(c.getColumnIndex(DBHelper.CATEGORY_TYPE)));
                categories.setCategoryStatus(c.getString(c.getColumnIndex(DBHelper.CATEGORY_STATUS)));
                categoriesArrayList.add(categories);
            } while (c.moveToNext());
        }
        setAdapter(categoriesArrayList);
    }

    private void getLocalSavedData(String storeId,String categoryId)
    {
        DbManager.getInstance().openDatabase();
        Cursor c = DbManager.getInstance().getDetails("SELECT * FROM " + DBHelper.ANSWER_TBL_NAME + " WHERE " + DBHelper.STORE_ID + "=" + storeId + " AND " +
                                                                                                                DBHelper.CATEGORY_ID +"=" + categoryId);
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
                    storeObject.put("audit_id","0");
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

            Log.e("Data",storeObject.toString());
            AuditJson.setObject(storeObject);

//            Fragment fragment = new Submit_report();
//            Bundle bundle = new Bundle();
//            bundle.putInt("Store_id", store_id);
//            bundle.putString("Store_name", store_name);
//            fragment.setArguments(bundle);
//            getFragmentManager().beginTransaction().replace(R.id.container_body, fragment)
//                    .addToBackStack("Submit Report")
//                    .commit();
        }
    }

    class loadLocalData extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            getLocalSavedData(String.valueOf(store_id),String.valueOf(categoriesArrayList.get(listPosition).getCategoryId()));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.dismiss();
            Fragment fragment = new Submit_report();
            Bundle bundle = new Bundle();
            bundle.putInt("Store_id", store_id);
            bundle.putString("Store_name", store_name);
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.container_body, fragment)
                    .addToBackStack("Submit Report")
                    .commit();
        }
    }

}
