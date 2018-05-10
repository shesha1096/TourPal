package com.example.shesha.tourpal.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shesha.tourpal.Model.Message;
import com.example.shesha.tourpal.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> msgList;
    private FirebaseAuth firebaseAuth;

    public MessageAdapter(List<Message> msgList) {
        this.msgList = msgList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_single_layout,parent,false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        firebaseAuth = FirebaseAuth.getInstance();
        String curr_user_id = firebaseAuth.getCurrentUser().getEmail();
        int index1 = curr_user_id.indexOf('@');
        String user1 = curr_user_id.substring(0,index1);
        Message message = msgList.get(position);
        String from_user = message.getFrom();
        if(from_user.equals(user1)){
            holder.msgText.setBackgroundColor(Color.WHITE);
            holder.msgText.setTextColor(Color.BLACK);

        }else{
            holder.msgText.setBackgroundResource(R.drawable.msg_text_background);
            holder.msgText.setTextColor(Color.WHITE);

        }
        holder.msgText.setText(message.getMessage());


    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView msgText;
        public MessageViewHolder(View itemView) {
            super(itemView);
            msgText = (TextView) itemView.findViewById(R.id.msg_single_text);
        }
    }
}
