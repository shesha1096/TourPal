package com.example.shesha.tourpal.Common;

import com.example.shesha.tourpal.Holder.QBUsersHolder;
import com.quickblox.users.model.QBUser;

import java.util.List;

public class Common {
    public static final String DIALOG_EXTRA = "Dialogs";
    public static String createchatDialogName(List<Integer> qbUsers){
        List<QBUser> qbUsers1 = QBUsersHolder.getInstance().getUsersByID(qbUsers);
        StringBuilder builder = new StringBuilder();
        for(QBUser qbUser:qbUsers1){
            builder.append(qbUser.getFullName()).append(" ");
            if(builder.length()>30){
                builder = builder.replace(30,builder.length()-1,"...");
            }
        }
        return builder.toString();
    }
}
