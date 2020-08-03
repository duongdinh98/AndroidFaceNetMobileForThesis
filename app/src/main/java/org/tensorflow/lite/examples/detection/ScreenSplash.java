package org.tensorflow.lite.examples.detection;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.examples.detection.Models.FaceIdDetails;
import org.tensorflow.lite.examples.detection.Models.ResultAllEmbeddings;
import org.tensorflow.lite.examples.detection.tflite.SaveDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ScreenSplash extends AppCompatActivity {

    private String convertArrToString(float[] emb) {
        String embArr = "";
        for (float feature : emb) {
            embArr = embArr + feature + "&";
        }

        return embArr;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Button btn_recognize = findViewById(R.id.btn_recognize);
        Button btn_register = findViewById(R.id.btn_register);
        TextView txt_check_hash_map = findViewById(R.id.txt_check_hash_map);
        ImageView splash_img = findViewById(R.id.splash_img);
        Button btnGetLatestData = findViewById(R.id.btn_get_latest_data);

        Bitmap image = SaveDataSet.readBitmapFromStorage("Black Obama3.png");
        if(image == null) {
            splash_img.setImageResource(R.drawable.ic_launcher);
        } else {
            splash_img.setImageBitmap(image);
        }
        HashMap<String, float[]> registered = SaveDataSet.deSerializeHashMap();
        if(registered != null) {
            String text = "";
            int numOfDim = 0;

            Set<String> keySet = registered.keySet();
            for (String key : keySet) {
//                text = text + key + convertArrToString(registered.get(key));
                numOfDim = registered.get(key).length;
            }

//            txt_check_hash_map.setText(text);
            txt_check_hash_map.setText("Num of dimension: " + numOfDim + " Num of face registered: " + registered.size());
        } else {
            txt_check_hash_map.setText("NULL");
        }

        btn_recognize.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Enter Recognition mode !",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ScreenSplash.this, DetectorActivity.class);
            intent.putExtra("Mode", false);
            startActivity(intent);
        });

        btn_register.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Enter Registration mode !",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ScreenSplash.this, DetectorActivity.class);
            intent.putExtra("Mode", true);
            startActivity(intent);
        });

        btnGetLatestData.setOnClickListener(view -> {
            Retrofit retrofit = APIClient.getClient();
            APIService callApi = retrofit.create(APIService.class);
            Call<ResultAllEmbeddings> call = callApi.getEmbeddingsData();
            call.enqueue(new Callback<ResultAllEmbeddings>() {
                @Override
                public void onResponse(Call<ResultAllEmbeddings> call, Response<ResultAllEmbeddings> response) {
                    if(response.isSuccessful()) {
                        HashMap<String, float[]> registeredData = new HashMap<>();
                        List<FaceIdDetails> data = response.body().getFaceIdData();
                        for (FaceIdDetails faceIdDetails : data) {
                            String name = faceIdDetails.getName();
                            float[] embeddings = transferStringToEmbedding(faceIdDetails.getEmbedding());

                            registeredData.put(name, embeddings);
                        }
                        SaveDataSet.serializeHashMap(registeredData);
                        Toast.makeText(ScreenSplash.this, "onResponse()", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResultAllEmbeddings> call, Throwable t) {
                    Toast.makeText(ScreenSplash.this, "onFailure()", Toast.LENGTH_SHORT).show();
                }
            });
        });
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
}
