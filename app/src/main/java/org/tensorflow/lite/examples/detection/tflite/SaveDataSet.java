package org.tensorflow.lite.examples.detection.tflite;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class SaveDataSet {
//    public static void serializeHashMap(String name, float[] emb) {
//        FileOutputStream fstream;
//        HashMap<String, float[]> dataSet = new HashMap<>();
//        dataSet.put(name, emb);
//        try
//        {
//            String root = Environment.getExternalStorageDirectory().toString();
//            File myDir = new File(root, "/LearnerDrivingCentre/EmbeddingsDetail");
//            if (!myDir.exists()) {
//                myDir.mkdirs();
//            }
//            File myFile = new File(myDir,"face_feature_details.ser");
//
//            fstream = new FileOutputStream(myFile);
//            ObjectOutputStream oos = new ObjectOutputStream(fstream);
//            oos.writeObject(dataSet);
//            oos.close();
//            fstream.close();
//
//            Log.d("duong", "face_feature_details.ser saved");
//        }catch(IOException ioe)
//        {
//            ioe.printStackTrace();
//        }
//    }

    public static HashMap<String, float[]> deSerializeHashMap() {
        HashMap<String, float[]> loadedData = new HashMap<>();
        FileInputStream fstream;

        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root, "/LearnerDrivingCentre/EmbeddingsDetail");
            File myFile = new File(myDir,"face_feature_details.ser");

            fstream = new FileInputStream(myFile);
            ObjectInputStream ois = new ObjectInputStream(fstream);
            loadedData = (HashMap<String, float[]>) ois.readObject();
            ois.close();
            fstream.close();

            Log.d("duong", "face_feature_details.ser reloaded");
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

    public static void serializeHashMap(HashMap<String, float[]> dataSet) {
        FileOutputStream fstream;
        try
        {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root, "/LearnerDrivingCentre/EmbeddingsDetail");
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            File myFile = new File(myDir,"face_feature_details.ser");

            fstream = new FileOutputStream(myFile);
            ObjectOutputStream oos = new ObjectOutputStream(fstream);
            oos.writeObject(dataSet);
            oos.close();
            fstream.close();

            Log.d("duong", "face_feature_details.ser saved");
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}
