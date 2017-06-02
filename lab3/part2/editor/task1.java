package editor;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;

public final class task1 {

    private task1() {}

    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private static final String TITLE = "Lines";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame(TITLE);
            JLines lines = new JLines();

            window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            window.getContentPane().add(lines, BorderLayout.CENTER);
            window.setSize(WIDTH, HEIGHT);
            window.setVisible(true);
            lines.requestFocusInWindow();
        });
    }
}
