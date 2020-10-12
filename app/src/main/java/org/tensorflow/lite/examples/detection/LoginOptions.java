package org.tensorflow.lite.examples.detection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Objects;

public class LoginOptions extends AppCompatActivity {
    View logInAccount, logInFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_options);

        Window window = LoginOptions.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(LoginOptions.this, R.color.blurWhite));

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        logInAccount = findViewById(R.id.viewLoginAccount);
        logInFace = findViewById(R.id.viewLoginFace);

        logInAccount.setOnClickListener(view -> {
            Intent intent = new Intent(LoginOptions.this, Login.class);
            startActivity(intent);
            finish();
        });

        logInFace.setOnClickListener(view -> {
            Intent intent = new Intent(LoginOptions.this, FaceConfirmLogin.class);
            startActivity(intent);
            finish();
        });
    }
}