package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.lite.examples.detection.response.StudentResponse;
import org.tensorflow.lite.examples.detection.response.UpdateLocationResponse;
import org.tensorflow.lite.examples.detection.serverdata.StudentAdapter;
import org.tensorflow.lite.examples.detection.serverdata.StudentData;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpdateStudentLocation extends AppCompatActivity {
    TextView tenHocVien;
    EditText editLocation;
    Button btnUpdateLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student_location);

        Window window = UpdateStudentLocation.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(UpdateStudentLocation.this, R.color.blueViolet));

        tenHocVien = findViewById(R.id.text_ten_hoc_vien);
        editLocation = findViewById(R.id.txtNewLocation);
        btnUpdateLocation = findViewById(R.id.btnUpdateLocation);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String currentLocation = intent.getStringExtra("currentLocation");
        editLocation.setText(currentLocation == null ? "" : currentLocation);
        tenHocVien.setText(intent.getStringExtra("name"));

        btnUpdateLocation.setOnClickListener(view -> {
         updateStudentLocation(id, editLocation.getText().toString().trim());
        });
    }

    private void updateStudentLocation(String id, String newLocation) {
        MyCustomDialog loadingSpinner = new MyCustomDialog(UpdateStudentLocation.this, "Cập nhật thông tin...");
        loadingSpinner.startLoadingDialog();

        Retrofit retrofit = APIClient.getClient();
        APIService callAPI = retrofit.create(APIService.class);
        Call<UpdateLocationResponse> call = callAPI.updateStudentLocation(id, newLocation);
        call.enqueue(new Callback<UpdateLocationResponse>() {
            @Override
            public void onResponse(Call<UpdateLocationResponse> call, Response<UpdateLocationResponse> response) {
                loadingSpinner.dismissDialog();
                if(response.isSuccessful()) {
                    Toasty.success(UpdateStudentLocation.this, "Cập nhật thành công", Toast.LENGTH_SHORT, true).show();
                } else {
                    Toasty.error(UpdateStudentLocation.this, "Có lỗi xảy ra, thử lại", Toast.LENGTH_SHORT, true).show();
                }
                finish();
            }

            @Override
            public void onFailure(Call<UpdateLocationResponse> call, Throwable t) {
                loadingSpinner.dismissDialog();
                Toasty.error(UpdateStudentLocation.this, "Lỗi ứng dụng, thử lại", Toast.LENGTH_SHORT, true).show();
                finish();
            }
        });
    }
}