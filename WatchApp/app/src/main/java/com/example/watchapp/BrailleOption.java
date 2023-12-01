package com.example.watchapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BrailleOption extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.braille_option);

        Button readButton = findViewById(R.id.readButton);
        Button writeButton = findViewById(R.id.writeButton);

        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BrailleOption.this, BrailleMessageReceive.class);
                startActivity(intent);
            }
        });

        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BrailleOption.this, BrailleMessageSend.class);
                startActivity(intent);
            }
        });

    }

}
