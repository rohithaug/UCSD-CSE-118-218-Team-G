package com.example.watchapp;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.watchapp.model.UserMessage;
import com.example.watchapp.restapi.RestAPIClient;
import com.example.watchapp.restapi.RestAPIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TextMessageReceive extends AppCompatActivity {

    private static final String TAG = "TextMessageReceive";
    private int msgId = -1;
    List<UserMessage> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_msg_receive);

        Bundle extras = getIntent().getExtras();

        String userId = extras.get("userId").toString();
        String from = extras.get("id").toString();
        msgId = 0;

        TextView msgView = findViewById(R.id.text_msg_view);
        msgView.setMovementMethod(new ScrollingMovementMethod());
        Button nextBtn = findViewById(R.id.btn_next);
        Button prevBtn = findViewById(R.id.btn_prev);

        RestAPIService service = RestAPIClient.getClient().create(RestAPIService.class);
        Log.d(TAG, "userId : " + userId);
        Call<List<UserMessage>> call = service.getMessages(userId, from, "true");
        call.enqueue(new Callback<List<UserMessage>>() {
            @Override
            public void onResponse(Call<List<UserMessage>> call, Response<List<UserMessage>> response) {
                Log.d(TAG, "onResponse : " + response.code() + " , " + response.body().size());
                list = response.body();
                prevBtn.setEnabled(false);
                if(list.size() == 0 || list.size() == 1) {
                    nextBtn.setEnabled(false);
                    if(list.size() == 0) {
                        msgView.setText(R.string.msg_none);
                    } else {
                        msgView.setText(list.get(msgId).message);
                    }
                } else {
                    msgView.setText(list.get(msgId).message);
                    nextBtn.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<List<UserMessage>> call, Throwable t) {
                Log.d(TAG, "onFailure : " + t.toString());
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: btn_next");
                msgView.scrollTo(0, 0);
                if (msgId < list.size() - 1) {
                    msgView.setText(list.get(++msgId).message);
                    prevBtn.setEnabled(true);
                }
                if(msgId == list.size() - 1) {
                    nextBtn.setEnabled(false);
                } else {
                    nextBtn.setEnabled(true);
                }
            }
        });
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: btn_prev");
                msgView.scrollTo(0, 0);
                if (msgId > 0) {
                    msgView.setText(list.get(--msgId).message);
                    nextBtn.setEnabled(true);
                }
                if(msgId == 0) {
                    prevBtn.setEnabled(false);
                } else {
                    prevBtn.setEnabled(true);
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