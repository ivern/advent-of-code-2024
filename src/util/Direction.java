package util;

public enum Direction {

    UP(-1, 0),
    DN(1, 0),
    RT(0, 1),
    LT(0, -1);

    public final int drow;
    public final int dcol;

    Direction(int drow, int dcol) {
        this.drow = drow;
        this.dcol = dcol;
    }

    public Direction turnClockwise() {
        return switch (this) {
            case UP -> RT;
            case RT -> DN;
            case DN -> LT;
            case LT -> UP;
        };
    }

    public Direction turnCounterClockwise() {
        return switch (this) {
            case UP -> LT;
            case LT -> DN;
            case DN -> RT;
            case RT -> UP;
        };
    }

}
