package com.itgc.foodsafety.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by root on 4/11/15.
 */
public class CheckLoginTask extends AsyncTask<Void, Void, String> {

    public interface CheckLoginTaskListener {
        void getLoginFinishedListener(String message, boolean status);
    }

    private CheckLoginTaskListener checkLoginTaskListener;
    private Context ctx;
    ProgressDialog pd;

    public CheckLoginTask (Context ctx, CheckLoginTaskListener checkLoginTaskListener){
        this.checkLoginTaskListener = checkLoginTaskListener;
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(ctx);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(Void... params) {
        return null;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

            /* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }
        return result;
    }
}
