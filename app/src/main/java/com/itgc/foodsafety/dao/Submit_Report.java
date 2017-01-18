package com.itgc.foodsafety.dao;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 23/11/15.
 */
public class Submit_Report implements Serializable {

    private String audit_by, audit_contact, audit_sign, audit_id;
    private ArrayList<Answers> answerses;

    public ArrayList<Answers> getAnswerses() {
        return answerses;
    }

    public void setAnswerses(ArrayList<Answers> answerses) {
        this.answerses = answerses;
    }

    public String getAudit_by() {
        return audit_by;
    }

    public void setAudit_by(String audit_by) {
        this.audit_by = audit_by;
    }

    public String getAudit_contact() {
        return audit_contact;
    }

    public void setAudit_contact(String audit_contact) {
        this.audit_contact = audit_contact;
    }

    public String getAudit_sign() {
        return audit_sign;
    }

    public void setAudit_sign(String audit_sign) {
        this.audit_sign = audit_sign;
    }

    public String getAudit_id() {
        return audit_id;
    }

    public void setAudit_id(String audit_id) {
        this.audit_id = audit_id;
    }
}
