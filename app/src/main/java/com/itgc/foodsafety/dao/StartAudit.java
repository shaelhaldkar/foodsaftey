package com.itgc.foodsafety.dao;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 13/10/15.
 */
public class StartAudit implements Serializable {

    private String question;
    private String subCat;


    private String discription;
    private int question_id, sample_no, subcategory_id;
    private ArrayList<Sample_Audit> sample_audits = new ArrayList<Sample_Audit>();

    public ArrayList<Sample_Audit> getSample_audits() {
        return sample_audits;
    }

    public void setSample_audits(ArrayList<Sample_Audit> sample_audits) {
        this.sample_audits = sample_audits;
    }

    public int getSubcategory_id() {
        return subcategory_id;
    }

    public void setSubcategory_id(int subcategory_id) {
        this.subcategory_id = subcategory_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSubCat() {
        return subCat;
    }

    public void setSubCat(String subCat) {
        this.subCat = subCat;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getSample_no() {
        return sample_no;
    }

    public void setSample_no(int sample_no) {
        this.sample_no = sample_no;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }
}
