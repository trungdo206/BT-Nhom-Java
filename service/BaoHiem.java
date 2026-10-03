package service;

/**
 * Bảo hiểm bắt buộc phần người lao động đóng:
 * BHXH 8% + BHYT 1.5% + BHTN 1% = 10.5% lương.
 * (Tỷ lệ dùng cho bài tập, có thể chỉnh trong hằng số.)
 *
 * Người phụ trách: NGƯỜI 2
 */
public class BaoHiem implements KhauTru {

    public static final double TY_LE_BHXH = 0.08;
    public static final double TY_LE_BHYT = 0.015;
    public static final double TY_LE_BHTN = 0.01;

    @Override
    public String getTenKhoan() {
        return "Bảo hiểm (BHXH+BHYT+BHTN)";
    }

    @Override
    public double tinhKhauTru(double thuNhap) {
        if (thuNhap <= 0) return 0;
        return thuNhap * (TY_LE_BHXH + TY_LE_BHYT + TY_LE_BHTN);
    }
}
