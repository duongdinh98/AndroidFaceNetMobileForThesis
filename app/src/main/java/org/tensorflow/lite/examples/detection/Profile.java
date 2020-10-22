package org.tensorflow.lite.examples.detection;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import org.tensorflow.lite.examples.detection.tflite.SaveDataSet;

import java.util.Objects;

public class Profile extends AppCompatActivity {
    CardView cvRollCall, cvTimeTable, cvClassroom, cvLogOut, cvSearch, cvSync;
    TextView txtName, txtRole;

    @Override
    protected void onResume() {
        super.onResume();

        String teacherName = SaveDataSet.retrieveFromMyPrefs(Profile.this, "teacherName");
        txtName.setText(teacherName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Window window = Profile.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(Profile.this, R.color.profilePage));

        txtName = findViewById(R.id.txt_name);
        txtRole = findViewById(R.id.txt_role);

        cvRollCall = findViewById(R.id.cv_roll_call);
        cvRollCall.setOnClickListener(view -> {
            Intent intent = new Intent(Profile.this, Classroom.class);
            intent.putExtra("isAttendance", true);
            startActivity(intent);
        });

        cvTimeTable = findViewById(R.id.cv_time_table);
        cvTimeTable.setOnClickListener(view -> {
            Intent intent = new Intent(Profile.this, TimeTable.class);
            startActivity(intent);
        });

        cvClassroom = findViewById(R.id.cv_classroom);
        cvClassroom.setOnClickListener(view -> {
            Intent intent = new Intent(Profile.this, Classroom.class);
            startActivity(intent);
        });

        cvLogOut = findViewById(R.id.cv_log_out);
        cvLogOut.setOnClickListener(view -> {
            logout();
        });

        cvSearch = findViewById(R.id.cv_search);
        cvSearch.setOnClickListener(view -> {
            Intent intent = new Intent(Profile.this, Search.class);
            startActivity(intent);
        });

        cvSync = findViewById(R.id.cv_sync);
        cvSync.setOnClickListener(view -> Toast.makeText(Profile.this, "Đồng bộ dữ liệu...", Toast.LENGTH_SHORT).show());
    }

    public void logout () {
        AlertDialog alertDialog = new AlertDialog.Builder(Profile.this).create();
        alertDialog.setMessage("Đăng xuất tài khoản khỏi thiết bị này");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ĐĂNG XUẤT", (dialog, which) -> {
            SaveDataSet.removeFromMyPrefs(Profile.this, "jwt");
            SaveDataSet.removeFromMyPrefs(Profile.this, "teacherName");
            SaveDataSet.removeFromMyPrefs(Profile.this, "beLongTo");
            finish();
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "HỦY", (dialog, which) -> {
            alertDialog.dismiss();
        });

        alertDialog.show();
    }
}