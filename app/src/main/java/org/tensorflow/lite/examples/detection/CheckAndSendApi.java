package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CheckAndSendApi extends AppCompatActivity {
    TextView name;
    TextView distance;
    ImageView face;
    Button btn_try_again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_and_send_api);

        name = findViewById(R.id.txt_name);
        distance = findViewById(R.id.txt_distance);
        face = findViewById(R.id.img_face_cropped);
        btn_try_again = findViewById(R.id.btn_try_again);

        btn_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String nameI = intent.getStringExtra("NameDetected");
        float distanceI = intent.getFloatExtra("ConfidenceDetected", (float) 0.0);
        Bitmap faceI = intent.getParcelableExtra("FaceDetected");
        if(faceI != null) {
            face.setImageBitmap(faceI);
        } else {
            face.setImageResource(R.drawable.ic_launcher);
        }

        name.setText(nameI);
        distance.setText(String.valueOf(distanceI));
    }
}