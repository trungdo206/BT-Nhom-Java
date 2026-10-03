import ui.Menu;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Điểm bắt đầu chương trình.
 *
 * Người phụ trách: NGƯỜI 3
 */
public class Main {
    public static void main(String[] args) {
        // Xuất tiếng Việt có dấu đúng trên console UTF-8 (IntelliJ, NetBeans, VS Code)
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));
        new Menu().chay();
    }
}
