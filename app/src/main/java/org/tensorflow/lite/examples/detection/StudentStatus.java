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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.lite.examples.detection.response.StudentResponse;
import org.tensorflow.lite.examples.detection.serverdata.StudentAdapter;
import org.tensorflow.lite.examples.detection.serverdata.StudentData;
import org.tensorflow.lite.examples.detection.serverdata.StudentStatusAdapter;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StudentStatus extends AppCompatActivity {
    TextView txtTenLop, txtSiSoLop;
    RecyclerView studentRV;
    Button startDiemDanh;
    String idOfClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_student);

        Window window = StudentStatus.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(StudentStatus.this, R.color.niceGreen));

        studentRV = findViewById(R.id.rvStudent);
        txtTenLop = findViewById(R.id.txt_ten_lop);
        txtSiSoLop = findViewById(R.id.txt_si_so_lop);
        startDiemDanh = findViewById(R.id.btn_start_diem_danh);

        Intent intent = getIntent();
        String classId = intent.getStringExtra("classId");
        idOfClass = classId;
        String className = intent.getStringExtra("className");

        txtTenLop.setText("Lớp: " + className);
        startDiemDanh.setOnClickListener(view -> {
            Intent intentNext = new Intent(StudentStatus.this, RecognitionType.class);
            intentNext.putExtra("classId", classId);
            startActivity(intentNext);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchStudentData(idOfClass);
    }

    private void fetchStudentData (String classId) {
        ArrayList<StudentData> studentData = new ArrayList<>();

        MyCustomDialog loadingSpinner = new MyCustomDialog(StudentStatus.this, "Đang tải dữ liệu học viên...");
        loadingSpinner.startLoadingDialog();

        Retrofit retrofit = APIClient.getClient();
        APIService callAPI = retrofit.create(APIService.class);
        Call<StudentResponse> call = callAPI.getStudentsByClass(classId);
        call.enqueue(new Callback<StudentResponse>() {
            @Override
            public void onResponse(Call<StudentResponse> call, Response<StudentResponse> response) {
                loadingSpinner.dismissDialog();
                if(response.isSuccessful()) {
                    List<StudentResponse.Datum> studentsData = response.body().getData();

                    txtSiSoLop.setText("Sĩ số: " + response.body().getResult().toString());
                    for (StudentResponse.Datum student : studentsData) {
                        studentData.add(new StudentData(student.getId(), student.getTen(), student.getNgaySinh().split("T")[0], student.getCmnd(), student.getSdt()));
                    }
                    StudentStatusAdapter studentAdapter = new StudentStatusAdapter(studentData, StudentStatus.this);
                    studentRV.setLayoutManager(new LinearLayoutManager(StudentStatus.this));
                    studentRV.setAdapter(studentAdapter);
                } else {
                    Toasty.error(StudentStatus.this, "Có lỗi xảy ra, thử lại", Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void onFailure(Call<StudentResponse> call, Throwable t) {
                loadingSpinner.dismissDialog();
                Toasty.error(StudentStatus.this, "Lỗi ứng dụng, thử lại", Toast.LENGTH_SHORT, true).show();
            }
        });
    }

}