package com.example.watchapp;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "getfilesdir : " + getFilesDir());

        TextView textView = findViewById(R.id.text1);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "textview");
                launchActivity("DebugUserIdCreate");
            }
        });
        Button sendBtn = findViewById(R.id.send);
        Button receiveBtn = findViewById(R.id.receive);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "39");
                launchActivity("TextMessageSend");
            }
        });
        receiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "47");
                launchActivity("TextMessageReceive");
            }
        });

    };

    private void launchActivity(String name) {
        Intent intent;
        switch (name) {
            case "TextMessageSend":
                Log.d(TAG, "55");
                intent = new Intent(this, TextMessageSend.class);
                startActivity(intent);
                break;
            case "TextMessageReceive":
                Log.d(TAG, "65");
                intent = new Intent(this, TextMessageReceive.class);
                startActivity(intent);
                break;
            case "DebugUserIdCreate":
                intent = new Intent(this, DebugUserIdCreate.class);
                startActivity(intent);
                break;
        }
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