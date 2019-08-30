package com.picksplug.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.picksplug.R;
import com.picksplug.helpers.CommonUtils;
import com.picksplug.helpers.PickPlugApp;
import com.picksplug.model.UserSubscriptionModel;

import java.util.ArrayList;

/**
 * Created by archive_infotech on 7/12/18.
 */

public class UserSubscriptionListAdapter extends RecyclerView.Adapter<UserSubscriptionListAdapter.ViewHolderUserSubscription>{
    private Context                             mContext;
    private ArrayList<UserSubscriptionModel>    userSubscriptionModelArrayList;
    private String                              sportIconImgPrefix;

    public UserSubscriptionListAdapter(Context mContext, ArrayList<UserSubscriptionModel> userSubscriptionModelArrayList, String sportIconImgPrefix) {
        this.mContext = mContext;
        this.userSubscriptionModelArrayList = userSubscriptionModelArrayList;
        this.sportIconImgPrefix = sportIconImgPrefix;
    }

    @Override
    public ViewHolderUserSubscription onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.list_premium_picks_row,parent,false);
        return new ViewHolderUserSubscription(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolderUserSubscription holder, int position) {

        if (userSubscriptionModelArrayList.get(position).getSportDetail() != null){
            holder.txtSportName.setText(String.format("%s Picks", userSubscriptionModelArrayList.get(position).getSportDetail().get(0).getSportName()));
            CommonUtils.loadImageWithGlide(holder.imgSport,sportIconImgPrefix+userSubscriptionModelArrayList.get(position).getSportDetail().get(0).getSportIcon(),mContext);
            holder.imgSport.setVisibility(View.VISIBLE);
        } else {
            holder.txtSportName.setText(userSubscriptionModelArrayList.get(position).getName());
            holder.imgSport.setVisibility(View.GONE);
        }

        holder.txtExpiryDate.setText(CommonUtils.getFormattedDate(userSubscriptionModelArrayList.get(position).getExpiryDate(),"yyyy-mm-dd","dd/mm/yyyy"));

        holder.txtSportName.setTypeface(PickPlugApp.getInstance().getMediumFont());
        holder.txtExpiryDate.setTypeface(PickPlugApp.getInstance().getMediumFont());
        holder.lblExpiryDate.setTypeface(PickPlugApp.getInstance().getRegularFont());


    }

    @Override
    public int getItemCount() {
        return userSubscriptionModelArrayList.size();
    }

    public class ViewHolderUserSubscription extends RecyclerView.ViewHolder{
        private ImageView   imgSport;
        private TextView    txtSportName,lblExpiryDate,txtExpiryDate;

        public ViewHolderUserSubscription(View itemView) {
            super(itemView);

            imgSport        =   (ImageView)itemView.findViewById(R.id.premium_img_sport);
            txtSportName    =   (TextView)itemView.findViewById(R.id.premium_txt_sport_name);
            lblExpiryDate   =   (TextView)itemView.findViewById(R.id.premium_lbl_expiry_date);
            txtExpiryDate   =   (TextView)itemView.findViewById(R.id.premium_txt_expiry_date);
        }
    }
}
