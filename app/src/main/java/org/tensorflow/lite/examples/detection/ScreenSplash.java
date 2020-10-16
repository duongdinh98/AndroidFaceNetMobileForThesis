package org.tensorflow.lite.examples.detection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.examples.detection.response.FaceIdDetails;
import org.tensorflow.lite.examples.detection.response.ResultAllEmbeddings;
import org.tensorflow.lite.examples.detection.tflite.FaceAntiSpoofing;
import org.tensorflow.lite.examples.detection.tflite.SaveDataSet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ScreenSplash extends AppCompatActivity {
    FaceCheckHelper faceCheckHelper;

    private String convertArrToString(float[] emb) {
        String embArr = "";
        for (float feature : emb) {
            embArr = embArr + feature + "&";
        }

        return embArr;
    }

    Button btnAnti;
    private FaceAntiSpoofing fas;
    TextView anitSpoofing;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        if (!hasPermission()) {
            requestPermission();
        }

        try {
            fas = new FaceAntiSpoofing(getAssets());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button btn_recognize = findViewById(R.id.btn_recognize);
        Button btn_register = findViewById(R.id.btn_register);
        TextView txt_check_hash_map = findViewById(R.id.txt_check_hash_map);
        ImageView splash_img = findViewById(R.id.splash_img);
        Button btnGetLatestData = findViewById(R.id.btn_get_latest_data);
        btnAnti = findViewById(R.id.btn_anti);
        anitSpoofing = findViewById(R.id.anitSpoofing);

        Bitmap image = SaveDataSet.readBitmapFromStorage("Black Obama3.png");
        if(image == null) {
            splash_img.setImageResource(R.drawable.ic_launcher);
        } else {
            splash_img.setImageBitmap(image);
        }
//        HashMap<String, float[]> registered = SaveDataSet.deSerializeHashMap();
//        if(registered != null) {
//            String text = "";
//            int numOfDim = 0;
//
//            Set<String> keySet = registered.keySet();
//            for (String key : keySet) {
////                text = text + key + convertArrToString(registered.get(key));
//                numOfDim = registered.get(key).length;
//            }
//
////            txt_check_hash_map.setText(text);
//            txt_check_hash_map.setText("Num of dimension: " + numOfDim + " Num of face registered: " + registered.size());
//        } else {
//            txt_check_hash_map.setText("NULL");
//        }

        btn_recognize.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Enter Recognition mode !",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ScreenSplash.this, DetectorActivity.class);
            intent.putExtra("Mode", false);
            intent.putExtra("faceData", "teacher_embeddings");
            startActivity(intent);
        });

        btn_register.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Enter Registration mode !",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ScreenSplash.this, DetectorActivity.class);
            intent.putExtra("Mode", true);
            startActivity(intent);
        });

        btnGetLatestData.setOnClickListener(view -> {
//            Retrofit retrofit = APIClient.getClient();
//            APIService callApi = retrofit.create(APIService.class);
//            Call<ResultAllEmbeddings> call = callApi.getEmbeddingsData();
//            call.enqueue(new Callback<ResultAllEmbeddings>() {
//                @Override
//                public void onResponse(Call<ResultAllEmbeddings> call, Response<ResultAllEmbeddings> response) {
//                    if(response.isSuccessful()) {
//                        HashMap<String, float[]> registeredData = new HashMap<>();
//                        List<FaceIdDetails> data = response.body().getFaceIdData();
//                        for (FaceIdDetails faceIdDetails : data) {
//                            String name = faceIdDetails.getName() + "&" + faceIdDetails.getId();
//                            float[] embeddings = transferStringToEmbedding(faceIdDetails.getEmbedding());
//
//                            registeredData.put(name, embeddings);
//                        }
//                        SaveDataSet.serializeHashMap(registeredData);
//                        Toast.makeText(ScreenSplash.this, "onResponse()", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(ScreenSplash.this, "404 or something !", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResultAllEmbeddings> call, Throwable t) {
//                    Toast.makeText(ScreenSplash.this, "onFailure()", Toast.LENGTH_SHORT).show();
//                }
//            });
        });

        btnAnti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bitmap imgCheck = SaveDataSet.readBitmapFromStorage("imgCheck.jpg");
//                antiSpoofing(imgCheck);
            }
        });

//        SQL LITE
        initSQLite();
        anitSpoofing.setText("*" + SaveDataSet.readApiUrl() + "*");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private float[] transferStringToEmbedding(String string) {
        // Vector 192 dimensions
        float[] arr = new float[192];

        String[] embeddingsInString = string.split("&");

        for (int i = 0; i < (embeddingsInString.length - 1); i++){
            arr[i] = Float.parseFloat(embeddingsInString[i]);
        }

        return arr;
    }

    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        } else {
            return true;
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 5360);
        }
    }

    private void antiSpoofing(Bitmap bitmapCrop1) {
        // Đánh giá độ rõ nét của hình ảnh trước khi phát hiện trực tiếp
        int laplace1 = fas.laplacian(bitmapCrop1);

        String text = "清晰度检测结果left：" + laplace1;

        if (laplace1 < FaceAntiSpoofing.LAPLACIAN_THRESHOLD) {
            text = text + "，" + "False";
            anitSpoofing.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        } else {
            long start = System.currentTimeMillis();

            // 活体检测
            float score1 = fas.antiSpoofing(bitmapCrop1);

            long end = System.currentTimeMillis();

            text = "活体检测结果left：" + score1;
            if (score1 < FaceAntiSpoofing.THRESHOLD) {
                text = text + "，" + "True";
                anitSpoofing.setTextColor(getResources().getColor(android.R.color.holo_green_light));
            } else {
                text = text + "，" + "False";
                anitSpoofing.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            }
            text = text + "。耗时" + (end - start);
        }
        anitSpoofing.setText(text);
    }
    private void initSQLite() {
        // Create database
        faceCheckHelper = new FaceCheckHelper(ScreenSplash.this, "lcd_data.sqlite", null, 1);

        // Create table
        String create_attendance_table_sql = "CREATE TABLE IF NOT EXISTS attendance (\n" +
                "\tid INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                " \tidCheckIn TEXT,\n" +
                " \tidLeaner TEXT\n" +
                ")";
        faceCheckHelper.queryData(create_attendance_table_sql);
    }

}
