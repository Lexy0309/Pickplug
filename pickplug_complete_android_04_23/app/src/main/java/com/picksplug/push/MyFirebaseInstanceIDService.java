package com.picksplug.push;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.picksplug.helpers.PreferenceConnector;

/**
 * Created by archive_infotech on 18/11/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("token:", refreshedToken);

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);
    }

    private void storeRegIdInPref(String token) {
        PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector.KEY_PUSH_TOKEN, token);
    }
}
