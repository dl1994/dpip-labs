package editor;

import editor.interfaces.CursorObserver;
import editor.interfaces.TextObserver;
import editor.model.Location;
import editor.model.LocationRange;
import editor.model.TextEditorModel;
import javax.swing.JComponent;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;
import static java.lang.Math.max;
import static java.lang.Math.min;

public final class TextEditor extends JComponent implements CursorObserver, TextObserver {

    private int caretX;
    private int caretY;
    private final TextEditorModel model;
    private final ClipboardStack clipboardStack;
    private static final String TAB_REPLACEMENT = "    ";
    private static final Pattern TAB_REPLACEMENT_PATTERN = Pattern.compile("\t", Pattern.LITERAL);
    private static final Color CARET_COLOR = new Color(10, 10, 10);
    private static final Color SELECTION_COLOR = new Color(99, 160, 228);
    private static final BiConsumer<TextEditor, Boolean> NO_OP = (e, s) -> {};
    private static final Set<Integer> SPECIAL_KEYS = new HashSet<>();
    private static final Map<Integer, BiConsumer<TextEditor, Boolean>> KEY_ACTIONS = new HashMap<>();
    private static final Map<Integer, BiConsumer<TextEditor, Boolean>> CTRL_ACTIONS = new HashMap<>();

    static {
        KEY_ACTIONS.put(KeyEvent.VK_UP, (editor, hasShift) -> editor.model.moveCursorUp(hasShift));
        KEY_ACTIONS.put(KeyEvent.VK_DOWN, (editor, hasShift) -> editor.model.moveCursorDown(hasShift));
        KEY_ACTIONS.put(KeyEvent.VK_LEFT, (editor, hasShift) -> editor.model.moveCursorLeft(hasShift));
        KEY_ACTIONS.put(KeyEvent.VK_RIGHT, (editor, hasShift) -> editor.model.moveCursorRight(hasShift));
        KEY_ACTIONS.put(KeyEvent.VK_HOME, (editor, hasShift) -> editor.model.moveCursorToLineStart(hasShift));
        KEY_ACTIONS.put(KeyEvent.VK_END, (editor, hasShift) -> editor.model.moveCursorToLineEnd(hasShift));
        KEY_ACTIONS.put(KeyEvent.VK_DELETE, (editor, hasShift) -> editor.model.deleteAfter());
        KEY_ACTIONS.put(KeyEvent.VK_BACK_SPACE, (editor, hasShift) -> editor.model.deleteBefore());
        SPECIAL_KEYS.addAll(KEY_ACTIONS.keySet());
        CTRL_ACTIONS.put(KeyEvent.VK_C, (editor, hasShift) -> {
            if (editor.model.hasSelection() && !hasShift) {
                editor.clipboardStack.push(editor.model.getSelectionText());
            }
        });
        CTRL_ACTIONS.put(KeyEvent.VK_X, (editor, hasShift) -> {
            if (editor.model.hasSelection() && !hasShift) {
                editor.clipboardStack.push(editor.model.getSelectionText());
                editor.model.deleteSelection();
            }
        });
        CTRL_ACTIONS.put(KeyEvent.VK_V, (editor, hasShift) -> {
            if (!editor.clipboardStack.isEmpty()) {
                editor.model.insert(
                        hasShift ? editor.clipboardStack.pop() : editor.clipboardStack.peek()
                );
            }
        });
        CTRL_ACTIONS.put(KeyEvent.VK_Z, (editor, hasShift) -> {
            if (UndoManager.INSTANCE.canUndo() && !hasShift) {
                UndoManager.INSTANCE.undo();
            }
        });
        CTRL_ACTIONS.put(KeyEvent.VK_Y, (editor, hasShift) -> {
            if (UndoManager.INSTANCE.canRedo() && !hasShift) {
                UndoManager.INSTANCE.redo();
            }
        });
    }

    private TextEditor(TextEditorModel model) {
        this.model = model;
        this.clipboardStack = new ClipboardStack();
    }

