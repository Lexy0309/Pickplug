package com.picksplug.helpers;

import android.app.Application;
import android.graphics.Typeface;

/**
 * Created by archive_infotech on 8/6/18.
 */

public class PickPlugApp extends Application {
    public static PickPlugApp   singleton;
    private Typeface            lightFont;
    private Typeface            regularFont;
    private Typeface            mediumFont;
    private Typeface            boldFont;

    public static synchronized PickPlugApp getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    public Typeface getLightFont() {
        if(lightFont == null) {
            lightFont = Typeface.createFromAsset(getAssets(),"fonts/Lato-Light.ttf");
        }
        return this.lightFont;
    }

    public Typeface getRegularFont() {
        if(regularFont == null) {
            regularFont = Typeface.createFromAsset(getAssets(),"fonts/Lato-Regular.ttf");
        }
        return this.regularFont;
    }

    public Typeface getMediumFont() {
        if(mediumFont == null) {
            mediumFont = Typeface.createFromAsset(getAssets(),"fonts/Lato-Medium.ttf");
        }
        return this.mediumFont;
    }

    public Typeface getBoldFont() {
        if(boldFont == null) {
            boldFont = Typeface.createFromAsset(getAssets(),"fonts/Lato-Bold.ttf");
        }
        return this.boldFont;
    }

}
