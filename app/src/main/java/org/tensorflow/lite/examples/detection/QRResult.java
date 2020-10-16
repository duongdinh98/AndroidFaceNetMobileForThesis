package org.tensorflow.lite.examples.detection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.king.zxing.CaptureActivity;
import com.king.zxing.Intents;

import java.util.Objects;

public class QRResult extends AppCompatActivity {
    TextView idNewFace, cardName, cardInfo;
    Button btnRegister;
    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_result);

        Window window = QRResult.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(QRResult.this, R.color.btnBlue));

        idNewFace = findViewById(R.id.idNewFace);
        btnRegister = findViewById(R.id.btn_start_register);
        cardName = findViewById(R.id.txt_card_name);
        cardInfo = findViewById(R.id.txt_card_info);
        aSwitch = findViewById(R.id.switch_register);
        aSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            btnRegister.setText( aSwitch.isChecked() ? "Đăng kí cho giảng viên" : "Đăng kí cho học viên" );
            cardName.setText( aSwitch.isChecked() ? "Giảng viên mới" : "Học viên mới" );
            cardInfo.setText( aSwitch.isChecked() ? "Giảng viên mới" : "Học viên mới" );
        });

        startActivityForResult(new Intent(QRResult.this, CaptureActivity.class), 5360);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            String result = data.getStringExtra(Intents.Scan.RESULT);
            idNewFace.setText(result);

            btnRegister.setOnClickListener(view -> {
                Intent intent = new Intent(QRResult.this, DetectorActivity.class);
                intent.putExtra("Mode", true);
                intent.putExtra("qrData", result);
                intent.putExtra("registerMode", aSwitch.isChecked());
                startActivity(intent);
                finish();
            });

        } else {
            btnRegister.setEnabled(false);
        }
    }
}