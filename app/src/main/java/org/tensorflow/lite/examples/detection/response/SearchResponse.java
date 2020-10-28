package org.tensorflow.lite.examples.detection.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("totalTime")
    @Expose
    private Integer totalTime;
    @SerializedName("data")
    @Expose
    private Datam data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public Datam getData() {
        return data;
    }

    public void setData(Datam data) {
        this.data = data;
    }

    public static class Datam {

        @SerializedName("sdt")
        @Expose
        private String sdt;
        @SerializedName("idLop")
        @Expose
        private IdLop idLop;
        @SerializedName("ngayTao")
        @Expose
        private String ngayTao;
        @SerializedName("isPassLyThuyet")
        @Expose
        private Boolean isPassLyThuyet;
        @SerializedName("ten")
        @Expose
        private String ten;
        @SerializedName("ngaySinh")
        @Expose
        private String ngaySinh;
        @SerializedName("cmnd")
        @Expose
        private String cmnd;
        @SerializedName("diaChi")
        @Expose
        private String diaChi;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("embedding")
        @Expose
        private String embedding;

        public String getSdt() {
            return sdt;
        }

        public void setSdt(String sdt) {
            this.sdt = sdt;
        }

        public IdLop getIdLop() {
            return idLop;
        }

        public void setIdLop(IdLop idLop) {
            this.idLop = idLop;
        }

        public String getNgayTao() {
            return ngayTao;
        }

        public void setNgayTao(String ngayTao) {
            this.ngayTao = ngayTao;
        }

        public Boolean getIsPassLyThuyet() {
            return isPassLyThuyet;
        }

        public void setIsPassLyThuyet(Boolean isPassLyThuyet) {
            this.isPassLyThuyet = isPassLyThuyet;
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

        public String getDiaChi() {
            return diaChi;
        }

        public void setDiaChi(String diaChi) {
            this.diaChi = diaChi;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEmbedding() {
            return embedding;
        }

        public void setEmbedding(String embedding) {
            this.embedding = embedding;
        }
    }

    public static class IdLop {

        @SerializedName("tenLop")
        @Expose
        private String tenLop;
        @SerializedName("idLoaiBang")
        @Expose
        private IdLoaiBang idLoaiBang;

        public String getTenLop() {
            return tenLop;
        }

        public void setTenLop(String tenLop) {
            this.tenLop = tenLop;
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
}
