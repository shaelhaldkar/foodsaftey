package com.itgc.foodsafety.dao;

/**
 * Created by Farhan on 5/30/17.
 */

public class SampleDetails
{
    private boolean isClicked;
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
