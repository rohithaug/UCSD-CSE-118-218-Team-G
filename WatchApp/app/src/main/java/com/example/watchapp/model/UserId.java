package com.example.watchapp.model;

import com.google.gson.annotations.SerializedName;

public class UserId {
    @SerializedName("userId")
    public String userId;

    public UserId(String userId) {
        this.userId = userId;
    }
}
