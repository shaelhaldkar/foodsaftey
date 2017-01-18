package com.itgc.foodsafety.dao;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 19/10/15.
 */
public class Drafts implements Serializable {

    private String draft_storename, draft_date, draft_cat, draft_status, draft_storeregion;
    private int draft_surveyid, draft_catid, draft_storeid, type;
    private ArrayList<Answers> answerses;
    private ArrayList<StartAudit> startAudits;

    public String getDraft_storeregion() {
        return draft_storeregion;
    }

    public void setDraft_storeregion(String draft_storeregion) {
        this.draft_storeregion = draft_storeregion;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDraft_status() {
        return draft_status;
    }

    public void setDraft_status(String draft_status) {
        this.draft_status = draft_status;
    }

    public ArrayList<StartAudit> getStartAudits() {
        return startAudits;
    }

    public void setStartAudits(ArrayList<StartAudit> startAudits) {
        this.startAudits = startAudits;
    }

    public ArrayList<Answers> getAnswerses() {
        return answerses;
    }

    public void setAnswerses(ArrayList<Answers> answerses) {
        this.answerses = answerses;
    }

    public String getDraft_cat() {
        return draft_cat;
    }

    public void setDraft_cat(String draft_cat) {
        this.draft_cat = draft_cat;
    }

    public int getDraft_catid() {
        return draft_catid;
    }

    public void setDraft_catid(int draft_catid) {
        this.draft_catid = draft_catid;
    }

    public int getDraft_storeid() {
        return draft_storeid;
    }

    public void setDraft_storeid(int draft_storeid) {
        this.draft_storeid = draft_storeid;
    }

    public String getDraft_storename() {
        return draft_storename;
    }

    public void setDraft_storename(String draft_storename) {
        this.draft_storename = draft_storename;
    }

    public int getDraft_surveyid() {
        return draft_surveyid;
    }

    public void setDraft_surveyid(int draft_surveyid) {
        this.draft_surveyid = draft_surveyid;
    }

    public String getDraft_date() {
        return draft_date;
    }

    public void setDraft_date(String draft_date) {
        this.draft_date = draft_date;
    }
}
