package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class RegisterFaceId extends AppCompatActivity {
    ImageView faceImg;
    EditText inputIdLearner, inputName;
    Button btnRegister, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_face_id);

        faceImg = findViewById(R.id.dlg_image);
        inputIdLearner = findViewById(R.id.dlg_input_idLearner);
        inputName = findViewById(R.id.dlg_input_name);
        btnRegister = findViewById(R.id.btn_register);
        btnCancel = findViewById(R.id.btn_cancel);
    }
}