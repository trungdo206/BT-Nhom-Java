package service;

import model.NhanVien;

/**
 * Bảng lương tháng của 1 nhân viên.
 * - Kết hợp (association) với NhanVien và ChamCong.
 * - Thành phần (composition) với các đối tượng KhauTru: BaoHiem, ThueTNCN được tạo bên trong.
 * - Gọi nv.tinhLuong() mà không cần biết nv là loại gì -> ĐA HÌNH.
 *
 * Công thức:
 *   lương tháng       = nv.tinhLuong()
 *   tiền tăng ca      = giờ tăng ca x (lương / 26 / 8) x 1.5
 *   trừ ngày nghỉ     = lương / 26 x số ngày nghỉ không phép
 *   tổng thu nhập     = lương tháng + tăng ca - trừ ngày nghỉ
 *   bảo hiểm          = 10.5% lương tháng (chỉ khi nv.coDongBaoHiem())
 *   thuế TNCN         = lũy tiến trên (tổng thu nhập - bảo hiểm)
 *   thực lĩnh         = tổng thu nhập - bảo hiểm - thuế
 *
 * Người phụ trách: NGƯỜI 2
 */
public class BangLuong {

    public static final double HE_SO_TANG_CA = 1.5;
    public static final int SO_GIO_MOT_NGAY = 8;

    private final NhanVien nhanVien;
    private final ChamCong chamCong;
    private final KhauTru baoHiem;
    private final KhauTru thueTNCN;

    private double luongThang;
    private double tienTangCa;
    private double truNgayNghi;
    private double tongThuNhap;
    private double tienBaoHiem;
    private double tienThue;
    private double thucLinh;

    public BangLuong(NhanVien nhanVien, ChamCong chamCong) {
        this(nhanVien, chamCong, 0);
    }

    public BangLuong(NhanVien nhanVien, ChamCong chamCong, int soNguoiPhuThuoc) {
        if (nhanVien == null || chamCong == null) {
            throw new IllegalArgumentException("Thiếu nhân viên hoặc chấm công");
        }
        if (!nhanVien.getMaNV().equals(chamCong.getMaNV())) {
            throw new IllegalArgumentException("Chấm công không thuộc nhân viên " + nhanVien.getMaNV());
        }
        this.nhanVien = nhanVien;
        this.chamCong = chamCong;
        this.baoHiem = new BaoHiem();
        this.thueTNCN = new ThueTNCN(soNguoiPhuThuoc);
        tinhToan();
    }

    private void tinhToan() {
        luongThang = nhanVien.tinhLuong();
        double luongMotNgay = luongThang / ChamCong.SO_NGAY_CONG_CHUAN;
        double luongMotGio = luongMotNgay / SO_GIO_MOT_NGAY;

        tienTangCa = chamCong.getSoGioTangCa() * luongMotGio * HE_SO_TANG_CA;
        truNgayNghi = chamCong.getSoNgayNghiKhongPhep() * luongMotNgay;
        tongThuNhap = luongThang + tienTangCa - truNgayNghi;

        tienBaoHiem = nhanVien.coDongBaoHiem() ? baoHiem.tinhKhauTru(luongThang) : 0;
        tienThue = thueTNCN.tinhKhauTru(tongThuNhap - tienBaoHiem);
        thucLinh = tongThuNhap - tienBaoHiem - tienThue;
    }

    public String inPhieuLuong() {
        String vach = "-------------------------------------------";
        return vach
                + String.format("%nPHIẾU LƯƠNG THÁNG %02d/%d", chamCong.getThang(), chamCong.getNam())
                + "\n" + vach
                + "\nNhân viên       : " + nhanVien.getMaNV() + " - " + nhanVien.getHoTen()
                + "\nLoại            : " + nhanVien.getLoaiNV()
                + "\nLương tháng     : " + NhanVien.dinhDangTien(luongThang)
                + "\n(+) Tăng ca     : " + NhanVien.dinhDangTien(tienTangCa)
                + "\n(-) Nghỉ KP     : " + NhanVien.dinhDangTien(truNgayNghi)
                + "\nTổng thu nhập   : " + NhanVien.dinhDangTien(tongThuNhap)
                + "\n(-) " + baoHiem.getTenKhoan() + ": " + NhanVien.dinhDangTien(tienBaoHiem)
                + "\n(-) " + thueTNCN.getTenKhoan() + "       : " + NhanVien.dinhDangTien(tienThue)
                + "\n" + vach
                + "\nTHỰC LĨNH       : " + NhanVien.dinhDangTien(thucLinh)
                + "\n" + vach;
    }

    public String toCSV() {
        return String.format("%s;%s;%s;%.0f;%.0f;%.0f;%.0f;%.0f;%.0f",
                nhanVien.getMaNV(), nhanVien.getHoTen(), nhanVien.getLoaiNV(),
                luongThang, tienTangCa, truNgayNghi, tienBaoHiem, tienThue, thucLinh);
    }

    public NhanVien getNhanVien() { return nhanVien; }

    public ChamCong getChamCong() { return chamCong; }

    public double getLuongThang() { return luongThang; }

    public double getTienTangCa() { return tienTangCa; }

    public double getTruNgayNghi() { return truNgayNghi; }

    public double getTongThuNhap() { return tongThuNhap; }

    public double getTienBaoHiem() { return tienBaoHiem; }

    public double getTienThue() { return tienThue; }

    public double getThucLinh() { return thucLinh; }

    @Override
    public String toString() {
        return String.format("%-6s | %-22s | %16s | %14s | %14s | %16s",
                nhanVien.getMaNV(), nhanVien.getHoTen(),
                NhanVien.dinhDangTien(tongThuNhap), NhanVien.dinhDangTien(tienBaoHiem),
                NhanVien.dinhDangTien(tienThue), NhanVien.dinhDangTien(thucLinh));
    }
}
