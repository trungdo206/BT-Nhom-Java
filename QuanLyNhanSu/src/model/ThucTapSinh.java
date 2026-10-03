package model;

import java.time.LocalDate;

/**
 * Thực tập sinh: nhận trợ cấp cố định, không đóng bảo hiểm.
 *
 * Người phụ trách: NGƯỜI 1
 */
public class ThucTapSinh extends NhanVien {

    private double troCap;
    private String truongHoc;

    public ThucTapSinh(String maNV, String hoTen, LocalDate ngaySinh, String gioiTinh,
                       String soDienThoai, String maPhongBan,
                       double troCap, String truongHoc) {
        super(maNV, hoTen, ngaySinh, gioiTinh, soDienThoai, maPhongBan);
        setTroCap(troCap);
        setTruongHoc(truongHoc);
    }

    @Override
    public double tinhLuong() {
        return troCap;
    }

    @Override
    public String getLoaiNV() {
        return "Thực tập sinh";
    }

    @Override
    public boolean coDongBaoHiem() {
        return false;
    }

    @Override
    protected String duLieuRiengCSV() {
        return troCap + ";" + truongHoc;
    }

    @Override
    public String hienThiChiTiet() {
        return super.hienThiChiTiet()
                + "\nTrường học  : " + truongHoc
                + "\nTrợ cấp     : " + dinhDangTien(troCap);
    }

    public double getTroCap() { return troCap; }

    public void setTroCap(double troCap) {
        if (troCap < 0) throw new IllegalArgumentException("Trợ cấp không được âm");
        this.troCap = troCap;
    }

    public String getTruongHoc() { return truongHoc; }

    public void setTruongHoc(String truongHoc) {
        if (truongHoc == null || truongHoc.trim().isEmpty()) {
            throw new IllegalArgumentException("Trường học không được để trống");
        }
        this.truongHoc = truongHoc.trim();
    }
}
