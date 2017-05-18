package com.itgc.foodsafety.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.itgc.foodsafety.db.DBHelper;
import com.itgc.foodsafety.db.DbManager;

/**
 * Created by Farhan on 5/17/17.
 */

public class SyncBroadCast extends BroadcastReceiver
{
    private int answerId;
    private String answerCatType;
    private String answerStoreId;
    private String answerStore;
    private String answerStoreReg;
    private String answerValue;
    private String answerDraftValue;
    private String answerDatetIme;
    private String answerStatus;
    private String answerCategory;
    private String answerType;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null)
        {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                String query="SELECT * FROM answer where ans_status = 'Complete' OR ans_status = 'Incomplete'";
                DbManager.getInstance().openDatabase();
                Cursor cursor = DbManager.getInstance().getDetails(query);
                Log.e("Item Count", cursor.getCount()+"");
                if(cursor!=null)
                {
                    cursor.moveToFirst();
                    do
                    {
                        answerId=cursor.getInt(cursor.getColumnIndex(DBHelper.ANSWER_id));
                        answerCatType=cursor.getString(cursor.getColumnIndex(DBHelper.ANSWER_Cat));
                        Log.e("Answer Details",answerId +"  " +answerCatType);
                    }while (cursor.moveToNext());
                }
            }
        }
    }

}
