package util;

public record Coordinate(int row, int col) {

    public Coordinate move(Direction direction) {
        return move(direction, 1);
    }

    public Coordinate move(Direction direction, int distance) {
        return new Coordinate(row + direction.drow * distance, col + direction.dcol * distance);
    }

    public boolean isAdjacentTo(Coordinate other) {
        return (row == other.row() && Math.abs(col - other.col) == 1)
                || (col == other.col() && Math.abs(row - other.row) == 1);
    }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ')';
    }
}
