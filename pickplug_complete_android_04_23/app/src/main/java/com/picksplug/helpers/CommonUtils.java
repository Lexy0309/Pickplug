/*
 * Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * 
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.picksplug.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.JsonObject;
import com.picksplug.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents all the common functions used throughout the application.
 */
public class CommonUtils {

    public static String TAG = CommonUtils.class.getSimpleName();

    public static final int SC_UNAUTHORIZED = 401;
    public static final int SC_AUTHFAILED = 400;

    public static String setTimeElapse(String dateString) {
        if (dateString != null) {
            String text = "";
            try {
                //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                long date = simpleDateFormat.parse(dateString).getTime();


                if (date > 0) {
                    long difference = Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTimeInMillis() - (date);

                    if (difference < 60000) {
                        text = "Few seconds Ago";
                    } else {
                        long diffSeconds = difference / 1000;
                        long diffMinutes = diffSeconds / 60;
                        long diffHours = diffMinutes / 60;
                        long diffDays = difference / (24 * 60 * 60 * 1000);
                        long diffMonths = diffDays / 30;
                        long diffYears = diffMonths / 12;
                        if (diffYears > 0) {
                            SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd,yyyy", Locale.getDefault());
                            dateFormatter.setTimeZone(TimeZone.getDefault());
                            text = dateFormatter.format(new Date(date));

                        } else if (diffMonths > 0) {
                            SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd", Locale.getDefault());
                            dateFormatter.setTimeZone(TimeZone.getDefault());
                            text = dateFormatter.format(new Date(date));

                        } else if (diffDays > 7) {
                            SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd' at 'hh:mm a", Locale.getDefault());
                            dateFormatter.setTimeZone(TimeZone.getDefault());
                            text = dateFormatter.format(new Date(date));

                        } else if (diffDays > 1) {
                            SimpleDateFormat dateFormatter = new SimpleDateFormat("E 'at' hh:mm a", Locale.getDefault());
                            dateFormatter.setTimeZone(TimeZone.getDefault());
                            text = dateFormatter.format(new Date(date));
                        } else if (diffDays == 1) {
                            SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                            dateFormatter.setTimeZone(TimeZone.getDefault());
                            text = "Yesterday at " + dateFormatter.format(new Date(date));
                        } else if (diffHours > 11) {
                            SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                            dateFormatter.setTimeZone(TimeZone.getDefault());
                            text = "Today at " + dateFormatter.format(new Date(date));
                        } else if (diffHours > 0) {
                            long minsLeft = 0;
                            if (false) {
                                minsLeft = diffMinutes - (diffHours * 60);
                            }
                            text = (diffHours + (diffHours == 1 ? " " + "hour" + " " : " " + "hours" + " ")) +
                                    (minsLeft > 0 ? minsLeft + (minsLeft == 1 ? " " + "minute" : " " + "minutes") : "");
                        } else if (diffMinutes > 0) {
                            text = diffMinutes == 1 ? diffMinutes + " " + "minute" : diffMinutes + " " + "minutes";
                        } else {
                            text = "Invalid";
                        }

                    }
                    return text;
                } else {

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return text;
        } else {
            return "";
        }
    }

    public static String getReadableTimeDifference(long difference, boolean showMins) {

        String timeago = "";
        try {
            long diffSeconds = difference / 1000;
            long diffMinutes = diffSeconds / 60;
            long diffHours = diffMinutes / 60;
            long diffDays = difference / (24 * 60 * 60 * 1000);
            long diffMonths = diffDays / 30;
            long diffYears = diffMonths / 12;
            if (diffYears > 0) {
                return diffYears == 1 ? diffYears + " " + "Year ago" : diffYears + " " + "years";
            } else if (diffMonths > 0) {
                return (diffMonths == 1 ? diffMonths + " " + "month" + " " : diffMonths + " " + "months" + " ");
            } else if (diffDays > 0) {
                return (diffDays == 1 ? diffDays + " " + "day" + " " : diffDays + " " + "days" + " ");
            } else if (diffHours > 0) {
                long minsLeft = 0;
                if (showMins) {
                    minsLeft = diffMinutes - (diffHours * 60);
                }
                return (diffHours + (diffHours == 1 ? " " + "hour" + " " : " " + "hours" + " ")) +
                        (minsLeft > 0 ? minsLeft + (minsLeft == 1 ? " " + "minute" : " " + "minutes") : "");
            } else if (diffMinutes > 0) {
                return diffMinutes == 1 ? diffMinutes + " " + "minute" : diffMinutes + " " + "minutes";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeago;
    }

    public static boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,7})$";

        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }

	public static void showSnackbar(final String string, final Context con, final View view) {
        ((Activity) con).runOnUiThread(new Runnable() {
			public void run() {

				Snackbar.make(view, string, Snackbar.LENGTH_LONG).show();

			}
		});
	}

    public static void showSnackbarWithoutView(final String string, final Context con) {
        ((Activity) con).runOnUiThread(new Runnable() {
            public void run() {

                Snackbar.make(((Activity) con).findViewById(android.R.id.content), string, Snackbar.LENGTH_LONG).show();

            }
        });
    }

	public static void showToast(final String string, final Context con) {
        ((Activity) con).runOnUiThread(new Runnable() {
			public void run() {
				View v = ((Activity) con).findViewById(android.R.id.content);
				if (v == null) {
					Toast.makeText(con, string, Toast.LENGTH_LONG).show();
				} else {
					showSnackbar(string, con, v);
				}
			}
		});
	}

    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int emptyEditTextError(EditText[] edtTexts, String[] errorMsg) {
        int count = 0;
        for (int i = 0; i < edtTexts.length; i++) {
            edtTexts[i].setError(null);
            if (edtTexts[i].getText().toString().trim().length() == 0) {
                edtTexts[i].setError(errorMsg[i]);
                count++;
            }
        }
        return count;
    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public static String strCurrentCountryCode(Context context) {
        String strCountryCode = "";

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();
        //Toast.makeText(context, "NN"+countryCodeValue, Toast.LENGTH_SHORT).show();
        //strCountryCode = Locale.getDefault().getCountry() ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            strCountryCode = context.getResources().getConfiguration().getLocales().get(0).getCountry();
            Log.i("GetLocale", "=" + strCountryCode);
        } else {
            strCountryCode = Resources.getSystem().getConfiguration().locale.getCountry();
            //strCountryCode = context.getResources().getConfiguration().locale.getCountry();
            Log.i("GetLocale2", "=" + strCountryCode);

        }
        Log.i("GetCountryCodeNew", "=" + strCountryCode);
        if (strCountryCode.equals("")) {
            strCountryCode = "US";
        }

        return strCountryCode;
    }

    public static String getDeviceId(Context context) {

        TelephonyManager telephonyManager = (TelephonyManager) context.
                getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();

        if (deviceId == null || deviceId.isEmpty()) {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        return deviceId;
    }


    public static int getHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        return height;
    }

    public static int getWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }

