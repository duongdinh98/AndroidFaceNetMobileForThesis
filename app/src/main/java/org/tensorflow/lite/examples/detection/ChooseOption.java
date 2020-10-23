package org.tensorflow.lite.examples.detection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.tensorflow.lite.examples.detection.tflite.SaveDataSet;

public class ChooseOption extends AppCompatActivity {
    Button btn_register, btn_reg;
    ImageView imgDhhh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_option);

        Window window = ChooseOption.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(ChooseOption.this, R.color.statusBarChooseOptions));

        btn_register = findViewById(R.id.button);
        btn_reg = findViewById(R.id.button2);
        imgDhhh = findViewById(R.id.imgDhhh);

        btn_reg.setOnClickListener(view -> {
            if(SaveDataSet.retrieveFromMyPrefs(ChooseOption.this, "jwt").equals("")) {
                Intent intent = new Intent(ChooseOption.this, LoginOptions.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(ChooseOption.this, Profile.class);
                startActivity(intent);
            }
        });

        btn_register.setOnClickListener(view -> {
            Intent intent = new Intent(ChooseOption.this, QRResult.class);
            startActivity(intent);
        });

        imgDhhh.setOnClickListener(view -> {

        });
    }
}