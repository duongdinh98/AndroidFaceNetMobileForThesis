package org.tensorflow.lite.examples.detection.tflite;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class SaveDataSet {
    public static void serializeHashMap(String name, float[] emb) {
        FileOutputStream fstream;
        HashMap<String, float[]> dataSet = new HashMap<>();
        dataSet.put(name, emb);
        try
        {
            // TODO fix path
            File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File myFile = new File(folder,"face_recognition_details.ser");

//            String root = Environment.getExternalStorageDirectory().toString();
//            File myDir = new File(root, "/attendance_data");
//            File myFile = new File(myDir,"face_recognition_details.ser");

//            File folder = Environment.getExternalStoragePublicDirectory(Environment.getExternalStorageDirectory().toString() + "/attendance_data");
//            File myFile = new File(folder,"face_recognition_details.ser");

            fstream = new FileOutputStream(myFile);
            ObjectOutputStream oos = new ObjectOutputStream(fstream);
            oos.writeObject(dataSet);
            oos.close();
            fstream.close();

            Log.d("duong", "face_recognition_details.ser saved");
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public static HashMap<String, float[]> deSerializeHashMap() {
        HashMap<String, float[]> loadedData = null;
        FileInputStream fstream;

        try {
            File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File myFile = new File(folder,"face_recognition_details.ser");

//            String root = Environment.getExternalStorageDirectory().toString();
//            File myDir = new File(root, "/attendance_data");
//            File myFile = new File(myDir,"face_recognition_details.ser");

//            File folder = Environment.getExternalStoragePublicDirectory(Environment.getExternalStorageDirectory().toString() + "/attendance_data");
//            File myFile = new File(folder,"face_recognition_details.ser");

            fstream = new FileInputStream(myFile);
            ObjectInputStream ois = new ObjectInputStream(fstream);
            loadedData = (HashMap<String, float[]>) ois.readObject();
            ois.close();
            fstream.close();

            Log.d("duong", "face_recognition_details.ser reloaded");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return loadedData;
    }
}
