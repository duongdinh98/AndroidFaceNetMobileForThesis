package org.tensorflow.lite.examples.detection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.examples.detection.tflite.SaveDataSet;

import java.util.HashMap;
import java.util.Set;

public class ScreenSplash extends AppCompatActivity {

    private String convertArrToString(float[] emb) {
        String embArr = "";
        for (float feature : emb) {
            embArr = embArr + feature + "-";
        }

        return embArr;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Button btn_recognize = findViewById(R.id.btn_recognize);
        Button btn_register = findViewById(R.id.btn_register);
        TextView txt_check_hash_map = findViewById(R.id.txt_check_hash_map);

        HashMap<String, float[]> registered = SaveDataSet.deSerializeHashMap();
        if(registered != null) {
            String text = "";

            Set<String> keySet = registered.keySet();
            for (String key : keySet) {
                text = text + key + convertArrToString(registered.get(key));
            }

            txt_check_hash_map.setText(text);
        } else {
            txt_check_hash_map.setText("NULL");
        }

        btn_recognize.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Enter Recognition mode !",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ScreenSplash.this, DetectorActivity.class);
            startActivity(intent);
        });

        btn_register.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Enter Registration mode !",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ScreenSplash.this, DetectorActivity.class);
            intent.putExtra("Mode", true);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
