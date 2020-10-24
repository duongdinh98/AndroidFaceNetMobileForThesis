package org.tensorflow.lite.examples.detection;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.lite.examples.detection.response.ClassroomResponse;
import org.tensorflow.lite.examples.detection.response.StudentResponse;
import org.tensorflow.lite.examples.detection.serverdata.ClassroomAdapter;
import org.tensorflow.lite.examples.detection.serverdata.ClassroomData;
import org.tensorflow.lite.examples.detection.serverdata.StudentAdapter;
import org.tensorflow.lite.examples.detection.serverdata.StudentData;
import org.tensorflow.lite.examples.detection.tflite.SaveDataSet;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Student extends AppCompatActivity {
    TextView txtTenLop, txtSiSoLop;
    RecyclerView studentRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        Window window = Student.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(Student.this, R.color.niceGreen));

        studentRV = findViewById(R.id.rvStudent);
        txtTenLop = findViewById(R.id.txt_ten_lop);
        txtSiSoLop = findViewById(R.id.txt_si_so_lop);

        Intent intent = getIntent();
        String classId = intent.getStringExtra("classId");
        String className = intent.getStringExtra("className");
        txtTenLop.setText("Lớp: " + className);
        fetchStudentData(classId);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void fetchStudentData (String classId) {
        ArrayList<StudentData> studentData = new ArrayList<>();

        MyCustomDialog loadingSpinner = new MyCustomDialog(Student.this, "Đang tải dữ liệu học viên...");
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
                        studentData.add(new StudentData(student.getId(), student.getTen(), student.getNgaySinh().split("T")[0], student.getCmnd()));
                    }
                    StudentAdapter studentAdapter = new StudentAdapter(studentData, Student.this);
                    studentRV.setLayoutManager(new LinearLayoutManager(Student.this));
                    studentRV.setAdapter(studentAdapter);
                } else {
                    Toasty.error(Student.this, "Có lỗi xảy ra, thử lại", Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void onFailure(Call<StudentResponse> call, Throwable t) {
                loadingSpinner.dismissDialog();
                Toasty.error(Student.this, "Lỗi ứng dụng, thử lại", Toast.LENGTH_SHORT, true).show();
            }
        });
    }

}