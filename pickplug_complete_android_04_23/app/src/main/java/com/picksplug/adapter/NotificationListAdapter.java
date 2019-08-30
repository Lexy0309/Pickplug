package com.picksplug.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.picksplug.R;
import com.picksplug.helpers.CommonUtils;
import com.picksplug.helpers.PickPlugApp;
import com.picksplug.model.NotificationResponseModel;

import java.util.ArrayList;

/**
 * Created by archive_infotech on 4/7/18.
 */

public class NotificationListAdapter  extends RecyclerView.Adapter<NotificationListAdapter.ViewHolderNotification>{
    private Context                                         mContext;
    private ArrayList<NotificationResponseModel.Result>     notificationModelArrayList;

    public NotificationListAdapter(Context mContext, ArrayList<NotificationResponseModel.Result> notificationModelArrayList) {
        this.mContext = mContext;
        this.notificationModelArrayList = notificationModelArrayList;
    }

    @Override
    public ViewHolderNotification onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.list_notification_row,parent,false);
        return new ViewHolderNotification(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolderNotification holder, int position) {
        holder.txtTitle.setText(notificationModelArrayList.get(position).getTitle());
        holder.txtMessage.setText(notificationModelArrayList.get(position).getMessage());
        holder.txtTime.setText(CommonUtils.getFormattedDate(notificationModelArrayList.get(position).getCreatedAt(),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd"));

        holder.txtTitle.setTypeface(PickPlugApp.getInstance().getBoldFont());
        holder.txtMessage.setTypeface(PickPlugApp.getInstance().getRegularFont());
        holder.txtTime.setTypeface(PickPlugApp.getInstance().getMediumFont());
    }

    @Override
    public int getItemCount() {
        return notificationModelArrayList.size();
    }

    public class ViewHolderNotification extends RecyclerView.ViewHolder{
        private TextView txtTitle,txtMessage,txtTime;

        public ViewHolderNotification(View itemView) {
            super(itemView);

            txtTitle        =   (TextView)itemView.findViewById(R.id.list_txt_notification_title);
            txtMessage      =   (TextView)itemView.findViewById(R.id.list_txt_notification_msg);
            txtTime         =   (TextView)itemView.findViewById(R.id.list_txt_time);
        }
    }
}
