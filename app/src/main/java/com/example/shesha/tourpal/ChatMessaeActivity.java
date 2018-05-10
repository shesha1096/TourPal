package com.example.shesha.tourpal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shesha.tourpal.Adapter.ChatMessageAdapter;
import com.example.shesha.tourpal.Common.Common;
import com.example.shesha.tourpal.Holder.QBChatMessageHolder;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.BaseService;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.request.QBMessageGetBuilder;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smackx.muc.DiscussionHistory;

import java.util.ArrayList;
import java.util.List;

public class ChatMessaeActivity extends AppCompatActivity implements QBChatDialogMessageListener {
    private QBChatDialog qbChatDialog;
    private ListView lstChatMessages;
    private ImageButton submitBtn;
    private EditText messageView;
    private ChatMessageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_messae);

        lstChatMessages = (ListView) findViewById(R.id.list_of_messages);
        submitBtn = (ImageButton) findViewById(R.id.send_button);
        messageView = (EditText) findViewById(R.id.edit_content);
        initChatDialogs();
        retrieveMessage();
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QBChatMessage qbChatMessage = new QBChatMessage();
                qbChatMessage.setBody(messageView.getText().toString());
                qbChatMessage.setSenderId(QBChatService.getInstance().getUser().getId());
                qbChatMessage.setSaveToHistory(true);
                try {
                    qbChatDialog.sendMessage(qbChatMessage);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
                if(qbChatDialog.getType() == QBDialogType.PRIVATE){
                    QBChatMessageHolder.getInstance().putMessage(qbChatMessage.getDialogId(),qbChatMessage);
                    List<QBChatMessage> messages = QBChatMessageHolder.getInstance().getMessagesbydialogID(qbChatMessage.getDialogId());
                    adapter = new ChatMessageAdapter(ChatMessaeActivity.this,messages);
                    lstChatMessages.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
                messageView.setText("");
                messageView.setFocusable(true);
            }
        });
    }



            private void retrieveMessage() {
        QBMessageGetBuilder qbMessageGetBuilder = new QBMessageGetBuilder();
        qbMessageGetBuilder.setLimit(500);
        if(qbChatDialog!=null){
            QBRestChatService.getDialogMessages(qbChatDialog,qbMessageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
                @Override
                public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {
                    QBChatMessageHolder.getInstance().putMessages(qbChatDialog.getDialogId(),qbChatMessages);
                    adapter = new ChatMessageAdapter(ChatMessaeActivity.this,qbChatMessages);
                    lstChatMessages.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(QBResponseException e) {

                }
            });
        }
    }

    private void initChatDialogs() {
        qbChatDialog = (QBChatDialog)getIntent().getSerializableExtra("Dialogs");
        Log.e("ERROR",qbChatDialog.getDialogId());
        qbChatDialog.initForChat(QBChatService.getInstance());
        boolean log= QBChatService.getInstance().isLoggedIn();
        Log.d("Logged",String.valueOf(log));
        QBIncomingMessagesManager qbIncomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
        Log.d("Incoming",qbIncomingMessagesManager.toString());

            qbIncomingMessagesManager.addDialogMessageListener(new QBChatDialogMessageListener() {
                @Override
                public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {

                }

                @Override
                public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {

                }
            });



        if(qbChatDialog.getType() == QBDialogType.GROUP || qbChatDialog.getType() == QBDialogType.PUBLIC_GROUP){
            DiscussionHistory discussionHistory = new DiscussionHistory();
            discussionHistory.setMaxStanzas(0);
            qbChatDialog.join(discussionHistory, new QBEntityCallback() {
                @Override
                public void onSuccess(Object o, Bundle bundle) {

                }

                @Override
                public void onError(QBResponseException e) {
                    Log.e("ERROR",e.getMessage());

                }
            });

        }
        qbChatDialog.addMessageListener(this);

    }



    @Override
    public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
        QBChatMessageHolder.getInstance().putMessage(qbChatMessage.getDialogId(),qbChatMessage);
        List<QBChatMessage> messages = QBChatMessageHolder.getInstance().getMessagesbydialogID(qbChatMessage.getDialogId());
        adapter = new ChatMessageAdapter(ChatMessaeActivity.this,messages);
        lstChatMessages.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        qbChatDialog.removeMessageListrener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        qbChatDialog.removeMessageListrener(this);
    }
}
