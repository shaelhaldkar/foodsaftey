package com.itgc.foodsafety.dao;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 13/10/15.
 */
public class Sample_Audit implements Serializable {

    private int sample_count, sample_pos;
    private float sample_current_rate, rate_x;
    private ArrayList<String> sample_rate;
    boolean isclicked=true;

    public boolean isclicked() {
        return isclicked;
    }

    public void setIsclicked(boolean isclicked) {
        this.isclicked = isclicked;
    }

    public float getRate_x() {
        return rate_x;
    }

    public void setRate_x(float rate_x) {
        this.rate_x = rate_x;
    }

    public float getSample_current_rate() {
        return sample_current_rate;
    }

    public void setSample_current_rate(float sample_current_rate) {
        this.sample_current_rate = sample_current_rate;
    }

    public int getSample_pos() {
        return sample_pos;
    }

    public void setSample_pos(int sample_pos) {
        this.sample_pos = sample_pos;
    }

    public int getSample_count() {
        return sample_count;
    }

    public void setSample_count(int sample_count) {
        this.sample_count = sample_count;
    }

    public ArrayList<String> getSample_rate() {
        return sample_rate;
    }

    public void setSample_rate(ArrayList<String> sample_rate) {
        this.sample_rate = sample_rate;
    }

}
