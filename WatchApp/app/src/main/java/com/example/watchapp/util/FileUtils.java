package com.example.watchapp.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {
    private static final String TAG = "WatchAppFileUtils";
    private static final String userFileName = "user_details.txt";
    public static String readUserDetails(File path) {
        Log.d(TAG, "readUserDetails : " +path.getPath());
        File file = new File(path, userFileName);
        FileInputStream is = null;
        String line = "";
        if (file.exists()) {
            try {
                is = new FileInputStream(file);
            } catch(FileNotFoundException fe) {
                Log.d(TAG, "file not found!");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            try {
                line = reader.readLine();
                Log.d(TAG, "readUserDetails: <line> : " + line);
            } catch(IOException e) {
                Log.d(TAG, "readLine error");
            }
        }
        return line;
    }

    public static void saveUserId(File path, String userId) {
        Log.d(TAG, "saveUserId: " + path.getPath() + " , " + userId);
        File file = new File(path, userFileName);
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
        } catch (FileNotFoundException fe) {
            Log.d(TAG, "file not found!");
        }
        try {
            stream.write(userId.getBytes());
            stream.close();
        } catch (IOException e) {
            Log.d(TAG, "error writing userId!");
        } finally {
            //
        }
    }
}
