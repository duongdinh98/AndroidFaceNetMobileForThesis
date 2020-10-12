package org.tensorflow.lite.examples.detection.response;

public class CheckInResponse {
    private String status;
    private String message;
    private String id;

    public CheckInResponse(String status, String message, String id) {
        this.status = status;
        this.message = message;
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
