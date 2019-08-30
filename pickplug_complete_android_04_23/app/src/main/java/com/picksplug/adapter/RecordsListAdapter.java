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
import com.picksplug.model.HomeTeamDetailsModel;
import com.picksplug.model.PickDetailModel;
import com.picksplug.model.VisitingTeamDetailsModel;

import java.util.ArrayList;

/**
 * Created by archive_infotech on 26/6/18.
 */

public class RecordsListAdapter extends RecyclerView.Adapter<RecordsListAdapter.ViewHolderRecords>{
    private Context                                     mContext;
    private ArrayList<PickDetailModel>                  pickDetailModelArrayList;
    private static OnRecyclerViewItemClickListener      mListener;

    public RecordsListAdapter(Context mContext, ArrayList<PickDetailModel> picksModelArrayList) {
        this.mContext = mContext;
        this.pickDetailModelArrayList = picksModelArrayList;
    }

    @Override
    public ViewHolderRecords onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.list_records_row,parent,false);
        return new ViewHolderRecords(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolderRecords holder, final int position) {
        HomeTeamDetailsModel        homeTeamDetailsModel        =   pickDetailModelArrayList.get(position).getHomeTeamDetails();
        VisitingTeamDetailsModel    visitingTeamDetailsModel    =   pickDetailModelArrayList.get(position).getVisitingTeamDetails();

        holder.txtDate.setText(CommonUtils.getDayFromDate(pickDetailModelArrayList.get(position).getPickDate()));
        holder.txtMonth.setText(CommonUtils.getMonthNamefromDate(pickDetailModelArrayList.get(position).getPickDate()));
        holder.txtTeamA.setText(visitingTeamDetailsModel.getTeamName());
        holder.txtTeamB.setText(homeTeamDetailsModel.getTeamName());

        holder.txtDate.setTypeface(PickPlugApp.getInstance().getBoldFont());
        holder.txtMonth.setTypeface(PickPlugApp.getInstance().getLightFont());
        holder.txtTeamA.setTypeface(PickPlugApp.getInstance().getMediumFont());
        holder.txtTeamB.setTypeface(PickPlugApp.getInstance().getMediumFont());
        holder.txtWin.setTypeface(PickPlugApp.getInstance().getBoldFont());
        holder.txtLost.setTypeface(PickPlugApp.getInstance().getBoldFont());

        CommonUtils.loadImageWithGlide(holder.imgTeamA,visitingTeamDetailsModel.getTeamIcon(),mContext);
        CommonUtils.loadImageWithGlide(holder.imgTeamB,homeTeamDetailsModel.getTeamIcon(),mContext);

        if (pickDetailModelArrayList.get(position).getPickStatus().equalsIgnoreCase("Lose")){
            holder.linearLost.setVisibility(View.VISIBLE);
            holder.linearWin.setVisibility(View.GONE);
        } else {
            holder.linearLost.setVisibility(View.GONE);
            holder.linearWin.setVisibility(View.VISIBLE);
        }

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

    public class ViewHolderRecords extends RecyclerView.ViewHolder {
        TextView        txtDate,txtMonth,txtTeamA,txtTeamB,txtWin,txtLost;
        ImageView       imgTeamA,imgTeamB;
        LinearLayout    linearWin,linearLost,root;

        public ViewHolderRecords(View itemView) {
            super(itemView);

            txtDate     =   (TextView)itemView.findViewById(R.id.list_txt_date);
            txtMonth    =   (TextView)itemView.findViewById(R.id.list_txt_month);
            txtTeamA    =   (TextView)itemView.findViewById(R.id.list_txt_team_a);
            txtTeamB    =   (TextView)itemView.findViewById(R.id.list_txt_team_b);
            txtWin      =   (TextView)itemView.findViewById(R.id.list_txt_won);
            txtLost     =   (TextView)itemView.findViewById(R.id.list_txt_lost);
            imgTeamA    =   (ImageView)itemView.findViewById(R.id.list_img_team_a);
            imgTeamB    =   (ImageView)itemView.findViewById(R.id.list_img_team_b);
            linearWin   =   (LinearLayout)itemView.findViewById(R.id.list_linear_win);
            linearLost  =   (LinearLayout)itemView.findViewById(R.id.list_linear_lost);
            root        =   (LinearLayout)itemView.findViewById(R.id.root_records_list);
        }
    }

    // Define the method that allows the parent activity or fragment to define the listener.
    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mListener = listener;
    }

    // Define the listener interface
    public interface OnRecyclerViewItemClickListener {
        void onItemClicked(int position,PickDetailModel detailModel);
    }
}
