package org.tensorflow.lite.examples.detection;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.tensorflow.lite.examples.detection.response.LoginResponse;
import org.tensorflow.lite.examples.detection.tflite.SaveDataSet;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FaceConfirmLogin extends AppCompatActivity {
    Button btnLogin;
    TextView txtName;
    private String teacherName, account;

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        teacherName = intent.getStringExtra("teacherName");
        account = intent.getStringExtra("accountId");
        txtName.setText(teacherName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_confirm_login);

        Window window = FaceConfirmLogin.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(FaceConfirmLogin.this, R.color.b4StartGra));

        btnLogin = findViewById(R.id.btn_send_check_in);
        txtName = findViewById(R.id.txt_card_name);

        btnLogin.setOnClickListener(view -> {
            loginWithFace();
        });
    }

    private void loginWithFace() {
        MyCustomDialog loadingSpinner = new MyCustomDialog(FaceConfirmLogin.this, "Đang xác thực tài khoản...");
        loadingSpinner.startLoadingDialog();

        Retrofit retrofit = APIClient.getClient();
        APIService callAPI = retrofit.create(APIService.class);
        Call<LoginResponse> call = callAPI.loginByFace(account);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()) {
                    String token = response.body().getToken();
                    String teacherName = response.body().getData().getUser().getName();
                    String role = response.body().getData().getUser().getRole();
                    String beLongTo = response.body().getData().getUser().getBeLongTo();

                    if (!role.equals("teacher")) {
                        Toasty.error(FaceConfirmLogin.this, "Phải đăng nhập bằng tài khoản giáo viên", Toast.LENGTH_SHORT, true).show();
                    } else {
                        SaveDataSet.saveToken(FaceConfirmLogin.this, token, teacherName, beLongTo);
                        Toasty.success(FaceConfirmLogin.this, "Đăng nhập thành công", Toast.LENGTH_SHORT, true).show();
                        Intent intent = new Intent(FaceConfirmLogin.this, Profile.class);
                        startActivity(intent);
                        finish();
                    }

                } else {
                    Toasty.error(FaceConfirmLogin.this, "Tài khoản không hợp lệ", Toast.LENGTH_LONG, true).show();
                }
                loadingSpinner.dismissDialog();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loadingSpinner.dismissDialog();
                Toasty.error(FaceConfirmLogin.this, "Lỗi ứng dụng, thử lại", Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}