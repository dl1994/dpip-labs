package editor.model;

import editor.UndoManager;
import editor.interfaces.CursorObserver;
import editor.interfaces.SelectionObserver;
import editor.interfaces.TextObserver;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import static java.lang.Math.max;
import static java.lang.Math.min;

public final class TextEditorModel {

    boolean hasSelection;
    final Location cursorLocation = new Location();
    final List<StringBuffer> lines;
    final LocationRange selection = new LocationRange();
    private final Set<TextObserver> textObservers = new HashSet<>();
    private final Set<CursorObserver> cursorObservers = new HashSet<>();
    private final Set<SelectionObserver> selectionObservers = new HashSet<>();
    static final Pattern NEWLINE_SPLITTER = Pattern.compile("\n", Pattern.LITERAL);

    public TextEditorModel(String text) {
        this.lines = Arrays.stream(NEWLINE_SPLITTER.split(text))
                .map(StringBuffer::new)
                .collect(Collectors.toList());
    }

    public Iterable<StringBuffer> allLines() {
        return this.lines;
    }

    public Iterable<StringBuffer> linesRange(int start, int end) {
        return this.lines.subList(start, end);
    }

    public void registerCursorObserver(CursorObserver cursorObserver) {
        this.cursorObservers.add(cursorObserver);
    }

    public void unregisterCursorObserver(CursorObserver cursorObserver) {
        this.cursorObservers.remove(cursorObserver);
    }

    public void registerTextObserver(TextObserver textObserver) {
        this.textObservers.add(textObserver);
    }

    public void unregisterTextObserver(TextObserver textObserver) {
        this.textObservers.remove(textObserver);
    }

    public void registerSelectionObserver(SelectionObserver selectionObserver) {
        this.selectionObservers.add(selectionObserver);
    }

    public void unregisterSelectionObserver(SelectionObserver selectionObserver) {
        this.selectionObservers.remove(selectionObserver);
    }

    void notifyCursorObservers() {
        this.cursorObservers.forEach(o -> o.updateCursorLocation(this.cursorLocation));
    }

    void notifyTextObservers() {
        this.textObservers.forEach(o -> o.updateText(this.lines));
    }

    void notifySelectionObservers() {
        this.selectionObservers.forEach(o -> o.updateSelection(this.selection));
    }

    int maxColumn() {
        return this.lines.get(this.cursorLocation.getRow()).length();
    }

    int maxRow() {
        return this.lines.size() - 1;
    }

    private void updateSelection(boolean updateSelection, boolean updated) {
        if (updateSelection && (updated || this.hasSelection)) {
            this.selection.setEndRow(this.cursorLocation.getRow());
            this.selection.setEndColumn(this.cursorLocation.getColumn());
            this.hasSelection = true;
        } else if (updateSelection) {
            this.selection.setStartRow(this.cursorLocation.getRow());
            this.selection.setStartColumn(this.cursorLocation.getColumn());
        } else {
            this.resetSelection();
        }

        this.notifySelectionObservers();
    }

    private void resetSelection() {
        this.selection.setStartRow(0);
        this.selection.setStartColumn(0);
        this.selection.setEndRow(0);
        this.selection.setEndColumn(0);
        this.hasSelection = false;
    }

    public void moveCursorToLineStart(boolean updateSelection) {
        this.updateSelection(updateSelection, false);
        int column = this.cursorLocation.getColumn();

        if (column > 0) {
            this.cursorLocation.setColumn(0);
            this.notifyCursorObservers();
            this.updateSelection(updateSelection, true);
        }
    }

    public void moveCursorToLineEnd(boolean updateSelection) {
        this.updateSelection(updateSelection, false);
        int column = this.cursorLocation.getColumn();
        int maxColumn = this.maxColumn();

        if (column < maxColumn) {
            this.cursorLocation.setColumn(maxColumn);
            this.notifyCursorObservers();
            this.updateSelection(updateSelection, true);
        }
    }

    public void moveCursorLeft(boolean updateSelection) {
        this.updateSelection(updateSelection, false);
        int row = this.cursorLocation.getRow();
        int column = this.cursorLocation.getColumn();

        if (row == 0 && column == 0) {
            return;
        }

        if (column == 0) {
            this.cursorLocation.setRow(row - 1);
            this.cursorLocation.setColumn(this.maxColumn());
        } else {
            this.cursorLocation.setColumn(column - 1);
        }

        this.notifyCursorObservers();
        this.updateSelection(updateSelection, true);
    }

