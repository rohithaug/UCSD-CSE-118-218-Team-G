package com.example.watchapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.watchapp.model.UserMessage;
import com.example.watchapp.restapi.RestAPIClient;
import com.example.watchapp.restapi.RestAPIService;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrailleMessageReceive extends Activity {

    private static final String TAG = "BrailleMessageReceive";
    private int i;
    private GestureDetector gestureDetectorDot3;
    private GestureDetector gestureDetectorDot6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.braille_msg_receive);

        // Reference buttons from the layout
        Button dot1 = findViewById(R.id.dot1);
        Button dot2 = findViewById(R.id.dot2);
        Button dot3 = findViewById(R.id.dot3);
        Button dot4 = findViewById(R.id.dot4);
        Button dot5 = findViewById(R.id.dot5);
        Button dot6 = findViewById(R.id.dot6);


        //APP_TEMP START
        String user = "abcd";
        String sentence = user + ":" + "ace";
        StringBuilder brailleDots = new StringBuilder("");
        //APP_TEMP END

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
            Log.d(TAG, String.valueOf(letter));
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
        }

        String brailleDotsSentence = brailleDots.toString();
        Log.d(TAG, "Sentence into braille dots:" + brailleDotsSentence);

        i = 0;
        /*
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

         */

        dot1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return handleDotClick(brailleDotsSentence, i);
            }
        });

        dot2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return handleDotClick(brailleDotsSentence, i + 1);
            }
        });

        dot3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return handleDotClick(brailleDotsSentence, i + 2);
            }
        });

        dot4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return handleDotClick(brailleDotsSentence, i + 3);
            }
        });

        dot5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return handleDotClick(brailleDotsSentence, i + 4);
            }
        });

        dot6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return handleDotClick(brailleDotsSentence, i + 5);
            }
        });

        dot3.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {

            }

            @Override
            public void onDoubleClick(View v) {
                // Move to prev letter
                if (i - 6 >= 0) {
                    i -= 6;
                    vibrateWatch(200); // vibrates when prev letter exists (otherwise nothing happens)
                }
                Log.d(TAG, "Double-tap detected on the dot3");
            }
        });

        dot6.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {

            }

            @Override
            public void onDoubleClick(View v) {
                // Move to prev letter
                if (i + 6 < sentence.length()) {
                    i += 6;
                    vibrateWatch(200); // vibrates when next letter exists (otherwise nothing happens)
                }
                Log.d(TAG, "Double-tap detected on the dot6");
            }
        });

        /*
        gestureDetectorDot3 = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                // Move to prev letter
                i -= 6;
                vibrateWatch(200);
                Log.d(TAG, "Double-tap detected on the dot3");
                return true;
            }
        });



        dot3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetectorDot3.onTouchEvent(event);
                return true;
            }
        });

        gestureDetectorDot6 = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                // Move to next letter
                i += 6;
                vibrateWatch(200);
                Log.d(TAG, "Double-tap detected on the dot6");
                return true;
            }
        });

        dot6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetectorDot6.onTouchEvent(event);
                return true;
            }
        });

         */

        //dot6.setOnLongClickListener(new View.OnLongClickListener() {
        //    public boolean onLongClick(View view) {
        //        // Move to next letter
        //        i += 6;
        //        vibrateWatch(200);
        //        return true;
        //    }
        //});


        /*
        rootLayout.setOnTouchListener(new OnSwipeTouchListener(BrailleMessageReceive.this) {
            // next letter
            @Override
            public void onSwipeLeft() {
                // Move to next letter
                i += 6;
                vibrateWatch(200);
                Log.d(TAG, "Move to next letter");
            }

            // prev letter
            @Override
            public void onSwipeTop() {
                // Move to previous letter
                i -= 6;
                vibrateWatch(200);
                Log.d(TAG, "Move to prev letter");
            }
        });

         */
    }

    private boolean handleDotClick(String sentence, int index) {
        if (index < sentence.length() && sentence.charAt(index) == '1') {
            Log.d(TAG, "Dot clicked and vibrate");
            // vibrate to let the user know that the button has been clicked
            //vibrateWatch(50);
            return true;
        } else {
            Log.d(TAG, "Dot clicked but nothing happens");
            return false;
        }
    }

    private void vibrateWatch(int duration) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            // Check if the device supports vibration
            if (vibrator.hasVibrator()) {
                VibrationEffect vibrationEffect = VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE);
                vibrator.vibrate(vibrationEffect);
                Log.d(TAG, "vibrating");
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




    /*
    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context context) {
            gestureDetector = new GestureDetector(context, new GestureListener());
        }

        public void onSwipeLeft() {
        }

        public void onSwipeRight() {
        }

        public void onSwipeTop() {
        }

        public void onSwipeBottom() {

        }

        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_DISTANCE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.d(TAG, "onfling called");
                if (e1 != null && e2 != null) {
                    float distanceX = e2.getX() - e1.getX();
                    float distanceY = e2.getY() - e1.getY();
                    if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (distanceX > 0) {
                            Log.d(TAG, "Swipe right detected");
                            onSwipeRight();
                        } else {
                            Log.d(TAG, "Swipe left detected");
                            onSwipeLeft();
                        }
                        return true;
                    } else if (Math.abs(distanceY) > Math.abs(distanceX) && Math.abs(distanceY) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (distanceY > 0) {
                            Log.d(TAG, "Swipe bottom detected");
                            onSwipeBottom();
                        } else {
                            Log.d(TAG, "Swipe top detected");
                            onSwipeTop();
                        }
                        return true;
                    }
                }
                return false;
            }
        }
    }

     */

}
