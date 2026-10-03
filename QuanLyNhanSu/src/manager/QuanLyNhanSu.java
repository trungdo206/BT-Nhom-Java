package manager;

import exception.DuLieuKhongHopLeException;
import exception.NhanVienKhongTonTaiException;
import model.NhanVien;
import model.PhongBan;
import service.BangLuong;
import service.ChamCong;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Lớp quản lý trung tâm: danh sách nhân viên, phòng ban, chấm công.
 * Dùng Collection: ArrayList, LinkedHashMap, TreeMap; sắp xếp bằng Comparator.
 *
 * Người phụ trách: NGƯỜI 3
 */
public class QuanLyNhanSu implements IQuanLy<NhanVien> {

    private final List<NhanVien> dsNhanVien = new ArrayList<>();
    private final Map<String, PhongBan> dsPhongBan = new LinkedHashMap<>();
    private final Map<String, ChamCong> dsChamCong = new LinkedHashMap<>(); // key = maNV

    // ===================== NHÂN VIÊN (cài đặt IQuanLy) =====================

    @Override
    public void them(NhanVien nv) {
        if (timTheoMa(nv.getMaNV()) != null) {
            throw new DuLieuKhongHopLeException("Mã nhân viên " + nv.getMaNV() + " đã tồn tại");
        }
        PhongBan pb = dsPhongBan.get(nv.getMaPhongBan());
        if (pb == null) {
            throw new DuLieuKhongHopLeException("Phòng ban " + nv.getMaPhongBan() + " không tồn tại");
        }
        dsNhanVien.add(nv);
        pb.themNhanVien(nv);
    }

    @Override
    public boolean xoa(String ma) {
        NhanVien nv = timTheoMa(ma);
        if (nv == null) return false;
        dsNhanVien.remove(nv);
        PhongBan pb = dsPhongBan.get(nv.getMaPhongBan());
        if (pb != null) {
            pb.xoaNhanVien(nv);
            if (nv.getMaNV().equals(pb.getMaTruongPhong())) {
                pb.setMaTruongPhong("");
            }
        }
        dsChamCong.remove(nv.getMaNV());
        return true;
    }

    @Override
    public NhanVien timTheoMa(String ma) {
        if (ma == null) return null;
        for (NhanVien nv : dsNhanVien) {
            if (nv.getMaNV().equalsIgnoreCase(ma.trim())) {
                return nv;
            }
        }
        return null;
    }

    @Override
    public List<NhanVien> layTatCa() {
        return Collections.unmodifiableList(dsNhanVien);
    }

    /** Giống timTheoMa nhưng ném ngoại lệ checked nếu không tìm thấy. */
    public NhanVien layNhanVien(String ma) throws NhanVienKhongTonTaiException {
        NhanVien nv = timTheoMa(ma);
        if (nv == null) {
            throw new NhanVienKhongTonTaiException(ma);
        }
        return nv;
    }

    public void chuyenPhongBan(String maNV, String maPBMoi) throws NhanVienKhongTonTaiException {
        NhanVien nv = layNhanVien(maNV);
        PhongBan pbMoi = dsPhongBan.get(maPBMoi.trim().toUpperCase());
        if (pbMoi == null) {
            throw new DuLieuKhongHopLeException("Phòng ban " + maPBMoi + " không tồn tại");
        }
        PhongBan pbCu = dsPhongBan.get(nv.getMaPhongBan());
        if (pbCu != null) pbCu.xoaNhanVien(nv);
        nv.setMaPhongBan(pbMoi.getMaPB());
        pbMoi.themNhanVien(nv);
    }

    // ===================== TÌM KIẾM / LỌC / SẮP XẾP =====================

    public List<NhanVien> timTheoTen(String tuKhoa) {
        List<NhanVien> ketQua = new ArrayList<>();
        String tk = tuKhoa.trim().toLowerCase();
        for (NhanVien nv : dsNhanVien) {
            if (nv.getHoTen().toLowerCase().contains(tk)) {
                ketQua.add(nv);
            }
        }
        return ketQua;
    }

    /** Lọc theo loại, ví dụ locTheoLoai(QuanLy.class). */
    public List<NhanVien> locTheoLoai(Class<? extends NhanVien> loai) {
        List<NhanVien> ketQua = new ArrayList<>();
        for (NhanVien nv : dsNhanVien) {
            if (nv.getClass() == loai) {
                ketQua.add(nv);
            }
        }
        return ketQua;
    }

