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

import org.tensorflow.lite.examples.detection.response.LoginResponse;
import org.tensorflow.lite.examples.detection.response.SearchResponse;
import org.tensorflow.lite.examples.detection.tflite.SaveDataSet;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StudentProfile extends AppCompatActivity {
    TextView txtStudentName, txtSoGioDaHoc, txtSoGioPhaiHoc, txtPassLyThuyet,
            txtPassThucHanh, txtLoaiBang, txtTenLop, txtSoDt, txtDob, txtNgayNhapHoc;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        Window window = StudentProfile.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(StudentProfile.this, R.color.niceGreen));

        txtStudentName = findViewById(R.id.txtStudentName);
        txtSoGioDaHoc = findViewById(R.id.txt_so_gio_da_hoc);
        txtSoGioPhaiHoc = findViewById(R.id.txt_so_gio_phai_hoc);
        txtPassLyThuyet = findViewById(R.id.txt_pass_ly_thuyet);
        txtPassThucHanh = findViewById(R.id.txt_pass_thuc_hanh);
        txtLoaiBang = findViewById(R.id.txt_loai_bang);
        txtTenLop = findViewById(R.id.txt_ten_lop);
        txtSoDt = findViewById(R.id.txt_so_dt);
        txtDob = findViewById(R.id.txt_dob);
        txtNgayNhapHoc = findViewById(R.id.txt_ngay_nhap_hoc);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String identity = intent.getStringExtra("identity");
        fetchStudentInfo(identity);
    }

    private void fetchStudentInfo(String identity) {
        MyCustomDialog loadingSpinner = new MyCustomDialog(StudentProfile.this, "Đang tìm kiếm thông tin học viên...");
        loadingSpinner.startLoadingDialog();

        Retrofit retrofit = APIClient.getClient();
        APIService callAPI = retrofit.create(APIService.class);
        Call<SearchResponse> call = callAPI.getSearchStudentData(identity);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                loadingSpinner.dismissDialog();
                if(response.isSuccessful()) {
                    String name = response.body().getData().getTen();
                    int totalTime = response.body().getTotalTime();

                    int requiredTime;
                    String loaiBang;
                    String tenLop;
                    try {
                        tenLop = response.body().getData().getIdLop().getTenLop();
                        requiredTime = response.body().getData().getIdLop().getIdLoaiBang().getThoiGianHoc();
                        loaiBang = response.body().getData().getIdLop().getIdLoaiBang().getTenBang();
                    } catch (Exception e) {
                        requiredTime = 0;
                        loaiBang = "chưa có";
                        tenLop = "chưa có";
                    }

                    boolean isPassLyThuyet = response.body().getData().getIsPassLyThuyet();
                    String sdt = response.body().getData().getSdt();
                    String dob = response.body().getData().getNgaySinh().split("T")[0];
                    String ngayNhapHoc = response.body().getData().getNgayTao().split("T")[0];

                    txtStudentName.setText(name);
                    txtSoGioDaHoc.setText("Đã lái xe " + SaveDataSet.convertSecondToTime(totalTime));
                    txtSoGioPhaiHoc.setText("Tiên quyết " + requiredTime + " giờ");
                    txtPassLyThuyet.setText(isPassLyThuyet ? "Đạt lý thuyết" : "Chưa đạt lý thuyết");
                    txtPassThucHanh.setText(totalTime > requiredTime * 3600 ? "Đạt thực hành" : "Chưa đạt thực hành");
                    txtLoaiBang.setText("Loại bằng " + loaiBang);
                    txtTenLop.setText("Lớp " + tenLop);
                    txtSoDt.setText("Điện thoại " + sdt);
                    txtDob.setText("Sinh ngày " + dob);
                    txtNgayNhapHoc.setText("Nhập học " + ngayNhapHoc);

                } else {
                    Toasty.error(StudentProfile.this, "Không tìm thấy học viên với mã " + identity, Toast.LENGTH_SHORT, true).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                loadingSpinner.dismissDialog();
                Toasty.error(StudentProfile.this, "Lỗi ứng dụng, thử lại", Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}