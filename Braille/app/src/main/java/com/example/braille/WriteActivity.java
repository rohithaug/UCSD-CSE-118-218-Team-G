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

public class WriteActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        // Reference buttons from the layout
        Button dot1 = findViewById(R.id.dot1);
        Button dot2 = findViewById(R.id.dot2);
        Button dot3 = findViewById(R.id.dot3);
        Button dot4 = findViewById(R.id.dot4);
        Button dot5 = findViewById(R.id.dot5);
        Button dot6 = findViewById(R.id.dot6);
        Button clearButton = findViewById(R.id.clear);
        Button sendButton = findViewById(R.id.send);

        StringBuilder brailleDotsSentence = new StringBuilder("");
        StringBuilder brailleDots = new StringBuilder("000000");
        StringBuilder brailleIndicator = new StringBuilder("alphabet");

        Handler handler = new Handler();

        List<Map<String, Map<String, String>>> brailleMap = loadJSON(this, "brailleMap.json");
        Map<String, String> brailleToAlphabet = brailleMap.get(0).get("alphabet");
        Map<String, String> brailleToNumber = brailleMap.get(1).get("number");

        dot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dot1 clicked, store this info
                brailleDots.setCharAt(0, '1');
                Log.d("Debug", "Dot1 clicked");
                // vibrate to let the user that the button has been clicked
                vibrateWatch(50);
            }
        });

        dot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dot1 clicked, store this info
                brailleDots.setCharAt(1, '1');
                Log.d("Debug", "Dot2 clicked");
                // vibrate to let the user that the button has been clicked
                vibrateWatch(50);
            }
        });

        dot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dot1 clicked, store this info
                brailleDots.setCharAt(2, '1');
                Log.d("Debug", "Dot3 clicked");
                // vibrate to let the user that the button has been clicked
                vibrateWatch(50);
            }
        });

        dot4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dot1 clicked, store this info
                brailleDots.setCharAt(3, '1');
                Log.d("Debug", "Dot4 clicked");
                // vibrate to let the user that the button has been clicked
                vibrateWatch(50);
            }
        });

        dot5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dot1 clicked, store this info
                brailleDots.setCharAt(4, '1');
                Log.d("Debug", "Dot5 clicked");
                // vibrate to let the user that the button has been clicked
                vibrateWatch(50);
            }
        });

        dot6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dot1 clicked, store this info
                brailleDots.setCharAt(5, '1');
                Log.d("Debug", "Dot6 clicked");
                // vibrate to let the user that the button has been clicked
                vibrateWatch(50);
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // clear the dots clicked so far
                brailleDots.setLength(0);
                brailleDots.append("000000");
                Log.d("Debug", "Clear clicked. BrailleDots is reset to " + brailleDots.toString());

                vibrateWatch(50);
                // Schedule the second vibration with a delay of 50 milliseconds
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Vibrate second time
                        vibrateWatch(50);
                    }
                }, 100);
            }
        });

        // clear whole sentence
        clearButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Reset the brailleDots and brailleDotsSentence
                brailleDots.setLength(0);
                brailleDots.append("000000");
                brailleDotsSentence.setLength(0);
                Log.d("Debug", "Clear is long pressed. Reset: dots - " + brailleDots.toString() + ", sentence - " + brailleDotsSentence.toString());
                return true; // Return true to indicate that the long click is consumed
            }
        });

        // register a letter
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // The end of a letter. Append to the sentence.
                // Translate to letter
                String letter = "";
                String brailleCharacter = brailleDots.toString();
                if ("000000".equals(brailleCharacter)) {    // space
                    letter = " ";
                } else if ("001111".equals(brailleCharacter)) {     // number indicator
                    // set indicator to be number
                    brailleIndicator.setLength(0);
                    brailleIndicator.append("number");

                    // Reset the brailleDots
                    brailleDots.setLength(0);
                    brailleDots.append("000000");
                    Log.d("Debug", "Number indicator clicked. Reset: " + brailleDots.toString());
                    vibrateWatch(200);
                    return;
                } else if ("000001".equals(brailleCharacter)) {     // number indicator
                    // set indicator to be number
                    brailleIndicator.setLength(0);
                    brailleIndicator.append("capital");

                    // Reset the brailleDots
                    brailleDots.setLength(0);
                    brailleDots.append("000000");
                    Log.d("Debug", "Capital indicator clicked. Reset: " + brailleDots.toString());
                    vibrateWatch(200);
                    return;
                } else if ( brailleIndicator.toString().equals("number")){
                    brailleIndicator.setLength(0);
                    brailleIndicator.append("alphabet");
                    if (brailleToNumber.containsKey(brailleCharacter)) {
                        letter = brailleToNumber.get(brailleDots.toString());
                    } else {
                        // if invalid input, clear it and
                        // indicate the user with the same vibration used when "Clear"
                        brailleDots.setLength(0);
                        brailleDots.append("000000");
                        Log.e("Error", "Number not found for braille pattern "
                                + brailleCharacter + ". BrailleDots is reset to "
                                + brailleDots.toString());
                        vibrateWatch(50);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Vibrate second time
                                vibrateWatch(50);
                            }
                        }, 100);
                        return;
                    }
                } else {    // "alphabet"
                    if (brailleToAlphabet.containsKey(brailleCharacter)) {
                        letter = brailleToAlphabet.get(brailleDots.toString());
                        if (brailleIndicator.toString().equals("capital")) {
                            letter = letter.toUpperCase(Locale.ROOT);
                            brailleIndicator.setLength(0);
                            brailleIndicator.append("alphabet");
                        }
                    } else {
                        // if invalid input, clear it and
                        // indicate the user with the same vibration used when "Clear"
                        brailleDots.setLength(0);
                        brailleDots.append("000000");
                        Log.e("Error", "Alphabet not found for braille pattern "
                                + brailleCharacter + ". BrailleDots is reset to "
                                + brailleDots.toString());
                        vibrateWatch(50);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Vibrate second time
                                vibrateWatch(50);
                            }
                        }, 100);
                        return;
                    }
                }
                Log.d("Debug", "Final dots: " +  brailleDots.toString() + " " + letter);
                brailleDotsSentence.append(letter);
                // Reset the brailleDots
                brailleDots.setLength(0);
                brailleDots.append("000000");
                Log.d("Debug", "Reset: " + brailleDots.toString());
                vibrateWatch(200);
            }
        });

        // send a sentence
        sendButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // send the message
                Log.d("Debug", "Message sent: " + brailleDotsSentence.toString());
                // Reset the brailleDots and brailleDotsSentence
                brailleDots.setLength(0);
                brailleDots.append("000000");
                brailleDotsSentence.setLength(0);
                Log.d("Debug", "Reset: dots - " + brailleDots.toString() + ", sentence - " + brailleDotsSentence.toString());
                return true; // Return true to indicate that the long click is consumed
            }
        });
    }

    private void vibrateWatch(int duration) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            // Check if the device supports vibration
            if (vibrator.hasVibrator()) {
                // Vibrate for 200 milliseconds
                VibrationEffect vibrationEffect = VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE);
                vibrator.vibrate(vibrationEffect);
                Log.d("Debug", "vibrating");
            }
        }
    }

    // Map braille character to alphabets (lowercase)
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