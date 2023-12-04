package com.example.watchapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.watchapp.model.UserId;
import com.example.watchapp.restapi.RestAPIClient;
import com.example.watchapp.restapi.RestAPIService;
import com.example.watchapp.util.FileUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DebugUserIdCreate extends AppCompatActivity  {

    private static final String TAG = "DebugUserIdCreate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug_userid_create);

        Button nextBtn = findViewById(R.id.debug_save);

        RestAPIService service = RestAPIClient.getClient().create(RestAPIService.class);


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit = findViewById(R.id.debug_userid);
                String text = edit.getText().toString();
                Log.d(TAG, "onClick: debug_save " + text);
                Call<UserId> call = service.getUserIdFromName(text);
                call.enqueue(new Callback<UserId>() {
                    @Override
                    public void onResponse(Call<UserId> call, Response<UserId> response) {
                        Log.d(TAG, "onResponse : " + response.code() + " , " + response.body());
                        UserId userId = response.body();
                        FileUtils.saveUserId(getFilesDir(), userId.userId);
                    }

                    @Override
                    public void onFailure(Call<UserId> call, Throwable t) {
                            Log.d(TAG, "onFailure : " + t.toString());
                        }
                    });
            }
        });
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();

    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }
}
