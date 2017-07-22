package com.itgc.foodsafety.dao;

import java.util.ArrayList;

/**
 * Created by Farhan on 5/30/17.
 */

public class SampleAuditHolder
{
    private int samplePos;
    private ArrayList<SampleDetails> sampleDetailses;

    public int getSamplePos() {
        return samplePos;
    }

    public void setSamplePos(int samplePos) {
        this.samplePos = samplePos;
    }

    public ArrayList<SampleDetails> getSampleDetailses() {
        return sampleDetailses;
    }

    public void setSampleDetailses(ArrayList<SampleDetails> sampleDetailses) {
        this.sampleDetailses = sampleDetailses;
    }
}
