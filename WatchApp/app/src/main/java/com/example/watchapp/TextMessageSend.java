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

        RestAPIService service = RestAPIClient.getClient().create(RestAPIService.class);

        //TEST -- hardcoded values -- START
        String from = "655c67b6c222a131939b0e26";
        String to = "655c67c1c222a131939b0e29";
        //TEST -- hardcoded values -- END

        Button nextBtn = findViewById(R.id.btn_send);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit = findViewById(R.id.text_input);
                String text = edit.getText().toString();
                Log.d(TAG, "onClick: btn_send " + text);
                UserMessage userMessage = new UserMessage(from, to, text);
                Call<String> call = service.sendMessage(userMessage);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d(TAG, "onResponse : " + response.code() + " , " + response.body());
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d(TAG, "onFailure : " + t.toString());
                    }
                });
                Toast toast = Toast.makeText(getApplicationContext(), R.string.msg_sent_toast, Toast.LENGTH_SHORT);
                toast.show();
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