    public List<NhanVien> sapXepTheoLuongGiamDan() {
        List<NhanVien> ds = new ArrayList<>(dsNhanVien);
        ds.sort(Comparator.comparingDouble(NhanVien::tinhLuong).reversed());
        return ds;
    }

    /** Sắp theo TÊN (từ cuối của họ tên) rồi đến họ, kiểu tiếng Việt. */
    public List<NhanVien> sapXepTheoTen() {
        List<NhanVien> ds = new ArrayList<>(dsNhanVien);
        ds.sort(Comparator.comparing((NhanVien nv) -> layTen(nv.getHoTen()))
                .thenComparing(NhanVien::getHoTen));
        return ds;
    }

    public List<NhanVien> sapXepTheoMa() {
        List<NhanVien> ds = new ArrayList<>(dsNhanVien);
        Collections.sort(ds); // dùng compareTo() trong NhanVien (Comparable)
        return ds;
    }

    private static String layTen(String hoTen) {
        String[] tu = hoTen.trim().split("\\s+");
        return tu[tu.length - 1];
    }

    // ===================== PHÒNG BAN =====================

    public void themPhongBan(PhongBan pb) {
        if (dsPhongBan.containsKey(pb.getMaPB())) {
            throw new DuLieuKhongHopLeException("Mã phòng ban " + pb.getMaPB() + " đã tồn tại");
        }
        dsPhongBan.put(pb.getMaPB(), pb);
    }

    public PhongBan timPhongBan(String maPB) {
        return maPB == null ? null : dsPhongBan.get(maPB.trim().toUpperCase());
    }

    public List<PhongBan> layDsPhongBan() {
        return new ArrayList<>(dsPhongBan.values());
    }

    public void ganTruongPhong(String maPB, String maNV) throws NhanVienKhongTonTaiException {
        PhongBan pb = timPhongBan(maPB);
        if (pb == null) throw new DuLieuKhongHopLeException("Phòng ban " + maPB + " không tồn tại");
        NhanVien nv = layNhanVien(maNV);
        if (!nv.getMaPhongBan().equals(pb.getMaPB())) {
            throw new DuLieuKhongHopLeException("Nhân viên " + maNV + " không thuộc phòng " + maPB);
        }
        pb.setMaTruongPhong(nv.getMaNV());
    }

    // ===================== CHẤM CÔNG & BẢNG LƯƠNG =====================

    public void capNhatChamCong(ChamCong cc) throws NhanVienKhongTonTaiException {
        layNhanVien(cc.getMaNV()); // kiểm tra nhân viên có tồn tại
        dsChamCong.put(cc.getMaNV(), cc);
    }

    public ChamCong layChamCong(String maNV) {
        return dsChamCong.get(maNV.trim().toUpperCase());
    }

    public List<ChamCong> layDsChamCong() {
        return new ArrayList<>(dsChamCong.values());
    }

    /** Lập bảng lương cho toàn bộ nhân viên. Ai chưa chấm công thì coi như đi làm đủ. */
    public List<BangLuong> lapBangLuong(int thang, int nam) {
        List<BangLuong> ds = new ArrayList<>();
        for (NhanVien nv : dsNhanVien) {
            ChamCong cc = dsChamCong.get(nv.getMaNV());
            if (cc == null || cc.getThang() != thang || cc.getNam() != nam) {
                cc = new ChamCong(nv.getMaNV(), thang, nam, 0, 0);
            }
            ds.add(new BangLuong(nv, cc));
        }
        return ds;
    }

    // ===================== THỐNG KÊ =====================

    public double tinhTongQuyLuong() {
        double tong = 0;
        for (NhanVien nv : dsNhanVien) tong += nv.tinhLuong();
        return tong;
    }

    public double tinhLuongTrungBinh() {
        return dsNhanVien.isEmpty() ? 0 : tinhTongQuyLuong() / dsNhanVien.size();
    }

    public NhanVien timNguoiLuongCaoNhat() {
        return dsNhanVien.isEmpty() ? null
                : Collections.max(dsNhanVien, Comparator.comparingDouble(NhanVien::tinhLuong));
    }

    /** Đếm số nhân viên theo từng loại. TreeMap giúp key được sắp xếp. */
    public Map<String, Integer> thongKeTheoLoai() {
        Map<String, Integer> tk = new TreeMap<>();
        for (NhanVien nv : dsNhanVien) {
            tk.put(nv.getLoaiNV(), tk.getOrDefault(nv.getLoaiNV(), 0) + 1);
        }
        return tk;
    }

    public int getSoLuongNhanVien() {
        return dsNhanVien.size();
    }
}
