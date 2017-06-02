package editor.model;

import editor.interfaces.EditAction;

public final class InsertAction implements EditAction {

    private final String text;
    private final Location cursorLocation = new Location();
    private final LocationRange selection = new LocationRange();
    private final TextEditorModel model;
    private DeleteSelectionAction action;
    private final boolean hasDeleteAction;

    public InsertAction(TextEditorModel model, String text) {
        this.model = model;
        this.text = text;
        this.hasDeleteAction = !(this.model.selection.getStartRow() == this.model.selection.getEndRow()
                && this.model.selection.getStartColumn() == this.model.selection.getEndColumn());
        this.cursorLocation.set(this.model.cursorLocation);
        this.selection.setStart(this.cursorLocation);
    }

    @Override
    public void executeDo() {
        this.model.cursorLocation.set(this.cursorLocation);

        if (this.hasDeleteAction) {
            this.action = new DeleteSelectionAction(this.model);
            this.action.executeDo();
            this.selection.setStart(this.model.cursorLocation);
        }

        int row = this.model.cursorLocation.getRow();
        int column = this.model.cursorLocation.getColumn();

        StringBuffer line = this.model.lines.get(row);
        line.insert(column, this.text);
        boolean endsWithNewline = line.charAt(line.length() - 1) == '\n';

        String[] lines = TextEditorModel.NEWLINE_SPLITTER.split(this.model.lines.remove(row).toString());

        if (endsWithNewline) {
            this.model.lines.add(row, new StringBuffer());

            if (lines.length == 0) {
                this.model.lines.add(row, new StringBuffer());
            }
        }

        for (int i = lines.length - 1; i >= 0; i--) {
            this.model.lines.add(row, new StringBuffer(lines[i]));
        }

        int moveAmount = this.text.length();

        for (int i = 0; i < moveAmount; i++) {
            this.model.moveCursorRight(false);
        }

        this.selection.setEnd(this.model.cursorLocation);
        this.model.notifyTextObservers();
    }

    @Override
    public void executeUndo() {
        this.model.selection.set(this.selection);
        this.model.hasSelection = true;
        new DeleteSelectionAction(this.model).executeDo();

        if (this.hasDeleteAction) {
            this.action.executeUndo();
        }
    }
}
