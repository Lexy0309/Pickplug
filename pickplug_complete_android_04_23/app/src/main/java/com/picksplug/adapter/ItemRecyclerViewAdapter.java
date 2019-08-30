package com.picksplug.adapter;

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
import com.picksplug.model.PicksDetailModel;

import java.util.ArrayList;

/**
 * Created by archive_infotech on 7/14/18.
 */

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemViewHolder>{
    private Context                             mContext;
    private ArrayList<PicksDetailModel>          pickDetailModelArrayList;
    private static ItemRecyclerViewAdapter.OnRecyclerViewItemClickListener mListener;

    public ItemRecyclerViewAdapter(Context mContext, ArrayList<PicksDetailModel> pickDetailModelArrayList) {
        this.mContext = mContext;
        this.pickDetailModelArrayList = pickDetailModelArrayList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.list_picks_row,parent,false);
        return new ItemViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        holder.txtHomeTeam.setText(pickDetailModelArrayList.get(position).getHomeTeamDetails().getTeamName());
        holder.txtVisitingTeam.setText(pickDetailModelArrayList.get(position).getVisitingTeamDetails().getTeamName());

        CommonUtils.loadImageWithGlide(holder.imgHomeTeam,pickDetailModelArrayList.get(position).getHomeTeamDetails().getTeamIcon(),mContext);
        CommonUtils.loadImageWithGlide(holder.imgVisitingTeam,pickDetailModelArrayList.get(position).getVisitingTeamDetails().getTeamIcon(),mContext);
        CommonUtils.loadImageWithGlide(holder.imgSport,pickDetailModelArrayList.get(position).getSportImage(),mContext);

        holder.txtHomeTeam.setTypeface(PickPlugApp.getInstance().getMediumFont());
        holder.txtVisitingTeam.setTypeface(PickPlugApp.getInstance().getMediumFont());

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClicked(position,pickDetailModelArrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return pickDetailModelArrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView txtHomeTeam,txtVisitingTeam;
        private ImageView imgHomeTeam,imgVisitingTeam,imgSport;
        private RelativeLayout root;

        public ItemViewHolder(View itemView) {
            super(itemView);

            txtHomeTeam         =   (TextView)itemView.findViewById(R.id.list_picks_txt_home_team);
            txtVisitingTeam     =   (TextView)itemView.findViewById(R.id.list_picks_txt_visiting_team);
            imgHomeTeam         =   (ImageView) itemView.findViewById(R.id.list_picks_img_home_team);
            imgVisitingTeam     =   (ImageView) itemView.findViewById(R.id.list_picks_img_visiting_team);
            imgSport            =   (ImageView) itemView.findViewById(R.id.list_picks_img_sport);
            root                =   (RelativeLayout) itemView.findViewById(R.id.list_picks_root);
        }
    }

    // Define the method that allows the parent activity or fragment to define the listener.
    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mListener = listener;
    }

    // Define the listener interface
    public interface OnRecyclerViewItemClickListener {
        void onItemClicked(int position, PicksDetailModel pickDetailModel);
    }
}
