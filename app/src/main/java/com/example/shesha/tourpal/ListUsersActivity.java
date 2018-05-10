package com.example.shesha.tourpal;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shesha.tourpal.Adapter.ListUsersAdapter;
import com.example.shesha.tourpal.Common.Common;
import com.example.shesha.tourpal.Holder.QBUsersHolder;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListUsersActivity extends AppCompatActivity {
    private ListView listView;
    private Button createBtn;
    private Bundle extras;
    private String username,passsword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);
        extras = getIntent().getExtras();
        username = extras.getString("username");
        passsword = extras.getString("password");
        Log.d("Username",username);
        retrieveAllUsers();
        listView = (ListView) findViewById(R.id.lstUsers);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        createBtn = (Button) findViewById(R.id.lstCreateChat);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int countChoice = listView.getCount();
                if(listView.getCheckedItemPositions().size() == 1){
                    createPrivateChat(listView.getCheckedItemPositions());
                }else if(listView.getCheckedItemPositions().size()>1){
                    createGroupChat(listView.getCheckedItemPositions());
                }else{
                    Toast.makeText(ListUsersActivity.this,"Pleasse select a user to chat with",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void createPrivateChat(SparseBooleanArray checkedItemPositions) {

    }
    private void createGroupChat(SparseBooleanArray checkedItemPositions){
        ProgressDialog progressDialog = new ProgressDialog(ListUsersActivity.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        int countChoice = listView.getCount();
        List<Integer> occupantIdsList = new ArrayList<>();
        for(int i = 0;i<countChoice;i++){
            if(checkedItemPositions.get(i)){
                QBUser qbUser = (QBUser) listView.getItemAtPosition(i);
                occupantIdsList.add(qbUser.getId());
            }
        }
        progressDialog.dismiss();
        QBChatDialog dialog = new QBChatDialog();
        dialog.setName(Common.createchatDialogName(occupantIdsList));
        dialog.setType(QBDialogType.GROUP);
        dialog.setOccupantsIds(occupantIdsList);
        QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                Toast.makeText(ListUsersActivity.this,"Chat Created Successfully",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("ERROR",e.getMessage());

            }
        });


    }

    private void retrieveAllUsers() {
        QBPagedRequestBuilder pagedRequestBuilder = new QBPagedRequestBuilder();
        pagedRequestBuilder.setPage(1);
        pagedRequestBuilder.setPerPage(50);
        QBUsers.getUsers(pagedRequestBuilder).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                Log.d("Users",qbUsers.toString());
                QBUsersHolder.getInstance().putUsers(qbUsers);
                List<QBUser> userWithoutCurrent = new ArrayList<>();
            for(QBUser user:qbUsers){

                    userWithoutCurrent.add(user);

            }
                ListUsersAdapter listUsersAdapter = new ListUsersAdapter(ListUsersActivity.this,userWithoutCurrent);
                listView.setAdapter(listUsersAdapter);
                listUsersAdapter.notifyDataSetChanged();


            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }
}
