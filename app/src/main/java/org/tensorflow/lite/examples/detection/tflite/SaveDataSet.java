package org.tensorflow.lite.examples.detection.tflite;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class SaveDataSet {
    public static HashMap<String, float[]> deSerializeHashMap(String fileName) {
        HashMap<String, float[]> loadedData = new HashMap<>();
        FileInputStream fstream;

        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root, "/LearnerDrivingCentre/EmbeddingsDetail");
            File myFile = new File(myDir,fileName + ".ser");

            fstream = new FileInputStream(myFile);
            ObjectInputStream ois = new ObjectInputStream(fstream);
            loadedData = (HashMap<String, float[]>) ois.readObject();
            ois.close();
            fstream.close();

            Log.d("duong",  fileName +".ser reloaded");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return loadedData;
    }

    public static void saveBitmapToStorage(Bitmap image, String filename) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root, "/LearnerDrivingCentre/AttendanceImages");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        File myFile = new File(myDir,filename);

        try (FileOutputStream out = new FileOutputStream(myFile)) {
            image.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap readBitmapFromStorage(String imageName) {
        Bitmap image = null;
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root, "/LearnerDrivingCentre/AttendanceImages");

        try {
            File myFile = new File(myDir,imageName);
            image = BitmapFactory.decodeStream(new FileInputStream(myFile));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return image;
    }

    public static void serializeHashMap(HashMap<String, float[]> dataSet, String fileName) {
        FileOutputStream fstream;
        try
        {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root, "/LearnerDrivingCentre/EmbeddingsDetail");
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            File myFile = new File(myDir,fileName + ".ser");

            fstream = new FileOutputStream(myFile);
            ObjectOutputStream oos = new ObjectOutputStream(fstream);
            oos.writeObject(dataSet);
            oos.close();
            fstream.close();

            Log.d("duong", fileName + ".ser saved");
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public static String readApiUrl () {
        String url = "";
        try{
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root, "/LearnerDrivingCentre/Config");
            File myFile = new File(myDir,"api_end_point.env");

            StringBuilder text = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(myFile));

            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();

            url = text.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static void saveToken (Context context, String token, String username, String beLongTo) {
        SharedPreferences prefs;
        SharedPreferences.Editor edit;
        prefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        edit=prefs.edit();

        edit.putString("jwt", token);
        edit.putString("teacherName", username);
        edit.putString("beLongTo", beLongTo);
        edit.apply();
    }

    public static String retrieveFromMyPrefs (Context context, String key) {
        SharedPreferences prefs;
        prefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        return prefs.getString(key,"");
    }

    public static void removeFromMyPrefs (Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        prefs.edit().remove(key).apply();
    }

    public static float[] transferStringToEmbedding(String string) {
        // Vector 192 dimensions
        float[] arr = new float[192];

        String[] embeddingsInString = string.split("&");

        for (int i = 0; i < (embeddingsInString.length - 1); i++){
            arr[i] = Float.parseFloat(embeddingsInString[i]);
        }

        return arr;
    }

    public static String convertSecondToTime(int seconds) {
        if (seconds <= 0) {
            return "0 giờ";
        }

        int ss = seconds % 60;
        int hh = seconds / 60;
        int mm = hh % 60;
        hh = hh / 60;

        String hour = "";
        String minute = "";
        String second = "";

        if(hh != 0) {
            hour = hh + " giờ ";
        }

        if(mm != 0) {
            minute = mm + " phút ";
        }

        if(ss != 0) {
            second = ss + " giây";
        }

        return ( hour + minute + second);
    }
}
