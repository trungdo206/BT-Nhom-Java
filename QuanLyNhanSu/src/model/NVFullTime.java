package model;

import java.time.LocalDate;

/**
 * Nhân viên toàn thời gian: lương = lương cơ bản x hệ số + phụ cấp.
 *
 * Người phụ trách: NGƯỜI 1
 */
public class NVFullTime extends NhanVien {

    private double luongCoBan;
    private double heSoLuong;
    private double phuCap;

    public NVFullTime(String maNV, String hoTen, LocalDate ngaySinh, String gioiTinh,
                      String soDienThoai, String maPhongBan,
                      double luongCoBan, double heSoLuong, double phuCap) {
        super(maNV, hoTen, ngaySinh, gioiTinh, soDienThoai, maPhongBan);
        setLuongCoBan(luongCoBan);
        setHeSoLuong(heSoLuong);
        setPhuCap(phuCap);
    }

    @Override
    public double tinhLuong() {
        return luongCoBan * heSoLuong + phuCap;
    }

    @Override
    public String getLoaiNV() {
        return "Toàn thời gian";
    }

    @Override
    protected String duLieuRiengCSV() {
        return luongCoBan + ";" + heSoLuong + ";" + phuCap;
    }

    @Override
    public String hienThiChiTiet() {
        return super.hienThiChiTiet()
                + "\nLương cơ bản: " + dinhDangTien(luongCoBan)
                + "\nHệ số lương : " + heSoLuong
                + "\nPhụ cấp     : " + dinhDangTien(phuCap)
                + "\nTổng lương  : " + dinhDangTien(tinhLuong());
    }

    public double getLuongCoBan() { return luongCoBan; }

    public void setLuongCoBan(double luongCoBan) {
        if (luongCoBan <= 0) throw new IllegalArgumentException("Lương cơ bản phải > 0");
        this.luongCoBan = luongCoBan;
    }

    public double getHeSoLuong() { return heSoLuong; }

    public void setHeSoLuong(double heSoLuong) {
        if (heSoLuong < 1.0 || heSoLuong > 10.0) {
            throw new IllegalArgumentException("Hệ số lương phải trong khoảng 1.0 - 10.0");
        }
        this.heSoLuong = heSoLuong;
    }

    public double getPhuCap() { return phuCap; }

    public void setPhuCap(double phuCap) {
        if (phuCap < 0) throw new IllegalArgumentException("Phụ cấp không được âm");
        this.phuCap = phuCap;
    }
}
