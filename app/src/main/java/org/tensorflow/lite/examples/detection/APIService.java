package org.tensorflow.lite.examples.detection;

import org.tensorflow.lite.examples.detection.Models.CheckInResults;
import org.tensorflow.lite.examples.detection.Models.CheckOutResults;
import org.tensorflow.lite.examples.detection.Models.FaceIdRegistration;
import org.tensorflow.lite.examples.detection.Models.Result;
import org.tensorflow.lite.examples.detection.Models.ResultAllEmbeddings;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService {
    @POST("/api/v1/face_id")
    Call<Result> register(@Body FaceIdRegistration faceId);

    @GET("/api/v1/face_id")
    Call<ResultAllEmbeddings> getEmbeddingsData();

    @Multipart
    @POST("/api/v1/attendance")
    Call<CheckInResults> doCheckIn(
            @Part("idLearner") RequestBody idLearner,
            @Part MultipartBody.Part imageCheckIn,
            @Part("checkInAt") RequestBody checkInAt);

    @Multipart
    @PATCH("/api/v1/attendance")
    Call<CheckOutResults> doCheckOut(
            @Part("id") RequestBody id,
            @Part("checkOutAt") RequestBody checkOutAt,
            @Part MultipartBody.Part imageCheckIn);
}
