package com.example.shesha.tourpal.Holder;

import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QBChatMessageHolder {
    private static QBChatMessageHolder instance;
    private HashMap<String,List<QBChatMessage>> qbChatMessageArray;
    public static synchronized QBChatMessageHolder getInstance(){
        QBChatMessageHolder qbChatMessageHolder;
        synchronized (QBChatMessageHolder.class){
            if(instance == null){
                instance = new QBChatMessageHolder();
            }
            qbChatMessageHolder = instance;
        }
        return  qbChatMessageHolder;
    }
    private QBChatMessageHolder(){
        this.qbChatMessageArray = new HashMap<>();
    }
    public void putMessages(String dialogid,List<QBChatMessage> qbChatMessages){
        this.qbChatMessageArray.put(dialogid,qbChatMessages);
    }
    public void putMessage(String dialogid,QBChatMessage qbChatMessage){
        List<QBChatMessage> lstResult = (List) this.qbChatMessageArray.get(dialogid);
        lstResult.add(qbChatMessage);
        List<QBChatMessage> lstAdded = new ArrayList(lstResult.size());
        lstAdded.addAll(lstResult);
        putMessages(dialogid,lstAdded);
    }
    public List<QBChatMessage> getMessagesbydialogID(String dialogID){
        return (List<QBChatMessage>) this.qbChatMessageArray.get(dialogID);
    }
}
