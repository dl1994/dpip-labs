package editor;

import editor.interfaces.CursorObserver;
import editor.interfaces.SelectionObserver;
import editor.interfaces.TextObserver;
import editor.model.Location;
import editor.model.LocationRange;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import java.awt.Color;
import java.util.List;

public final class StatusPanel extends JLabel implements CursorObserver, TextObserver, SelectionObserver {

    private int row = 1;
    private int column = 1;
    private int lines;
    private LocationRange selection;
    private static final long serialVersionUID = 9018834277097104942L;

    public StatusPanel(int lines) {
        this.lines = lines;
        this.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        this.updateText();
    }

    private void updateText() {
        this.setText(
                "cursor: [" + this.row + ':' + this.column + "] lines: " + this.lines + " selection: " +
                        (this.selection == null
                                ? "none"
                                : "[" + this.selection.getStartRow() + ':' + this.selection.getStartColumn() + " - " +
                                this.selection.getEndRow() + ':' + this.selection.getEndColumn() + ']')
        );
    }

    @Override
    public void updateCursorLocation(Location location) {
        this.row = location.getRow() + 1;
        this.column = location.getColumn() + 1;
        this.updateText();
    }

    @Override
    public void updateText(List<StringBuffer> textLines) {
        this.lines = textLines.size();
        this.updateText();
    }

    @Override
    public void updateSelection(LocationRange selection) {
        if (selection.getStartColumn() != selection.getEndColumn()
                || selection.getStartRow() != selection.getEndRow()) {
            this.selection = new LocationRange();
            this.selection.set(selection);
        } else {
            this.selection = null;
        }

        this.updateText();
    }
}
