package com.itgc.foodsafety.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.itgc.foodsafety.utils.Vars;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by root on 4/11/15.
 */
public class GetReportTask extends AsyncTask<Void, Void, String> {

    public interface GetReportTaskListener {
        void onGetReportTaskFinish(String url);
    }

    private GetReportTaskListener getReportTaskListener;
    ProgressDialog pd;
    private Context ctx;

    public GetReportTask(Context context, GetReportTaskListener getReportTaskListener){
        this.getReportTaskListener = getReportTaskListener;
        ctx = context;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (pd != null && pd.isShowing())
            pd.dismiss();

        try {
            JSONObject main = new JSONObject(s);

            String code = main.getString("Code");
            String msg = main.getString("Message");
            boolean status = main.getBoolean("Status");

            if (status) {
                JSONArray c = main.getJSONArray("Payload");

                getReportTaskListener.onGetReportTaskFinish(c.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    protected String doInBackground(Void... params) {
        return validatehistoryList(ctx);
    }

    private String validatehistoryList(Context ctx) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        String result = "";
        try {
            /* forming th java.net.URL object */
            URL url = new URL(Vars.BASE_URL + Vars.REPORT);
            urlConnection = (HttpURLConnection) url.openConnection();

                 /* optional request header */
            urlConnection.setRequestProperty("Content-Type", "application/json");

                /* optional request header */
            urlConnection.setRequestProperty("Accept", "application/json");

                /* for Get request */
            urlConnection.setRequestMethod("POST");
            int statusCode = urlConnection.getResponseCode();

                /* 200 represents HTTP OK */
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                result = convertInputStreamToString(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 11. return result
        return result;
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
