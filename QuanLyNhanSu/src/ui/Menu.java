package ui;

import exception.NhanVienKhongTonTaiException;
import manager.DuLieuMau;
import manager.FileHandler;
import manager.QuanLyNhanSu;
import model.NVFullTime;
import model.NVPartTime;
import model.NhanVien;
import model.PhongBan;
import model.QuanLy;
import model.ThucTapSinh;
import service.BangLuong;
import service.ChamCong;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Giao diện dòng lệnh (console). Chỉ lo nhập / xuất, mọi nghiệp vụ gọi sang QuanLyNhanSu.
 *
 * Người phụ trách: NGƯỜI 3
 */
public class Menu {

    private final Scanner sc = new Scanner(System.in);
    private final QuanLyNhanSu ql = new QuanLyNhanSu();
    private boolean daThayDoi = false;

    public void chay() {
        napDuLieu();
        int chon;
        do {
            inMenu();
            try {
                chon = nhapSoNguyen("Chọn chức năng: ", 0, 11);
            } catch (IllegalStateException e) { // hết dữ liệu nhập (Ctrl+D / Ctrl+Z)
                return;
            }
            try {
                xuLy(chon);
            } catch (NhanVienKhongTonTaiException e) {
                System.out.println(">> Lỗi: " + e.getMessage());
            } catch (IllegalArgumentException | IllegalStateException e) {
                // bắt cả DuLieuKhongHopLeException (là RuntimeException)
                System.out.println(">> Lỗi dữ liệu: " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println(">> Lỗi: " + e.getMessage());
            }
        } while (chon != 0);
    }

    private void inMenu() {
        System.out.println("\n================ QUẢN LÝ NHÂN SỰ & TÍNH LƯƠNG ================");
        System.out.println(" 1. Hiển thị danh sách nhân viên");
        System.out.println(" 2. Thêm nhân viên");
        System.out.println(" 3. Sửa thông tin nhân viên");
        System.out.println(" 4. Xóa nhân viên");
        System.out.println(" 5. Tìm kiếm nhân viên");
        System.out.println(" 6. Sắp xếp danh sách");
        System.out.println(" 7. Quản lý phòng ban");
        System.out.println(" 8. Chấm công");
        System.out.println(" 9. Lập bảng lương tháng");
        System.out.println("10. Thống kê");
        System.out.println("11. Lưu dữ liệu");
        System.out.println(" 0. Thoát");
        System.out.println("===============================================================");
    }

    private void xuLy(int chon) throws NhanVienKhongTonTaiException {
        switch (chon) {
            case 1: inDanhSach(ql.layTatCa()); break;
            case 2: themNhanVien(); break;
            case 3: suaNhanVien(); break;
            case 4: xoaNhanVien(); break;
            case 5: timKiem(); break;
            case 6: sapXep(); break;
            case 7: quanLyPhongBan(); break;
            case 8: chamCong(); break;
            case 9: lapBangLuong(); break;
            case 10: thongKe(); break;
            case 11: luuDuLieu(); break;
            case 0: thoat(); break;
            default: break;
        }
    }

    // ===================== CÁC CHỨC NĂNG =====================

    private void inDanhSach(List<NhanVien> ds) {
        if (ds.isEmpty()) {
            System.out.println("(Danh sách trống)");
            return;
        }
        System.out.printf("%-6s | %-22s | %-15s | %-5s | %18s%n", "Mã", "Họ tên", "Loại", "PB", "Lương");
        System.out.println("-".repeat(78));
        for (NhanVien nv : ds) {
            System.out.println(nv); // gọi toString() -> bên trong gọi tinhLuong() đa hình
        }
        System.out.println("Tổng: " + ds.size() + " nhân viên");
    }

    private void themNhanVien() {
        System.out.println("Loại: 1. Toàn thời gian  2. Quản lý  3. Bán thời gian  4. Thực tập sinh");
        int loai = nhapSoNguyen("Chọn loại: ", 1, 4);

        String ma = nhapChuoi("Mã NV: ");
        if (ql.timTheoMa(ma) != null) {
            System.out.println(">> Mã đã tồn tại!");
            return;
        }
        String ten = nhapChuoi("Họ tên: ");
        LocalDate ngaySinh = nhapNgay("Ngày sinh (dd/MM/yyyy): ");
        String gt = nhapChuoi("Giới tính (Nam/Nữ): ");
        String sdt = nhapChuoi("SĐT: ");
        inDsPhongBanNgan();
        String pb = nhapChuoi("Mã phòng ban: ");

        NhanVien nv;
        switch (loai) {
            case 1:
                nv = new NVFullTime(ma, ten, ngaySinh, gt, sdt, pb,
                        nhapSoThuc("Lương cơ bản: "), nhapSoThuc("Hệ số lương: "), nhapSoThuc("Phụ cấp: "));
                break;
            case 2:
                nv = new QuanLy(ma, ten, ngaySinh, gt, sdt, pb,
                        nhapSoThuc("Lương cơ bản: "), nhapSoThuc("Hệ số lương: "),
                        nhapSoThuc("Phụ cấp: "), nhapSoThuc("Phụ cấp chức vụ: "));
                break;
            case 3:
                nv = new NVPartTime(ma, ten, ngaySinh, gt, sdt, pb,
                        nhapSoNguyen("Số giờ làm: ", 0, 200), nhapSoThuc("Đơn giá giờ: "));
                break;
            default:
                nv = new ThucTapSinh(ma, ten, ngaySinh, gt, sdt, pb,
                        nhapSoThuc("Trợ cấp: "), nhapChuoi("Trường học: "));
                break;
        }
        ql.them(nv);
        daThayDoi = true;
        System.out.println(">> Đã thêm: " + nv.getHoTen());
    }

    private void suaNhanVien() throws NhanVienKhongTonTaiException {
        NhanVien nv = ql.layNhanVien(nhapChuoi("Mã NV cần sửa: "));
        System.out.println(nv.hienThiChiTiet());
        System.out.println("(Bỏ trống = giữ nguyên)");

        String ten = nhapChuoiCoTheRong("Họ tên mới: ");
        if (!ten.isEmpty()) nv.setHoTen(ten);
        String sdt = nhapChuoiCoTheRong("SĐT mới: ");
        if (!sdt.isEmpty()) nv.setSoDienThoai(sdt);
        String pb = nhapChuoiCoTheRong("Chuyển sang phòng ban (mã): ");
        if (!pb.isEmpty()) ql.chuyenPhongBan(nv.getMaNV(), pb);

        // Phần riêng theo từng loại: dùng instanceof để ép kiểu xuống (downcasting)
        if (nv instanceof NVFullTime) {
            NVFullTime ft = (NVFullTime) nv;
            String hs = nhapChuoiCoTheRong("Hệ số lương mới: ");
            if (!hs.isEmpty()) ft.setHeSoLuong(Double.parseDouble(hs));
        } else if (nv instanceof NVPartTime) {
            NVPartTime pt = (NVPartTime) nv;
            String gio = nhapChuoiCoTheRong("Số giờ làm mới: ");
            if (!gio.isEmpty()) pt.setSoGioLam(Integer.parseInt(gio));
        } else if (nv instanceof ThucTapSinh) {
            ThucTapSinh tts = (ThucTapSinh) nv;
            String tc = nhapChuoiCoTheRong("Trợ cấp mới: ");
            if (!tc.isEmpty()) tts.setTroCap(Double.parseDouble(tc));
        }
        daThayDoi = true;
        System.out.println(">> Đã cập nhật.");
    }

    private void xoaNhanVien() throws NhanVienKhongTonTaiException {
        NhanVien nv = ql.layNhanVien(nhapChuoi("Mã NV cần xóa: "));
        String xn = nhapChuoi("Xóa " + nv.getHoTen() + "? (y/n): ");
        if (xn.equalsIgnoreCase("y")) {
            ql.xoa(nv.getMaNV());
            daThayDoi = true;
            System.out.println(">> Đã xóa.");
        }
    }

    private void timKiem() throws NhanVienKhongTonTaiException {
        System.out.println("1. Theo mã   2. Theo tên   3. Theo loại");
        int cach = nhapSoNguyen("Chọn: ", 1, 3);
        if (cach == 1) {
            System.out.println(ql.layNhanVien(nhapChuoi("Mã NV: ")).hienThiChiTiet());
        } else if (cach == 2) {
            inDanhSach(ql.timTheoTen(nhapChuoi("Từ khóa tên: ")));
        } else {
            System.out.println("1. Toàn thời gian  2. Quản lý  3. Bán thời gian  4. Thực tập sinh");
            int loai = nhapSoNguyen("Chọn: ", 1, 4);
            List<Class<? extends NhanVien>> cacLoai = List.of(
                    NVFullTime.class, QuanLy.class, NVPartTime.class, ThucTapSinh.class);
            inDanhSach(ql.locTheoLoai(cacLoai.get(loai - 1)));
        }
    }

    private void sapXep() {
        System.out.println("1. Theo lương giảm dần   2. Theo tên   3. Theo mã");
        int cach = nhapSoNguyen("Chọn: ", 1, 3);
        if (cach == 1) inDanhSach(ql.sapXepTheoLuongGiamDan());
        else if (cach == 2) inDanhSach(ql.sapXepTheoTen());
        else inDanhSach(ql.sapXepTheoMa());
    }

    private void quanLyPhongBan() throws NhanVienKhongTonTaiException {
        System.out.println("1. Xem phòng ban   2. Thêm phòng ban   3. Gán trưởng phòng   4. Xem NV của phòng");
        int cach = nhapSoNguyen("Chọn: ", 1, 4);
        switch (cach) {
            case 1:
                for (PhongBan pb : ql.layDsPhongBan()) System.out.println(pb);
                break;
            case 2:
                ql.themPhongBan(new PhongBan(nhapChuoi("Mã PB: "), nhapChuoi("Tên PB: "), ""));
                daThayDoi = true;
                System.out.println(">> Đã thêm phòng ban.");
                break;
            case 3:
                ql.ganTruongPhong(nhapChuoi("Mã PB: "), nhapChuoi("Mã NV trưởng phòng: "));
                daThayDoi = true;
                System.out.println(">> Đã gán trưởng phòng.");
                break;
            default:
                PhongBan pb = ql.timPhongBan(nhapChuoi("Mã PB: "));
                if (pb == null) System.out.println(">> Không có phòng ban này.");
                else inDanhSach(pb.getDsNhanVien());
                break;
        }
    }

    private void chamCong() throws NhanVienKhongTonTaiException {
        System.out.println("1. Nhập chấm công   2. Xem bảng chấm công");
        if (nhapSoNguyen("Chọn: ", 1, 2) == 2) {
            List<ChamCong> ds = ql.layDsChamCong();
            if (ds.isEmpty()) System.out.println("(Chưa có dữ liệu chấm công)");
            for (ChamCong cc : ds) System.out.println(cc);
            return;
        }
        NhanVien nv = ql.layNhanVien(nhapChuoi("Mã NV: "));
        int thang = nhapSoNguyen("Tháng: ", 1, 12);
        int nam = nhapSoNguyen("Năm: ", 2000, 2100);
        int nghi = nhapSoNguyen("Số ngày nghỉ không phép: ", 0, ChamCong.SO_NGAY_CONG_CHUAN);
        int tangCa = nhapSoNguyen("Số giờ tăng ca: ", 0, ChamCong.GIO_TANG_CA_TOI_DA);
        ql.capNhatChamCong(new ChamCong(nv.getMaNV(), thang, nam, nghi, tangCa));
        daThayDoi = true;
        System.out.println(">> Đã chấm công cho " + nv.getHoTen());
    }

    private void lapBangLuong() throws NhanVienKhongTonTaiException {
        int thang = nhapSoNguyen("Tháng: ", 1, 12);
        int nam = nhapSoNguyen("Năm: ", 2000, 2100);
        List<BangLuong> ds = ql.lapBangLuong(thang, nam);

        System.out.printf("%-6s | %-22s | %16s | %14s | %14s | %16s%n",
                "Mã", "Họ tên", "Tổng thu nhập", "Bảo hiểm", "Thuế", "Thực lĩnh");
        System.out.println("-".repeat(102));
        double tong = 0;
        for (BangLuong bl : ds) {
            System.out.println(bl);
            tong += bl.getThucLinh();
        }
        System.out.println("Tổng thực lĩnh toàn công ty: " + NhanVien.dinhDangTien(tong));

        System.out.println("1. Xem phiếu lương 1 người   2. Xuất file CSV   0. Quay lại");
        int chon = nhapSoNguyen("Chọn: ", 0, 2);
        if (chon == 1) {
            String ma = ql.layNhanVien(nhapChuoi("Mã NV: ")).getMaNV();
            for (BangLuong bl : ds) {
                if (bl.getNhanVien().getMaNV().equals(ma)) System.out.println(bl.inPhieuLuong());
            }
        } else if (chon == 2) {
            try {
                System.out.println(">> Đã xuất: " + FileHandler.xuatBangLuong(ds, thang, nam));
            } catch (IOException e) {
                System.out.println(">> Không ghi được file: " + e.getMessage());
            }
        }
    }

    private void thongKe() {
        System.out.println("Tổng số nhân viên : " + ql.getSoLuongNhanVien());
        System.out.println("Tổng quỹ lương    : " + NhanVien.dinhDangTien(ql.tinhTongQuyLuong()));
        System.out.println("Lương trung bình  : " + NhanVien.dinhDangTien(ql.tinhLuongTrungBinh()));
        NhanVien max = ql.timNguoiLuongCaoNhat();
        if (max != null) {
            System.out.println("Lương cao nhất    : " + max.getHoTen()
                    + " (" + NhanVien.dinhDangTien(max.tinhLuong()) + ")");
        }
        System.out.println("Theo loại:");
        for (Map.Entry<String, Integer> e : ql.thongKeTheoLoai().entrySet()) {
            System.out.printf("   - %-15s: %d%n", e.getKey(), e.getValue());
        }
        System.out.println("Theo phòng ban:");
        for (PhongBan pb : ql.layDsPhongBan()) {
            System.out.println("   " + pb);
        }
    }

    // ===================== LƯU / ĐỌC =====================

    private void napDuLieu() {
        if (FileHandler.coDuLieu()) {
            try {
                int loi = FileHandler.docTatCa(ql);
                System.out.println(">> Đã đọc " + ql.getSoLuongNhanVien() + " nhân viên từ file"
                        + (loi > 0 ? " (bỏ qua " + loi + " dòng lỗi)" : ""));
                return;
            } catch (IOException e) {
                System.out.println(">> Không đọc được file, dùng dữ liệu mẫu: " + e.getMessage());
            }
        }
        DuLieuMau.nap(ql);
        daThayDoi = true;
        System.out.println(">> Đã nạp dữ liệu mẫu (" + ql.getSoLuongNhanVien() + " nhân viên)");
    }

    private void luuDuLieu() {
        try {
            FileHandler.luuTatCa(ql);
            daThayDoi = false;
            System.out.println(">> Đã lưu vào thư mục " + FileHandler.THU_MUC + "/");
        } catch (IOException e) {
            System.out.println(">> Lỗi khi lưu: " + e.getMessage());
        }
    }

    private void thoat() {
        if (daThayDoi && nhapChuoi("Có thay đổi chưa lưu. Lưu trước khi thoát? (y/n): ")
                .equalsIgnoreCase("y")) {
            luuDuLieu();
        }
        System.out.println("Tạm biệt!");
    }

    // ===================== HÀM NHẬP LIỆU (có kiểm tra, nhập lại khi sai) =====================

    private void inDsPhongBanNgan() {
        StringBuilder sb = new StringBuilder("Phòng ban hiện có: ");
        for (PhongBan pb : ql.layDsPhongBan()) {
            sb.append(pb.getMaPB()).append(" (").append(pb.getTenPB()).append(")  ");
        }
        System.out.println(sb);
    }

    private String docDong() {
        if (!sc.hasNextLine()) {
            throw new IllegalStateException("Hết dữ liệu nhập");
        }
        return sc.nextLine().trim();
    }

    private String nhapChuoi(String nhac) {
        while (true) {
            System.out.print(nhac);
            String s = docDong();
            if (!s.isEmpty()) return s;
            System.out.println("   Không được để trống!");
        }
    }

    private String nhapChuoiCoTheRong(String nhac) {
        System.out.print(nhac);
        return docDong();
    }

    private int nhapSoNguyen(String nhac, int min, int max) {
        while (true) {
            System.out.print(nhac);
            try {
                int so = Integer.parseInt(docDong());
                if (so >= min && so <= max) return so;
                System.out.println("   Phải từ " + min + " đến " + max);
            } catch (NumberFormatException e) {
                System.out.println("   Vui lòng nhập số nguyên!");
            }
        }
    }

    private double nhapSoThuc(String nhac) {
        while (true) {
            System.out.print(nhac);
            try {
                double so = Double.parseDouble(docDong().replace(",", ""));
                if (so >= 0) return so;
                System.out.println("   Không được âm!");
            } catch (NumberFormatException e) {
                System.out.println("   Vui lòng nhập số!");
            }
        }
    }

    private LocalDate nhapNgay(String nhac) {
        while (true) {
            System.out.print(nhac);
            try {
                return LocalDate.parse(docDong(), NhanVien.DINH_DANG_NGAY);
            } catch (DateTimeParseException e) {
                System.out.println("   Sai định dạng, ví dụ: 15/08/2000");
            }
        }
    }
}
