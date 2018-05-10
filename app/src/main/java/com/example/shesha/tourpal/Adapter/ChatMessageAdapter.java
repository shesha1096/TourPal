package com.example.shesha.tourpal.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shesha.tourpal.Holder.QBUsersHolder;
import com.example.shesha.tourpal.R;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatMessage;

import java.util.List;

public class ChatMessageAdapter extends BaseAdapter {
    private Context context;
    private List<QBChatMessage> qbChatMessages;

    public ChatMessageAdapter() {
    }

    public ChatMessageAdapter(Context context, List<QBChatMessage> qbChatMessages) {
        this.context = context;
        this.qbChatMessages = qbChatMessages;
    }

    @Override
    public int getCount() {
        return qbChatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return qbChatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(qbChatMessages.get(position).getSenderId().equals(QBChatService.getInstance().getUser().getId())){
                view = inflater.inflate(R.layout.msg_single_layout,null);
                TextView textView = (TextView) view.findViewById(R.id.msg_single_text);
                textView.setText(qbChatMessages.get(position).getBody());
            }else{
                view = inflater.inflate(R.layout.msg_single_layout,null);
                TextView textView = (TextView) view.findViewById(R.id.msg_single_text);
                textView.setText(QBUsersHolder.getInstance().getUserbyID(qbChatMessages.get(position).getSenderId()).getFullName());
            }

        }
        return view;
    }
}
