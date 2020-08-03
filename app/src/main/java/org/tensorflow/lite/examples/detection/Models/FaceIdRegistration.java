package org.tensorflow.lite.examples.detection.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaceIdRegistration {
    @SerializedName("idLearner")
    @Expose
    private String idLearner;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("embedding")
    @Expose
    private String embedding;

    public FaceIdRegistration(String idLearner, String name, String embedding) {
        this.idLearner = idLearner;
        this.name = name;
        this.embedding = embedding;
    }

    public String getIdLearner() {
        return idLearner;
    }

    public void setIdLearner(String idLearner) {
        this.idLearner = idLearner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmbedding() {
        return embedding;
    }

    public void setEmbedding(String embedding) {
        this.embedding = embedding;
    }
}
