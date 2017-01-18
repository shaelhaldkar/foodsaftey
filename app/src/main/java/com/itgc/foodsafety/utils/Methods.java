package com.itgc.foodsafety.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by root on 26/10/15.
 */
public class Methods {

    // check for internet connectivity
    public static boolean checkInternetConnection(Context ctx) {
        ConnectivityManager mManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mManager.getActiveNetworkInfo();
        if ((mNetworkInfo != null) && (mNetworkInfo.isConnected())) {
            return true;
        }
        return false;
    }

}
