package org.tensorflow.lite.examples.detection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.tensorflow.lite.examples.detection.response.LoginResponse;
import org.tensorflow.lite.examples.detection.response.TeacherEmbeddingResponse;
import org.tensorflow.lite.examples.detection.tflite.SaveDataSet;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
            intent.putExtra("login-from", "teacher");
            startActivity(intent);
            finish();
        });

        logInFace.setOnClickListener(view -> {
            getTeacherFaceEmbedding();
        });
    }

    private void getTeacherFaceEmbedding() {
        MyCustomDialog loadingSpinner = new MyCustomDialog(LoginOptions.this, "Đang chuẩn bị dữ liệu khuôn mặt...");
        loadingSpinner.startLoadingDialog();

        Retrofit retrofit = APIClient.getClient();
        APIService callAPI = retrofit.create(APIService.class);
        Call<TeacherEmbeddingResponse> call = callAPI.getTeacherEmbeddingsData();
        call.enqueue(new Callback<TeacherEmbeddingResponse>() {
            @Override
            public void onResponse(Call<TeacherEmbeddingResponse> call, Response<TeacherEmbeddingResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResult() == 0 ) {
                        loadingSpinner.dismissDialog();
                        Toasty.warning(LoginOptions.this, "Không có khuôn mặt nào được đăng kí", Toast.LENGTH_SHORT, true).show();
                    } else {
                        HashMap<String, float[]> registeredTeacher = new HashMap<>();
                        List<TeacherEmbeddingResponse.Datum> data = response.body().getData();

                        for (TeacherEmbeddingResponse.Datum faceIdDetails : data) {
                            if (faceIdDetails.getAccount() == null || faceIdDetails.getEmbedding() == null) continue;
                            String extraData = faceIdDetails.getTen() + "&" + faceIdDetails.getAccount();
                            float[] embeddings = SaveDataSet.transferStringToEmbedding(faceIdDetails.getEmbedding());

                            registeredTeacher.put(extraData, embeddings);
                        }
                        SaveDataSet.serializeHashMap(registeredTeacher, "teacher_embeddings");
                        loadingSpinner.dismissDialog();

                        Intent intent = new Intent(LoginOptions.this, DetectorActivity.class);
                        intent.putExtra("Mode", false);
                        intent.putExtra("faceData", "teacher_embeddings");
                        intent.putExtra("isForLogIn", true);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    loadingSpinner.dismissDialog();
                    Toast.makeText(LoginOptions.this, "Lấy dữ liệu khuôn mặt thất bại", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TeacherEmbeddingResponse> call, Throwable t) {
                loadingSpinner.dismissDialog();
                Toasty.error(LoginOptions.this, "Lỗi ứng dụng, thử lại", Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}