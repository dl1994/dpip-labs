package editor;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

public final class JLines extends JComponent {

    private static final int FONT_SIZE = 48;
    private static final String FIRST_LINE = "Some text";
    private static final String SECOND_LINE = "More text";
    private static final long serialVersionUID = 8592955776136485243L;

    public JLines() {
        this.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Window window = SwingUtilities.getWindowAncestor(JLines.this);

                    window.dispatchEvent(new WindowEvent(
                            window,
                            WindowEvent.WINDOW_CLOSING
                    ));
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Rectangle bounds = this.getBounds();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        g2d.setColor(Color.BLACK);
        int lineY = bounds.y + bounds.height / 2;
        g2d.drawLine(bounds.x, lineY, bounds.x + bounds.width, lineY);
        int lineX = bounds.x + bounds.width / 2;
        g2d.drawLine(lineX, bounds.y, lineX, bounds.y + bounds.width);

        Font font = g2d.getFont();
        Font newFont = new Font(font.getName(), font.getStyle(), FONT_SIZE);

        g2d.setFont(newFont);

        FontMetrics fontMetrics = g2d.getFontMetrics();

        int text1Offset = fontMetrics.stringWidth(FIRST_LINE) / 2;
        int text2Offset = fontMetrics.stringWidth(SECOND_LINE) / 2;
        int textHeight = fontMetrics.getHeight();
        int textHeightOffset = textHeight / 4;

        g2d.drawString(FIRST_LINE, lineX - text1Offset, lineY - textHeightOffset);
        g2d.drawString(SECOND_LINE, lineX - text2Offset, lineY + textHeight - textHeightOffset);
    }
}
