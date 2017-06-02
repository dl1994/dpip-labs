package editor.interfaces;

import editor.model.LocationRange;

public interface SelectionObserver {

    public void updateSelection(LocationRange selection);
}
