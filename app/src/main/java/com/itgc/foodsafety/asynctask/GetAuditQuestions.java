package com.itgc.foodsafety.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.itgc.foodsafety.dao.Audit;
import com.itgc.foodsafety.dao.StartAudit;
import com.itgc.foodsafety.utils.Vars;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 29/10/15.
 */
public class GetAuditQuestions extends AsyncTask<Void, Void, String> {

    public interface GetAuditQuestionsListener {
        void onGetQuestionsFinish(ArrayList<Audit> audits, boolean message);
    }

    private final GetAuditQuestionsListener questionsListener;
    ProgressDialog pd;
    private Context ctx;
    private boolean flag;
    private String merchantId;

    public GetAuditQuestions(Context _ctx, GetAuditQuestionsListener questionsListener, Boolean flag, String mer) {
        ctx = _ctx;
        this.questionsListener = questionsListener;
        this.flag = flag;
        this.merchantId = mer;

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
        Log.e("Response", s);
        if (pd != null && pd.isShowing())
            pd.dismiss();

        try {
            JSONObject main = new JSONObject(s);

            String code = main.getString("Code");
            String msg = main.getString("Message");
            boolean status = main.getBoolean("Status");

            if (status) {
                JSONArray c = main.getJSONArray("Payload");

                ArrayList<Audit> audits = new ArrayList<>();
                for (int i = 0; i < c.length(); i++) {
                    JSONObject jsonObject = (JSONObject) c.get(i);
                    Audit audit = new Audit();
                    audit.setCat_id(Integer.valueOf(jsonObject.getString("catId")));
                    audit.setCategory(jsonObject.getString("categoryName"));
                    audit.setType(jsonObject.getInt("type"));

                    JSONArray quesarray = jsonObject.getJSONArray("Questions");
                    ArrayList<StartAudit> startAudits = new ArrayList<>();
                    for (int j = 0; j < quesarray.length(); j++) {
                        JSONObject auditObject = (JSONObject) quesarray.get(j);
                        StartAudit startAudit = new StartAudit();
                        startAudit.setQuestion_id(Integer.valueOf(auditObject.getString("quesId")));
                        startAudit.setQuestion(auditObject.getString("questionText"));
                        startAudit.setSample_no(Integer.valueOf(auditObject.getString("numberOfSamples")));
                        startAudit.setSubCat(auditObject.getString("subCategoryName"));
                        startAudit.setSubcategory_id(auditObject.getInt("subCategoryId"));
                        startAudit.setDiscription(auditObject.getString("questionDesc"));

                        startAudits.add(startAudit);
                    }

                    audit.setAudits(startAudits);
                    audits.add(audit);

                }

                questionsListener.onGetQuestionsFinish(audits, status);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected String doInBackground(Void... params) {
        return validatehistoryList(ctx);
    }

    public String validatehistoryList(Context context) {
        HttpClient hc = new DefaultHttpClient(setTimeout());

        ResponseHandler<String> res = new BasicResponseHandler();

        HttpPost postMethod = new HttpPost(Vars.BASE_URL + Vars.AUDIT);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        String response = null;
        String ms = "";

        nameValuePairs.add(new BasicNameValuePair("merchantId", merchantId));

        try {
            postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException e) {

        }
        try {
            response = hc.execute(postMethod, res);
            ms = response.toString().trim();
        } catch (ClientProtocolException e) {

        } catch (IOException e) {

        }

        Log.e("RESPO", ms);
        return ms;


       /* InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        String result = "";
        try {

         String urlParameters =
                    "merchantId=" + URLEncoder.encode(merchantId, "UTF-8");


            URL url = new URL(Vars.BASE_URL + Vars.AUDIT);
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            urlConnection.setRequestProperty("Accept", "application/json");

            urlConnection.setRequestMethod("POST");

            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();



            urlConnection.connect();

            int statusCode = urlConnection.getResponseCode();


            if (statusCode == 200) {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                result = convertInputStreamToString(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 11. return result
        return result;*/

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

    public static HttpParams setTimeout() {
        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        int timeoutConnection = 30000;
        HttpConnectionParams.setConnectionTimeout(httpParameters,
                timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.


        return httpParameters;
    }

}
