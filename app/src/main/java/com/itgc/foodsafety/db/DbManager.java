package com.itgc.foodsafety.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    public Cursor getDetails(String query) {
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
            Log.w("Status", "Details inserted Successfully");
        } catch (SQLiteException e) {

        }
    }

    public void deleteDetails(String pTblName, String wheredata) {
        try {
            mDb.delete(pTblName, wheredata, null);
        } catch (Exception e) {

        }
    }

}