    public void moveCursorRight(boolean updateSelection) {
        this.updateSelection(updateSelection, false);
        int row = this.cursorLocation.getRow();
        int column = this.cursorLocation.getColumn();
        int maxColumn = this.maxColumn();

        if (row == this.maxRow() && column == maxColumn) {
            return;
        }

        if (column == maxColumn) {
            this.cursorLocation.setRow(row + 1);
            this.cursorLocation.setColumn(0);
        } else {
            this.cursorLocation.setColumn(column + 1);
        }

        this.notifyCursorObservers();
        this.updateSelection(updateSelection, true);
    }

    public void moveCursorUp(boolean updateSelection) {
        this.updateSelection(updateSelection, false);
        int row = this.cursorLocation.getRow();

        if (row > 0) {
            this.cursorLocation.setRow(row - 1);
            int maxColumn = this.maxColumn();

            if (this.cursorLocation.getColumn() > maxColumn) {
                this.cursorLocation.setColumn(maxColumn);
            }

            this.notifyCursorObservers();
            this.updateSelection(updateSelection, true);
        }
    }

    public void moveCursorDown(boolean updateSelection) {
        this.updateSelection(updateSelection, false);
        int row = this.cursorLocation.getRow();

        if (row < this.maxRow()) {
            this.cursorLocation.setRow(row + 1);
            int maxColumn = this.maxColumn();

            if (this.cursorLocation.getColumn() > maxColumn) {
                this.cursorLocation.setColumn(maxColumn);
            }

            this.notifyCursorObservers();
            this.updateSelection(updateSelection, true);
        }
    }

    public StringBuffer getLine(int index) {
        return this.lines.get(index);
    }

    public void deleteBefore() {
        if (this.hasSelection) {
            this.deleteSelection();
            return;
        }

        int row = this.cursorLocation.getRow();
        int column = this.cursorLocation.getColumn();

        if (row == 0 && column == 0) {
            return;
        }

        UndoManager.INSTANCE.push(new DeleteBeforeAction(this));
    }

    public void deleteAfter() {
        if (this.hasSelection) {
            this.deleteSelection();
            return;
        }

        int row = this.cursorLocation.getRow();
        int column = this.cursorLocation.getColumn();
        int maxColumn = this.maxColumn();

        if (row == this.maxRow() && column == maxColumn) {
            return;
        }

        UndoManager.INSTANCE.push(new DeleteAfterAction(this));
    }

    public void deleteSelection() {
        if (this.selection.getStartRow() == this.selection.getEndRow()
                && this.selection.getStartColumn() == this.selection.getEndColumn()) {
            return;
        }

        UndoManager.INSTANCE.push(new DeleteSelectionAction(this));

        this.resetSelection();
        this.notifySelectionObservers();
    }

    int findStartColumn(int startRow, int endRow) {
        if (startRow == endRow) {
            return min(this.selection.getStartColumn(), this.selection.getEndColumn());
        } else if (startRow < endRow) {
            return this.selection.getStartColumn();
        } else {
            return this.selection.getEndColumn();
        }
    }

    int findEndColumn(int startRow, int endRow) {
        if (startRow == endRow) {
            return max(this.selection.getStartColumn(), this.selection.getEndColumn());
        } else if (startRow < endRow) {
            return this.selection.getEndColumn();
        } else {
            return this.selection.getStartColumn();
        }
    }

    public String getSelectionText() {
        int startRow = this.selection.getStartRow();
        int endRow = this.selection.getEndRow();
        int minRow = min(startRow, endRow);
        int maxRow = max(startRow, endRow);
        int startColumn = this.findStartColumn(startRow, endRow);
        int endColumn = this.findEndColumn(startRow, endRow);

        if (minRow == maxRow) {
            return this.lines.get(startRow).substring(startColumn, endColumn);
        } else {
            StringBuilder builder = new StringBuilder();

            for (int row = minRow; row <= maxRow; row++) {
                if (row == minRow) {
                    builder.append(this.lines.get(row).substring(startColumn))
                            .append('\n');
                } else if (row == maxRow) {
                    builder.append(this.lines.get(row).substring(0, endColumn));
                } else {
                    builder.append(this.lines.get(row))
                            .append('\n');
                }
            }

            return builder.toString();
        }
    }

    public LocationRange getSelectionRange() {
        return this.selection;
    }

    public boolean hasSelection() {
        return this.hasSelection;
    }

    public void setSelectionRange(LocationRange range) {
        this.selection.setStartColumn(range.getStartColumn());
        this.selection.setStartRow(range.getStartRow());
        this.selection.setEndColumn(range.getEndColumn());
        this.selection.setEndRow(range.getEndRow());
        this.hasSelection = true;
    }

    public void insert(char c) {
        this.insert(Character.toString(c));
    }

    public void insert(String text) {
        UndoManager.INSTANCE.push(new InsertAction(this, text));
    }
}
