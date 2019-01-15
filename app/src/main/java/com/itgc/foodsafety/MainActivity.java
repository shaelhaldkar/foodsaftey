package com.itgc.foodsafety;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.itgc.foodsafety.fragment.AuditStartFragment;
import com.itgc.foodsafety.fragment.ContactUsFragment;
import com.itgc.foodsafety.fragment.DraftsFragment;
import com.itgc.foodsafety.fragment.FeedbackUsFragment;
import com.itgc.foodsafety.fragment.FragmentDrawer;
import com.itgc.foodsafety.fragment.MyProfileFragment;
import com.itgc.foodsafety.fragment.ReportFragment;
import com.itgc.foodsafety.fragment.Store_Fragement;
import com.itgc.foodsafety.utils.AppPrefrences;

/**
 * Created by root on 9/10/15.
 */
public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static String TAG = MainActivity.class.getSimpleName();

    public static Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private Boolean doubleBackToExitPressedOnce = false;
    public static String title;
    /* public static GoogleAnalytics analytics;
     public static Tracker tracker;*/
    private static int PERMISSION_LOCATION_REQUEST_CODE = 111;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        mToolbar.setTitleTextColor(Color.BLACK);
        mToolbar.setTitleTextAppearance(this, R.style.MyTitleTextApperance);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        displayView(0);

        if (checkPlayServices()) {
            //Building the GoogleApi client
            buildGoogleApiClient();
        }

        Log.e("Last Known Location",AppPrefrences.getLatitude(MainActivity.this)+"," +AppPrefrences.getLongitude(MainActivity.this));

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    public void displayView(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new Store_Fragement();
                title = "Stores";
                break;
            case 1:
                fragment = new DraftsFragment();
                title = "Drafts";
                break;
            case 2:
                fragment = new ReportFragment();
                title = "Reports";
                break;
            case 3:
                fragment = new ContactUsFragment();
                title = "Contact Us";
                break;
            case 4:
                fragment = new FeedbackUsFragment();
                title = "Feedback Us";
                break;
            case 5:
                fragment = new MyProfileFragment();
                title = "My Profile";
                break;
            case 6:
                signout();
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment, title);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    private void signout() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Sign Out!");
        dialog.setCancelable(true);
        dialog.setMessage("Are you sure you want to sign-out.");
        dialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppPrefrences.setEmail(getApplicationContext(), "");
                        AppPrefrences.setOwnername(getApplicationContext(), "");
                        AppPrefrences.setLicense(getApplicationContext(), "");
                        AppPrefrences.setMobileNo(getApplicationContext(), "");
                        AppPrefrences.setPhoneNo(getApplicationContext(), "");
                        AppPrefrences.setPinCode(getApplicationContext(), "");
                        AppPrefrences.setIsLogin(getApplicationContext(), false);
                        AppPrefrences.setStoreJson(getApplicationContext(), "");
                        AppPrefrences.setStartTime(getApplicationContext(), "");
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        dialog.dismiss();
                    }
                });
        dialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        Fragment myFragment = (Fragment) getSupportFragmentManager().findFragmentByTag("Stores");
        if (myFragment != null && myFragment.isVisible()) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press back once more to exit",
                    Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
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
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onResume() {
        super.onResume();
        checkPlayServices();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(MainActivity.this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, MainActivity.this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(MainActivity.this,
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
            }
            return false;
        }
        return true;
    }

    private void displayLocation() {

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions((MainActivity.this),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_LOCATION_REQUEST_CODE);
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            AppPrefrences.setLatitude(MainActivity.this, latitude + "");
            AppPrefrences.setLongiTude(MainActivity.this, longitude + "");
            Log.e(TAG,"LOCATION UPDATE "+ latitude + "----" + longitude);
        } else if (!isGpsEnabled()) {
            // openLocationDialog(ctx, "Please Enable Location");

        }
    }

    private boolean isGpsEnabled() {
        LocationManager service = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        return service.isProviderEnabled(LocationManager.GPS_PROVIDER) && service.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1000) {
            Fragment frg = getSupportFragmentManager().findFragmentById(R.id.container_body);
            if (frg != null) {
                frg.onActivityResult(requestCode, resultCode, data);
            }
        }


        if (requestCode == AuditStartFragment.GALLERY_IMG) {
            Fragment frg = getSupportFragmentManager().findFragmentById(R.id.container_body);
            if (frg != null) {
                frg.onActivityResult(requestCode, resultCode, data);
            }
        }

        if (requestCode == AuditStartFragment.LOAD_CAMERA_RESULTS) {
            Fragment frg = getSupportFragmentManager().findFragmentById(R.id.container_body);
            if (frg != null) {
                frg.onActivityResult(requestCode, resultCode, data);
            }
        }

    }*/
}
