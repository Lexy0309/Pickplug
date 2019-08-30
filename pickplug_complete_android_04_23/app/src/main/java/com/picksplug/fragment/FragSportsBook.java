package com.picksplug.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.picksplug.R;
import com.picksplug.adapter.SportsBookListAdapter;
import com.picksplug.helpers.SimpleDividerItemDecoration;
import com.picksplug.model.SportsBookModel;

import java.util.ArrayList;

public class FragSportsBook extends Fragment {
    private Context mContext;
    private View rootView;
    private boolean mAlreadyLoaded;
    private RecyclerView recyclerViewSportsBook;
    private SportsBookListAdapter sportsBookListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_sports_book, container, false);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null && !mAlreadyLoaded) {
            mContext = getActivity();
            mAlreadyLoaded = true;
            initViews();

            ArrayList<SportsBookModel> sportsBookModelArrayList = new ArrayList<>();
            sportsBookModelArrayList.add(new SportsBookModel("1", "https://www.sportsbookpromocodes.com/wp-content/uploads/2017/01/sportsbookpromocodes-betonline-logo-1.png", "https://record.commission.bz/_3E2mxfib6zVLcRLGwHoTKWNd7ZgqdRLk/1/", "BetOnline", "BetOnline ownership has invested in experienced wagering and customer service managers who understand that the sportsbook's first priority is service over sales. This attitude is passed along to the staff during training classes that each wagering clerk and service agent is required to pass. BetOnline treats players with respect and aims to gain a player's loyalty by providing VIP level service; including a generous sign-up and reload bonus.", "Operating Since: 2001"));
            sportsBookModelArrayList.add(new SportsBookModel("2", "http://www.pokerakademia.com/uploads/intertops-45347578.png", "http://link.intertops.eu/c/394572", "Intertops", "Intertops is one of the oldest online sportsbooks around today having first opened its doors back in 1998. The sportsbook developed a reputation as being a reliable, old-fashioned betting site that paid on time and operated in an efficient, no frills manner. The sports betting site is typically mentioned at the top of most users must-have sportsbook accounts", "Operating Since: 2002"));
            sportsBookModelArrayList.add(new SportsBookModel("3", "https://388803b8fbeaf8c08fc98fcd-yqjivnwq09blzymkn6q.netdna-ssl.com/wp-content/uploads/2018/07/SportsBetting-ag-logo.png", "https://record.commission.bz/_3E2mxfib6zWMT4LEHD22I2Nd7ZgqdRLk/1/", "Sportsbetting.ag", "Sportsbetting has a competitive bonus program, a leading mobile betting platform, and typically scores highly in the customer service and speed of payout categories. User feedback has been consistently positive. Sportsbetting.ag offers a live betting platform.", "Operating Since: 2002"));

            setUpRecycleView(sportsBookModelArrayList);
        }
    }

    private void initViews() {
        recyclerViewSportsBook = (RecyclerView) rootView.findViewById(R.id.recycle_view_sportsbook);
    }

    private void setUpRecycleView(ArrayList<SportsBookModel> sportsBookModelArrayList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        sportsBookListAdapter = new SportsBookListAdapter(mContext, sportsBookModelArrayList);
        recyclerViewSportsBook.setAdapter(sportsBookListAdapter);
        recyclerViewSportsBook.setLayoutManager(layoutManager);

        recyclerViewSportsBook.addItemDecoration(new SimpleDividerItemDecoration(mContext));
    }
}
