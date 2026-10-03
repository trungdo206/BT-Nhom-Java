package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Phòng ban: chứa danh sách nhân viên (quan hệ KẾT TẬP / aggregation 1 - n).
 * Xóa phòng ban thì nhân viên vẫn tồn tại trong công ty.
 *
 * Người phụ trách: NGƯỜI 1
 */
public class PhongBan {

    private String maPB;
    private String tenPB;
    private String maTruongPhong; // mã NV của trưởng phòng, có thể rỗng
    private final List<NhanVien> dsNhanVien = new ArrayList<>();

    public PhongBan(String maPB, String tenPB, String maTruongPhong) {
        setMaPB(maPB);
        setTenPB(tenPB);
        this.maTruongPhong = maTruongPhong == null ? "" : maTruongPhong.trim().toUpperCase();
    }

    public void themNhanVien(NhanVien nv) {
        if (!dsNhanVien.contains(nv)) {
            dsNhanVien.add(nv);
        }
    }

    public boolean xoaNhanVien(NhanVien nv) {
        return dsNhanVien.remove(nv);
    }

    public int getSoLuongNhanVien() {
        return dsNhanVien.size();
    }

    /** Tổng lương phòng ban: gọi tinhLuong() trên từng phần tử -> đa hình lúc chạy. */
    public double tinhTongQuyLuong() {
        double tong = 0;
        for (NhanVien nv : dsNhanVien) {
            tong += nv.tinhLuong();
        }
        return tong;
    }

    /** Trả về bản chỉ đọc để bên ngoài không sửa trực tiếp danh sách (đóng gói). */
    public List<NhanVien> getDsNhanVien() {
        return Collections.unmodifiableList(dsNhanVien);
    }

    public String toCSV() {
        return maPB + ";" + tenPB + ";" + maTruongPhong;
    }

    public String getMaPB() { return maPB; }

    public void setMaPB(String maPB) {
        if (maPB == null || maPB.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phòng ban không được để trống");
        }
        this.maPB = maPB.trim().toUpperCase();
    }

    public String getTenPB() { return tenPB; }

    public void setTenPB(String tenPB) {
        if (tenPB == null || tenPB.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên phòng ban không được để trống");
        }
        this.tenPB = tenPB.trim();
    }

    public String getMaTruongPhong() { return maTruongPhong; }

    public void setMaTruongPhong(String maTruongPhong) {
        this.maTruongPhong = maTruongPhong == null ? "" : maTruongPhong.trim().toUpperCase();
    }

    @Override
    public String toString() {
        return String.format("%-5s | %-20s | Trưởng phòng: %-6s | Số NV: %2d | Quỹ lương: %s",
                maPB, tenPB, maTruongPhong.isEmpty() ? "(chưa)" : maTruongPhong,
                getSoLuongNhanVien(), NhanVien.dinhDangTien(tinhTongQuyLuong()));
    }
}
