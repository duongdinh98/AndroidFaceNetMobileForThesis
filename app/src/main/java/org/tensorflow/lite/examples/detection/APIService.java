package org.tensorflow.lite.examples.detection;

import org.tensorflow.lite.examples.detection.response.CheckInResponse;
import org.tensorflow.lite.examples.detection.response.CheckOutResponse;
import org.tensorflow.lite.examples.detection.response.FaceIdRegistration;
import org.tensorflow.lite.examples.detection.response.LoginResponse;
import org.tensorflow.lite.examples.detection.response.Result;
import org.tensorflow.lite.examples.detection.response.ResultAllEmbeddings;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService {
    // Authentication, authorization API
    @FormUrlEncoded
    @POST("/api/v1/users/login")
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api/v1/users/login-by-face")
    Call<LoginResponse> loginByFace(
            @Field("idUser") String idUser
    );

    // Register new face id API
    @POST("/api/v1/face_id")
    Call<Result> register(@Body FaceIdRegistration faceId);

    @GET("/api/v1/face_id")
    Call<ResultAllEmbeddings> getEmbeddingsData();

    // Check-in, check-out API
    @Multipart
    @POST("/api/v1/attendance")
    Call<CheckInResponse> doCheckIn(
            @Part("idLearner") RequestBody idLearner,
            @Part MultipartBody.Part imageCheckIn,
            @Part("checkInAt") RequestBody checkInAt);

    @Multipart
    @PATCH("/api/v1/attendance")
    Call<CheckOutResponse> doCheckOut(
            @Part("id") RequestBody id,
            @Part("checkOutAt") RequestBody checkOutAt,
            @Part MultipartBody.Part imageCheckIn);
}
