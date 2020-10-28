package org.tensorflow.lite.examples.detection.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public static class Datum {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("ten")
        @Expose
        private String ten;
        @SerializedName("ngaySinh")
        @Expose
        private String ngaySinh;
        @SerializedName("cmnd")
        @Expose
        private String cmnd;
        @SerializedName("sdt")
        @Expose
        private String sdt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTen() {
            return ten;
        }

        public void setTen(String ten) {
            this.ten = ten;
        }

        public String getNgaySinh() {
            return ngaySinh;
        }

        public void setNgaySinh(String ngaySinh) {
            this.ngaySinh = ngaySinh;
        }

        public String getCmnd() {
            return cmnd;
        }

        public void setCmnd(String cmnd) {
            this.cmnd = cmnd;
        }

        public String getSdt() {
            return sdt;
        }

        public void setSdt(String sdt) {
            this.sdt = sdt;
        }
    }
}
