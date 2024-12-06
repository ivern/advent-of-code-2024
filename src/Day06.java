import aoc.Day;
import util.Coordinate;
import util.Direction;

import java.util.HashSet;
import java.util.List;

@SuppressWarnings("unused")
public class Day06 extends Day {

    private record Guard(Coordinate coordinate, Direction direction) {
    }

    @Override
    protected Long partOne(List<String> input) {
        var map = toMap(input);
        var guard = findGuard(map);
        var seen = new HashSet<Coordinate>();

        while (guard.coordinate().isIn(map)) {
            seen.add(guard.coordinate());

            var nextPosition = guard.coordinate().move(guard.direction());

            if (nextPosition.isIn(map) && map[nextPosition.row()][nextPosition.col()] == '#') {
                guard = new Guard(guard.coordinate(), guard.direction().turnClockwise());
            } else {
                guard = new Guard(guard.coordinate().move(guard.direction()), guard.direction());
            }
        }

        return (long) seen.size();
    }

    @Override
    protected Long partTwo(List<String> input) {
        var map = toMap(input);
        long options = 0;

        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                if (map[row][col] == '.') {
                    map[row][col] = '#';

                    var guard = findGuard(map);
                    var seen = new HashSet<Guard>();

                    while (guard.coordinate().isIn(map)) {
                        if (seen.contains(guard)) {
                            ++options;
                            break;
                        } else {
                            seen.add(guard);
                        }

                        var nextPosition = guard.coordinate().move(guard.direction());

                        if (nextPosition.isIn(map) && map[nextPosition.row()][nextPosition.col()] == '#') {
                            guard = new Guard(guard.coordinate(), guard.direction().turnClockwise());
                        } else {
                            guard = new Guard(guard.coordinate().move(guard.direction()), guard.direction());
                        }
                    }

                    map[row][col] = '.';
                }
            }
        }

        return options;
    }

    private char[][] toMap(List<String> input) {
        char[][] map = new char[input.size()][];
        for (int i = 0; i < input.size(); i++) {
            map[i] = input.get(i).toCharArray();
        }
        return map;
    }

    private Guard findGuard(char[][] map) {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                if (isGuard(map[row][col])) {
                    return new Guard(new Coordinate(row, col), getDirection(map[row][col]));
                }
            }
        }

        throw new RuntimeException("Guard not found");
    }

    private boolean isGuard(char position) {
        return position == '<' || position == '>' || position == '^' || position == 'v';
    }

    private Direction getDirection(char guard) {
        return switch (guard) {
            case '^' -> Direction.UP;
            case '>' -> Direction.RT;
            case 'v' -> Direction.DN;
            case '<' -> Direction.LT;
            default -> throw new RuntimeException("Invalid guard");
        };
    }

}
