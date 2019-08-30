package com.picksplug.helpers;


import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

import com.picksplug.R;


public class CustomeProgressDialog extends Dialog {
    public CustomeProgressDialog(Context context, int layoutResID) {
        super(context, R.style.TransparentProgressDialog);
        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        setContentView(layoutResID);

    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
