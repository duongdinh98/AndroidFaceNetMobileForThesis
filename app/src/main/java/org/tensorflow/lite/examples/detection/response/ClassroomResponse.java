package org.tensorflow.lite.examples.detection.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClassroomResponse {
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

        @SerializedName("idGiangVien")
        @Expose
        private IdGiangVien idGiangVien;
        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("tenLop")
        @Expose
        private String tenLop;
        @SerializedName("khaiGiang")
        @Expose
        private String khaiGiang;
        @SerializedName("idLoaiBang")
        @Expose
        private IdLoaiBang idLoaiBang;

        public IdGiangVien getIdGiangVien() {
            return idGiangVien;
        }

        public void setIdGiangVien(IdGiangVien idGiangVien) {
            this.idGiangVien = idGiangVien;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTenLop() {
            return tenLop;
        }

        public void setTenLop(String tenLop) {
            this.tenLop = tenLop;
        }

        public String getKhaiGiang() {
            return khaiGiang;
        }

        public void setKhaiGiang(String khaiGiang) {
            this.khaiGiang = khaiGiang;
        }

        public IdLoaiBang getIdLoaiBang() {
            return idLoaiBang;
        }

        public void setIdLoaiBang(IdLoaiBang idLoaiBang) {
            this.idLoaiBang = idLoaiBang;
        }
    }

    public static class IdLoaiBang {

        @SerializedName("tenBang")
        @Expose
        private String tenBang;
        @SerializedName("thoiGianHoc")
        @Expose
        private Integer thoiGianHoc;

        public String getTenBang() {
            return tenBang;
        }

        public void setTenBang(String tenBang) {
            this.tenBang = tenBang;
        }

        public Integer getThoiGianHoc() {
            return thoiGianHoc;
        }

        public void setThoiGianHoc(Integer thoiGianHoc) {
            this.thoiGianHoc = thoiGianHoc;
        }

    }

    public static class IdGiangVien {

    }
}
