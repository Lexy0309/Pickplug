package com.picksplug.drawer_click;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.picksplug.R;
import com.picksplug.helpers.CommonUtils;
import com.picksplug.helpers.PickPlugApp;

import java.util.Collections;
import java.util.List;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    private static final int TYPE_ITEM_ROW = 0; // item row here
    private static final int TYPE_SEPARATOR = 1;  // header here

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void refreshList(List<NavDrawerItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType == TYPE_ITEM_ROW) {
            View view = inflater.inflate(R.layout.nav_drawer_row_item, parent, false);
            return new RowViewHolder(view);

        } else {
            View view = inflater.inflate(R.layout.header, parent, false);

            return new SectionViewHolder(view);
        }
    }


    @Override
    public int getItemViewType(int position) {

        NavDrawerItem item = data.get(position);
        if(!item.getIsHeader()) {
            return TYPE_ITEM_ROW;
        } else {
            return TYPE_SEPARATOR;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int rowType = getItemViewType(position);
        switch (rowType) {
            case TYPE_ITEM_ROW:
                NavDrawerItem current = data.get(position);
                RowViewHolder holderRow = (RowViewHolder) holder;
                holderRow.title.setText(current.getTitle());

                holderRow.title.setTypeface(PickPlugApp.getInstance().getMediumFont());

                holderRow.sliderIcon.setImageResource(current.getImageId());
                holderRow.title.setTextColor(CommonUtils.getColor(this.context, R.color.colorWhite));
                if (current.isSelected()) {
                    holderRow.sliderIcon.setImageResource(current.getSelImageId());
                    holderRow.title.setTextColor(CommonUtils.getColor(this.context, R.color.colorWhite));
                }

                break;

            case TYPE_SEPARATOR:
                NavDrawerItem current1 = data.get(position);
                SectionViewHolder holderSection = (SectionViewHolder) holder;
                holderSection.textView.setText(current1.getTitle());
                break;
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class RowViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView sliderIcon;
        RelativeLayout RelativeRow ;

        public RowViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            sliderIcon = (ImageView) itemView.findViewById(R.id.Slider_icon);
            RelativeRow = (RelativeLayout) itemView.findViewById(R.id.RelativeRow);

        }
    }


    public class SectionViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public SectionViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.headerTxt);

        }

    }
}