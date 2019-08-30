package com.picksplug.helpers;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.app.Dialog;
import android.widget.*;
import android.view.*;
import android.net.Uri;
import android.view.View.OnClickListener;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;

public class AppRater{
    private final static String APP_TITLE = "Pick Plug";// App Name
    private final static String APP_PNAME = "com.picksplug";// Package Name

    private final static int DAYS_UNTIL_PROMPT = 5;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 0;//Min number of launches

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }

        editor.commit();
    }

    public static void showRateDialog (final Context mContext, final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("Rate " + APP_TITLE);

        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setMinimumWidth(480);

//        TextView title = new TextView(mContext);
//        title.setText("Rate Our App");
//        title.setPadding(50, 30, 10, 10);
//        title.setTextSize(23);
//        title.setTextColor(Color.BLACK);
//        ll.addView(title);

        TextView tv = new TextView(mContext);
        tv.setText("If you enjoy using " + APP_TITLE + ", please take a moment to rate it. Thanks for your support!");
        tv.setTextSize(16);
        tv.setPadding(50, 40, 20, 25);
        ll.addView(tv);

        LinearLayout ll1 = new LinearLayout(mContext);
        ll1.setOrientation(LinearLayout.HORIZONTAL);
        ll.addView(ll1);

        Button b1 = new Button(mContext);
        b1.setText("Rate Us");
        b1.setTextColor(Color.GREEN);
        b1.setBackgroundColor(Color.TRANSPARENT);
        b1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                dialog.dismiss();
            }
        });
        ll1.addView(b1);

        Button b2 = new Button(mContext);
        b2.setText("Later");
        b2.setTextColor(Color.GREEN);
        b2.setBackgroundColor(Color.TRANSPARENT);
        b2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll1.addView(b2);

        Button b3 = new Button(mContext);
        b3.setText("No, thanks");
        b3.setTextColor(Color.GREEN);
        b3.setBackgroundColor(Color.TRANSPARENT);
        b3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        ll1.addView(b3);

        dialog.setContentView(ll);
        dialog.show();
    }
}