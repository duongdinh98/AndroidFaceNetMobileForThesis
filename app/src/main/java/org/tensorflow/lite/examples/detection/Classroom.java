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
import org.tensorflow.lite.examples.detection.serverdata.ClassroomAdapter;
import org.tensorflow.lite.examples.detection.serverdata.ClassroomData;
import org.tensorflow.lite.examples.detection.tflite.SaveDataSet;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Classroom extends AppCompatActivity {
    RecyclerView classroomRv;
    TextView txtSoLuongLop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);

        Window window = Classroom.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(Classroom.this, R.color.niceGreen));

        classroomRv = findViewById(R.id.rvClassroom);
        txtSoLuongLop = findViewById(R.id.txt_so_luong_lop);

        Intent intent = getIntent();
        boolean isAttendance = intent.getBooleanExtra("isAttendance", false);

        fetchClassroomData(isAttendance);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void fetchClassroomData (boolean isAttendance) {
        ArrayList<ClassroomData> classData = new ArrayList<>();

        MyCustomDialog loadingSpinner = new MyCustomDialog(Classroom.this, "Đang tải dữ liệu lớp...");
        loadingSpinner.startLoadingDialog();

        Retrofit retrofit = APIClient.getClient();
        APIService callAPI = retrofit.create(APIService.class);
        Call<ClassroomResponse> call = callAPI.getClassesByTeacher(SaveDataSet.retrieveFromMyPrefs(Classroom.this,"beLongTo"));
        call.enqueue(new Callback<ClassroomResponse>() {
            @Override
            public void onResponse(Call<ClassroomResponse> call, Response<ClassroomResponse> response) {
                loadingSpinner.dismissDialog();
                if(response.isSuccessful()) {
                    txtSoLuongLop.setText("Số lượng: " + response.body().getResult().toString());
                    List<ClassroomResponse.Datum> classroomData = response.body().getData();

                    for (ClassroomResponse.Datum classroom : classroomData) {
                        classData.add(new ClassroomData(classroom.getId(), classroom.getTenLop(), classroom.getKhaiGiang().split("T")[0], classroom.getIdLoaiBang().getTenBang(), classroom.getIdLoaiBang().getThoiGianHoc().toString()));
                    }
                    ClassroomAdapter classroomAdapter = new ClassroomAdapter(classData, Classroom.this, isAttendance);
                    classroomRv.setLayoutManager(new LinearLayoutManager(Classroom.this));
                    classroomRv.setAdapter(classroomAdapter);
                } else {
                    Toasty.error(Classroom.this, "Có lỗi xảy ra, thử lại", Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void onFailure(Call<ClassroomResponse> call, Throwable t) {
                loadingSpinner.dismissDialog();
                Toasty.error(Classroom.this, "Lỗi ứng dụng, thử lại", Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}