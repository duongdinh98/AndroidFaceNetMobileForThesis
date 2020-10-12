package org.tensorflow.lite.examples.detection;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Objects;

public class FaceCheckOutConfirm extends AppCompatActivity {
    Button btnCheckOut, btnTryAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_check_out_confirm);

        Window window = FaceCheckOutConfirm.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(FaceCheckOutConfirm.this, R.color.errorColor));

        btnCheckOut = findViewById(R.id.btn_send_check_out);
        btnTryAgain = findViewById(R.id.btn_try_again);

        btnCheckOut.setOnClickListener(view -> {
            MyCustomDialog checkOutDialog = new MyCustomDialog(FaceCheckOutConfirm.this, "Tổng thời gian: 1 giờ 2 phút 30 giây");
            checkOutDialog.startSuccessMakeARollCallDialog();
        });

        btnTryAgain.setOnClickListener(view -> {
            MyCustomDialog checkOutDialog = new MyCustomDialog(FaceCheckOutConfirm.this, "Có lỗi xảy ra, thử lại sau");
            checkOutDialog.startErrorMakeARollCallDialog();

        });
    }
}