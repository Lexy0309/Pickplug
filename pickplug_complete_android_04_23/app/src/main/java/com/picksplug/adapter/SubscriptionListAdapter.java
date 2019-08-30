package com.picksplug.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.SkuDetails;
//import com.google.gson.JsonElement;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.picksplug.R;
import com.picksplug.activity.ActDashboard;
import com.picksplug.helpers.CommonUtils;
import com.picksplug.helpers.PickPlugApp;
import com.picksplug.model.InappPurchaseModel;
import com.picksplug.model.SubscriptionModel;


import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.picksplug.activity.ActDashboard.billingClient;

/**
 * Created by archive_infotech on 29/6/18.
 */

public class SubscriptionListAdapter extends RecyclerView.Adapter<SubscriptionListAdapter.ViewHolderSubscription>{
    private Context mContext;
    private ArrayList<SubscriptionModel> subscriptionModelArrayList;
    private ArrayList<InappPurchaseModel> inappPurchaseModelArrayList;
    private boolean isBestValuePurchased;

//    public SubscriptionListAdapter(Context mContext, ArrayList<SubscriptionModel> subscriptionModelArrayList) {
//        this.mContext = mContext;
//        this.subscriptionModelArrayList = subscriptionModelArrayList;
//        isBestValuePurchased(subscriptionModelArrayList);
//    }
    public SubscriptionListAdapter(Context mContext, ArrayList<InappPurchaseModel> inappPurchaseModelArrayList) {
        this.mContext = mContext;
        this.inappPurchaseModelArrayList = inappPurchaseModelArrayList;
//        isBestValuePurchased(inappPurchaseModelArrayList);
    }

    @Override
    public ViewHolderSubscription onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.list_subscription_row,parent,false);
        return new ViewHolderSubscription(convertView);
    }

    private Boolean isPurchased(InappPurchaseModel model) {
        for (JsonElement _obj : ActDashboard.jsonArrayUserSub){
            JsonObject obj = _obj.getAsJsonObject();
            String sku        =   CommonUtils.getJsonStringMemeber(obj,"package_name");
            String tt = model.productId;
            String[] tmp = tt.split(Pattern.quote("."));
            String sport = tmp[tmp.length - 1];
            if (sku.contains(sport)){
                return true;
            }

        }

        return false;
    }
    @Override
    public void onBindViewHolder(ViewHolderSubscription holder, final int position) {



        if (inappPurchaseModelArrayList.get(position).isSubscription){
            holder.txtName.setText(inappPurchaseModelArrayList.get(position).name + "- Renew");
        }else{
            holder.txtName.setText(inappPurchaseModelArrayList.get(position).name);
        }


        if (inappPurchaseModelArrayList.get(position).isSubscription){
            holder.txtEndDate.setVisibility(View.GONE);
                holder.txtEndDate.setText(String.format("End Date: %s", inappPurchaseModelArrayList.get(position).endDate));
            }

            if (isPurchased(inappPurchaseModelArrayList.get(position))) {
                holder.linearBuy.setVisibility(View.GONE);
                holder.linearBestBuy.setVisibility(View.GONE);

                holder.linearPurchased.setVisibility(View.VISIBLE);
                holder.txtPurchasedBuyAmount.setText(inappPurchaseModelArrayList.get(position).price);

            } else {
                if (inappPurchaseModelArrayList.get(position).isSubscription){
                    holder.linearBestBuy.setVisibility(View.VISIBLE);
                holder.linearBuy.setVisibility(View.GONE);
                holder.linearPurchased.setVisibility(View.GONE);
                holder.txtBestBuyAmount.setText(inappPurchaseModelArrayList.get(position).price);
            } else {
                holder.linearBestBuy.setVisibility(View.GONE);
                holder.linearBuy.setVisibility(View.VISIBLE);
                holder.linearPurchased.setVisibility(View.GONE);
                holder.txtBuyAmount.setText(inappPurchaseModelArrayList.get(position).price);
            }
        }

        holder.txtName.setTypeface(PickPlugApp.getInstance().getMediumFont());
        holder.txtBuyAmount.setTypeface(PickPlugApp.getInstance().getMediumFont());
        holder.txtBestBuyAmount.setTypeface(PickPlugApp.getInstance().getMediumFont());
        holder.txtPurchasedBuyAmount.setTypeface(PickPlugApp.getInstance().getMediumFont());
        holder.txtPurchased.setTypeface(PickPlugApp.getInstance().getMediumFont());
        holder.txtBestValue.setTypeface(PickPlugApp.getInstance().getMediumFont());
        holder.txtEndDate.setTypeface(PickPlugApp.getInstance().getMediumFont());



        holder.linearBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkuDetails _details = inappPurchaseModelArrayList.get(position).skuDetails;
                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(_details)
                        .build();
                billingClient.launchBillingFlow((ActDashboard)mContext, flowParams);


            }
        });

        holder.linearBestBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkuDetails _details = inappPurchaseModelArrayList.get(position).skuDetails;
                BillingFlowParams flowParams = BillingFlowParams.newBuilder().setSkuDetails(_details).build();
                billingClient.launchBillingFlow((ActDashboard)mContext, flowParams);

            }
        });
    }

    @Override
    public int getItemCount() {
        return inappPurchaseModelArrayList.size();
    }

    public class ViewHolderSubscription extends RecyclerView.ViewHolder {
        private TextView txtName,txtBuyAmount,txtBestBuyAmount,txtPurchasedBuyAmount,txtPurchased,txtBestValue,txtEndDate;
        private LinearLayout linearBuy,linearBestBuy,linearPurchased;

        public ViewHolderSubscription(View itemView) {
            super(itemView);

            txtName                 =   (TextView)itemView.findViewById(R.id.list_txt_subs_name);
            txtBuyAmount            =   (TextView)itemView.findViewById(R.id.list_txt_buy);
            txtBestBuyAmount        =   (TextView)itemView.findViewById(R.id.list_txt_buy_best_value);
            txtPurchasedBuyAmount   =   (TextView)itemView.findViewById(R.id.list_txt_buy_purchased);
            txtPurchased            =   (TextView)itemView.findViewById(R.id.list_txt_purchased);
            txtBestValue            =   (TextView)itemView.findViewById(R.id.list_txt_best_value);
            txtEndDate              =   (TextView)itemView.findViewById(R.id.list_txt_end_date);
            linearBuy               =   (LinearLayout)itemView.findViewById(R.id.list_linear_buy);
            linearBestBuy           =   (LinearLayout)itemView.findViewById(R.id.list_linear_buy_best_value);
            linearPurchased         =   (LinearLayout)itemView.findViewById(R.id.list_linear_purchased);
        }
    }

    private void isBestValuePurchased(ArrayList<SubscriptionModel> subscriptionModelArrayList){
        for (int i=0; i<subscriptionModelArrayList.size();i++){
            if (subscriptionModelArrayList.get(i).getIsBestValue().equals("1") && subscriptionModelArrayList.get(i).getIsPurchased().equals("1")){
                isBestValuePurchased =true;
                break;
            }
        }
    }
}
