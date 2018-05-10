package com.example.shesha.tourpal.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shesha.tourpal.R;
import com.example.shesha.tourpal.TextDrawable;
import com.quickblox.chat.model.QBChatDialog;

import java.util.ArrayList;
import java.util.Random;

public class ChatDialogsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<QBChatDialog> qbChatDialogs;

    public ChatDialogsAdapter(Context context, ArrayList<QBChatDialog> qbChatDialogs) {
        this.context = context;
        this.qbChatDialogs = qbChatDialogs;
    }

    public ChatDialogsAdapter() {
    }


    @Override
    public int getCount() {
        return qbChatDialogs.size();
    }

    @Override
    public Object getItem(int position) {
        return qbChatDialogs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lsit_chat_dialog,null);
            TextView txtMessage,txtTitle;
            ImageView imageView;
            txtMessage = (TextView) view.findViewById(R.id.lsit_message);
            txtTitle = (TextView) view.findViewById(R.id.lsit_title);
            imageView = (ImageView) view.findViewById(R.id.lsit_imgDialog);
            txtMessage.setText(qbChatDialogs.get(position).getLastMessage());
            txtTitle.setText(qbChatDialogs.get(position).getName());
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            com.amulyakhare.textdrawable.TextDrawable.IBuilder builder = com.amulyakhare.textdrawable.TextDrawable.builder().beginConfig().
                    withBorder(4).endConfig().round();
            com.amulyakhare.textdrawable.TextDrawable drawable = builder.build(txtTitle.getText().toString().substring(0,1).toUpperCase(),color);
            imageView.setImageDrawable(drawable);

        }
        return view;
    }
}
