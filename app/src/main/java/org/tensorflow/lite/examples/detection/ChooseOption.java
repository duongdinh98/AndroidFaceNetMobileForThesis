package org.tensorflow.lite.examples.detection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.tensorflow.lite.examples.detection.response.CheckConnectionResponse;
import org.tensorflow.lite.examples.detection.response.TeacherEmbeddingResponse;
import org.tensorflow.lite.examples.detection.tflite.SaveDataSet;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChooseOption extends AppCompatActivity {
    Button btn_register, btn_reg;
    ImageView imgDhhh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_option);

        Window window = ChooseOption.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(ChooseOption.this, R.color.statusBarChooseOptions));

        btn_register = findViewById(R.id.button);
        btn_reg = findViewById(R.id.button2);
        imgDhhh = findViewById(R.id.imgDhhh);

        imgDhhh.setOnClickListener(view -> {

        });
        checkServerConnection();
    }

    private void onFailCheckConnection() {
        Toasty.error(ChooseOption.this, "Lỗi kết nối tới máy chủ", Toast.LENGTH_SHORT, true).show();
        btn_reg.setOnClickListener(view -> {
            Toasty.error(ChooseOption.this, "Lỗi kết nối tới máy chủ", Toast.LENGTH_SHORT, true).show();
        });
        btn_register.setOnClickListener(view -> {
            Toasty.error(ChooseOption.this, "Lỗi kết nối tới máy chủ", Toast.LENGTH_SHORT, true).show();
        });
    }

    private void checkServerConnection() {
        Retrofit retrofit = APIClient.getClient();
        APIService callAPI = retrofit.create(APIService.class);
        Call<CheckConnectionResponse> call = callAPI.checkConnection();
        call.enqueue(new Callback<CheckConnectionResponse>() {
            @Override
            public void onResponse(Call<CheckConnectionResponse> call, Response<CheckConnectionResponse> response) {
                if (response.isSuccessful()) {
                    Toasty.success(ChooseOption.this, "Kết nối tới máy chủ", Toast.LENGTH_SHORT, true).show();
                    btn_reg.setOnClickListener(view -> {
                        if(SaveDataSet.retrieveFromMyPrefs(ChooseOption.this, "jwt").equals("")) {
                            Intent intent = new Intent(ChooseOption.this, LoginOptions.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(ChooseOption.this, Profile.class);
                            startActivity(intent);
                        }
                    });

                    btn_register.setOnClickListener(view -> {
                        Intent intent = new Intent(ChooseOption.this, QRResult.class);
                        startActivity(intent);
                    });
                } else {
                    onFailCheckConnection();
                }
            }

            @Override
            public void onFailure(Call<CheckConnectionResponse> call, Throwable t) {
                onFailCheckConnection();
            }
        });

    }
}