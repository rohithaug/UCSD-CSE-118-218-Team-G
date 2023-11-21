package com.example.watchapp.model;

import com.google.gson.annotations.SerializedName;

public class UserMessage {

    @SerializedName("from")
    public String from;
    @SerializedName("to")
    public String to;
    @SerializedName("message")
    public String message;

    public UserMessage(String from, String to,String message) {
        this.from = from;
        this.to = to;
        this.message = message;
    }

}
