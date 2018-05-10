package com.example.shesha.tourpal.Model;

public class Message {
    private String message,type;
    private long timestamp;
    private boolean seen;
    private String from;

    public Message() {
    }

    public Message(String message, String type, long timestamp, boolean seen,String from) {
        this.message = message;
        this.type = type;
        this.timestamp = timestamp;
        this.seen = seen;
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
