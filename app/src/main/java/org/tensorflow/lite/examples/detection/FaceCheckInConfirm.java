package org.tensorflow.lite.examples.detection;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Objects;

public class FaceCheckInConfirm extends AppCompatActivity {

    Button button, btnSendAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_check_in_confirm);

        Window window = FaceCheckInConfirm.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(FaceCheckInConfirm.this, R.color.btnBlue));

        button = findViewById(R.id.btn_try_again);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(this, UnknownFace.class);
            startActivity(intent);
        });
        btnSendAPI = findViewById(R.id.btn_send_check_out);
        btnSendAPI.setOnClickListener(view -> {
            MyCustomDialog successCheckIn = new MyCustomDialog(FaceCheckInConfirm.this, "Check in thành công !");
            successCheckIn.startSuccessMakeARollCallDialog();
        });
    }
}