package model;

import java.time.LocalDate;

/**
 * Nhân viên bán thời gian: lương = số giờ làm x đơn giá giờ. Không đóng bảo hiểm.
 *
 * Người phụ trách: NGƯỜI 1
 */
public class NVPartTime extends NhanVien {

    private int soGioLam;
    private double donGiaGio;

    public NVPartTime(String maNV, String hoTen, LocalDate ngaySinh, String gioiTinh,
                      String soDienThoai, String maPhongBan,
                      int soGioLam, double donGiaGio) {
        super(maNV, hoTen, ngaySinh, gioiTinh, soDienThoai, maPhongBan);
        setSoGioLam(soGioLam);
        setDonGiaGio(donGiaGio);
    }

    @Override
    public double tinhLuong() {
        return soGioLam * donGiaGio;
    }

    @Override
    public String getLoaiNV() {
        return "Bán thời gian";
    }

    @Override
    public boolean coDongBaoHiem() {
        return false;
    }

    @Override
    protected String duLieuRiengCSV() {
        return soGioLam + ";" + donGiaGio;
    }

    @Override
    public String hienThiChiTiet() {
        return super.hienThiChiTiet()
                + "\nSố giờ làm  : " + soGioLam
                + "\nĐơn giá giờ : " + dinhDangTien(donGiaGio)
                + "\nTổng lương  : " + dinhDangTien(tinhLuong());
    }

    public int getSoGioLam() { return soGioLam; }

    public void setSoGioLam(int soGioLam) {
        if (soGioLam < 0 || soGioLam > 200) {
            throw new IllegalArgumentException("Số giờ làm phải trong khoảng 0 - 200");
        }
        this.soGioLam = soGioLam;
    }

    public double getDonGiaGio() { return donGiaGio; }

    public void setDonGiaGio(double donGiaGio) {
        if (donGiaGio <= 0) throw new IllegalArgumentException("Đơn giá giờ phải > 0");
        this.donGiaGio = donGiaGio;
    }
}
