package org.tensorflow.lite.examples.detection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.tensorflow.lite.examples.detection.response.CheckInResponse;
import org.tensorflow.lite.examples.detection.tflite.SaveDataSet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FaceCheckInConfirm extends AppCompatActivity {

    Button btnCheckIn, btnTryAgain;
    CircleImageView imgCheckin;
    TextView tenCheckIn, thoiGianCheckIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_check_in_confirm);

        Window window = FaceCheckInConfirm.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(FaceCheckInConfirm.this, R.color.btnBlue));

        btnCheckIn = findViewById(R.id.btn_send_check_in);
        btnTryAgain = findViewById(R.id.btn_try_again);
        imgCheckin = findViewById(R.id.img_check_in);
        tenCheckIn = findViewById(R.id.txt_ten_check_in);
        thoiGianCheckIn = findViewById(R.id.txt_thoi_gian_check_in);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date dateNow = new Date();
        thoiGianCheckIn.setText(formatter.format(dateNow));
        btnTryAgain.setOnClickListener(view -> {
            finish();
        });

        Intent intent = getIntent();
        String studentName = intent.getStringExtra("NameDetected").split("&")[0];
        String idHocVien = intent.getStringExtra("NameDetected").split("&")[1];

        byte[] bytes = getIntent().getByteArrayExtra("FaceDetected");
        Bitmap faceI = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        imgCheckin.setImageBitmap(faceI);
        tenCheckIn.setText(studentName);
        btnCheckIn.setOnClickListener(view -> {
            checkIn(idHocVien, studentName, faceI);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void checkIn(String id, String studentName, Bitmap faceI) {
        MyCustomDialog loadingSpinner = new MyCustomDialog(FaceCheckInConfirm.this, "Đang thực hiện check-in...");
        loadingSpinner.startLoadingDialog();

        String time = thoiGianCheckIn.getText().toString();

        String fileName = id + "_check_in.png";
        SaveDataSet.saveBitmapToStorage(faceI, fileName);

        sendCheckInApi(fileName, id, time, loadingSpinner);
    }

    private void sendCheckInApi(String fileName, String id, String time, MyCustomDialog loadingSpinner) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root, "/LearnerDrivingCentre/AttendanceImages");
        File imageFile = new File(myDir,fileName);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), imageFile);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("imageCheckIn", imageFile.getName(), requestFile);

        // Add another part within the multipart request
        RequestBody idHocVien = RequestBody.create(MediaType.parse("multipart/form-data"), id);
        RequestBody checkInAt = RequestBody.create(MediaType.parse("multipart/form-data"), time);

        Retrofit retrofit = APIClient.getClient();
        APIService callApi = retrofit.create(APIService.class);
        Call<CheckInResponse> call = callApi.doCheckIn(idHocVien, body, checkInAt, "Bearer " + SaveDataSet.retrieveFromMyPrefs(FaceCheckInConfirm.this,"jwt"));

        call.enqueue(new Callback<CheckInResponse>() {
            @Override
            public void onResponse(Call<CheckInResponse> call, Response<CheckInResponse> response) {
                loadingSpinner.dismissDialog();
                if(response.isSuccessful()) {
                    String checkInId = response.body().getId();
                    FaceCheckHelper faceCheckHelper = new FaceCheckHelper(FaceCheckInConfirm.this, "hnd_data.sqlite", null, 1);

                    // Check if idLeaner existed, so we delete
                    faceCheckHelper.queryData("DELETE FROM attendance WHERE idHocVien='" + id + "'");
                    faceCheckHelper.queryData("INSERT INTO attendance (idHocVien, idCheckIn) VALUES ('" + id + "' ,'" + checkInId + "')");

                    MyCustomDialog successSpinner = new MyCustomDialog(FaceCheckInConfirm.this, "Check-in thành công");
                    successSpinner.startSuccessMakeARollCallDialog();
                } else {
                    MyCustomDialog failSpinner = new MyCustomDialog(FaceCheckInConfirm.this, "Check-in thất bại, thử lại");
                    failSpinner.startErrorMakeARollCallDialog();
                }
            }

            @Override
            public void onFailure(Call<CheckInResponse> call, Throwable t) {
                loadingSpinner.dismissDialog();
                Toasty.error(FaceCheckInConfirm.this, "Lỗi ứng dụng, thử lại", Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}