package com.itgc.foodsafety;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.itgc.foodsafety.service.FoodService;
import com.itgc.foodsafety.utils.AppPrefrences;
import com.itgc.foodsafety.utils.AppUtils;
import com.itgc.foodsafety.utils.Methods;

import io.sentry.Sentry;


public class SplashActivity extends Activity {
    private Runnable runnable;
    private Handler handler;
    private static final int REQUEST= 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        int versionCode = BuildConfig.VERSION_CODE;
       TextView bversio = findViewById(R.id.tvversion);
        bversio.setText("Version :"+versionCode);
        AWSMobileClient.getInstance().initialize(this).execute();
        handler = new Handler();

        String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};

        if (!hasPermissions(SplashActivity.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) this, PERMISSIONS, REQUEST);
        }
        else {
            showSplashScreen();
        }
   //     Lookback.show(getApplicationContext(), "rakshit.choudhary@sirez.com");

    }

    private void updateDB() {
        if (Methods.checkInternetConnection(this))
            startService(new Intent(getApplicationContext(), FoodService.class));
        else
            AppUtils.noInternetDialog(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (runnable != null)
            handler.removeCallbacks(runnable);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //updateDB();

    }

    private void showSplashScreen() {
        runnable = new Runnable() {
            public void run() {
                if (AppPrefrences.getIsLogin(getApplicationContext())){
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    //finish();
                }
                else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    //finish();
                }

            }
        };
        handler.postDelayed(runnable, 3000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showSplashScreen();
                } else
                {
                    Toast.makeText(SplashActivity.this, "PERMISSIONS Denied", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }

    }

    public static boolean hasPermissions(Context context, String... permissions)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null)
        {
            for (String permission : permissions)
            {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                {
                    return false;
                }
            }

        }

        return true;
    }
}