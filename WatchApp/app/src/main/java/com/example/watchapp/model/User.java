package com.example.watchapp.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("name")
    public String name;
    @SerializedName("email")
    public String email;
    @SerializedName("role")
    public String role;
    @SerializedName("id")
    public String id;

    public User(String name, String email, String role, String id) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.id = id;
    }
}
