package com.itgc.foodsafety.utils;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by root on 4/11/15.
 */
public class AppPrefrences {

    public static void setStartTime(Context ctx, String Starttime) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString("starttime", Starttime).commit();
    }

    public static String getStartTime(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString("starttime", "");
    }

    public static void setUserName(Context ctx, String UserName) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString("username", UserName).commit();
    }

    public static String getUserName(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString("username", "");
    }

    public static void setUserId(Context ctx, String UserId) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString("userid", UserId).commit();
    }

    public static String getUserId(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString("userid", "");
    }


    public static void setAuditId(Context ctx, String UserId) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString("Auditid", UserId).commit();
    }

    public static String getAuditId(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString("Auditid", "0");
    }

    public static void setAuditCODE(Context ctx, String auditcode) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString("Auditcode", auditcode).commit();
    }

    public static String getAuditCODE(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString("Auditcode", "0");
    }

    public static void setMerchantId(Context ctx, String UserId) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString("merchant", UserId).commit();
    }

    public static String getMerchatId(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString("merchant", "");
    }

    public static void setOwnername(Context ctx, String Ownername) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString("ownername", Ownername).commit();
    }

    public static String getOwnername(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString("ownername", "");
    }

    public static void setLicense(Context ctx, String License) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString("license", License).commit();
    }

    public static String getLicense(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString("license", "");
    }

    public static void setEmail(Context ctx, String Email) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString("email", Email).commit();
    }

    public static String getEmail(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString("email", "");
    }

    public static void setMobileNo(Context ctx, String MobileNo) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString("mobileno", MobileNo).commit();
    }

    public static String getMobileNo(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString("mobileno", "");
    }

    public static void setPhoneNo(Context ctx, String PhoneNo) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString("phoneno", PhoneNo).commit();
    }

    public static String getPhoneNo(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString("phoneno", "");
    }

    public static void setPinCode(Context ctx, String Pincode) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString("pincode", Pincode).commit();
    }

    public static String getPinCode(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString("pincode", "");
    }

    public static void setIsLogin(Context ctx, boolean isLogin) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putBoolean("isLogin", isLogin).commit();
    }

    public static boolean getIsLogin(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean("isLogin", false);
    }

    public static void setStoreJson(Context ctx, String StoreJson) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString("storejson", StoreJson).commit();
    }

    public static String getStoreJson(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString("storejson", "");
    }

    public static void setReportCount(Context ctx, int count) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putInt("count", count).commit();
    }

    public static int getReportCount(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getInt("count", 0);
    }

    public static void setLatitude(Context ctx, String Ownername) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString("latidude", Ownername).commit();
    }

    public static String getLatitude(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString("latidude", "0");
    }

    public static void setLongiTude(Context ctx, String Ownername) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString("longitude", Ownername).commit();
    }

    public static String getLongitude(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString("longitude", "0");
    }

    public static void setimageuploadcount(Context ctx,int value)
    {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putInt("imageuploadcount",value).commit();
    }


    public static int getimageuploadcount(Context cyx)
    {
        return PreferenceManager.getDefaultSharedPreferences(cyx).getInt("imageuploadcount",0);
    }
}
