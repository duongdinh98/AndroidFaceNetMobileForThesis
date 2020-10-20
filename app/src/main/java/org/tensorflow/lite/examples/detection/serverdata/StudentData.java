package org.tensorflow.lite.examples.detection.serverdata;

public class StudentData {
    private String tenHocVien;
    private String ngaySinh;
    private String soCmnd;

    public StudentData(String tenHocVien, String ngaySinh, String soCmnd) {
        this.tenHocVien = tenHocVien;
        this.ngaySinh = ngaySinh;
        this.soCmnd = soCmnd;
    }

    public String getTenHocVien() {
        return tenHocVien;
    }

    public void setTenHocVien(String tenHocVien) {
        this.tenHocVien = tenHocVien;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getSoCmnd() {
        return soCmnd;
    }

    public void setSoCmnd(String soCmnd) {
        this.soCmnd = soCmnd;
    }
}
