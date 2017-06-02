package editor.model;

import editor.interfaces.EditAction;

public final class DeleteBeforeAction implements EditAction {

    private final char deletedChar;
    private final Location cursorLocation = new Location();
    private final TextEditorModel model;

    public DeleteBeforeAction(TextEditorModel model) {
        this.model = model;
        this.cursorLocation.set(this.model.cursorLocation);

        int row = this.model.cursorLocation.getRow();
        int column = this.model.cursorLocation.getColumn();

        if (column == 0) {
            this.deletedChar = '\n';
        } else {
            this.deletedChar = this.model.lines.get(row).charAt(column - 1);
        }
    }

    @Override
    public void executeDo() {
        this.model.cursorLocation.set(this.cursorLocation);
        int row = this.model.cursorLocation.getRow();
        int column = this.model.cursorLocation.getColumn();

        this.model.moveCursorLeft(false);

        if (column == 0) {
            StringBuffer previousLine = this.model.lines.get(row - 1);
            StringBuffer line = this.model.lines.remove(row);

            previousLine.append(line);
        } else {
            this.model.lines.get(row).deleteCharAt(column - 1);
        }

        this.model.notifyTextObservers();
    }

    @Override
    public void executeUndo() {
        this.model.cursorLocation.set(this.cursorLocation);
        this.model.moveCursorLeft(false);
        new InsertAction(this.model, Character.toString(this.deletedChar)).executeDo();
    }
}
