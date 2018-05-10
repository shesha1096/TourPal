package com.example.shesha.tourpal.Adapter;

import android.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shesha.tourpal.Model.GroupMessage;
import com.example.shesha.tourpal.R;

import java.util.List;

/**
 * Created by Nitish Kumar on 14-12-2017.
 */

public class GroupMessageAdapter extends RecyclerView.Adapter<GroupMessageAdapter.MyViewHolder> {
    private List<GroupMessage> mMessageList;

    public GroupMessageAdapter(List<GroupMessage>messageList){
        this.mMessageList=messageList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout=-1;
        switch (viewType){
            case GroupMessage.TYPE_MESSAGE_RECEIVED:
                layout= R.layout.message_item_received;
                break;
            case GroupMessage.TYPE_LOG:
                layout=R.layout.log_item;
                break;
            case GroupMessage.TYPE_MESSAGE_SENT:
                layout=R.layout.message_item_sent;
                break;
        }
        View itemView= LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GroupMessageAdapter.MyViewHolder holder, int position) {
        GroupMessage currentMessage=mMessageList.get(position);
        if(currentMessage.getmUsername()!=null) {
            if (currentMessage.getmUsername().equals(mMessageList.get(position - 1).getmUsername())) {
                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                textParams.setMargins(0, 4, 0, 0);
                holder.mView.setLayoutParams(textParams);
                if(holder.mUsername!=null)
                    holder.mUsername.setVisibility(View.GONE);
            } else {
                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                textParams.setMargins(0, 16, 0, 0);
                holder.mView.setLayoutParams(textParams);
                holder.setmUsername(currentMessage.getmUsername());
            }
        }
        holder.setmMessage(currentMessage.getmMessage());
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mMessageList.get(position).getmType();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView mMessage,mUsername;
        public View mView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mMessage=(TextView)itemView.findViewById(R.id.message);
            mUsername=(TextView)itemView.findViewById(R.id.username);
            mView=itemView;
        }

        public void setmUsername(String username){
            if(mUsername==null)
                return;
            mUsername.setText(username);
        }

        public void setmMessage(String message){
            if(mMessage==null)
                return;
            mMessage.setText(message);
        }
    }




}
