package com.itgc.foodsafety.dao;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 28/10/15.
 */
public class Branch implements Serializable {

    private ArrayList<Integer> branch_id;

    public ArrayList<Integer> getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(ArrayList<Integer> branch_id) {
        this.branch_id = branch_id;
    }
}
