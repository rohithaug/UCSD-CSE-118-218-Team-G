package com.example.watchapp.restapi;

import com.example.watchapp.model.UserMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestAPIService {
    @GET("message")
    public Call<List<UserMessage>> getMessages(@Query("userId") String userId);


    @POST("message")
    public Call<String> sendMessage(@Body UserMessage userMessage);
}