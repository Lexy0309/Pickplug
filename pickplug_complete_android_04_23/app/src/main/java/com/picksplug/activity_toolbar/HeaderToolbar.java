package com.picksplug.activity_toolbar;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.picksplug.R;
import com.picksplug.helpers.PickPlugApp;


/**
 * Created by abhinav on 06/12/17.
 */

public class HeaderToolbar extends RelativeLayout implements View.OnClickListener{


    private Context                     mContext;
    private TextView                    lblHeaderTitle;
    private LinearLayout                layoutBack,layoutNotification;
    private HeaderToolbarListener       headerToolbarListener;


    public void setUpToolbar(Context context,HeaderToolbarListener listener){
        mContext                =   context;
        headerToolbarListener   =   listener;
        lblHeaderTitle          =   (TextView) ((Activity)mContext).findViewById(R.id.lblHeaderTitle);
        layoutBack              =   (LinearLayout)((Activity)mContext).findViewById(R.id.layoutBack);
        layoutNotification      =   (LinearLayout)((Activity)mContext).findViewById(R.id.layoutNotification);

        layoutBack.setOnClickListener(this);
        layoutNotification.setOnClickListener(this);

        setFont();
    }

    public void setFont(){
        lblHeaderTitle.setTypeface(PickPlugApp.getInstance().getMediumFont());
    }

    public void setHeaderTitle(String title){
        lblHeaderTitle.setText(title);
    }

    public void hideNotificationIcon(){
        layoutNotification.setVisibility(GONE);
    }

    public HeaderToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        System.out.println("Excute Constructor=====");

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.layoutBack){
            headerToolbarListener.onClickHeaderToolbarBack();
        } else if (view.getId() == R.id.layoutNotification){
            headerToolbarListener.onClickNotification();
        }
    }
}




