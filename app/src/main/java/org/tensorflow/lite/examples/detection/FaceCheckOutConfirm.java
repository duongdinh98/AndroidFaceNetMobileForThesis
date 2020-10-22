package org.tensorflow.lite.examples.detection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.tensorflow.lite.examples.detection.response.CheckOutResponse;
import org.tensorflow.lite.examples.detection.tflite.SaveDataSet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FaceCheckOutConfirm extends AppCompatActivity {
    Button btnCheckOut, btnTryAgain;
    CircleImageView imgCheckOut;
    TextView tenCheckOut, thoiGianCheckOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_check_out_confirm);

        Window window = FaceCheckOutConfirm.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(FaceCheckOutConfirm.this, R.color.errorColor));

        btnCheckOut = findViewById(R.id.btn_send_check_out);
        btnTryAgain = findViewById(R.id.btn_try_again);
        imgCheckOut = findViewById(R.id.img_check_out);
        tenCheckOut = findViewById(R.id.txt_ten_check_out);
        thoiGianCheckOut = findViewById(R.id.txt_thoi_gian_check_out);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date dateNow = new Date();
        thoiGianCheckOut.setText(formatter.format(dateNow));
        btnTryAgain.setOnClickListener(view -> {
            finish();
        });

        Intent intent = getIntent();
        String studentName = intent.getStringExtra("NameDetected").split("&")[0];
        String idHocVien = intent.getStringExtra("NameDetected").split("&")[1];
        byte[] bytes = getIntent().getByteArrayExtra("FaceDetected");

        Bitmap faceI = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imgCheckOut.setImageBitmap(faceI);
        tenCheckOut.setText(studentName);

        btnCheckOut.setOnClickListener(view -> {
            sendCheckOut(idHocVien, faceI, studentName);
        });
    }

    private void sendCheckOut(String idHocVien, Bitmap faceI, String studentName) {
        FaceCheckHelper faceCheckHelper = new FaceCheckHelper(FaceCheckOutConfirm.this, "hnd_data.sqlite", null, 1);
        Cursor cursor = faceCheckHelper.getData("SELECT * FROM attendance WHERE idHocVien='" + idHocVien + "'");

        if((cursor != null) && (cursor.getCount() > 0)){
            String id = "";
            while (cursor.moveToNext()) {
                id = cursor.getString(1);
            }

            String time = thoiGianCheckOut.getText().toString();

            String fileName = idHocVien + "_check_out.png";
            SaveDataSet.saveBitmapToStorage(faceI, fileName);
            sendCheckOutApi(fileName, id, time);
        } else {
            MyCustomDialog failSpinner = new MyCustomDialog(FaceCheckOutConfirm.this, studentName + " chưa check in trước đó !");
            failSpinner.startErrorMakeARollCallDialog();
        }
    }

    private void sendCheckOutApi(String fileName, String id, String time) {
        MyCustomDialog loadingSpinner = new MyCustomDialog(FaceCheckOutConfirm.this, "Thực hiện check out...");
        loadingSpinner.startLoadingDialog();

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root, "/LearnerDrivingCentre/AttendanceImages");
        File imageFile = new File(myDir, fileName);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), imageFile);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("imageCheckOut", imageFile.getName(), requestFile);

        // add another part within the multipart request
        RequestBody idAttendance = RequestBody.create(MediaType.parse("multipart/form-data"), id);
        RequestBody checkOutAt = RequestBody.create(MediaType.parse("multipart/form-data"), time);

        Retrofit retrofit = APIClient.getClient();
        APIService callApi = retrofit.create(APIService.class);
        Call<CheckOutResponse> call = callApi.doCheckOut(idAttendance, checkOutAt, body, "Bearer " + SaveDataSet.retrieveFromMyPrefs(FaceCheckOutConfirm.this,"jwt"));

        call.enqueue(new Callback<CheckOutResponse>() {
            @Override
            public void onResponse(Call<CheckOutResponse> call, Response<CheckOutResponse> response) {
                loadingSpinner.dismissDialog();
                if(response.isSuccessful()) {
                    String totalTime = SaveDataSet.convertSecondToTime(response.body().getTotalTime());

                    FaceCheckHelper faceCheckHelper = new FaceCheckHelper(FaceCheckOutConfirm.this, "hnd_data.sqlite", null, 1);
                    // Clear check-in-out log
                    faceCheckHelper.queryData("DELETE FROM attendance WHERE idCheckIn='" + id + "'");

                    MyCustomDialog successSpinner = new MyCustomDialog(FaceCheckOutConfirm.this, "Tổng thời gian lái xe: " + totalTime);
                    successSpinner.startSuccessMakeARollCallDialog();
                } else {
                    MyCustomDialog failSpinner = new MyCustomDialog(FaceCheckOutConfirm.this, "Check out thất bại, thử lại");
                    failSpinner.startErrorMakeARollCallDialog();
                }
            }

            @Override
            public void onFailure(Call<CheckOutResponse> call, Throwable t) {
                loadingSpinner.dismissDialog();
                MyCustomDialog failSpinner = new MyCustomDialog(FaceCheckOutConfirm.this, "Lỗi mạng, thử lại sau");
                failSpinner.startErrorMakeARollCallDialog();
            }
        });
    }
}