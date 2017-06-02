package editor.model;

public final class LocationRange {

    private final Location start = new Location();
    private final Location end = new Location();

    public void setStartRow(int row) {
        this.start.setRow(row);
    }

    public int getStartRow() {
        return this.start.getRow();
    }

    public void setEndRow(int row) {
        this.end.setRow(row);
    }

    public int getEndRow() {
        return this.end.getRow();
    }

    public void setStartColumn(int column) {
        this.start.setColumn(column);
    }

    public int getStartColumn() {
        return this.start.getColumn();
    }

    public void setEndColumn(int column) {
        this.end.setColumn(column);
    }

    public int getEndColumn() {
        return this.end.getColumn();
    }

    public void setStart(Location start) {
        this.start.set(start);
    }

    public void setEnd(Location end) {
        this.end.set(end);
    }

    public void set(LocationRange other) {
        this.start.set(other.start);
        this.end.set(other.end);
    }
}
