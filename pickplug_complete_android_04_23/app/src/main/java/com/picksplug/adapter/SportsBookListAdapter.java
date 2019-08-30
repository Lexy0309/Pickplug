package com.picksplug.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.picksplug.R;
import com.picksplug.activity.ActPayment;
import com.picksplug.helpers.CommonUtils;
import com.picksplug.helpers.PickPlugApp;
import com.picksplug.model.SportsBookModel;

import java.util.ArrayList;

/**
 * Created by archive_infotech on 4/7/18.
 */

public class SportsBookListAdapter extends RecyclerView.Adapter<SportsBookListAdapter.ViewHolderSportsBook>{
    private Context                             mContext;
    private ArrayList<SportsBookModel>          sportsBookModelArrayList;

    public SportsBookListAdapter(Context mContext, ArrayList<SportsBookModel> sportsBookModelArrayList) {
        this.mContext = mContext;
        this.sportsBookModelArrayList = sportsBookModelArrayList;
    }

    @Override
    public ViewHolderSportsBook onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.list_sportsbook_row,parent,false);
        return new ViewHolderSportsBook(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolderSportsBook holder, final int position) {
        holder.txtTitle.setText(sportsBookModelArrayList.get(position).getTitle());
        holder.txtDescription.setText(sportsBookModelArrayList.get(position).getDescription());
        holder.txtOperating.setText(sportsBookModelArrayList.get(position).getOperating());
        CommonUtils.loadCircularImageWithGlideFitCenter(holder.imgSportsBook,sportsBookModelArrayList.get(position).getImage_url(),mContext);

        holder.txtTitle.setTypeface(PickPlugApp.getInstance().getBoldFont());
        holder.txtDescription.setTypeface(PickPlugApp.getInstance().getRegularFont());
        holder.txtOperating.setTypeface(PickPlugApp.getInstance().getBoldFont());

        holder.imgJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ActPayment.class);
                intent.putExtra("from","SportsBooksList");
                intent.putExtra("joinUrl",sportsBookModelArrayList.get(position).getJoin_url());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sportsBookModelArrayList.size();
    }

    public class ViewHolderSportsBook extends RecyclerView.ViewHolder{
        private ImageView   imgSportsBook,imgJoin;
        private TextView    txtTitle,txtDescription,txtOperating;

        public ViewHolderSportsBook(View itemView) {
            super(itemView);

            imgSportsBook       =   (ImageView)itemView.findViewById(R.id.list_img_sportsbook);
            imgJoin             =   (ImageView)itemView.findViewById(R.id.list_img_join);
            txtTitle            =   (TextView)itemView.findViewById(R.id.list_txt_sportsbook);
            txtDescription      =   (TextView)itemView.findViewById(R.id.list_txt_desc);
            txtOperating        =   (TextView)itemView.findViewById(R.id.list_txt_operating);
        }
    }
}
