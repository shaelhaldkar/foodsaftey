package com.itgc.foodsafety.dao;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 19/11/15.
 */
public class Answers implements Serializable {

    private int store_id, cat_id, question_id, answer_type, no_sample, max_no, max_sample, subcat_id, type;
    private String remark;
    private String comment;
    private String question;
    private String ques_skip;
    private String cat_skip;

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    private String actions="";
    ArrayList<String> image = new ArrayList<>();
    private boolean isSeen = false;
    private ArrayList<Sample_Audit> sampleAudits;

    public boolean isSeen() {
        return isSeen;
    }

    public void setIsSeen(boolean isSeen) {
        this.isSeen = isSeen;
    }

    public String getQues_skip() {
        return ques_skip;
    }

    public void setQues_skip(String ques_skip) {
        this.ques_skip = ques_skip;
    }

    public String getCat_skip() {
        return cat_skip;
    }

    public void setCat_skip(String cat_skip) {
        this.cat_skip = cat_skip;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSubcat_id() {
        return subcat_id;
    }

    public void setSubcat_id(int subcat_id) {
        this.subcat_id = subcat_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getMax_sample() {
        return max_sample;
    }

    public void setMax_sample(int max_sample) {
        this.max_sample = max_sample;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public ArrayList<Sample_Audit> getSampleAudits() {
        return sampleAudits;
    }

    public void setSampleAudits(ArrayList<Sample_Audit> sampleAudits) {
        this.sampleAudits = sampleAudits;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getAnswer_type() {
        return answer_type;
    }

    public void setAnswer_type(int answer_type) {
        this.answer_type = answer_type;
    }

    public int getNo_sample() {
        return no_sample;
    }

    public void setNo_sample(int no_sample) {
        this.no_sample = no_sample;
    }

    public int getMax_no() {
        return max_no;
    }

    public void setMax_no(int max_no) {
        this.max_no = max_no;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getComment() {
        return comment;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
