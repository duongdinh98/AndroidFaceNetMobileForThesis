package org.tensorflow.lite.examples.detection.Models;

import android.content.Intent;

public class CheckOutResults {
    private String status;
    private String message;
    private int totalTime;

    public CheckOutResults(String status, String message, int totalTime) {
        this.status = status;
        this.message = message;
        this.totalTime = totalTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }
}
