package com.picksplug.helpers;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessOnScrollListener extends RecyclerView.OnScrollListener {



        public static String TAG = EndlessOnScrollListener.class.getSimpleName();

        // use your LayoutManager instead
        private LinearLayoutManager llm;

        public EndlessOnScrollListener(LinearLayoutManager linearLayoutManager) {
            this.llm = linearLayoutManager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (!recyclerView.canScrollVertically(1)) {
                onScrolledToEnd();
            }
        }

        public abstract void onScrolledToEnd();


}
