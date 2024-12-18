import aoc.Day;
import util.Coordinate;
import util.DenseGrid;
import util.Direction;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Day15 extends Day {

    @Override
    protected Long partOne(List<String> input) {
        input = new ArrayList<>(input);

        var moves = input.removeLast();
        input.removeLast();

        var map = DenseGrid.fromInput(input).getFirst();
        var robot = map.find(c -> c == '@');

        for (char c : moves.toCharArray()) {
            var d = getDirection(c);
            if (canMove(map, robot, d)) {
                map = doMove(map, robot, d);
            }
        }

        return null;
    }

    @Override
    protected Long partTwo(List<String> input) {
        return null;
    }

    private boolean canMove(DenseGrid<Character> map, Coordinate robot, Direction direction) {
        var next = robot.move(direction);
        if (!map.contains(next)) return false;
        if (map.get(next) == '#') return false;
        if (map.get(next) == 'O') return canMove(map, next, direction);
        if (map.get(next) == '.') return true;

        throw new RuntimeException("Illegal state encountered in canMove");
    }

    private DenseGrid<Character> doMove(DenseGrid<Character> map, Coordinate robot, Direction direction) {
//        while (true) {
//            var next = robot.move(direction);
//            if (map.get(next) == '.') {
//                map.put(next, map.get(robot));
//            };
//        }

        return map;
    }

    private Direction getDirection(char c) {
        return switch (c) {
            case '^' -> Direction.UP;
            case 'v' -> Direction.DN;
            case '<' -> Direction.LT;
            case '>' -> Direction.RT;
            default -> throw new RuntimeException("Invalid direction: " + c);
        };
    }

}
