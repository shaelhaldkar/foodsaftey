package com.itgc.foodsafety.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.itgc.foodsafety.MySingleton;
import com.itgc.foodsafety.R;
import com.itgc.foodsafety.utils.Vars;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 8/10/15.
 */
public class ForgetPassword extends Fragment implements View.OnClickListener {

    private ImageView img_back;
    private TextView toolbar_title, textView1;
    private EditText fgtpassEt;
    private Button sendEmailBtn;
    private Context ctx;
    private ProgressDialog pd;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = (Activity) context;
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forget_password, container, false);
        setUpView(view);

        return view;
    }

    private void setUpView(View view) {
        img_back = (ImageView) view.findViewById(R.id.img_back);

        toolbar_title = (TextView) view.findViewById(R.id.toolbar_title);
        textView1 = (TextView) view.findViewById(R.id.textView1);

        fgtpassEt = (EditText) view.findViewById(R.id.fgtpassEt);

        sendEmailBtn = (Button) view.findViewById(R.id.sendEmailBtn);

        img_back.setOnClickListener(this);
        sendEmailBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                getFragmentManager().beginTransaction().replace(R.id.container, new LoginFragment()).
                        addToBackStack("Login")
                        .commit();
                break;
            case R.id.sendEmailBtn:
                sendEmail();
                break;
        }
    }

    private void sendEmail() {
        if (fgtpassEt.getText().toString() != null && !fgtpassEt.getText().toString().isEmpty()){
            if (isEmailValid(fgtpassEt.getText().toString())){
                forgotPassword(fgtpassEt.getText().toString());
            }
            else {
                Toast.makeText(ctx, "Enter correct email format", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(ctx, "Enter email", Toast.LENGTH_LONG).show();
        }
    }

    private void forgotPassword(final String email) {
        pd = new ProgressDialog(ctx);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();

        String URL=Vars.BASE_URL+Vars.FORGOT_PASSWORD;

        JSONObject jsonObject=new JSONObject();

        try{
            jsonObject.put("email", email);
        }catch(Exception e){}

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (pd != null && pd.isShowing())
                            pd.dismiss();

                        try {
                            JSONArray jsonArray1 = response.getJSONArray("forgotPasswordResult");
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);

                            boolean Status = jsonObject1.getBoolean("Status");
                            if(Status) {

                                JSONArray payload=jsonObject1.getJSONArray("Payload");
                                JSONObject message = payload.getJSONObject(0);
                                String msg = message.getString("Message");
                                Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(ctx, "Request Failed!!", Toast.LENGTH_LONG).show();
                            }


                        }catch (Exception e)
                        {
                            Toast.makeText(ctx, "Request Failed!!", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                Toast.makeText(ctx, "Request Failed", Toast.LENGTH_LONG).show();
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(ctx).addToRequestQueue(jsonObjectRequest);
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

}
