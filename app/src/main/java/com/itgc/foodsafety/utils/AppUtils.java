package com.itgc.foodsafety.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by root on 8/10/15.
 */
public class AppUtils {

    public static boolean registercmpny = false;
    public static boolean registerindi = false;

    public static int next = 1;
    public static int previous = 1;

    public static boolean nextclick = false;
    public static boolean previousclick = false;
    public static boolean isLogin = false;
    public static boolean isDraft = false;
    public static String encodedimageAuditor = "";
    public static String encodedstoreimage = "";

    public static AlertDialog d;

    public static void noInternetDialog(Context ctx) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
        dialog.setTitle("Oops");
        dialog.setMessage("Darn this internet is slow. Check you're online or try again later.");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // dialog.show();
        d = dialog.create();
        d.show();
    }

    public static final String FOOD_DIR = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/Android/data/com.itgc.foodsafety/";

    public final static void hideKeyBoard(Activity activity, View view) {
        if (activity != null) {
            // Code goes here.
            getInputManager(activity).hideSoftInputFromWindow(view.getWindowToken(), 0);

        }
    }

    private static InputMethodManager getInputManager(Activity activity) {
        return (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

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
