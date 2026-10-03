package manager;

import java.util.List;

/**
 * Interface GENERIC cho các lớp quản lý danh sách đối tượng.
 * T là kiểu đối tượng được quản lý (NhanVien, PhongBan, ...).
 *
 * Người phụ trách: NGƯỜI 3
 */
public interface IQuanLy<T> {

    void them(T doiTuong);

    boolean xoa(String ma);

    /** Trả về đối tượng có mã tương ứng, hoặc null nếu không có. */
    T timTheoMa(String ma);

    List<T> layTatCa();
}
