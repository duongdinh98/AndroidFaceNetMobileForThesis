package org.tensorflow.lite.examples.detection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Objects;

public class Search extends AppCompatActivity {
    Button btnSearch;
    TextView txtIdentity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Window window = Search.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(Search.this, R.color.startGradient));

        btnSearch = findViewById(R.id.btnSearch);
        txtIdentity = findViewById(R.id.txt_search_cmnd);

        btnSearch.setOnClickListener(view -> {
            if (txtIdentity.getText().toString().trim().equals("")) {
                Toast.makeText(Search.this, "Nhập mã số CMND", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(Search.this, StudentProfile.class);
                startActivity(intent);
            }
        });
    }
}