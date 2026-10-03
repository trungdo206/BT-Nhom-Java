package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Lớp trừu tượng NhanVien - lớp cha của mọi loại nhân viên.
 * Thể hiện: TRỪU TƯỢNG (abstract), ĐÓNG GÓI (private + getter/setter có kiểm tra),
 * và là gốc cho KẾ THỪA / ĐA HÌNH (tinhLuong() mỗi lớp con tính một kiểu).
 *
 * Người phụ trách: NGƯỜI 1
 */
public abstract class NhanVien implements Comparable<NhanVien> {

    public static final DateTimeFormatter DINH_DANG_NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Thuộc tính static: dùng chung cho mọi đối tượng, đếm số nhân viên đã tạo
    private static int soLuongDaTao = 0;

    private String maNV;
    private String hoTen;
    private LocalDate ngaySinh;
    private String gioiTinh;
    private String soDienThoai;
    private String maPhongBan;

    protected NhanVien(String maNV, String hoTen, LocalDate ngaySinh,
                       String gioiTinh, String soDienThoai, String maPhongBan) {
        setMaNV(maNV);
        setHoTen(hoTen);
        setNgaySinh(ngaySinh);
        setGioiTinh(gioiTinh);
        setSoDienThoai(soDienThoai);
        setMaPhongBan(maPhongBan);
        soLuongDaTao++;
    }

    // ===================== PHƯƠNG THỨC TRỪU TƯỢNG =====================

    /** Lương tháng (chưa trừ bảo hiểm, thuế). Mỗi lớp con tự cài đặt -> ĐA HÌNH. */
    public abstract double tinhLuong();

    /** Tên loại nhân viên để hiển thị. */
    public abstract String getLoaiNV();

    /** Phần dữ liệu riêng của lớp con khi ghi file (ngăn cách bằng ';'). */
    protected abstract String duLieuRiengCSV();

    // ===================== PHƯƠNG THỨC CÓ THỂ GHI ĐÈ =====================

    /** Mặc định nhân viên có đóng bảo hiểm. Thực tập sinh, part-time ghi đè trả về false. */
    public boolean coDongBaoHiem() {
        return true;
    }

    /** In thông tin chi tiết; lớp con gọi super.hienThiChiTiet() rồi in thêm phần riêng. */
    public String hienThiChiTiet() {
        return "Mã NV       : " + maNV
                + "\nHọ tên      : " + hoTen
                + "\nNgày sinh   : " + ngaySinh.format(DINH_DANG_NGAY)
                + "\nGiới tính   : " + gioiTinh
                + "\nSĐT         : " + soDienThoai
                + "\nPhòng ban   : " + maPhongBan
                + "\nLoại NV     : " + getLoaiNV();
    }

    /**
     * Chuỗi để ghi file. Phần chung viết ở lớp cha, phần riêng do lớp con cung cấp
     * (mẫu thiết kế Template Method).
     */
    public final String toCSV() {
        return getClass().getSimpleName() + ";" + maNV + ";" + hoTen + ";"
                + ngaySinh.format(DINH_DANG_NGAY) + ";" + gioiTinh + ";"
                + soDienThoai + ";" + maPhongBan + ";" + duLieuRiengCSV();
    }

    public int getTuoi() {
        return LocalDate.now().getYear() - ngaySinh.getYear();
    }

    public static String dinhDangTien(double soTien) {
        return String.format("%,.0f đ", soTien);
    }

    // ===================== GETTER / SETTER (ĐÓNG GÓI) =====================

    public String getMaNV() { return maNV; }

    public void setMaNV(String maNV) {
        if (maNV == null || maNV.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã nhân viên không được để trống");
        }
        this.maNV = maNV.trim().toUpperCase();
    }

    public String getHoTen() { return hoTen; }

    public void setHoTen(String hoTen) {
        if (hoTen == null || hoTen.trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được để trống");
        }
        this.hoTen = hoTen.trim();
    }

    public LocalDate getNgaySinh() { return ngaySinh; }

    public void setNgaySinh(LocalDate ngaySinh) {
        if (ngaySinh == null || ngaySinh.isAfter(LocalDate.now().minusYears(16))) {
            throw new IllegalArgumentException("Nhân viên phải từ 16 tuổi trở lên");
        }
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() { return gioiTinh; }

    public void setGioiTinh(String gioiTinh) {
        if (!"Nam".equalsIgnoreCase(gioiTinh) && !"Nữ".equalsIgnoreCase(gioiTinh)) {
            throw new IllegalArgumentException("Giới tính phải là Nam hoặc Nữ");
        }
        this.gioiTinh = gioiTinh.equalsIgnoreCase("Nam") ? "Nam" : "Nữ";
    }

    public String getSoDienThoai() { return soDienThoai; }

    public void setSoDienThoai(String soDienThoai) {
        if (soDienThoai == null || !soDienThoai.matches("0\\d{9}")) {
            throw new IllegalArgumentException("SĐT phải gồm 10 chữ số và bắt đầu bằng 0");
        }
        this.soDienThoai = soDienThoai;
    }

    public String getMaPhongBan() { return maPhongBan; }

    public void setMaPhongBan(String maPhongBan) {
        if (maPhongBan == null || maPhongBan.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phòng ban không được để trống");
        }
        this.maPhongBan = maPhongBan.trim().toUpperCase();
    }

    public static int getSoLuongDaTao() { return soLuongDaTao; }

    // ===================== GHI ĐÈ CÁC PHƯƠNG THỨC CỦA Object =====================

    @Override
    public int compareTo(NhanVien khac) {
        return this.maNV.compareTo(khac.maNV);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NhanVien)) return false;
        return maNV.equals(((NhanVien) o).maNV);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maNV);
    }

    @Override
    public String toString() {
        return String.format("%-6s | %-22s | %-15s | %-5s | %18s",
                maNV, hoTen, getLoaiNV(), maPhongBan, dinhDangTien(tinhLuong()));
    }
}
