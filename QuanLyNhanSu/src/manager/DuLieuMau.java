package manager;

import model.NVFullTime;
import model.NVPartTime;
import model.PhongBan;
import model.QuanLy;
import model.ThucTapSinh;

import java.time.LocalDate;

/**
 * Dữ liệu mẫu dùng khi chạy lần đầu (chưa có file data/).
 *
 * Người phụ trách: NGƯỜI 3
 */
public class DuLieuMau {

    private DuLieuMau() {
    }

    public static void nap(QuanLyNhanSu ql) {
        ql.themPhongBan(new PhongBan("KT", "Kỹ thuật", ""));
        ql.themPhongBan(new PhongBan("KD", "Kinh doanh", ""));
        ql.themPhongBan(new PhongBan("NS", "Nhân sự", ""));

        ql.them(new QuanLy("NV001", "Nguyễn Văn An", LocalDate.of(1985, 3, 12), "Nam",
                "0901234567", "KT", 5_000_000, 4.5, 1_000_000, 5_000_000));
        ql.them(new NVFullTime("NV002", "Trần Thị Bình", LocalDate.of(1995, 7, 20), "Nữ",
                "0912345678", "KT", 5_000_000, 2.8, 800_000));
        ql.them(new NVFullTime("NV003", "Lê Hoàng Cường", LocalDate.of(1998, 11, 5), "Nam",
                "0923456789", "KD", 5_000_000, 2.2, 1_500_000));
        ql.them(new QuanLy("NV004", "Phạm Thu Dung", LocalDate.of(1988, 1, 30), "Nữ",
                "0934567890", "KD", 5_000_000, 4.0, 1_000_000, 4_000_000));
        ql.them(new NVPartTime("NV005", "Võ Minh Em", LocalDate.of(2003, 9, 9), "Nam",
                "0945678901", "KD", 80, 35_000));
        ql.them(new ThucTapSinh("NV006", "Đặng Gia Hân", LocalDate.of(2004, 5, 15), "Nữ",
                "0956789012", "KT", 3_000_000, "Học viện Công nghệ BCVT"));
        ql.them(new NVFullTime("NV007", "Bùi Quốc Khánh", LocalDate.of(1992, 12, 1), "Nam",
                "0967890123", "NS", 5_000_000, 3.2, 700_000));

        try {
            ql.ganTruongPhong("KT", "NV001");
            ql.ganTruongPhong("KD", "NV004");
        } catch (Exception e) {
            // dữ liệu mẫu cố định nên không xảy ra
        }
    }
}
