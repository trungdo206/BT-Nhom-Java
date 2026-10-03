package exception;

/**
 * Ngoại lệ KHÔNG KIỂM TRA (unchecked - kế thừa RuntimeException):
 * không bắt buộc try-catch. Dùng khi dữ liệu nghiệp vụ sai
 * (trùng mã nhân viên, số giờ tăng ca vượt quy định, ...).
 *
 * Người phụ trách: NGƯỜI 2
 */
public class DuLieuKhongHopLeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuLieuKhongHopLeException(String thongBao) {
        super(thongBao);
    }
}
