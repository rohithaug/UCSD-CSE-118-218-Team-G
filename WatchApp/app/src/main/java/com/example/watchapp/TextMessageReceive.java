package com.example.watchapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.watchapp.model.UserMessage;
import com.example.watchapp.restapi.RestAPIClient;
import com.example.watchapp.restapi.RestAPIService;
import com.example.watchapp.util.FileUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TextMessageReceive extends AppCompatActivity {

    private static final String TAG = "TextMessageReceive";
    private int msgId = -1;
    private static String userId = "";
    List<UserMessage> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_msg_receive);

        userId = FileUtils.readUserDetails(getFilesDir());
        msgId = 0;

        TextView msgView = findViewById(R.id.text_msg_view);
        RestAPIService service = RestAPIClient.getClient().create(RestAPIService.class);
        Log.d(TAG, "userId : " + userId);
        Call<List<UserMessage>> call = service.getMessages(userId);
        call.enqueue(new Callback<List<UserMessage>>() {
            @Override
            public void onResponse(Call<List<UserMessage>> call, Response<List<UserMessage>> response) {
                Log.d(TAG, "onResponse : " + response.code() + " , " + response.body().size());
                list = response.body();
                msgView.setText(list.get(msgId).message);
            }

            @Override
            public void onFailure(Call<List<UserMessage>> call, Throwable t) {
                Log.d(TAG, "onFailure : " + t.toString());
            }
        });

        Button nextBtn = findViewById(R.id.btn_next);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: btn_next");
                if (msgId < list.size() - 1) {
                    msgView.setText(list.get(++msgId).message);
                }
            }
        });
        Button prevBtn = findViewById(R.id.btn_prev);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: btn_prev");
                if (msgId > 0) {
                    msgView.setText(list.get(--msgId).message);
                }
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