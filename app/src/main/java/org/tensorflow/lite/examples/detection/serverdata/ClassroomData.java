package org.tensorflow.lite.examples.detection.serverdata;

public class ClassroomData {
    private String id;
    private String tenLop;
    private String khaiGiang;
    private String loaiBang;
    private String thoiGianHoc;

    public ClassroomData(String id, String tenLop, String khaiGiang, String loaiBang, String thoiGianHoc) {
        this.id = id;
        this.tenLop = tenLop;
        this.khaiGiang = khaiGiang;
        this.loaiBang = loaiBang;
        this.thoiGianHoc = thoiGianHoc;
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

    public String getLoaiBang() {
        return loaiBang;
    }

    public void setLoaiBang(String loaiBang) {
        this.loaiBang = loaiBang;
    }

    public String getThoiGianHoc() {
        return thoiGianHoc;
    }

    public void setThoiGianHoc(String thoiGianHoc) {
        this.thoiGianHoc = thoiGianHoc;
    }
}
