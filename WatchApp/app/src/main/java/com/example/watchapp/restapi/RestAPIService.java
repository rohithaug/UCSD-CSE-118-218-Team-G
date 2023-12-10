package com.example.watchapp.restapi;

import com.example.watchapp.model.User;
import com.example.watchapp.model.UserId;
import com.example.watchapp.model.UserMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestAPIService {
    /**
     * Returns the list of users registered for the service.
     * @return The list of users.
     */
    @GET("user")
    public Call<List<User>> getUsers();

    /**
     * Returns the user id for the given user name.
     * @param userName The user name.
     * @return The user id.
     */
    @GET("user/id")
    public Call<UserId> getUserIdFromName(@Query("userName") String userName);

    /**
     * Returns the User object for the given user id.
     * @param userId The user id.
     * @return The User object.
     */
    @GET("user/{id}")
    public Call<User> getUserFromId(@Path("id") String userId);

    /**
     * Returns the list of messages pending for the receiver from the sender.
     * @param userId The user id of the receiver.
     * @param from The user id of the sender.
     * @param consolidated Set it to True to receive all messages, and to False to receive messages one by one.
     * @return The list of messages.
     */
    @GET("message")
    public Call<List<UserMessage>> getMessages(@Query("userId") String userId, @Query("from") String from, @Query("consolidated") String consolidated);

    /**
     * Returns the list of all messages pending for the user from all senders.
     * @param userId The user id of the receiver.
     * @return The list of messages.
     */
    @GET("message")
    public Call<List<UserMessage>> getAllMessages(@Query("userId") String userId);

    /**
     * Sends the message contained in the UserMessage object.
     * @param userMessage The UserMessage object containing the receiver user information and the content of the message.
     * @return A message indicating the result of the operation.
     */
    @POST("message")
    public Call<String> sendMessage(@Body UserMessage userMessage);
}