    public static int getTextSize(Activity activity) {
        int screenWidth = getWidth(activity);
        int screenHeight = getHeight(activity);
        int density = activity.getResources().getDisplayMetrics().densityDpi;
        if (screenWidth <= 240) {
            return screenWidth / density * 6;
        } else {
            return screenHeight / density * 35 / 10;
        }
    }

    public static void hideKeyboard(Context context) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // check if no view has focus:
        View view = ((Activity) context).getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    public static String timeElapsed(String prettyTimeStr) {
        String timeElapsed = prettyTimeStr;
        if (timeElapsed.contains(" year ago")) {
            timeElapsed = timeElapsed.replace(" year ago", "Y");
        } else if (timeElapsed.contains(" years ago")) {
            timeElapsed = timeElapsed.replace(" years ago", "Y");
        } else if (timeElapsed.contains(" months ago")) {
            timeElapsed = timeElapsed.replace(" months ago", "M");
        } else if (timeElapsed.contains(" month ago")) {
            timeElapsed = timeElapsed.replace(" month ago", "M");
        } else if (timeElapsed.contains(" weeks ago")) {
            timeElapsed = timeElapsed.replace(" weeks ago", "W");
        } else if (timeElapsed.contains(" week ago")) {
            timeElapsed = timeElapsed.replace(" week ago", "W");
        } else if (timeElapsed.contains(" days ago")) {
            timeElapsed = timeElapsed.replace(" days ago", "d");
        } else if (timeElapsed.contains(" day ago")) {
            timeElapsed = timeElapsed.replace(" day ago", "d");
        } else if (timeElapsed.contains(" hours ago")) {
            timeElapsed = timeElapsed.replace(" hours ago", "h");
        } else if (timeElapsed.contains(" hour ago")) {
            timeElapsed = timeElapsed.replace(" hour ago", "h");
        } else if (timeElapsed.contains(" hours from now")) {
            timeElapsed = timeElapsed.replace(" hours from now", "h");
        } else if (timeElapsed.contains(" hour from now")) {
            timeElapsed = timeElapsed.replace(" hour from now", "h");
        } else if (timeElapsed.contains(" minutes from now")) {
            timeElapsed = timeElapsed.replace(" minutes from now", "m");
        } else if (timeElapsed.contains(" minute from now")) {
            timeElapsed = timeElapsed.replace(" minute from now", "m");
        } else if (timeElapsed.contains(" minutes ago")) {
            timeElapsed = timeElapsed.replace(" minutes ago", "m");
        }
        return timeElapsed;

    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public static Drawable getDrawable(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getDrawable(context, id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static int getDrawableImageByName(String name, Context mContext) {
        Resources resources = mContext.getResources();
        final int resourceId = resources.getIdentifier("com.pbtpleads/drawable:" + name, null, null);
        return resourceId;
    }

    public static void print(String string) {
        System.out.println(string);
    }


    /**
     * Returns network availability status.
     *
     * @param context - Application context.
     * @return - Network availability status.
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        return info.isConnected();
    }


    public static Typeface setFont(Context context) {
        String fontPath = "fonts/HARLOWSI.ttf";
        Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPath);
        return tf;
    }

    public static void deleteDirectoryTree(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {

                if (child.getName().equals("picasso-cache")) {
                    deleteDirectoryTree(child);
                }
            }
        }
        fileOrDirectory.delete();
    }

    public static void showDismiss(final Context context, CustomeProgressDialog customeProgressDialog) {
        if (customeProgressDialog != null && customeProgressDialog.isShowing()) {
            customeProgressDialog.dismiss();
        }
    }

    public static Bitmap decodeFile(File f){

        Bitmap b = null;

        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();

            int scale = 1;
            if (o.outHeight > 800 || o.outWidth >800) {
                scale = (int)Math.pow(2, (int) Math.ceil(Math.log(800 /
                        (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return b;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getPath(Uri uri,Context context) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static String getMonthNamefromDate(String strDate) {
        String inputPattern = "dd-MM-yyyy HH:mm:ss";

        String outputPattern = "MMMM";

        //TimeZone obj = TimeZone.getTimeZone("CET");

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.ENGLISH);
        //outputFormat.setTimeZone(obj);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(strDate);
            str = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getDayFromDate(String strDate) {
        String inputPattern = "dd-MM-yyyy HH:mm:ss";

        String outputPattern = "dd";

        //TimeZone obj = TimeZone.getTimeZone("CET");

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.ENGLISH);
        //outputFormat.setTimeZone(obj);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(strDate);
            str = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getFormattedDate(String strPicksDate,String inputPattern, String outputPattern){
        String strDateTime = "";
        Calendar mCalendar = Calendar.getInstance();
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.ENGLISH);
        try {
            Date date = inputFormat.parse(strPicksDate);
            mCalendar.setTime(date);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern,Locale.ENGLISH);
            strDateTime = outputFormat.format(mCalendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return strDateTime;
    }

    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public static void loadImageWithGlide(final ImageView imageView, String url, Context context) {
        Glide.with(context)
                .load(url)
                .fitCenter()
                .into(imageView);
    }

    public static void loadImageUsingUriWithGlide(final ImageView imageView, Uri pictureUri, Context context) {
        Glide.with(context)
                .load(new File(CommonUtils.getPath(pictureUri,context)))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(imageView);
    }

    public static void loadCircularImageWithGlide(final ImageView imageView, String url, final Context context){
        Glide.with(context).load(url).asBitmap().placeholder(R.drawable.img_user_default).centerCrop().into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    public static void loadCircularImageWithGlideFitCenter(final ImageView imageView, String url, final Context context){
        Glide.with(context).load(url).asBitmap().placeholder(R.drawable.img_user_default).fitCenter().into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    public static void loadCircularImageUriWithGlide(final ImageView imageView, Uri picUri, final Context context){
        Glide.with(context).load(new File(CommonUtils.getPath(picUri,context))).asBitmap().placeholder(R.drawable.img_user_default).centerCrop().into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    public static boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    public static String getJsonStringMemeber(JsonObject jsonObject, String member){
        return  jsonObject.get(member).getAsString();
    }

}
