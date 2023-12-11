package com.example.watchapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.watchapp.model.UserMessage;
import com.example.watchapp.restapi.RestAPIClient;
import com.example.watchapp.restapi.RestAPIService;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TextMessageSend extends AppCompatActivity {

    private static final String TAG = "TextMessageSend";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_msg_send);
        Bundle extras = getIntent().getExtras();
        String toId = extras.get("id").toString();
        String toName = extras.get("name").toString();
        String from = extras.get("userId").toString();

        RestAPIService service = RestAPIClient.getClient().create(RestAPIService.class);

        Button nextBtn = findViewById(R.id.btn_send);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit = findViewById(R.id.text_input);
                String text = edit.getText().toString();
                Log.d(TAG, "onClick: btn_send " + text);
                UserMessage userMessage = new UserMessage(from, toId, text);
                Call<String> call = service.sendMessage(userMessage);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d(TAG, "onResponse : " + response.code() + " , " + response.body());
                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.msg_sent_toast) + " to " + toName, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d(TAG, "onFailure : " + t.toString());
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.msg_failed_toast, Toast.LENGTH_SHORT);
                        toast.show();
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