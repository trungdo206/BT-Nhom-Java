package manager;

import model.NVFullTime;
import model.NVPartTime;
import model.NhanVien;
import model.PhongBan;
import model.QuanLy;
import model.ThucTapSinh;
import service.BangLuong;
import service.ChamCong;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Đọc / ghi dữ liệu ra file văn bản (mỗi dòng 1 đối tượng, các trường cách nhau bởi ';').
 * Dùng try-with-resources để tự đóng file, xử lý IOException.
 *
 * Người phụ trách: NGƯỜI 3
 */
public class FileHandler {

    public static final String THU_MUC = "data";
    public static final String FILE_PHONG_BAN = THU_MUC + "/phongban.txt";
    public static final String FILE_NHAN_VIEN = THU_MUC + "/nhanvien.txt";
    public static final String FILE_CHAM_CONG = THU_MUC + "/chamcong.txt";

    private FileHandler() {
        // lớp tiện ích, không cho tạo đối tượng
    }

    public static boolean coDuLieu() {
        return Files.exists(Paths.get(FILE_NHAN_VIEN)) && Files.exists(Paths.get(FILE_PHONG_BAN));
    }

    // ===================== GHI =====================

    public static void luuTatCa(QuanLyNhanSu ql) throws IOException {
        List<String> dongPB = new ArrayList<>();
        for (PhongBan pb : ql.layDsPhongBan()) dongPB.add(pb.toCSV());
        ghiFile(FILE_PHONG_BAN, dongPB);

        List<String> dongNV = new ArrayList<>();
        for (NhanVien nv : ql.layTatCa()) dongNV.add(nv.toCSV()); // đa hình: toCSV mỗi loại khác nhau
        ghiFile(FILE_NHAN_VIEN, dongNV);

        List<String> dongCC = new ArrayList<>();
        for (ChamCong cc : ql.layDsChamCong()) dongCC.add(cc.toCSV());
        ghiFile(FILE_CHAM_CONG, dongCC);
    }

    public static String xuatBangLuong(List<BangLuong> ds, int thang, int nam) throws IOException {
        String tenFile = String.format("%s/bangluong_%02d_%d.csv", THU_MUC, thang, nam);
        List<String> dong = new ArrayList<>();
        dong.add("MaNV;HoTen;Loai;LuongThang;TangCa;TruNghi;BaoHiem;Thue;ThucLinh");
        for (BangLuong bl : ds) dong.add(bl.toCSV());
        ghiFile(tenFile, dong);
        return tenFile;
    }

    private static void ghiFile(String duongDan, List<String> cacDong) throws IOException {
        Path path = Paths.get(duongDan);
        Files.createDirectories(path.getParent());
        try (BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            for (String dong : cacDong) {
                bw.write(dong);
                bw.newLine();
            }
        }
    }

    // ===================== ĐỌC =====================

    /** Đọc toàn bộ dữ liệu vào ql. Trả về số dòng bị lỗi (bỏ qua). */
    public static int docTatCa(QuanLyNhanSu ql) throws IOException {
        int soDongLoi = 0;
        for (String dong : docFile(FILE_PHONG_BAN)) {
            try {
                String[] p = dong.split(";", -1);
                ql.themPhongBan(new PhongBan(p[0], p[1], p.length > 2 ? p[2] : ""));
            } catch (RuntimeException e) {
                soDongLoi++;
            }
        }
        for (String dong : docFile(FILE_NHAN_VIEN)) {
            try {
                ql.them(taoNhanVien(dong));
            } catch (RuntimeException e) {
                soDongLoi++;
            }
        }
        if (Files.exists(Paths.get(FILE_CHAM_CONG))) {
            for (String dong : docFile(FILE_CHAM_CONG)) {
                try {
                    String[] p = dong.split(";");
                    ql.capNhatChamCong(new ChamCong(p[0], Integer.parseInt(p[1]),
                            Integer.parseInt(p[2]), Integer.parseInt(p[3]), Integer.parseInt(p[4])));
                } catch (Exception e) {
                    soDongLoi++;
                }
            }
        }
        return soDongLoi;
    }

    /** Tạo đúng lớp con từ 1 dòng file (dựa vào trường đầu tiên là tên lớp). */
    private static NhanVien taoNhanVien(String dong) {
        String[] p = dong.split(";");
        String loai = p[0];
        String ma = p[1], ten = p[2], gt = p[4], sdt = p[5], pb = p[6];
        LocalDate ngaySinh = LocalDate.parse(p[3], NhanVien.DINH_DANG_NGAY);

        switch (loai) {
            case "NVFullTime":
                return new NVFullTime(ma, ten, ngaySinh, gt, sdt, pb,
                        Double.parseDouble(p[7]), Double.parseDouble(p[8]), Double.parseDouble(p[9]));
            case "QuanLy":
                return new QuanLy(ma, ten, ngaySinh, gt, sdt, pb,
                        Double.parseDouble(p[7]), Double.parseDouble(p[8]),
                        Double.parseDouble(p[9]), Double.parseDouble(p[10]));
            case "NVPartTime":
                return new NVPartTime(ma, ten, ngaySinh, gt, sdt, pb,
                        Integer.parseInt(p[7]), Double.parseDouble(p[8]));
            case "ThucTapSinh":
                return new ThucTapSinh(ma, ten, ngaySinh, gt, sdt, pb,
                        Double.parseDouble(p[7]), p[8]);
            default:
                throw new IllegalArgumentException("Loại nhân viên không hợp lệ: " + loai);
        }
    }

    private static List<String> docFile(String duongDan) throws IOException {
        List<String> ketQua = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(duongDan), StandardCharsets.UTF_8)) {
            String dong;
            while ((dong = br.readLine()) != null) {
                if (!dong.trim().isEmpty()) ketQua.add(dong.trim());
            }
        }
        return ketQua;
    }
}
