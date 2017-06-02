package editor;

import editor.model.TextEditorModel;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

public final class task2 {

    private task2() {}

    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private static final String TITLE = "NotAPad";
    private static final String LINES = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod\n" +
            "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud\n" +
            '\n' +
            "exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in\n" +
            "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat\n" +
            "cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame(TITLE);
            TextEditorModel model = new TextEditorModel(LINES);
            TextEditor editor = TextEditor.build(model);
            JPanel panel = new JPanel(new BorderLayout());
            JPanel menuPanel = new JPanel(new BorderLayout());
            StatusPanel statusLabel = new StatusPanel(6);

            model.registerTextObserver(statusLabel);
            model.registerCursorObserver(statusLabel);
            model.registerSelectionObserver(statusLabel);

            menuPanel.add(createMenu(), BorderLayout.NORTH);
            menuPanel.add(createToolbar(), BorderLayout.SOUTH);
            panel.add(menuPanel, BorderLayout.NORTH);
            panel.add(editor, BorderLayout.CENTER);
            panel.add(statusLabel, BorderLayout.SOUTH);

            window.add(panel);
            window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            window.setSize(WIDTH, HEIGHT);
            window.setVisible(true);
            window.setLocationRelativeTo(null);
            editor.requestFocusInWindow();
        });
    }

    private static JComponent createMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        JMenuItem open = new JMenuItem("Open");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem exit = new JMenuItem(new AbstractAction("Exit") {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                Window window = SwingUtilities.getWindowAncestor(menuBar);
                window.dispatchEvent(new WindowEvent(
                        window,
                        WindowEvent.WINDOW_CLOSING
                ));
            }
        });

        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(exit);

        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu("Edit");

        JMenuItem undo = new JMenuItem("Undo");
        JMenuItem redo = new JMenuItem("Redo");
        JMenuItem cut = new JMenuItem("Cut");
        JMenuItem copy = new JMenuItem("Copy");
        JMenuItem paste = new JMenuItem("Paste");
        JMenuItem pasteAndTake = new JMenuItem("Paste and take");
        JMenuItem deleteSelection = new JMenuItem("Delete selection");
        JMenuItem clearDocument = new JMenuItem("Clear document");

        editMenu.add(undo);
        editMenu.add(redo);
        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);
        editMenu.add(pasteAndTake);
        editMenu.add(deleteSelection);
        editMenu.add(clearDocument);

        menuBar.add(editMenu);

        JMenu moveMenu = new JMenu("Move");

        JMenuItem toStart = new JMenuItem("Cursor to document start");
        JMenuItem toEnd = new JMenuItem("Cursor to document end");

        moveMenu.add(toStart);
        moveMenu.add(toEnd);

        menuBar.add(moveMenu);

        return menuBar;
    }

    private static JComponent createToolbar() {
        JToolBar toolBar = new JToolBar();
        JButton undo = new JButton("Undo");
        JButton redo = new JButton("Redo");
        JButton cut = new JButton("Cut");
        JButton copy = new JButton("Copy");
        JButton paste = new JButton("Paste");

        toolBar.add(undo);
        toolBar.add(redo);
        toolBar.add(cut);
        toolBar.add(copy);
        toolBar.add(paste);

        return toolBar;
    }
}
