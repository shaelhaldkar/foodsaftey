package com.itgc.foodsafety.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.itgc.foodsafety.utils.AppPrefrences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by root on 26/10/15.
 */
public class DbManager {

    private AtomicInteger mOpenCounter = new AtomicInteger();

    private static DbManager instance;
    private static SQLiteOpenHelper mDatabaseHelper;
    public SQLiteDatabase mDb;
    private DBHelper dbHelper;
    private Context context;

    public static synchronized void initializeInstance(SQLiteOpenHelper helper, Context context) {
        if (instance == null) {
            instance = new DbManager();
            mDatabaseHelper = helper;
        }
    }

    public static synchronized DbManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(DbManager.class.getSimpleName() + " is not initialized, call initializeInstance(..) method first.");
        }
        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
            mDb = mDatabaseHelper.getWritableDatabase();
        }
        return mDb;
    }

    public synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0) {
            mDb.close();
        }
    }

    public Cursor getDetails(String query)
    {
        return mDb.rawQuery(query, null);
    }

    public void updateDetails(ContentValues listcv, String pTblName, String wheredata) {
        try {
            mDb.beginTransaction();
            //mDb.replace(pTblName, null, cv);
            mDb.update(pTblName, listcv, wheredata, null);
            mDb.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            mDb.endTransaction();
        }
    }


    public void insertDetails(ContentValues listcv, String pTblName) {
        try {
            mDb.insert(pTblName, null, listcv);
            Log.e("Data Inserted On:> ",pTblName);
        } catch (SQLiteException e) {
e.printStackTrace();
        }
    }

    public void deleteDetails(String pTblName, String wheredata)
    {
        try {
            mDb.delete(pTblName, wheredata, null);
            Log.e("Data Deleted From:> ",pTblName);
        } catch (Exception e) {

        }
    }

    public void deleteStoreDetails()
    {

        mDb.execSQL("DELETE FROM " + DBHelper.STORE_INFO_TBL_NAME);
        mDb.execSQL("DELETE FROM " + DBHelper.STORE_SIGNATURE_TBL_NAME);
        mDb.execSQL("DELETE FROM " + DBHelper.CATEGORY_TBL_NAME);
        mDb.execSQL("DELETE FROM " + DBHelper.QUESTION_TBL_NAME);
        mDb.execSQL("DELETE FROM " + DBHelper.ANSWER_TBL_NAME);
        mDb.execSQL("DELETE FROM " + DBHelper.ANSWER_IMAGE_TBL_NAME);
      //  mDb.execSQL("DELETE FROM " + DBHelper.ANSWER_IMAGE_TBL_PATH);
        mDb.execSQL("DELETE FROM " + DBHelper.AUDIT_SAMPLE_TBL_NAME);
        mDb.execSQL("DELETE FROM " + DBHelper.STORE_DETAILS_TBL_NAME1);
        mDb.execSQL("DELETE FROM " + DBHelper.STORE_START_TIME_TABLE);
        Log.e("Delete", "Data From StoreInfo,CategoryInfo & QuestionInfo Table");
    }

    public void updateCategory(String status,int storeId,int categoryId)
    {
        mDb.execSQL("UPDATE " + DBHelper.CATEGORY_TBL_NAME + " SET " + DBHelper.CATEGORY_STATUS +"='" + status + "' WHERE "  + DBHelper.STORE_ID +"=" + storeId + " AND " + DBHelper.CATEGORY_ID +"=" + categoryId);
        Log.e("Update Successfully", "Category Table");
    }

    public void saveStoreDetails(JSONObject object)
    {
        String storeID="",storeName="", marchantId="",storeRegion="",auditdate="",auditcode="",latitude="",longitude="",merchantname="";
        try {
            storeID=object.getString("store_id");
            storeName=object.getString("storeName");
            marchantId=object.getString("merchantId");
            storeRegion=object.getString("region");

            auditdate=object.getString("auditdate");
            auditcode=object.getString("auditcode");
            latitude=object.getString("lat");
            longitude=object.getString("longs");
            merchantname=object.getString("merchantName");

            ContentValues cv = new ContentValues();
            cv.put(DBHelper.STORE_ID, String.valueOf(storeID));
            cv.put(DBHelper.ACCOUNT_NAME, object.getString("AccountName"));
            cv.put(DBHelper.CHILLERS, object.getString("NoofChillers"));
            cv.put(DBHelper.FREZERS, object.getString("NoofFreezers"));
            cv.put(DBHelper.VENDOR_CHILLERS, object.getString("NoofChillersVendors"));
            cv.put(DBHelper.VENDOR_FREZEERS, object.getString("NoofFreezersVendors"));
            cv.put(DBHelper.REDANT_BOXEX, object.getString("NoofRodentBoxes"));
            cv.put(DBHelper.FLY_CATCHERS, object.getString("NoofFlyCatchers"));
            cv.put(DBHelper.AIR_CUTTERS, object.getString("NoofAirCutters"));
            cv.put(DBHelper.THERMOMETERS, object.getString("NoofThermometers"));
            cv.put(DBHelper.MANAGER_NAME, object.getString("StoreManagerName"));
            cv.put(DBHelper.MANAGER_EMAIL, object.getString("StoreManagerEmailID"));
            cv.put(DBHelper.FSSAI_LIC,object.getString("FSSAILicDate"));
            DbManager.getInstance().insertDetails(cv, DBHelper.STORE_DETAILS_TBL_NAME1);
        }catch (Exception e){Log.e("Error","At saveStoreDetails()"+e.getMessage());}

        ContentValues c=new ContentValues();
        c.put(DBHelper.STORE_ID,storeID);
        c.put(DBHelper.STORE_NAME,storeName);
        c.put(DBHelper.STORE_MARCHANT_ID,marchantId);
        c.put(DBHelper.AUDIT_CODE,auditcode);
        c.put(DBHelper.AUDIT_DATE,auditdate);
        c.put(DBHelper.LATITUDE,latitude);
        c.put(DBHelper.LONGITUDE,longitude);
        c.put(DBHelper.MERCHANT_NAME,merchantname);
        c.put(DBHelper.STORE_REGION,storeRegion);
        mDb.insert(DBHelper.STORE_INFO_TBL_NAME,null,c);
        //Log.e("Insert", "Into StoreInfo Table");

        try
        {
            JSONArray array=object.getJSONArray("auditList");
            for(int i=0;i<array.length();i++)
            {
                savecategory(storeID,array.getJSONObject(i),auditcode);
            }
        }catch (Exception e){Log.e("Error","At Second Catch saveStoreDetails()"+e.getMessage());}
    }

    void savecategory(String storeId,JSONObject object,String auditcode)
    {
        try
        {
            String categoryId=object.getString("catId");
            ContentValues c=new ContentValues();
            c.put(DBHelper.STORE_ID,storeId);
            c.put(DBHelper.CATEGORY_ID,categoryId);
            c.put(DBHelper.AUDIT_CODE_IN_CATOERY,auditcode);
            c.put(DBHelper.CATEGORY_NAME,object.getString("categoryName"));
            c.put(DBHelper.CATEGORY_TYPE,object.getString("type"));
            c.put(DBHelper.CATEGORY_STATUS,"NULL");
            mDb.insert(DBHelper.CATEGORY_TBL_NAME,null,c);
            //Log.e("Insert", "Into CategoryInfo Table");

            JSONArray question=object.getJSONArray("Questions");
            for(int q=0;q<question.length();q++)
            {
                //Log.e("Inserting Into", object.getString("categoryName"));
                saveCategoryQuestions(storeId,categoryId,question.getJSONObject(q));
            }

        }catch (Exception e){Log.e("Error","At saveCategoty()"+e.getMessage());}
    }

    void saveCategoryQuestions(String storeId,String categoryId,JSONObject object)
    {
        try
        {
            //Log.e("Inserting Question Id", object.getString("quesId"));
            ContentValues c=new ContentValues();
            c.put(DBHelper.STORE_ID,storeId);
            c.put(DBHelper.CATEGORY_ID,categoryId);
            c.put(DBHelper.QUESTION_ID,object.getString("quesId"));
            c.put(DBHelper.QUESTION_SUB_CAT_ID,object.getString("subCategoryId"));
            c.put(DBHelper.QUESTION_SUB_CAT_NAME,object.getString("subCategoryName"));
            c.put(DBHelper.QUESTION_TEXT,object.getString("questionText"));
            c.put(DBHelper.QUESTION_DESC,object.getString("questionDesc"));
            c.put(DBHelper.QUESTION_TYPE,object.getString("type"));
            c.put(DBHelper.VERSION,object.getString("version"));
            c.put(DBHelper.QUESTION_SAMPLES,object.getString("numberOfSamples"));

            mDb.insert(DBHelper.QUESTION_TBL_NAME,null,c);
            Log.e("Insert", storeId + " " + categoryId + " " +object.getString("quesId"));

        }catch (Exception e){Log.e("Error","At saveCategotyQuestion()"+e.getMessage());}
    }



}
