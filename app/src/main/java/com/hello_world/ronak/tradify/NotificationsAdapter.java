package com.hello_world.ronak.tradify;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sharm_000 on 4/27/2016.
 */
public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<String> notifs;

    public NotificationsAdapter(List<String> notifications){
        notifs = notifications;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View newView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_notifications, parent, false);
        ViewHolder vh = new ViewHolder(newView);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String notif = "No data found";
        if(position>=0 && position<notifs.size()) {
            notif = notifs.get(position);
        }
        if(holder instanceof ViewHolder)
            ((ViewHolder) holder).bindNotifData(notif);
    }

    @Override
    public int getItemCount() {
        Log.d("Notif size: ", String.valueOf(notifs.size()));
        return notifs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView msg;
        public ViewHolder(View itemView) {
            super(itemView);
            msg = (TextView) itemView.findViewById(R.id.notification);
        }

        public void bindNotifData(String notif){
            if(notif!=null) {
                msg.setText(notif);
            }
        }
    }
}

