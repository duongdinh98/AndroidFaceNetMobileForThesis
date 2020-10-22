package org.tensorflow.lite.examples.detection;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Search extends AppCompatActivity {
    Button btnSearch;
    EditText identityInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Window window = Search.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(Search.this, R.color.startGradient));

        btnSearch = findViewById(R.id.btnSearch);
        identityInput = findViewById(R.id.edit_search_cmnd);

        btnSearch.setOnClickListener(view -> {
            String identity = identityInput.getText().toString().trim();
            if (identity.equals("")) {
                Toast.makeText(Search.this, "Nhập số CMND để tìm kiếm", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(Search.this, StudentProfile.class);
                intent.putExtra("identity", identity);
                startActivity(intent);
            }
        });
    }
}