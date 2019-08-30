package com.picksplug.push;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.picksplug.activity.SplashActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    public static final String PUSH_NOTIFICATION = "pushNotification";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.e(TAG, "remoteMessage :" + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            handleDataMessage(remoteMessage);
        }
    }

    private void handleNotification(String message) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mSimpleDateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String timestamp = mSimpleDateFormater.format(calendar.getTime());

        if (!NotificationUtils.isAppInBackgroundRunning(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            //intent.putExtra("url", "https://www.google.co.in");
            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.showNotificationMessage("PickPlug", message, timestamp, intent);
            //notificationUtils.playNotificationSound();
        } else {
            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            //intent.putExtra("url", "https://www.google.co.in");
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.showNotificationMessage("PickPlug", message, timestamp, intent);
            //notificationUtils.playNotificationSound();
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(RemoteMessage json) {

        //JSONObject json = jParser.getJSONFromUrlByGet(url);
        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mSimpleDateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String timestamp = mSimpleDateFormater.format(calendar.getTime());

            String title = json.getData().get("title");
            String message = json.getData().get("message");
            //boolean isBackground = json.getData().get("is_background");
            String imageUrl = json.getData().get("image");
            //String timestamp = json.getData().get("timestamp");
            //JSONObject payload = data.getJSONObject("payload");
            String str_url = json.getData().get("url");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            //Log.e(TAG, "isBackground: " + isBackground);
            //Log.e(TAG, "payload: " + payload.toString());

            Log.e(TAG, "timestamp: " + timestamp);
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "str_url: " + str_url);

            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
            resultIntent.putExtra("url", str_url);

            if (title == null){
                title = "PickPlug";
                Log.e(TAG, "title: " + title);
            }

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
            }

        } catch (Exception e) {
            Log.d(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
        Log.e("timeStamp:", timeStamp);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
        Log.e("timeStamp:", timeStamp);
    }
}