    public static TextEditor build(TextEditorModel model) {
        TextEditor editor = new TextEditor(model);

        editor.model.registerCursorObserver(editor);
        editor.setFocusTraversalKeysEnabled(false);
        editor.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                char c = e.getKeyChar();

                if (e.isControlDown()) {
                    editor.doAction(CTRL_ACTIONS, e);
                } else if (SPECIAL_KEYS.contains(keyCode)) {
                    editor.doAction(KEY_ACTIONS, e);
                } else if (isPrintableChar(c)) {
                    editor.model.insert(c);
                } else if (keyCode == KeyEvent.VK_TAB) {
                    editor.model.insert('\t');
                } else if (keyCode == KeyEvent.VK_ENTER) {
                    editor.model.insert('\n');
                }
            }
        });

        return editor;
    }

    private void doAction(Map<Integer, BiConsumer<TextEditor, Boolean>> actionMap, KeyEvent e) {
        actionMap.getOrDefault(e.getKeyCode(), NO_OP).accept(
                this,
                e.isShiftDown()
        );
    }

    private static boolean isPrintableChar(char c) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        return (!Character.isISOControl(c)) &&
                c != KeyEvent.CHAR_UNDEFINED &&
                block != null &&
                block != Character.UnicodeBlock.SPECIALS;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Rectangle bounds = this.getBounds();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, bounds.width, bounds.height);

        FontMetrics fontMetrics = g2d.getFontMetrics();
        int textHeight = fontMetrics.getHeight();
        int textLeftOffset = textHeight / 2;

        this.drawSelection(g2d, fontMetrics, textHeight, textLeftOffset);
        this.drawText(g2d, textHeight, textLeftOffset);
        this.drawCaret(g2d, textHeight, textLeftOffset);
    }

    private void drawSelection(Graphics2D g2d, FontMetrics fontMetrics, int textHeight, int textLeftOffset) {
        if (this.model.hasSelection()) {
            LocationRange selection = this.model.getSelectionRange();

            int startRow = selection.getStartRow();
            int endRow = selection.getEndRow();
            int minRow = min(startRow, endRow);
            int maxRow = max(startRow, endRow);
            int startColumn;
            int endColumn;

            if (startRow == endRow) {
                startColumn = min(selection.getStartColumn(), selection.getEndColumn());
                endColumn = max(selection.getStartColumn(), selection.getEndColumn());
            } else if (startRow < endRow) {
                startColumn = selection.getStartColumn();
                endColumn = selection.getEndColumn();
            } else {
                startColumn = selection.getEndColumn();
                endColumn = selection.getStartColumn();
            }

            g2d.setColor(SELECTION_COLOR);

            if (minRow == maxRow) {
                this.fillLine(minRow, startColumn, endColumn,
                        fontMetrics, textHeight, textLeftOffset, g2d);
            } else {
                for (int row = minRow; row <= maxRow; row++) {
                    if (row == minRow) {
                        this.fillLine(row, startColumn, this.model.getLine(row).length(),
                                fontMetrics, textHeight, textLeftOffset, g2d);
                    } else if (row == maxRow) {
                        this.fillLine(row, 0, endColumn,
                                fontMetrics, textHeight, textLeftOffset, g2d);
                    } else {
                        this.fillLine(row, 0, this.model.getLine(row).length(),
                                fontMetrics, textHeight, textLeftOffset, g2d);
                    }
                }
            }
        }
    }

    private void fillLine(int row, int start, int end, FontMetrics fontMetrics,
                          int textHeight, int textLeftOffset, Graphics2D g2d) {
        StringBuffer line = this.model.getLine(row);
        String beforeText = TAB_REPLACEMENT_PATTERN.matcher(line.substring(0, start))
                .replaceAll(TAB_REPLACEMENT);
        String highlightedText = TAB_REPLACEMENT_PATTERN.matcher(line.substring(start, end))
                .replaceAll(TAB_REPLACEMENT);

        int startX = fontMetrics.stringWidth(beforeText) + textLeftOffset - 1;
        int width = fontMetrics.stringWidth(highlightedText) + 1;
        int y = row * textHeight;
        int startY = y + textHeight / 4 + 1;
        int height = (y + textHeight + 2) - startY;

        g2d.fillRect(startX, startY, width, height);
    }

    private void drawText(Graphics2D g2d, int textHeight, int textLeftOffset) {
        g2d.setColor(Color.BLACK);

        int lineNumber = 1;

        for (StringBuffer line : this.model.allLines()) {
            g2d.drawString(TAB_REPLACEMENT_PATTERN.matcher(line.toString())
                    .replaceAll(TAB_REPLACEMENT), textLeftOffset, textHeight * lineNumber);
            lineNumber += 1;
        }
    }

    private void drawCaret(Graphics2D g2d, int textHeight, int textLeftOffset) {
        g2d.setColor(CARET_COLOR);
        int x = this.caretX + textLeftOffset - 1;
        g2d.drawLine(x, this.caretY + textHeight / 4 + 1, x, this.caretY + textHeight + 1);
    }

    @Override
    public void updateCursorLocation(Location location) {
        Graphics2D g2d = (Graphics2D) this.getGraphics();
        FontMetrics fontMetrics = g2d.getFontMetrics();

        int textHeight = fontMetrics.getHeight();
        int row = location.getRow();

        String lineText = TAB_REPLACEMENT_PATTERN.matcher(this.model.getLine(row)
                .substring(0, location.getColumn())).replaceAll(TAB_REPLACEMENT);

        this.caretX = fontMetrics.stringWidth(lineText);
        this.caretY = row * textHeight;
        this.repaint();
    }

    @Override
    public void updateText(List<StringBuffer> lines) {
        this.repaint();
    }
}
