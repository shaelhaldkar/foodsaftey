package com.itgc.foodsafety.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.itgc.foodsafety.MainActivity;
import com.itgc.foodsafety.MySingleton;
import com.itgc.foodsafety.R;
import com.itgc.foodsafety.db.DBHelper;
import com.itgc.foodsafety.db.DbManager;
import com.itgc.foodsafety.utils.AppPrefrences;
import com.itgc.foodsafety.utils.Vars;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 8/10/15.
 */
public class LoginFragment extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private EditText edt_username, edt_password;
    private TextView txt_forgetpass;
    private Button btn_sign;
    private Context ctx;
    private ProgressDialog pd;
    private String DeviceId;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private double latitude, longitude;
    DBHelper dbHelper;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = (Activity) context;
        dbHelper = new DBHelper(ctx, "FoodSafety.db");
        dbHelper.openDataBase();
        DbManager.initializeInstance(dbHelper, ctx);
        DbManager.getInstance().openDatabase();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        setUpView(view);
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    private void setUpView(View view) {
        DeviceId = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);

        edt_username = (EditText) view.findViewById(R.id.edt_username);
        edt_password = (EditText) view.findViewById(R.id.edt_password);

        txt_forgetpass = (TextView) view.findViewById(R.id.forget_password);
        SpannableString content = new SpannableString(txt_forgetpass.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        txt_forgetpass.setText(content);

        btn_sign = (Button) view.findViewById(R.id.btn_signin);

        txt_forgetpass.setOnClickListener(this);
        btn_sign.setOnClickListener(this);

        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forget_password:
                getFragmentManager().beginTransaction().replace(R.id.container, new ForgetPassword()).
                        addToBackStack("Forget Passwrd")
                        .commit();
                break;
            case R.id.btn_signin:
                if (edt_username.getText().toString() != null && !edt_username.getText().toString().isEmpty()) {
                    if (edt_password.getText().toString() != null && !edt_password.getText().toString().isEmpty()) {
                        if (emailValidator(edt_username.getText().toString())) {
                            displayLocation();
                            checkLogin(edt_password.getText().toString(), edt_username.getText().toString());
                        } else
                            Toast.makeText(ctx, "Please enter correct Email.", Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(ctx, "Please enter Password.", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(ctx, "Please enter Email Id.", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void checkLogin(final String password, final String username) {
        final ProgressDialog pd = new ProgressDialog(ctx);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();

        StringRequest str = new StringRequest(Request.Method.POST,
                Vars.BASE_URL + Vars.OFFLINELOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Login Response--", response);
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        boolean Status = jsonObject.getBoolean("Status");
                        String msg = jsonObject.getString("Message");

                        JSONObject payload = jsonObject.getJSONObject("Payload");

                        if (Status)
                        {
                            AppPrefrences.setUserId(ctx, payload.getString("UserId"));
                            AppPrefrences.setEmail(ctx, payload.getString("email"));
                            AppPrefrences.setUserName(ctx, payload.getString("userName"));
                            AppPrefrences.setMobileNo(ctx, payload.getString("mobileNumber"));

                            JSONArray jsonArray = payload.getJSONArray("store");
                            AppPrefrences.setStoreJson(ctx, jsonArray.toString());

                            if (jsonArray.length() > 0)
                            {
                                DbManager.getInstance().openDatabase();
                                DbManager.getInstance().deleteStoreDetails();
                                JSONObject ob = jsonArray.getJSONObject(0);
                                AppPrefrences.setMerchantId(ctx, ob.getString("merchantId"));

                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    DbManager.getInstance().saveStoreDetails(jsonArray.getJSONObject(i));
                                }
                            }


                            AppPrefrences.setIsLogin(ctx, true);
                            Intent i = new Intent(ctx, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        } else {
                            Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
                            edt_username.setText("");
                            edt_password.setText("");
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
                params.put("email", username);
                params.put("password", password);
                params.put("deviceType", "a");
                params.put("deviceId", DeviceId);
                Log.e("Login Params",new JSONObject(params).toString());
                return params;
            }
        };
        str.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(ctx).addToRequestQueue(str);
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

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
