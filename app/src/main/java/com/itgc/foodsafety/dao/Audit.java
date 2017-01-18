package com.itgc.foodsafety.dao;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 10/10/15.
 */
public class Audit implements Serializable{

    private String category, status;
    private int cat_task, cat_id, type;
    private ArrayList<StartAudit> audits;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public ArrayList<StartAudit> getAudits() {
        return audits;
    }

    public void setAudits(ArrayList<StartAudit> audits) {
        this.audits = audits;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCat_task() {
        return cat_task;
    }

    public void setCat_task(int cat_task) {
        this.cat_task = cat_task;
    }
}
