package com.picksplug.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.picksplug.R;
import com.picksplug.activity.ActDashboard;
import com.picksplug.helpers.PickPlugApp;

public class FragNoPicks extends Fragment {
    private Context                             mContext;
    private View                                rootView;
    private boolean                             mAlreadyLoaded;
    private TextView                            txtDailyUpdated,txtCheckLater;
    private String                              strTitle = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null){
            rootView = inflater.inflate(R.layout.frag_no_picks, container, false);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null && !mAlreadyLoaded){
            mContext                =   getActivity();
            mAlreadyLoaded          =   true;

            initViews();

            if (getArguments() != null){
                strTitle        = getArguments().getString("title");
                singleTextView(mContext,txtDailyUpdated,strTitle+" are updated "+"\n"+"daily at"," 2pm EST");
            }
        }

        if (mContext != null){
            ((ActDashboard)mContext).setToolbarTitle(strTitle);
            ((ActDashboard)mContext).hideHamburgerIcon();
            ((ActDashboard)mContext).hideAppLogo();
        }
    }

    private void initViews(){
        txtDailyUpdated     =   (TextView)rootView.findViewById(R.id.txt_daily_updated);
        txtCheckLater       =   (TextView)rootView.findViewById(R.id.txt_check_later);
        setFonts();
    }

    private void setFonts(){
        txtDailyUpdated.setTypeface(PickPlugApp.getInstance().getMediumFont());
        txtCheckLater.setTypeface(PickPlugApp.getInstance().getMediumFont());
    }

    private static void singleTextView(final Context context, TextView textView, final String title, String time) {

        SpannableStringBuilder spanText = new SpannableStringBuilder();
        spanText.append(title);
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
            }

            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(ContextCompat.getColor(context,R.color.colorDarkGrey));    // you can use custom color
                textPaint.setUnderlineText(false);    // this remove the underline
            }
        }, spanText.length() - title.length(), spanText.length(), 0);

        spanText.append(time);
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

            }

            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(ContextCompat.getColor(context,R.color.colorNoPicksText));    // you can use custom color
                textPaint.setUnderlineText(false);    // this remove the underline
            }
        },spanText.length() - time.length(), spanText.length(), 0);

        spanText.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),spanText.length() - time.length(), spanText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spanText, TextView.BufferType.SPANNABLE);


    }
}
