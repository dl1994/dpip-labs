package editor.interfaces;

import editor.model.Location;

public interface CursorObserver {

    void updateCursorLocation(Location location);
}
