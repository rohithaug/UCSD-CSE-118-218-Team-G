package com.example.watchapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.watchapp.model.User;
import com.example.watchapp.model.UserId;
import com.example.watchapp.restapi.RestAPIClient;
import com.example.watchapp.restapi.RestAPIService;
import com.example.watchapp.util.FileUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrailleUser extends AppCompatActivity {
    private static final String TAG = "BrailleFrom";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.braille_msg_send);

        Button dot1 = findViewById(R.id.dot1);
        Button dot2 = findViewById(R.id.dot2);
        Button dot3 = findViewById(R.id.dot3);
        Button dot4 = findViewById(R.id.dot4);
        Button dot5 = findViewById(R.id.dot5);
        Button dot6 = findViewById(R.id.dot6);

        Intent intent = getIntent();
        RestAPIService service = RestAPIClient.getClient().create(RestAPIService.class);

        StringBuilder brailleDotsName = new StringBuilder("");
        StringBuilder brailleDots = new StringBuilder("000000");
        StringBuilder brailleIndicator = new StringBuilder("alphabet");

        Handler handler = new Handler();

        List<Map<String, Map<String, String>>> brailleMap = loadJSON(this, "brailleMap.json");
        Map<String, String> brailleToAlphabet = brailleMap.get(0).get("alphabet");
        Map<String, String> brailleToNumber = brailleMap.get(1).get("number");

        dot1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // dot1 clicked, store this info
                brailleDots.setCharAt(0, '1');
                Log.d("Debug", "Dot1 clicked");
                return true;
            }
        });

        dot2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // dot1 clicked, store this info
                brailleDots.setCharAt(1, '1');
                Log.d("Debug", "Dot2 clicked");
                return true;
            }
        });

        dot3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // dot1 clicked, store this info
                brailleDots.setCharAt(2, '1');
                Log.d("Debug", "Dot3 clicked");
                return true;
            }
        });

        dot4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // dot1 clicked, store this info
                brailleDots.setCharAt(3, '1');
                Log.d("Debug", "Dot4 clicked");
                return true;
            }
        });

        dot5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // dot1 clicked, store this info
                brailleDots.setCharAt(4, '1');
                Log.d("Debug", "Dot5 clicked");
                return true;
            }
        });

        dot6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // dot1 clicked, store this info
                brailleDots.setCharAt(5, '1');
                Log.d("Debug", "Dot6 clicked");
                return true;
            }
        });

        // clear whole name
        dot1.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {

            }

            @Override
            public void onDoubleClick(View v) {
                // clear whole sentence
                brailleDots.setLength(0);
                brailleDots.append("000000");
                Log.d("Debug", "Double-tap on Dot3. BrailleDots is reset to " + brailleDots.toString());

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

        // double click dot4 to finish typing user name
        dot4.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {

            }

            @Override
            public void onDoubleClick(View v) {
                String userName = brailleDotsName.toString();
                Log.d("Debug", "Name: " + userName);

                // Reset the brailleDots and brailleDotsName
                brailleDots.setLength(0);
                brailleDots.append("000000");
                brailleDotsName.setLength(0);
                Log.d("Debug", "Reset: dots - " + brailleDots.toString() + ", name - " + brailleDotsName.toString());

                Call<UserId> call = service.getUserIdFromName(userName);
                call.enqueue(new Callback<UserId>() {
                    @Override
                    public void onResponse(Call<UserId> call, Response<UserId> response) {
                        if (response.isSuccessful()) {
                            // User name exists
                            vibrateWatch(200);
                            UserId userId = response.body();
                            String id = userId.userId;
                            Log.d(TAG, "UserId for userName " + userName + ": " + id);

                            //Intent nextIntent = intent.getBooleanExtra("send", true)
                            //        ? new Intent(BrailleUser.this, BrailleMessageSend.class)
                            //        : new Intent(BrailleUser.this, BrailleMessageReceive.class);

                            Intent nextIntent = new Intent(BrailleUser.this, BrailleMessageSend.class);
                            nextIntent.putExtra("id", id);
                            nextIntent.putExtra("userName", userName);

                            startActivity(nextIntent);
                        } else {
                            // User name does not exist
                            vibrateWatch(50);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Vibrate second time
                                    vibrateWatch(50);
                                }
                            }, 100);
                            Log.d(TAG, "Error getting userId: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserId> call, Throwable t) {
                        // network errors
                        vibrateWatch(50);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Vibrate second time
                                vibrateWatch(50);
                            }
                        }, 100);
                        Log.e(TAG, "Error getting userId: " + t.getMessage(), t);
                    }
                });

            }

        });

        dot3.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {

            }

            @Override
            public void onDoubleClick(View v) {
                // clear current letter
                // clear the dots clicked so far
                brailleDots.setLength(0);
                brailleDots.append("000000");
                Log.d("Debug", "Double-tap on Dot3. BrailleDots is reset to " + brailleDots.toString());

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

        dot6.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {

            }

            @Override
            public void onDoubleClick(View v) {
                // Register a letter
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
                brailleDotsName.append(letter);
                vibrateWatch(200);
                // Reset the brailleDots
                brailleDots.setLength(0);
                brailleDots.append("000000");
                Log.d("Debug", "Reset: " + brailleDots.toString());

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

    public abstract class DoubleClickListener implements View.OnClickListener {

        private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds

        long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long clickTime = System.currentTimeMillis();
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
                onDoubleClick(v);
                lastClickTime = 0;
            } else {
                onSingleClick(v);
            }
            lastClickTime = clickTime;
        }

        public abstract void onSingleClick(View v);
        public abstract void onDoubleClick(View v);
    }
}
