package org.tensorflow.lite.examples.detection.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultAllEmbeddings {
    @SerializedName("faceIdData")
    @Expose
    private List<FaceIdDetails> faceIdData = null;

    public List<FaceIdDetails> getFaceIdData() {
        return faceIdData;
    }

    public void setFaceIdData(List<FaceIdDetails> faceIdData) {
        this.faceIdData = faceIdData;
    }
}
