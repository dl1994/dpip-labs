package editor;

import editor.interfaces.EditAction;
import java.util.Stack;

public final class UndoManager {

    public static final UndoManager INSTANCE = new UndoManager();

    private UndoManager() {}

    private final Stack<EditAction> undoStack = new Stack<>();
    private final Stack<EditAction> redoStack = new Stack<>();

    public void undo() {
        EditAction command = this.undoStack.pop();
        command.executeUndo();
        this.redoStack.push(command);
    }

    public boolean canUndo() {
        return !this.undoStack.isEmpty();
    }

    public void redo() {
        EditAction command = this.redoStack.pop();
        command.executeDo();
        this.undoStack.push(command);
    }

    public boolean canRedo() {
        return !this.redoStack.isEmpty();
    }

    public void push(EditAction command) {
        this.redoStack.clear();
        command.executeDo();
        this.undoStack.push(command);
    }
}
