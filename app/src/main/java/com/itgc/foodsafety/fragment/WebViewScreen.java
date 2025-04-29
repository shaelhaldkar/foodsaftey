package com.itgc.foodsafety.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.itgc.foodsafety.R;


public class WebViewScreen extends Fragment implements View.OnClickListener {
    private ProgressDialog dialog;
    private Context context;
    private WebView mWebview;
    private ImageView img_back;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.webview, container, false);
        setUpView(view);

        return view;
    }

    private void setUpView(View view) {

        img_back = (ImageView) view.findViewById(R.id.img_back);

        mWebview = (WebView) view.findViewById(R.id.webview);
        mWebview.getSettings().setLoadsImagesAutomatically(true);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setAllowContentAccess(true);
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                dialog = new ProgressDialog(context);
                dialog.setMessage("Loading...");
                dialog.setCancelable(false);
                dialog.show();
            }

            public void onPageFinished(WebView view, String url) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        mWebview.loadUrl("file:///android_asset/html/Client_ Guidelines.htm");

        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                getFragmentManager().popBackStack();
                break;
        }
    }
}
