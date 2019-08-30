package com.picksplug.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.picksplug.R;
import com.picksplug.activity_toolbar.HeaderToolbar;
import com.picksplug.activity_toolbar.HeaderToolbarListener;
import com.picksplug.helpers.Config;

public class ActLegal extends AppCompatActivity implements HeaderToolbarListener {
    private Context         mContext;
    private HeaderToolbar   headerToolbar;
    private ProgressBar     mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_legal);

        mContext    =   this;
        initViews();
        setProgressBarColor();
        setUpWebView();
    }

    private void initViews() {
        headerToolbar   =   (HeaderToolbar) findViewById(R.id.legal_toolbar);
        mProgressBar    =   (ProgressBar) findViewById(R.id.legalHorizontalProgressBar);

        headerToolbar.setUpToolbar(mContext,this);
        headerToolbar.setHeaderTitle(getResources().getString(R.string.nav_legal));
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setUpWebView(){
        WebView mWebView = (WebView) findViewById(R.id.webview_legal);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(Config.LEGAL_URL);

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView webView, int newProgress) {
                mProgressBar.setProgress(newProgress);
            }
        });

        mWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility(View.GONE);
            }

        });
    }

    @Override
    public void onClickHeaderToolbarBack() {
        onBackPressed();
    }

    @Override
    public void onClickNotification() {

    }

    private void setProgressBarColor(){
        Drawable progressDrawable = mProgressBar.getProgressDrawable().mutate();
        progressDrawable.setColorFilter(ContextCompat.getColor(mContext,R.color.colorProgressBar), android.graphics.PorterDuff.Mode.SRC_IN);
        mProgressBar.setProgressDrawable(progressDrawable);
    }
}
