package org.tensorflow.lite.examples.detection;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Objects;

public class Splash extends AppCompatActivity {
    FaceCheckHelper faceCheckHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Window window = Splash.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(Splash.this, R.color.blurWhite));

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        initSQLite();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(Splash.this, ChooseOption.class);
            startActivity(intent);
            finish();
        }, 2000);
    }

    private void initSQLite() {
        // Create database
        faceCheckHelper = new FaceCheckHelper(Splash.this, "hnd_data.sqlite", null, 1);

        // Create table
        String create_attendance_table_sql = "CREATE TABLE IF NOT EXISTS attendance (\n" +
                "\tid INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                " \tidCheckIn TEXT,\n" +
                " \tidHocVien TEXT\n" +
                ")";
        faceCheckHelper.queryData(create_attendance_table_sql);
    }
}