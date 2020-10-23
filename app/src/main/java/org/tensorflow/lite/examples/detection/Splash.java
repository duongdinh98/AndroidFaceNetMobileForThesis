package org.tensorflow.lite.examples.detection;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class Splash extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static final int RC_CAMERA_AND_STORAGE = 1998;
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

        initMyApp();

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_CAMERA_AND_STORAGE)
    private void initMyApp() {
        String[] perms = {Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(this, perms)) {
            initSQLite();

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent intent = new Intent(Splash.this, ChooseOption.class);
                startActivity(intent);
                finish();
            }, 2000);
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(this, RC_CAMERA_AND_STORAGE, perms)
                            .setRationale("Ứng dụng cần bạn cấp quyền để hoạt động")
                            .setPositiveButtonText("ĐỒNG Ý")
                            .setNegativeButtonText("TỪ CHỐI VÀ THOÁT")
                            .build());
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        finish();
    }
}