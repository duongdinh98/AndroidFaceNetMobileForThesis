package org.tensorflow.lite.examples.detection.serverdata;

public class StudentLocationData {
    private String id;
    private String tenHocVien;
    private String ngaySinh;
    private String soCmnd;
    private String sdt;
    private String location;

    public StudentLocationData(String id, String tenHocVien, String ngaySinh, String soCmnd, String sdt, String location) {
        this.id = id;
        this.tenHocVien = tenHocVien;
        this.ngaySinh = ngaySinh;
        this.soCmnd = soCmnd;
        this.sdt = sdt;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
