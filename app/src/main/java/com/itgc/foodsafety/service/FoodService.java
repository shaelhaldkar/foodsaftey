package com.itgc.foodsafety.service;

import android.app.IntentService;
import android.content.Intent;

import com.itgc.foodsafety.db.DbManager;

/**
 * Created by root on 26/10/15.
 */
public class FoodService extends IntentService {

    public FoodService()
    {
        super("FoodService");
    }

    public FoodService(String name) {
        super("FoodService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DbManager.getInstance().openDatabase();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

}
