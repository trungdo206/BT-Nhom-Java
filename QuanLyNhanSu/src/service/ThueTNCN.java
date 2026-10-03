package service;

/**
 * Thuế thu nhập cá nhân tính theo biểu thuế LŨY TIẾN TỪNG PHẦN.
 * Số liệu (mức giảm trừ, các bậc) là số MINH HỌA cho bài tập,
 * có thể cập nhật theo quy định hiện hành bằng cách sửa các hằng số.
 *
 * Người phụ trách: NGƯỜI 2
 */
public class ThueTNCN implements KhauTru {

    public static final double GIAM_TRU_BAN_THAN = 11_000_000;
    public static final double GIAM_TRU_NGUOI_PHU_THUOC = 4_400_000;

    // Giới hạn trên của từng bậc (thu nhập tính thuế / tháng) và thuế suất tương ứng
    private static final double[] MUC_BAC = {5_000_000, 10_000_000, 18_000_000,
            32_000_000, 52_000_000, 80_000_000, Double.MAX_VALUE};
    private static final double[] THUE_SUAT = {0.05, 0.10, 0.15, 0.20, 0.25, 0.30, 0.35};

    private int soNguoiPhuThuoc;

    public ThueTNCN() {
        this(0);
    }

    public ThueTNCN(int soNguoiPhuThuoc) {
        setSoNguoiPhuThuoc(soNguoiPhuThuoc);
    }

    @Override
    public String getTenKhoan() {
        return "Thuế TNCN";
    }

    /**
     * @param thuNhap thu nhập SAU khi đã trừ bảo hiểm
     */
    @Override
    public double tinhKhauTru(double thuNhap) {
        double thuNhapTinhThue = thuNhap - GIAM_TRU_BAN_THAN
                - soNguoiPhuThuoc * GIAM_TRU_NGUOI_PHU_THUOC;
        if (thuNhapTinhThue <= 0) return 0;

        double thue = 0;
        double canDuoi = 0;
        for (int i = 0; i < MUC_BAC.length; i++) {
            if (thuNhapTinhThue <= canDuoi) break;
            double phanTrongBac = Math.min(thuNhapTinhThue, MUC_BAC[i]) - canDuoi;
            thue += phanTrongBac * THUE_SUAT[i];
            canDuoi = MUC_BAC[i];
        }
        return thue;
    }

    public int getSoNguoiPhuThuoc() { return soNguoiPhuThuoc; }

    public void setSoNguoiPhuThuoc(int soNguoiPhuThuoc) {
        if (soNguoiPhuThuoc < 0) throw new IllegalArgumentException("Số người phụ thuộc không được âm");
        this.soNguoiPhuThuoc = soNguoiPhuThuoc;
    }
}
