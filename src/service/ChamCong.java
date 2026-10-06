package service;

import exception.DuLieuKhongHopLeException;

/**
 * Bảng chấm công của 1 nhân viên trong 1 tháng.
 *
 * Người phụ trách: NGƯỜI 2
 */
public class ChamCong {

    public static final int SO_NGAY_CONG_CHUAN = 26;
    public static final int GIO_TANG_CA_TOI_DA = 40; // giới hạn giờ tăng ca / tháng

    private final String maNV;
    private final int thang;
    private final int nam;
    private int soNgayNghiKhongPhep;
    private int soGioTangCa;

    public ChamCong(String maNV, int thang, int nam, int soNgayNghiKhongPhep, int soGioTangCa) {
        if (thang < 1 || thang > 12) throw new DuLieuKhongHopLeException("Tháng phải từ 1 đến 12");
        if (nam < 2000) throw new DuLieuKhongHopLeException("Năm không hợp lệ");
        this.maNV = maNV.trim().toUpperCase();
        this.thang = thang;
        this.nam = nam;
        setSoNgayNghiKhongPhep(soNgayNghiKhongPhep);
        setSoGioTangCa(soGioTangCa);
    }

    public String getMaNV() { return maNV; }

    public int getThang() { return thang; }

    public int getNam() { return nam; }

    public int getSoNgayNghiKhongPhep() { return soNgayNghiKhongPhep; }

    public void setSoNgayNghiKhongPhep(int soNgay) {
        if (soNgay < 0 || soNgay > SO_NGAY_CONG_CHUAN) {
            throw new DuLieuKhongHopLeException("Số ngày nghỉ phải từ 0 đến " + SO_NGAY_CONG_CHUAN);
        }
        this.soNgayNghiKhongPhep = soNgay;
    }

    public int getSoGioTangCa() { return soGioTangCa; }

    public void setSoGioTangCa(int soGio) {
        if (soGio < 0 || soGio > GIO_TANG_CA_TOI_DA) {
            throw new DuLieuKhongHopLeException("Giờ tăng ca phải từ 0 đến " + GIO_TANG_CA_TOI_DA);
        }
        this.soGioTangCa = soGio;
    }

    public String toCSV() {
        return maNV + ";" + thang + ";" + nam + ";" + soNgayNghiKhongPhep + ";" + soGioTangCa;
    }

    @Override
    public String toString() {
        return String.format("%-6s | %02d/%d | Nghỉ không phép: %2d ngày | Tăng ca: %2d giờ",
                maNV, thang, nam, soNgayNghiKhongPhep, soGioTangCa);
    }
}
