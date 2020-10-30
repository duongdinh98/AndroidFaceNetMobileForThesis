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

import org.tensorflow.lite.examples.detection.response.StudentResponse;
import org.tensorflow.lite.examples.detection.serverdata.StudentAdapter;
import org.tensorflow.lite.examples.detection.serverdata.StudentData;
import org.tensorflow.lite.examples.detection.serverdata.StudentLocationAdapter;
import org.tensorflow.lite.examples.detection.serverdata.StudentLocationData;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StudentLocation extends AppCompatActivity {
    TextView txtTenLop;
    RecyclerView rvStudentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_location);

        Window window = StudentLocation.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(StudentLocation.this, R.color.blueViolet));

        rvStudentLocation = findViewById(R.id.rv_student_location);
        txtTenLop = findViewById(R.id.btn_text_ten_lop);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String classId = intent.getStringExtra("classId");
        String className = intent.getStringExtra("className");
        txtTenLop.setText("Lớp " + className);
        fetchStudentData(classId);
    }

    private void fetchStudentData (String classId) {
        ArrayList<StudentLocationData> studentData = new ArrayList<>();

        MyCustomDialog loadingSpinner = new MyCustomDialog(StudentLocation.this, "Đang tải dữ liệu học viên...");
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

                    if (studentsData.size() > 0) {
                        for (StudentResponse.Datum student : studentsData) {
                            studentData.add(new StudentLocationData(student.getId(), student.getTen(), student.getNgaySinh().split("T")[0], student.getCmnd(), student.getSdt(), student.getPickUpLocation()));
                        }
                        StudentLocationAdapter studentAdapter = new StudentLocationAdapter(studentData, StudentLocation.this);
                        rvStudentLocation.setLayoutManager(new LinearLayoutManager(StudentLocation.this));
                        rvStudentLocation.setAdapter(studentAdapter);
                    } else {
                        Toasty.info(StudentLocation.this, "Lớp không có học viên", Toast.LENGTH_LONG, true).show();
                    }
                } else {
                    Toasty.error(StudentLocation.this, "Có lỗi xảy ra, thử lại", Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void onFailure(Call<StudentResponse> call, Throwable t) {
                loadingSpinner.dismissDialog();
                Toasty.error(StudentLocation.this, "Lỗi ứng dụng, thử lại", Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}