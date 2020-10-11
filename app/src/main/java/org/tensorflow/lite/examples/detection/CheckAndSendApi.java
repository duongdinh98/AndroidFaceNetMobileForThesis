package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.lite.examples.detection.Models.CheckInResults;
import org.tensorflow.lite.examples.detection.Models.CheckOutResults;
import org.tensorflow.lite.examples.detection.Models.Result;
import org.tensorflow.lite.examples.detection.tflite.FaceAntiSpoofing;
import org.tensorflow.lite.examples.detection.tflite.SaveDataSet;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CheckAndSendApi extends AppCompatActivity {
    TextView name;
    TextView distance, txtAntiSpoof, txtIdUser, txtTotalTime;
    ImageView face;
    Button btn_try_again, btn_save_send_api, btnCheckIn, btnCheckOut;
    private FaceAntiSpoofing fas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_and_send_api);

        name = findViewById(R.id.txt_name);
        distance = findViewById(R.id.txt_distance);
        face = findViewById(R.id.img_face_cropped);
        btn_try_again = findViewById(R.id.btn_try_again);
        btn_save_send_api = findViewById(R.id.btn_save_send_api);
        txtAntiSpoof = findViewById(R.id.txtAntiSpoof);
        txtIdUser = findViewById(R.id.txtIdUser);
        btnCheckIn = findViewById(R.id.btnCheckIn);
        btnCheckOut = findViewById(R.id.btnCheckOut);
        txtTotalTime = findViewById(R.id.txt_total_time);

        btn_try_again.setOnClickListener(view -> finish());

        try {
            fas = new FaceAntiSpoofing(getAssets());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String nameI = intent.getStringExtra("NameDetected").split("&")[0];
        String idUserI = intent.getStringExtra("NameDetected").split("&")[1];
        float distanceI = intent.getFloatExtra("ConfidenceDetected", (float) 0.0);
//        Bitmap faceI = intent.getParcelableExtra("FaceDetected");
        byte[] bytes = getIntent().getByteArrayExtra("FaceDetected");
        Bitmap faceI = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        if(faceI != null) {
            face.setImageBitmap(faceI);
        } else {
            face.setImageResource(R.drawable.ic_launcher);
        }

        name.setText(nameI);
        distance.setText(String.valueOf(distanceI));
//        antiSpoofing(faceI);
        txtIdUser.setText(idUserI);

        btn_save_send_api.setOnClickListener(view -> {
            String fileName = nameI + ".png";
            SaveDataSet.saveBitmapToStorage(faceI, fileName);
            Toast.makeText(CheckAndSendApi.this, "Saved data", Toast.LENGTH_SHORT).show();

            String idLearner = "TempoID";
            Date currentTime = Calendar.getInstance().getTime();
//            callPostAttendanceAPI(idLearner, nameI, currentTime.toString(), fileName);
        });

        btnCheckIn.setOnClickListener(view -> {
            LoadingDialog loadingDialog = new LoadingDialog(CheckAndSendApi.this);
            loadingDialog.startLoadingDialog();

            String idLearner = idUserI;
            String checkInAt = Calendar.getInstance().getTime().toString();

            String fileName = nameI + "_check_in.png";
            SaveDataSet.saveBitmapToStorage(faceI, fileName);
            Toast.makeText(CheckAndSendApi.this, "Saved data", Toast.LENGTH_SHORT).show();

            sendCheckInAPI(idLearner, checkInAt, fileName, loadingDialog);
        });

        btnCheckOut.setOnClickListener(view -> {
            FaceCheckHelper faceCheckHelper = new FaceCheckHelper(CheckAndSendApi.this, "lcd_data.sqlite", null, 1);
            Cursor cursor = faceCheckHelper.getData("SELECT * FROM attendance WHERE idLeaner='" + idUserI + "'");

            if((cursor != null) && (cursor.getCount() > 0)){
                LoadingDialog loadingDialog = new LoadingDialog(CheckAndSendApi.this);
                loadingDialog.startLoadingDialog();

                String id = "";
                while (cursor.moveToNext()) {
                    id = cursor.getString(1);
                }

                String checkOutAt = Calendar.getInstance().getTime().toString();

                String fileName = nameI + "_check_out.png";
                SaveDataSet.saveBitmapToStorage(faceI, fileName);
                Toast.makeText(CheckAndSendApi.this, "Saved data", Toast.LENGTH_SHORT).show();

                sendCheckOutAPI(id, checkOutAt, fileName, loadingDialog);
            } else {
                Toast.makeText(CheckAndSendApi.this, nameI + " has not checked in before !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callPostAttendanceAPI(String id, String nameI, String time, String imageFileName) {
//        String root = Environment.getExternalStorageDirectory().toString();
//        File myDir = new File(root, "/LearnerDrivingCentre/AttendanceImages");
//        File imageFile = new File(myDir,imageFileName);
//
//        RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), imageFile);
//
//        // MultipartBody.Part is used to send also the actual file name
//        MultipartBody.Part body = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);
//
//        // add another part within the multipart request
//        RequestBody idLearner = RequestBody.create(MediaType.parse("multipart/form-data"), id);
//        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), nameI);
//        RequestBody attemptAt = RequestBody.create(MediaType.parse("multipart/form-data"), time);
//
//        Retrofit retrofit = APIClient.getClient();
//        APIService callApi = retrofit.create(APIService.class);
//        Call<Result> call = callApi.attendance(idLearner, name, body, attemptAt);
//
//        call.enqueue(new Callback<Result>() {
//            @Override
//            public void onResponse(Call<Result> call, Response<Result> response) {
//                Toast.makeText(CheckAndSendApi.this, "Sent POST", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<Result> call, Throwable t) {
//                Toast.makeText(CheckAndSendApi.this, "Failed POST", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void antiSpoofing(Bitmap bitmapCrop1) {
        // Đánh giá độ rõ nét của hình ảnh trước khi phát hiện trực tiếp
        int laplace1 = fas.laplacian(bitmapCrop1);

        String text = "Độ nét của ảnh：" + laplace1;
        float score1 = fas.antiSpoofing(bitmapCrop1);
        text += " Điểm Spoofing：" + score1;
        txtAntiSpoof.setText(text);

//        if (laplace1 < FaceAntiSpoofing.LAPLACIAN_THRESHOLD) {
//            text = text + "，" + "False";
//            txtAntiSpoof.setTextColor(getResources().getColor(android.R.color.holo_red_light));
//        } else {
//            long start = System.currentTimeMillis();
//
//            // 活体检测
//            float score1 = fas.antiSpoofing(bitmapCrop1);
//
//            long end = System.currentTimeMillis();
//
//            text = "Điểm Spoofing：" + score1;
//            if (score1 < FaceAntiSpoofing.THRESHOLD) {
//                text = text + "，" + "True";
//                txtAntiSpoof.setTextColor(getResources().getColor(android.R.color.holo_green_light));
//            } else {
//                text = text + "，" + "False";
//                txtAntiSpoof.setTextColor(getResources().getColor(android.R.color.holo_red_light));
//            }
//            text = text + "Time: " + (end - start);
//        }
//        txtAntiSpoof.setText(text);
    }

    private void sendCheckInAPI(String id, String time, String imageFileName, LoadingDialog loadingDialog) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root, "/LearnerDrivingCentre/AttendanceImages");
        File imageFile = new File(myDir,imageFileName);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), imageFile);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("imageCheckIn", imageFile.getName(), requestFile);

        // add another part within the multipart request
        RequestBody idLearner = RequestBody.create(MediaType.parse("multipart/form-data"), id);
        RequestBody checkInAt = RequestBody.create(MediaType.parse("multipart/form-data"), time);

        Retrofit retrofit = APIClient.getClient();
        APIService callApi = retrofit.create(APIService.class);
        Call<CheckInResults> call = callApi.doCheckIn(idLearner, body, checkInAt);

        call.enqueue(new Callback<CheckInResults>() {
            @Override
            public void onResponse(Call<CheckInResults> call, Response<CheckInResults> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(CheckAndSendApi.this, "Sent check in API", Toast.LENGTH_SHORT).show();
                    String checkInId = response.body().getId();
                    FaceCheckHelper faceCheckHelper = new FaceCheckHelper(CheckAndSendApi.this, "lcd_data.sqlite", null, 1);

//                    Check if idLeaner existed, so we delete
                    faceCheckHelper.queryData("DELETE FROM attendance WHERE idLeaner='" + id + "'");

                    faceCheckHelper.queryData("INSERT INTO attendance (idLeaner, idCheckIn) VALUES ('" + id + "' ,'" + checkInId + "')");
                    Toast.makeText(CheckAndSendApi.this, "Save checkInId to SQLite", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismissLoadingDialog();
                    LoadingDialog successDialog = new LoadingDialog(CheckAndSendApi.this);
                    successDialog.startSuccessDialog();
                }
            }

            @Override
            public void onFailure(Call<CheckInResults> call, Throwable t) {
                Toast.makeText(CheckAndSendApi.this, "Failed check in !", Toast.LENGTH_SHORT).show();
                loadingDialog.dismissLoadingDialog();
                LoadingDialog errorDialog = new LoadingDialog(CheckAndSendApi.this);
                errorDialog.startErrorDialog();
            }
        });
    }

    private void sendCheckOutAPI(String idCheckIn, String time, String imageFileName, LoadingDialog loadingDialog) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root, "/LearnerDrivingCentre/AttendanceImages");
        File imageFile = new File(myDir,imageFileName);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), imageFile);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("imageCheckOut", imageFile.getName(), requestFile);

        // add another part within the multipart request
        RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), idCheckIn);
        RequestBody checkOutAt = RequestBody.create(MediaType.parse("multipart/form-data"), time);

        Retrofit retrofit = APIClient.getClient();
        APIService callApi = retrofit.create(APIService.class);
        Call<CheckOutResults> call = callApi.doCheckOut(id, checkOutAt, body);

        call.enqueue(new Callback<CheckOutResults>() {
            @Override
            public void onResponse(Call<CheckOutResults> call, Response<CheckOutResults> response) {
                Toast.makeText(CheckAndSendApi.this, "Sent check out API", Toast.LENGTH_SHORT).show();
                if(response.isSuccessful()) {
                    int totalTime = response.body().getTotalTime();
                    txtTotalTime.setText("Total time: " + convertSecondToTime(totalTime));

                    FaceCheckHelper faceCheckHelper = new FaceCheckHelper(CheckAndSendApi.this, "lcd_data.sqlite", null, 1);
                    // Clear
                    faceCheckHelper.queryData("DELETE FROM attendance WHERE idCheckIn='" + idCheckIn + "'");
                    loadingDialog.dismissLoadingDialog();
                    LoadingDialog successDialog = new LoadingDialog(CheckAndSendApi.this, totalTime);
                    successDialog.startSuccessCheckOutDialog();
                }
            }

            @Override
            public void onFailure(Call<CheckOutResults> call, Throwable t) {
                Toast.makeText(CheckAndSendApi.this, "Failed check out !", Toast.LENGTH_SHORT).show();
                loadingDialog.dismissLoadingDialog();
                LoadingDialog errorDialog = new LoadingDialog(CheckAndSendApi.this);
                errorDialog.startErrorDialog();
            }
        });
    }

    private  String convertSecondToTime(int seconds) {
        int ss = seconds % 60;
        int hh = seconds / 60;
        int mm = hh % 60;
        hh = hh / 60;

        String hour = "";
        String minute = "";
        String second = "";

        if(hh != 0) {
            hour = hh + "giờ ";
        }

        if(mm != 0) {
            minute = mm + "phút ";
        }

        if(ss != 0) {
            second = ss + "giây";
        }

        return ( hour + minute + second);
    }
}

