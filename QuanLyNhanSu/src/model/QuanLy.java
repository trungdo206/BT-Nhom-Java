package model;

import java.time.LocalDate;

/**
 * Quản lý: là một nhân viên toàn thời gian (kế thừa NVFullTime) có thêm phụ cấp chức vụ.
 * Ví dụ kế thừa nhiều tầng: NhanVien -> NVFullTime -> QuanLy, và dùng super.tinhLuong().
 *
 * Người phụ trách: NGƯỜI 1
 */
public class QuanLy extends NVFullTime {

    private double phuCapChucVu;

    public QuanLy(String maNV, String hoTen, LocalDate ngaySinh, String gioiTinh,
                  String soDienThoai, String maPhongBan,
                  double luongCoBan, double heSoLuong, double phuCap, double phuCapChucVu) {
        super(maNV, hoTen, ngaySinh, gioiTinh, soDienThoai, maPhongBan,
                luongCoBan, heSoLuong, phuCap);
        setPhuCapChucVu(phuCapChucVu);
    }

    @Override
    public double tinhLuong() {
        return super.tinhLuong() + phuCapChucVu;
    }

    @Override
    public String getLoaiNV() {
        return "Quản lý";
    }

    @Override
    protected String duLieuRiengCSV() {
        return super.duLieuRiengCSV() + ";" + phuCapChucVu;
    }

    @Override
    public String hienThiChiTiet() {
        return super.hienThiChiTiet()
                + "\nPC chức vụ  : " + dinhDangTien(phuCapChucVu);
    }

    public double getPhuCapChucVu() { return phuCapChucVu; }

    public void setPhuCapChucVu(double phuCapChucVu) {
        if (phuCapChucVu < 0) throw new IllegalArgumentException("Phụ cấp chức vụ không được âm");
        this.phuCapChucVu = phuCapChucVu;
    }
}
