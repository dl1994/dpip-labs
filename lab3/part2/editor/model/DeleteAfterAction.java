package editor.model;

import editor.interfaces.EditAction;

public final class DeleteAfterAction implements EditAction {

    private final char deletedChar;
    private final Location cursorLocation = new Location();
    private final TextEditorModel model;

    public DeleteAfterAction(TextEditorModel model) {
        this.model = model;
        this.cursorLocation.set(this.model.cursorLocation);

        int row = this.model.cursorLocation.getRow();
        int column = this.model.cursorLocation.getColumn();
        int maxColumn = this.model.maxColumn();

        if (column == maxColumn) {
            this.deletedChar = '\n';
        } else {
            this.deletedChar = this.model.lines.get(row).charAt(column);
        }
    }

    @Override
    public void executeDo() {
        this.model.cursorLocation.set(this.cursorLocation);
        int row = this.model.cursorLocation.getRow();
        int column = this.model.cursorLocation.getColumn();
        int maxColumn = this.model.maxColumn();

        if (column == maxColumn) {
            StringBuffer line = this.model.lines.get(row);
            StringBuffer nextLine = this.model.lines.remove(row + 1);

            line.append(nextLine);
        } else {
            this.model.lines.get(row).deleteCharAt(column);
        }

        this.model.notifyCursorObservers();
        this.model.notifyTextObservers();
    }

    @Override
    public void executeUndo() {
        this.model.cursorLocation.set(this.cursorLocation);
        new InsertAction(this.model, Character.toString(this.deletedChar)).executeDo();
        this.model.moveCursorLeft(false);
    }
}
