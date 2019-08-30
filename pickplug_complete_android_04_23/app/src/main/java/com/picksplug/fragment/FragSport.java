package com.picksplug.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.picksplug.R;
import com.picksplug.activity.ActDashboard;
import com.picksplug.helpers.PickPlugApp;
import com.picksplug.helpers.PreferenceConnector;
import com.picksplug.model.PicksDetailModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragSport extends Fragment  {
    private Context                                         mContext;
    private View                                            rootView;
    private boolean                                         mAlreadyLoaded;
    private TabLayout                                       tabLayout;
    private ViewPager                                       viewPager;
    private String                                          strTitle = "", strSportId = "", strFrom ="", strDate="",strIsAllow="", strCount="0";
    private HashMap<String,ArrayList<PicksDetailModel>>     premiumPicksHashMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null){
            rootView = inflater.inflate(R.layout.frag_sport, container, false);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null && !mAlreadyLoaded){
            mContext                        =   getActivity();
            mAlreadyLoaded                  =   true;

            Log.e("fcm:","======="+ PreferenceConnector.readString(mContext,PreferenceConnector.KEY_PUSH_TOKEN,""));

            if (getArguments() != null){

                strFrom             =   getArguments().getString("from");
                strTitle            =   getArguments().getString("title");

                System.out.println("from ======= " + strFrom);

                if (strFrom.equals("allSportList")){
                    strCount = getArguments().getString("count");
                }

                premiumPicksHashMap     =   new HashMap<>();
                strSportId              =   getArguments().getString("sportId");
                strDate                 =   getArguments().getString("date");
                strIsAllow              =   getArguments().getString("is_allow");
                premiumPicksHashMap     =   (HashMap<String,ArrayList<PicksDetailModel>>) getArguments().getSerializable("PremiumPicksModelHashMap");
            }

            initViews();
        }

        if (mContext != null){
            ((ActDashboard)mContext).setToolbarTitle(strTitle);
            ((ActDashboard)mContext).hideHamburgerIcon();
            ((ActDashboard)mContext).hideAppLogo();
        }

    }

    private void initViews(){
        tabLayout = (TabLayout) rootView.findViewById(R.id.sliding_tabs);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);

        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.img_picks);
        tabLayout.getTabAt(1).setIcon(R.drawable.img_record);
//        tabLayout.getTabAt(2).setIcon(R.drawable.img_sportbook);

        changeTabsFont();
    }

    private void changeTabsFont() {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(PickPlugApp.getInstance().getMediumFont());
                }
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        if (strCount.equals("0") && strFrom.equals("allSportList")){
            FragNoPicks fragNoPicks = new FragNoPicks();
            Bundle bundle = new Bundle();
            bundle.putString("title",strTitle);
            fragNoPicks.setArguments(bundle);
            adapter.addFragment(fragNoPicks,"Picks");
        } else {
            FragPicks fragPicks = new FragPicks();
            Bundle bundle = new Bundle();
            bundle.putString("from", strFrom);
            bundle.putString("sportId", strSportId);
            bundle.putString("date", strDate);
            bundle.putString("is_allow", strIsAllow);
            bundle.putSerializable("PremiumPicksModelHashMap", premiumPicksHashMap);
            fragPicks.setArguments(bundle);
            adapter.addFragment(fragPicks, "Picks");
        }

        FragRecords fragRecords = new FragRecords();
        Bundle bundleRecords = new Bundle();
        bundleRecords.putString("sportId",strSportId);
        bundleRecords.putString("from",strFrom);
        fragRecords.setArguments(bundleRecords);

        adapter.addFragment(fragRecords, "Records");
//        adapter.addFragment(new FragSportsBook(), "Sportsbooks");
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
