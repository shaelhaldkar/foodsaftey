package com.itgc.foodsafety.dao;

import java.io.Serializable;

/**
 * Created by root on 20/11/15.
 */
public class Stores implements Serializable {

    private String store_name, store_loc;
    private int store_id;
    private String merchant_id;

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
