package com.picksplug.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.picksplug.R;
import com.picksplug.helpers.CommonUtils;
import com.picksplug.helpers.PickPlugApp;
import com.picksplug.model.AllSportsModel;

import java.util.ArrayList;

/**
 * Created by archive_infotech on 27/6/18.
 */

public class SportsListAdapter extends RecyclerView.Adapter<SportsListAdapter.ViewHolderSports>{
    private Context                                 mContext;
    private ArrayList<AllSportsModel>               allSportsModelArrayList;
    private static OnRecyclerViewItemClickListener  mListener;

    public SportsListAdapter(Context mContext, ArrayList<AllSportsModel> allSportsModelArrayList) {
        this.mContext = mContext;
        this.allSportsModelArrayList = allSportsModelArrayList;
    }

    @Override
    public ViewHolderSports onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.list_sports_row,parent,false);
        return new ViewHolderSports(convertView);
    }

    @Override
    public void onBindViewHolder(final ViewHolderSports holder, final int position) {
        holder.txtSportName.setText(allSportsModelArrayList.get(position).getSportName() + " " + mContext.getResources().getString(R.string.bottom_nav_picks));
        CommonUtils.loadImageWithGlide(holder.imgSport, allSportsModelArrayList.get(position).getSportIcon(),mContext);

        holder.txtSportName.setTypeface(PickPlugApp.getInstance().getRegularFont());
        holder.txtBadge.setTypeface(PickPlugApp.getInstance().getBoldFont());

        if (allSportsModelArrayList.get(position).getCount().equalsIgnoreCase("0")){
            holder.txtBadge.setVisibility(View.GONE);
        } else {
            holder.txtBadge.setVisibility(View.VISIBLE);
            holder.txtBadge.setText(allSportsModelArrayList.get(position).getCount());
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClicked(position,allSportsModelArrayList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return allSportsModelArrayList.size();
    }

    public class ViewHolderSports extends RecyclerView.ViewHolder {
        private TextView        txtSportName,txtBadge;
        private ImageView       imgSport;
        private LinearLayout    root;

        public ViewHolderSports(View itemView) {
            super(itemView);

            txtSportName    =   (TextView)itemView.findViewById(R.id.list_txt_sport_name);
            txtBadge        =   (TextView)itemView.findViewById(R.id.list_txt_badge);
            imgSport        =   (ImageView)itemView.findViewById(R.id.list_img_sport);
            root            =   (LinearLayout)itemView.findViewById(R.id.sports_list_root);
        }
    }

    // Define the method that allows the parent activity or fragment to define the listener.
    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mListener = listener;
    }

    // Define the listener interface
    public interface OnRecyclerViewItemClickListener {
        void onItemClicked(int position,AllSportsModel allSportsModel);
    }

}
