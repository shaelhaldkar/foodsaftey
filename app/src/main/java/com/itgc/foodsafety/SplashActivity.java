package com.itgc.foodsafety;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.itgc.foodsafety.service.FoodService;
import com.itgc.foodsafety.utils.AppPrefrences;
import com.itgc.foodsafety.utils.AppUtils;
import com.itgc.foodsafety.utils.Methods;

import io.lookback.sdk.Lookback;


public class SplashActivity extends Activity {
    private Runnable runnable;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
   //     Lookback.show(getApplicationContext(), "rakshit.choudhary@sirez.com");
        handler = new Handler();
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
        showSplashScreen();
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
}