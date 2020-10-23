package org.tensorflow.lite.examples.detection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.tensorflow.lite.examples.detection.response.StudentEmbeddingResponse;
import org.tensorflow.lite.examples.detection.response.TeacherEmbeddingResponse;
import org.tensorflow.lite.examples.detection.tflite.SaveDataSet;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecognitionType extends AppCompatActivity {
    View vCheckIn, vCheckOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recogition_type);

        Window window = RecognitionType.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(RecognitionType.this, R.color.blurWhite));

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        vCheckIn = findViewById(R.id.viewLoginAccount);
        vCheckOut = findViewById(R.id.viewLoginFace);

        Intent intentPrevious = getIntent();
        String classId = intentPrevious.getStringExtra("classId");
        fetchStudentEmbeddingsData(classId);
    }

    private void setWhenFetchFail() {
        vCheckIn.setOnClickListener(view -> {
            Toast.makeText(RecognitionType.this, "Không có khuôn mặt nào được đăng kí", Toast.LENGTH_LONG).show();
        });
        vCheckOut.setOnClickListener(view -> {
            Toast.makeText(RecognitionType.this, "Không có khuôn mặt nào được đăng kí", Toast.LENGTH_LONG).show();
        });
    }

    private void fetchStudentEmbeddingsData(String classId) {
        MyCustomDialog loadingSpinner = new MyCustomDialog(RecognitionType.this, "Chuẩn bị dữ liệu khuôn mặt...");
        loadingSpinner.startLoadingDialog();

        Retrofit retrofit = APIClient.getClient();
        APIService callAPI = retrofit.create(APIService.class);
        Call<StudentEmbeddingResponse> call = callAPI.getStudentEmbeddingsData(classId);
        call.enqueue(new Callback<StudentEmbeddingResponse>() {
            @Override
            public void onResponse(Call<StudentEmbeddingResponse> call, Response<StudentEmbeddingResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResult() == 0 ) {
                        loadingSpinner.dismissDialog();
                        setWhenFetchFail();
                    } else {
                        HashMap<String, float[]> registeredStudents = new HashMap<>();
                        List<StudentEmbeddingResponse.Datum> data = response.body().getData();

                        for (StudentEmbeddingResponse.Datum student : data) {
                            String extraData = student.getTen() + "&" + student.getId();
                            float[] embeddings = SaveDataSet.transferStringToEmbedding(student.getEmbedding());

                            registeredStudents.put(extraData, embeddings);
                        }
                        SaveDataSet.serializeHashMap(registeredStudents, classId);
                        loadingSpinner.dismissDialog();
                        vCheckIn.setOnClickListener(view -> {
                            Intent intent = new Intent(RecognitionType.this, DetectorActivity.class);
                            intent.putExtra("Mode", false);
                            intent.putExtra("faceData", classId);
                            intent.putExtra("isForLogIn", false);
                            intent.putExtra("isCheckIn", true);
                            startActivity(intent);
                        });

                        vCheckOut.setOnClickListener(view -> {
                            Intent intent = new Intent(RecognitionType.this, DetectorActivity.class);
                            intent.putExtra("Mode", false);
                            intent.putExtra("faceData", classId);
                            intent.putExtra("isForLogIn", false);
                            intent.putExtra("isCheckIn", false);
                            startActivity(intent);
                        });
                    }
                } else {
                    loadingSpinner.dismissDialog();
                    Toast.makeText(RecognitionType.this, "Lấy dữ liệu khuôn mặt thất bại", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<StudentEmbeddingResponse> call, Throwable t) {
                loadingSpinner.dismissDialog();
            }
        });
    }
}