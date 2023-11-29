package com.example.braille;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReadActivity extends Activity {
    private int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        // Reference buttons from the layout
        Button dot1 = findViewById(R.id.dot1);
        Button dot2 = findViewById(R.id.dot2);
        Button dot3 = findViewById(R.id.dot3);
        Button dot4 = findViewById(R.id.dot4);
        Button dot5 = findViewById(R.id.dot5);
        Button dot6 = findViewById(R.id.dot6);
        Button nextButton = findViewById(R.id.next);

        String sentence = "I love CSE118";
        StringBuilder brailleDots = new StringBuilder("");

        Handler handler = new Handler();

        // reverse key & value, merge Maps b/c no key overlaps
        List<Map<String, Map<String, String>>> brailleMap = loadJSON(this, "brailleMap.json");
        Map<String, String> brailleToAlphabet = brailleMap.get(0).get("alphabet");
        Map<String, String> brailleToNumber = brailleMap.get(1).get("number");

        Map<String, String> letterToBraille = new HashMap<>();
        for (Map.Entry<String, String> entry : brailleToAlphabet.entrySet()) {
            letterToBraille.put(entry.getValue(), entry.getKey());
        }

        for (Map.Entry<String, String> entry : brailleToNumber.entrySet()) {
            letterToBraille.put(entry.getValue(), entry.getKey());
        }

        for (int i = 0; i < sentence.length(); i++) {
            char letter = sentence.charAt(i);
            Log.d("Debug", String.valueOf(letter));
            if (letter == ' ')  {
                brailleDots.append("000000");
            }
            else if (Character.isDigit(letter)) {
                brailleDots.append("001111");
                brailleDots.append(letterToBraille.get(letter + ""));
            }
            else if (Character.isUpperCase(letter) && letterToBraille.containsKey((letter +"").toLowerCase())) {
                brailleDots.append("000001");
                brailleDots.append(letterToBraille.get((letter + "").toLowerCase(Locale.ROOT)));
            }
            else if (letterToBraille.containsKey(letter + "")) {
                brailleDots.append(letterToBraille.get(letter + ""));
            }
            else {
                // invalid input
            }
            //else if (letterToBraille.containsKey(letter +"")) {
            //    Log.d("Debug", String.valueOf(letter));
            //    if (Character.isDigit(letter)) {
            //        brailleDots.append("001111");
            //        brailleDots.append(letterToBraille.get(letter + ""));
            //    }
            //    else if (Character.isUpperCase(letter)) {
            //        brailleDots.append("000001");
            //        brailleDots.append(letterToBraille.get((letter + "").toLowerCase(Locale.ROOT)));
            //    } else {
            //        brailleDots.append(letterToBraille.get(letter + ""));
            //    }
            //}
            //else {
            //    // if no letter in the map, handle error
//
            //}
        }

        String brailleDotsSentence = brailleDots.toString();
        Log.d("Debug", "Sentence into braille dots:" + brailleDotsSentence);

        i = 0;
        dot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDotClick(brailleDotsSentence, i);
            }
        });

        dot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDotClick(brailleDotsSentence, i + 1);
            }
        });

        dot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDotClick(brailleDotsSentence, i + 2);
            }
        });

        dot4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDotClick(brailleDotsSentence, i + 3);
            }
        });

        dot5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDotClick(brailleDotsSentence, i + 4);
            }
        });

        dot6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDotClick(brailleDotsSentence, i + 5);
            }
        });

        // next letter
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Move to next letter
                i += 6;
                vibrateWatch(200);
            }
        });
    }

    private void handleDotClick(String sentence, int index) {
        if (index < sentence.length() && sentence.charAt(index) == '1') {
            Log.d("Debug", "Dot clicked and vibrate");
            // vibrate to let the user know that the button has been clicked
            vibrateWatch(50);
        } else {
            Log.d("Debug", "Dot clicked but nothing happens");
        }
    }

    private void vibrateWatch(int duration) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            // Check if the device supports vibration
            if (vibrator.hasVibrator()) {
                VibrationEffect vibrationEffect = VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE);
                vibrator.vibrate(vibrationEffect);
                Log.d("Debug", "vibrating");
            }
        }
    }

    private static List<Map<String, Map<String, String>>> loadJSON(Context context, String path) {
        try {
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Map<String, Map<String, String>>>>(){}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


}