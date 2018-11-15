package com.itgc.foodsafety.dao;

import java.io.Serializable;

/**
 * Created by root on 20/11/15.
 */
public class Stores implements Serializable {

    private String store_name, store_loc;
    private int store_id;
    private String merchant_id;

    public String getAudit_code() {
        return audit_code;
    }

    public void setAudit_code(String audit_code) {
        this.audit_code = audit_code;
    }

    public String getAudit_date() {
        return audit_date;
    }

    public void setAudit_date(String audit_date) {
        this.audit_date = audit_date;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String audit_code;
    public  String audit_date;
    public String latitude,longitude;
    public String merchant_name;

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getStore_loc() {
        return store_loc;
    }

    public void setStore_loc(String store_loc) {
        this.store_loc = store_loc;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }
}
