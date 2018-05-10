package com.example.shesha.tourpal.Holder;

import android.util.SparseArray;
import android.util.SparseBooleanArray;

import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

public class QBUsersHolder {
    private static QBUsersHolder instance;
    private SparseArray<QBUser> qbUserSparseArray;
    public static synchronized QBUsersHolder getInstance(){
        if(instance == null){
            instance = new QBUsersHolder();

        }
        return instance;
    }
    private QBUsersHolder(){
        qbUserSparseArray =new SparseArray<>();
    }
    public void putUsers(List<QBUser> users){
        for(QBUser user:users){
            putUser(user);
        }
    }

    private void putUser(QBUser user) {
        qbUserSparseArray.put(user.getId(),user);
    }
    public QBUser getUserbyID(int id){
        return qbUserSparseArray.get(id);
    }
    public List<QBUser> getUsersByID(List<Integer> ids){
        List<QBUser> qbUsers = new ArrayList<>();
        for(Integer id:ids){
            QBUser qbUser = getUserbyID(id);
            if(qbUser !=null){
                qbUsers.add(qbUser);
            }
        }
        return qbUsers;
    }
}
