package service;

/**
 * Interface cho mọi khoản khấu trừ vào lương (bảo hiểm, thuế, ...).
 * Thêm khoản khấu trừ mới chỉ cần tạo lớp mới implements KhauTru,
 * không phải sửa BangLuong (nguyên lý Open/Closed).
 *
 * Người phụ trách: NGƯỜI 2
 */
public interface KhauTru {

    /** Tên khoản khấu trừ, ví dụ "Bảo hiểm", "Thuế TNCN". */
    String getTenKhoan();

    /** Số tiền bị trừ dựa trên thu nhập truyền vào. */
    double tinhKhauTru(double thuNhap);
}
