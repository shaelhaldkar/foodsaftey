package com.itgc.foodsafety.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
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
import com.itgc.foodsafety.asynctask.GetAuditQuestions;
import com.itgc.foodsafety.dao.Audit;
import com.itgc.foodsafety.db.DBHelper;
import com.itgc.foodsafety.db.DbManager;
import com.itgc.foodsafety.ui.ExpandableHeightListView;
import com.itgc.foodsafety.utils.AppPrefrences;
import com.itgc.foodsafety.utils.AppUtils;
import com.itgc.foodsafety.utils.Methods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by root on 9/10/15.
 */
public class StartAuditFragment extends Fragment implements GetAuditQuestions.GetAuditQuestionsListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private Context ctx;
    private TextView audit_date;
    private ExpandableHeightListView audit_list;
    private Button btn_submit;
    private AuditAdapter adapter;
    private ArrayList<Audit> audit;
    private Fragment fragment;
    private Bundle b;
    private String store_name, store_loc,merchant_id;
    private int store_id, page = 1;
    private ArrayList<String> strings = new ArrayList<>();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private double latitude, longitude;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        ctx = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        audit = new ArrayList<>();

        b = getArguments();
        store_name = b.getString("Store_name");
        store_id = b.getInt("Store_id");
        store_loc = b.getString("Store_region");
        merchant_id=b.getString("merchant_id");

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
        getData();

        return view;
    }

    private void getData() {
        if (Methods.checkInternetConnection(ctx)) {
            new GetAuditQuestions(ctx, StartAuditFragment.this, true, AppPrefrences.getMerchatId(ctx)).execute();
        } else
            AppUtils.noInternetDialog(ctx);
    }

    private void setUpView(View view) {
        AppUtils.isDraft = false;
        audit_date = (TextView) view.findViewById(R.id.audit_date);
        String formattedDate = new SimpleDateFormat("dd MMM yyyy HH:mm a").format(Calendar.getInstance().getTime());
        audit_date.setText(getResources().getText(R.string.audit_title) + " " + formattedDate);

        audit_list = (ExpandableHeightListView) view.findViewById(R.id.audit_list);
        audit_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkStatus(audit.get(position).getCat_id(), position, audit.get(position).getType());
            }
        });

        btn_submit = (Button) view.findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(this);
        audit.clear();

        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }
    }

    private void checkStatus(int Cat_id, int position, int type) {
        Cursor curs = null;
        String query = "";
        String status = "";

        try {
            query = "SELECT * FROM answer where ans_catid = '" + Cat_id + "' AND ans_storeid = '" + store_id + "' ";
        } catch (Exception e) {

        }

        DbManager.getInstance().openDatabase();
        curs = DbManager.getInstance().getDetails(query);

        while (curs != null && curs.moveToNext()) {
            status = curs.getString(curs.getColumnIndex(DBHelper.ANSWER_Status));
        }

        if (status.equals("Complete")) {
            Toast.makeText(ctx, "Please submit the current Audit Survey", Toast.LENGTH_LONG).show();
        } else {
            if (type == 0) {
                fragment = new AuditStartFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("DAO", audit.get(position).getAudits());
                bundle.putInt("Cat_id", audit.get(position).getCat_id());
                bundle.putString("Cat_name", audit.get(position).getCategory());
                bundle.putInt("Type", audit.get(position).getType());
                bundle.putInt("Store_id", store_id);
                bundle.putString("Store_name", store_name);
                bundle.putString("Store_region", store_loc);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container_body, fragment)
                        .addToBackStack("Audit").commit();
            } else {
                fragment = new CheckAuditStatus();
                Bundle bundle = new Bundle();
                Log.e("AUDITS---->>",audit.get(position).getAudits().toString());
                bundle.putSerializable("DAO", audit.get(position).getAudits());
                bundle.putInt("Cat_id", audit.get(position).getCat_id());
                bundle.putString("Cat_name", audit.get(position).getCategory());
                bundle.putInt("Type", audit.get(position).getType());
                bundle.putInt("Store_id", store_id);
                bundle.putString("Store_name", store_name);
                bundle.putString("Store_region", store_loc);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container_body, fragment)
                        .addToBackStack("Audit").commit();
            }
        }

    }

    private void setAdapter(ArrayList<Audit> audits, ArrayList<String> strings) {
        adapter = new AuditAdapter(ctx, audits, store_id, strings);
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
    public void onGetQuestionsFinish(ArrayList<Audit> audits, boolean status) {
        if (status) {
            this.audit = audits;
            for (int i = 0; i < audits.size(); i++) {
                strings.add(i, checkStatus(audits.get(i).getCat_id()));
            }
            setAdapter(audits, strings);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                Log.d("Status", "CLicked");
                try {
                    Cursor curs = null;
                    String query = "";

                    try {
                        query = "SELECT * FROM answer where ans_storeid = '" + store_id + "' AND ans_status = 'Skipped' OR " +
                                "ans_status = 'Complete' ";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    DbManager.getInstance().openDatabase();
                    curs = DbManager.getInstance().getDetails(query);
                    curs.getCount();
                    if (curs != null) {
                        if (curs.moveToFirst()) {
                            Fragment fragment = new Submit_report();
                            Bundle bundle = new Bundle();
                            bundle.putInt("Store_id", store_id);
                            bundle.putString("Store_name", store_name);
                            fragment.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(R.id.container_body, fragment).
                                    addToBackStack("Submit Report")
                                    .commit();
                        } else
                            Toast.makeText(ctx, "No Audit has been completed", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private String checkStatus(int catid) {
        //Log.d("Cat_id", "" + catid);
        Cursor curs = null;
        String query = "";
        String status = "";
        try {
            query = "SELECT ans_status FROM answer where ans_storeid = '" + store_id + "' AND ans_catid = '" + catid + "' ";

            DbManager.getInstance().openDatabase();
            curs = DbManager.getInstance().getDetails(query);

            if (curs != null && curs.moveToFirst()) {
                status = curs.getString(curs.getColumnIndex(DBHelper.ANSWER_Status));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
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

    /**
     * Method to verify google play services on the device
     */
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
}
