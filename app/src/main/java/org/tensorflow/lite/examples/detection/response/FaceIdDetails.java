package org.tensorflow.lite.examples.detection.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaceIdDetails {
    @SerializedName("idLearner")
    @Expose
    private String idLearner;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("embedding")
    @Expose
    private String embedding;
    @SerializedName("id")
    @Expose
    private String id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
