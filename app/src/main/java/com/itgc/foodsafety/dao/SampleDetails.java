package com.itgc.foodsafety.dao;

/**
 * Created by Farhan on 5/30/17.
 */

public class SampleDetails
{
    private boolean isClicked;


    public String getNo_sample_product() {
        return no_sample_product;
    }

    public void setNo_sample_product(String no_sample_product) {
        this.no_sample_product = no_sample_product;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getMfdpkd() {
        return mfdpkd;
    }

    public void setMfdpkd(String mfdpkd) {
        this.mfdpkd = mfdpkd;
    }

    public String getMfd_date() {
        return mfd_date;
    }

    public void setMfd_date(String mfd_date) {
        this.mfd_date = mfd_date;
    }

    public String getBb_exp() {
        return bb_exp;
    }

    public void setBb_exp(String bb_exp) {
        this.bb_exp = bb_exp;
    }

    public String getBb_exp_date() {
        return bb_exp_date;
    }

    public void setBb_exp_date(String bb_exp_date) {
        this.bb_exp_date = bb_exp_date;
    }

    public int getShellife_value() {
        return shellife_value;
    }

    public void setShellife_value(int shellife_value) {
        this.shellife_value = shellife_value;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getIs_sample_failed() {
        return is_sample_failed;
    }

    public void setIs_sample_failed(int is_sample_failed) {
        this.is_sample_failed = is_sample_failed;
    }

    public String no_sample_product;
    public String product_name;
    public String brand_name;
    public String mfdpkd;
    public String mfd_date;
    public String bb_exp;
    public String bb_exp_date;
    public int  shellife_value;
    public String temperature;
    public int is_sample_failed;

    public boolean isIs_temp_visible() {
        return is_temp_visible;
    }

    public void setIs_temp_visible(boolean is_temp_visible) {
        this.is_temp_visible = is_temp_visible;
    }

    public  boolean is_temp_visible;
    private int rateX,sampleCount, sampleCurrentRate,sampleRate,samplePos;

    public int getSamplePos() {
        return samplePos;
    }

    public void setSamplePos(int samplePos) {
        this.samplePos = samplePos;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public int getRateX() {
        return rateX;
    }

    public void setRateX(int rateX) {
        this.rateX = rateX;
    }

    public int getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(int sampleCount) {
        this.sampleCount = sampleCount;
    }

    public int getSampleCurrentRate() {
        return sampleCurrentRate;
    }

    public void setSampleCurrentRate(int sampleCurrentRate) {
        this.sampleCurrentRate = sampleCurrentRate;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }
}
