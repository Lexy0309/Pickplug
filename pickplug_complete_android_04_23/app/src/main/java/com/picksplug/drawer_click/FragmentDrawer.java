package com.picksplug.drawer_click;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.picksplug.R;
import com.picksplug.activity.LoginActivity;
import com.picksplug.helpers.CommonUtils;
import com.picksplug.helpers.PickPlugApp;
import com.picksplug.helpers.PreferenceConnector;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FragmentDrawer extends Fragment {

    private static String TAG = FragmentDrawer.class.getSimpleName();

    private static RecyclerView recyclerView;
    public ActionBarDrawerToggle mDrawerToggle;
    public DrawerLayout mDrawerLayout;
    private static NavigationDrawerAdapter adapter;
    private View containerView;
    private Button btnLogOut;
    private ImageView imgProfile;


    private static int[] titles_new = {R.string.nav_home, R.string.nav_my_profile,R.string.nav_about ,R.string.nav_rate_n_review, R.string.nav_legal, R.string.nav_contact_us};
    private static int[] titles_new_LOGIN = {R.string.nav_home, R.string.nav_my_profile,R.string.nav_about , R.string.nav_rate_n_review, R.string.nav_legal,R.string.nav_contact_us};
    private static int[] arrSliderImgs = {R.drawable.menu_tick, R.drawable.menu_profile,R.drawable.menu_about,R.drawable.menu_rate,R.drawable.menu_legal,R.drawable.img_contact_us};
    private static int[] arrSliderSelImgs = {R.drawable.menu_tick,R.drawable.menu_profile,R.drawable.menu_about,R.drawable.menu_rate,R.drawable.menu_legal,R.drawable.img_contact_us};

    private FragmentDrawerListener drawerListener;
    private Toolbar toolbar;
    private static Context context;
    private static String StrTitlePreference;
    private ImageView ImgReedemDrawer;
    private TextView textViewProfrofileName, txtRewardsPointValue;

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();
        for (int i = 0; i < titles_new.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(context.getResources().getString(titles_new[i]));
            navItem.setImageId(arrSliderImgs[i]);
            navItem.setSelImageId(arrSliderSelImgs[i]);
            navItem.setIsHeader(false);
               if (i == 0) {
                navItem.setIsHeader(false);
                navItem.setSelected(true);
            }

            data.add(navItem);
        }
        return data;
    }



    public static List<NavDrawerItem> getDataOnLogin() {
        List<NavDrawerItem> data = new ArrayList<>();
        for (int i = 0; i < titles_new_LOGIN.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(context.getResources().getString(titles_new_LOGIN[i]));
            navItem.setImageId(arrSliderImgs[i]);
            navItem.setSelImageId(arrSliderSelImgs[i]);
            navItem.setIsHeader(false);
            if (i == 0) {
                navItem.setIsHeader(false);
                navItem.setSelected(true);
            }

            data.add(navItem);
        }
        return data;
    }


    public static double round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // drawer labels
        //titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        context         =   getActivity();
        View layout     =   inflater.inflate(R.layout.frag_profile_drawer, container, false);
        recyclerView    =   (RecyclerView) layout.findViewById(R.id.drawerList);
        btnLogOut       =   (Button) layout.findViewById(R.id.btn_log_out);
        imgProfile      =   (ImageView) layout.findViewById(R.id.img_profile);

        adapter = new NavigationDrawerAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                    drawerListener.onDrawerItemSelected(view, position);
                for (int i = 0; i < adapter.data.size(); i++) {
                    NavDrawerItem item = adapter.data.get(i);
                    item.setSelected(false);
                }
                NavDrawerItem item = adapter.data.get(position);

                mDrawerLayout.closeDrawer(containerView);
                item.setSelected(true);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        btnLogOut.setTypeface(PickPlugApp.getInstance().getMediumFont());

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(getResources().getString(R.string.str_log_out_alert))
                        .setTitle(getResources().getString(R.string.btn_str_log_out))
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // dialog.cancel();
                                PreferenceConnector.writeBoolean(context,PreferenceConnector.TAG_IS_LOGIN,false);
                                PreferenceConnector.writeString(context,PreferenceConnector.TAG_USER_ID,"");
                                PreferenceConnector.writeString(context,PreferenceConnector.TAG_USER_NAME,"");
                                PreferenceConnector.writeString(context,PreferenceConnector.TAG_USER_EMAIL,"");
                                PreferenceConnector.writeString(context,PreferenceConnector.TAG_USER_PROFILE,"");
                                context.startActivity(new Intent(context, LoginActivity.class));
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ///same page
                            }
                        });;

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        if (!PreferenceConnector.readString(context,PreferenceConnector.TAG_USER_PROFILE,"").trim().isEmpty()){
            CommonUtils.loadCircularImageWithGlide(imgProfile,PreferenceConnector.readString(context,PreferenceConnector.TAG_USER_PROFILE,""),context);
        }

        return layout;
    }

    public void refreshDataOnLogout(Context context){
        Log.i("RefreshDrawerCalled","Checked") ;
        adapter = new NavigationDrawerAdapter(context, getData());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void setUp(final LinearLayout MainBody, int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerToggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.menu_icon, getActivity().getTheme());
        mDrawerToggle.setHomeAsUpIndicator(drawable);
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }


}
