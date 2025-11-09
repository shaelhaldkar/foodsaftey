package com.itgc.foodsafety.utils;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonRequest;
import com.itgc.foodsafety.BuildConfig;

import java.util.Collections;
import java.util.Map;

public class VolleyLogger {
    private static final String TAG = "VolleyLogger";
    private static VolleyLogger instance;
    private boolean isEnabled = BuildConfig.DEBUG;

    public static synchronized VolleyLogger getInstance() {
        if (instance == null) {
            instance = new VolleyLogger();
        }
        return instance;
    }

    public void enableLogging(boolean enable) {
        this.isEnabled = enable;
    }

    public void logRequest(Request<?> request) {
        if (!isEnabled) return;

        Log.d(TAG, "┌────── Volley Request ──────");
        Log.d(TAG, "│ URL: " + request.getUrl());
        Log.d(TAG, "│ Method: " + getMethodString(request.getMethod()));
        Log.d(TAG, "│ Headers: " + getHeadersSafe(request));

        if (request instanceof JsonRequest) {
            logJsonBody(request);
        }
        Log.d(TAG, "└──────────────────────────");
    }

    public void logResponse(NetworkResponse response, String url) {
        if (!isEnabled) return;

        Log.d(TAG, "┌────── Volley Response ──────");
        Log.d(TAG, "│ URL: " + url);
        Log.d(TAG, "│ Status: " + response.statusCode);
        Log.d(TAG, "│ Size: " + response.data.length + " bytes");
        Log.d(TAG, "│ Data: " + new String(response.data));
        Log.d(TAG, "└───────────────────────────");
    }

    private String getMethodString(int method) {
        switch (method) {
            case Request.Method.GET: return "GET";
            case Request.Method.POST: return "POST";
            case Request.Method.PUT: return "PUT";
            case Request.Method.DELETE: return "DELETE";
            default: return "UNKNOWN";
        }
    }

    private Map<String, String> getHeadersSafe(Request<?> request) {
        try {
            return request.getHeaders();
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    private void logJsonBody(Request<?> request) {
        try {
            byte[] body = request.getBody();
            if (body != null && body.length > 0) {
                String bodyStr = new String(body, "UTF-8");
                Log.d(TAG, "│ Body: " + bodyStr);
            }
        } catch (Exception e) {
            Log.d(TAG, "│ Body: [Unable to parse]");
        }
    }
}