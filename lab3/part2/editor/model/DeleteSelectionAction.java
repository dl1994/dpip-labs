package editor.model;

import editor.interfaces.EditAction;
import static java.lang.Math.max;
import static java.lang.Math.min;

public final class DeleteSelectionAction implements EditAction {

    private final String deletedText;
    private final Location undoLocation = new Location();
    private final Location cursorLocation = new Location();
    private final LocationRange selection = new LocationRange();
    private final boolean hasSelection;
    private final TextEditorModel model;

    public DeleteSelectionAction(TextEditorModel model) {
        this.model = model;
        this.deletedText = this.model.getSelectionText();
        this.cursorLocation.set(this.model.cursorLocation);
        this.selection.set(this.model.selection);
        this.hasSelection = this.model.hasSelection;
    }

    @Override
    public void executeDo() {
        this.model.hasSelection = false;
        this.model.cursorLocation.set(this.cursorLocation);
        this.model.selection.set(this.selection);
        int startRow = this.model.selection.getStartRow();
        int endRow = this.model.selection.getEndRow();
        int minRow = min(startRow, endRow);
        int maxRow = max(startRow, endRow);
        int startColumn = this.model.findStartColumn(startRow, endRow);
        int endColumn = this.model.findEndColumn(startRow, endRow);

        if (minRow == 0 && maxRow == this.model.maxRow() && startColumn == 0
                && endColumn == this.model.lines.get(this.model.maxRow()).length()) {
            this.model.lines.clear();
            this.model.lines.add(new StringBuffer());
            this.model.cursorLocation.setRow(0);
            this.model.cursorLocation.setColumn(0);
            this.model.notifyTextObservers();
            this.model.notifyCursorObservers();
            this.model.notifySelectionObservers();
            this.undoLocation.set(this.model.cursorLocation);

            return;
        }

        if (minRow == maxRow) {
            this.deleteLinePart(minRow, startColumn, endColumn);
        } else {
            int row = minRow;
            for (int rowCounter = minRow; rowCounter <= maxRow; rowCounter++) {
                if (rowCounter == minRow) {
                    this.deleteLinePart(row, startColumn, this.model.lines.get(row).length());

                    if (startColumn > 0) {
                        row += 1;
                    }
                } else if (rowCounter == maxRow) {
                    if (endColumn == this.model.lines.get(row).length()) {
                        this.model.lines.remove(row);
                    } else if (startColumn > 0) {
                        this.deleteLinePart(row, 0, endColumn);
                        this.model.lines.get(row - 1).append(this.model.lines.remove(row));
                    } else {
                        this.deleteLinePart(row, 0, endColumn);
                    }
                } else {
                    this.model.lines.remove(row);
                }
            }
        }

        this.model.cursorLocation.setRow(max(min(minRow, this.model.maxRow()), 0));
        this.model.cursorLocation.setColumn(max(min(startColumn, this.model.maxColumn()), 0));
        this.model.selection.setStartColumn(0);
        this.model.selection.setEndColumn(0);
        this.model.selection.setStartRow(0);
        this.model.selection.setEndRow(0);
        this.undoLocation.set(this.model.cursorLocation);
        this.model.notifyTextObservers();
        this.model.notifyCursorObservers();
        this.model.notifySelectionObservers();
    }

    private void deleteLinePart(int index, int start, int end) {
        StringBuffer line = this.model.lines.get(index);

        if (start == 0 && end == line.length()) {
            this.model.lines.remove(index);
        } else {
            line.delete(start, end);
        }
    }

    @Override
    public void executeUndo() {
        this.model.hasSelection = false;
        this.model.cursorLocation.set(this.undoLocation);
        new InsertAction(this.model, this.deletedText).executeDo();
        this.model.cursorLocation.set(this.cursorLocation);
        this.model.selection.set(this.selection);
        this.model.hasSelection = this.hasSelection;
        this.model.notifyTextObservers();
        this.model.notifyCursorObservers();
        this.model.notifySelectionObservers();
    }
}
