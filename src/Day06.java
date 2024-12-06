import aoc.Day;
import util.Coordinate;
import util.DenseGrid;
import util.Direction;

import java.util.HashSet;
import java.util.List;

@SuppressWarnings("unused")
public class Day06 extends Day {

    private record Guard(Coordinate coordinate, Direction direction) {
    }

    @Override
    protected Long partOne(List<String> input) {
        var map = DenseGrid.fromInput(input).get(0);
        var guardPosition = map.find(this::isGuard);
        var guard = new Guard(guardPosition, getDirection(map.get(guardPosition)));
        var seen = new HashSet<Coordinate>();

        while (map.contains(guard.coordinate())) {
            seen.add(guard.coordinate());

            var nextPosition = guard.coordinate().move(guard.direction());

            guard = (map.contains(nextPosition) && map.get(nextPosition) == '#')
                    ? new Guard(guard.coordinate(), guard.direction().turnClockwise())
                    : new Guard(guard.coordinate().move(guard.direction()), guard.direction());
        }

        return (long) seen.size();
    }

    @Override
    protected Long partTwo(List<String> input) {
        var map = DenseGrid.fromInput(input).get(0);

        return map.mapReduce((grid, row, col) -> {
            if (grid.get(row, col) != '.') {
                return 0L;
            }

            map.put(row, col, '#');

            var guardPosition = map.find(this::isGuard);
            var guard = new Guard(guardPosition, getDirection(map.get(guardPosition)));
            var seen = new HashSet<Guard>();

            while (map.contains(guard.coordinate())) {
                if (seen.contains(guard)) {
                    map.put(row, col, '.');
                    return 1L;
                } else {
                    seen.add(guard);
                }

                var nextPosition = guard.coordinate().move(guard.direction());

                guard = (map.contains(nextPosition) && map.get(nextPosition) == '#')
                        ? new Guard(guard.coordinate(), guard.direction().turnClockwise())
                        : new Guard(guard.coordinate().move(guard.direction()), guard.direction());
            }

            map.put(row, col, '.');
            return 0L;
        }, Long::sum, 0L);
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
