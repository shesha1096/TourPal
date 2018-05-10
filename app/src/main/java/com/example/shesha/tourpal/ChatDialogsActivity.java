package com.example.shesha.tourpal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.shesha.tourpal.Adapter.ChatDialogsAdapter;
import com.example.shesha.tourpal.Common.Common;
import com.example.shesha.tourpal.Holder.QBUsersHolder;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.BaseService;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestBuilder;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.chat.Chat;

import java.util.ArrayList;

public class ChatDialogsActivity extends AppCompatActivity {
    private FloatingActionButton floatingActionButton;
    private ListView lstChatDialogs;
    private Bundle extras;
    private String user,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_dialogs);
        extras = getIntent().getExtras();
        user = extras.getString("username");
        password = extras.getString("passsword");
        lstChatDialogs = (ListView) findViewById(R.id.lstChatdialogs);
        createSessionForChat();
        loadChatDialogs();
        floatingActionButton = (FloatingActionButton) findViewById(R.id.chatdialog_adduser);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent usersIntent = new Intent(ChatDialogsActivity.this,ListUsersActivity.class);
                usersIntent.putExtra("username",user);
                usersIntent.putExtra("password",password);
                startActivity(usersIntent);

            }
        });
        lstChatDialogs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QBChatDialog qbChatDialog = (QBChatDialog) lstChatDialogs.getAdapter().getItem(position);
                Intent messageIntent = new Intent(ChatDialogsActivity.this,ChatMessaeActivity.class);
                messageIntent.putExtra(Common.DIALOG_EXTRA,qbChatDialog);
                startActivity(messageIntent);
            }
        });
    }

    private void loadChatDialogs() {
        QBRequestGetBuilder qbRequestGetBuilder = new QBRequestGetBuilder();
        qbRequestGetBuilder.setLimit(100);
        QBRestChatService.getChatDialogs(null,qbRequestGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle bundle) {
                ChatDialogsAdapter chatDialogsAdapter = new ChatDialogsAdapter(ChatDialogsActivity.this,qbChatDialogs);
                lstChatDialogs.setAdapter(chatDialogsAdapter);
                chatDialogsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("ERROR",e.getMessage());

            }
        });
    }

    private void createSessionForChat() {
        QBSettings.getInstance().init(ChatDialogsActivity.this,"70537","CYLfmdOCKpFTHTu","VdNtELbvkW3OE6C");
        QBSettings.getInstance().setAccountKey("28ZMy9Ps8JL1U7A8R2hU");
        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                QBUsersHolder.getInstance().putUsers(qbUsers);
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
        final QBUser qbUser = new QBUser(user,password);
        QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                qbUser.setId(qbSession.getUserId());
                try {
                    qbUser.setPassword(BaseService.getBaseService().getToken());
                } catch (BaseServiceException e) {
                    e.printStackTrace();
                }
                QBChatService.getInstance().login(qbUser, new QBEntityCallback() {
                    @Override
                    public void onSuccess(Object o, Bundle bundle) {

                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e("Error",e.getMessage());

                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });

    }
}
