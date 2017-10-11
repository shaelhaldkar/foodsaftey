package com.itgc.foodsafety.dao;

/**
 * Created by Farhan on 5/30/17.
 */

public class DraftDetails
{
    private String storeId,categoryId,storeName,storeLocation,categoryName,marchantId,categoryStatus,statrDateTime;
    private int categoryType;

    public String getStatrDateTime() {
        return statrDateTime;
    }

    public void setStatrDateTime(String statrDateTime) {
        this.statrDateTime = statrDateTime;
    }

    public String getCategoryStatus() {
        return categoryStatus;
    }

    public void setCategoryStatus(String categoryStatus) {
        this.categoryStatus = categoryStatus;
    }

    public String getMarchantId() {
        return marchantId;
    }

    public void setMarchantId(String marchantId) {
        this.marchantId = marchantId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(String storeLocation) {
        this.storeLocation = storeLocation;
    }
}
