package com.picksplug.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.picksplug.R;
import com.picksplug.helpers.Config;
import com.picksplug.helpers.PickPlugApp;
import com.picksplug.helpers.PreferenceConnector;

import static com.picksplug.helpers.Config.PAYPAL_PAYMENT_URL;
import static com.picksplug.helpers.PreferenceConnector.RELOAD_SUBSCRIPTION;
import static com.picksplug.helpers.PreferenceConnector.TAG_USER_ID;

public class ActPayment extends AppCompatActivity {
    private Context                 mContext;
    private WebView                 mWebView;
    private ProgressBar             mProgressBar;
    private String                  amount="",item_id="",item_name="",product_id="",REQUEST_URL = "",from="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_payment);

        mContext    =   this;

        initViews();
        //setProgressBarColor();

        Intent intent = getIntent();
        if(intent != null){

            from = intent.getStringExtra("from");

            if (from.equals("SubscriptionList")) {
                REQUEST_URL = PAYPAL_PAYMENT_URL;
                amount = intent.getStringExtra("item_amount");
                item_id = intent.getStringExtra("item_id");
                item_name = intent.getStringExtra("item_name");
                product_id = intent.getStringExtra("product_id");
                REQUEST_URL = REQUEST_URL + PreferenceConnector.readString(mContext, TAG_USER_ID, "") + "&amount=" + amount + "&item_name=" + item_name + "&item_id=" + item_id + "&product_id=" + product_id;
                System.out.println("Request Url ===== " + REQUEST_URL);
                setUpWebView(REQUEST_URL);
            } else if (from.equals("AllSportMonthlyRenew")){
                REQUEST_URL = Config.PAYPAL_MONTHLY_RENEW_URL;
                amount = intent.getStringExtra("item_amount");
                item_id = intent.getStringExtra("item_id");
                item_name = intent.getStringExtra("item_name");
                product_id = intent.getStringExtra("product_id");
                REQUEST_URL = REQUEST_URL + PreferenceConnector.readString(mContext, TAG_USER_ID, "") + "&amount=" + amount + "&item_name=" + item_name + "&item_id=" + item_id + "&product_id=" + product_id+ "&subs_type=monthly";
                System.out.println("Request Url ===== " + REQUEST_URL);
                setUpWebView(REQUEST_URL);
            }else if (from.equals("AllSportYearlyRenew")){
                REQUEST_URL = Config.PAYPAL_MONTHLY_RENEW_URL;
                amount = intent.getStringExtra("item_amount");
                item_id = intent.getStringExtra("item_id");
                item_name = intent.getStringExtra("item_name");
                product_id = intent.getStringExtra("product_id");
                REQUEST_URL = REQUEST_URL + PreferenceConnector.readString(mContext, TAG_USER_ID, "") + "&amount=" + amount + "&item_name=" + item_name + "&item_id=" + item_id + "&product_id=" + product_id+ "&subs_type=yearly";
                System.out.println("Request Url ===== " + REQUEST_URL);
                setUpWebView(REQUEST_URL);
            }else {
                REQUEST_URL = intent.getStringExtra("joinUrl");
                setUpWebView(REQUEST_URL);
            }
        }

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView webView, int newProgress) {
                mProgressBar.setProgress(newProgress);
            }
        });
    }

    private void initViews(){
        mWebView        =   (WebView)findViewById(R.id.webview);
        mProgressBar    =   (ProgressBar) findViewById(R.id.horizontalProgressBar);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setUpWebView(String REQUEST_URL){
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(REQUEST_URL);
        mWebView.setHorizontalScrollBarEnabled(false);

        mWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                 return super.shouldOverrideUrlLoading(view, request);
            }

            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                AlertDialog alertDialog = builder.create();
                String message = "SSL Certificate error.";
                switch (error.getPrimaryError()) {
                    case SslError.SSL_UNTRUSTED:
                        message = "The certificate authority is not trusted.";
                        break;
                    case SslError.SSL_EXPIRED:
                        message = "The certificate has expired.";
                        break;
                    case SslError.SSL_IDMISMATCH:
                        message = "The certificate Hostname mismatch.";
                        break;
                    case SslError.SSL_NOTYETVALID:
                        message = "The certificate is not yet valid.";
                        break;
                }

                message += " Do you want to continue anyway?";
                alertDialog.setTitle("SSL Certificate Error");
                alertDialog.setMessage(message);
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ignore SSL certificate errors
                        handler.proceed();
                    }
                });

                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        handler.cancel();
                    }
                });
                alertDialog.show();

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("success url","============"+url);
                if(url.contains("paypal.php?action=success") || url.contains("paypal.php?auth=")){
                    System.out.println("success url ====== " + url);
                    PreferenceConnector.writeString(mContext,RELOAD_SUBSCRIPTION,"YES");
                    showPurchaseDialog();
                } else if(url.contains("paypal.php?action=cancel")){
                    PreferenceConnector.writeString(mContext,RELOAD_SUBSCRIPTION,"YES");
                    onBackPressed();
                }
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

        });
    }

    @Override
    public void onBackPressed() {
        if (from.equals("SubscriptionList") || from.equals("AllSportMonthlyRenew") || from.equals("AllSportYearlyRenew")){
            finish();
            startActivity(new Intent(mContext,ActDashboard.class));
        } else {
            super.onBackPressed();
        }
    }

    private void showPurchaseDialog(){
            mWebView.setVisibility(View.GONE);

            final Dialog dialogOffer = new Dialog(mContext, R.style.CustomDialogTheme);
            dialogOffer.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogOffer.setCanceledOnTouchOutside(false);
            dialogOffer.setCancelable(false);
            dialogOffer.setContentView(R.layout.dialog_purchased);

            TextView txtTitle       =   (TextView) dialogOffer.findViewById(R.id.dialog_txt_title);
            TextView txtMsg         =   (TextView) dialogOffer.findViewById(R.id.dialog_txt_msg);
            Button btnOk            =   (Button) dialogOffer.findViewById(R.id.dialog_btn_ok);

            txtTitle.setTypeface(PickPlugApp.getInstance().getMediumFont());
            txtMsg.setTypeface(PickPlugApp.getInstance().getRegularFont());
            btnOk.setTypeface(PickPlugApp.getInstance().getMediumFont());

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogOffer.dismiss();
                    onBackPressed();
                }
            });

            dialogOffer.show();
    }

    private void setProgressBarColor(){
        Drawable progressDrawable = mProgressBar.getProgressDrawable().mutate();
        progressDrawable.setColorFilter(ContextCompat.getColor(mContext,R.color.colorProgressBar), android.graphics.PorterDuff.Mode.SRC_IN);
        mProgressBar.setProgressDrawable(progressDrawable);
    }
}
