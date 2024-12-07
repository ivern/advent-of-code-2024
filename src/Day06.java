import aoc.Day;
import util.Coordinate;
import util.DenseGrid;
import util.Direction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class Day06 extends Day {

    private record Guard(Coordinate coordinate, Direction direction) {
    }

    @Override
    protected Long partOne(List<String> input) {
        var map = DenseGrid.fromInput(input).getFirst();

        var guardPosition = map.find(this::isGuard);
        var guard = new Guard(guardPosition, getDirection(map.get(guardPosition)));

        return (long) getPath(map, guard).size();
    }

    @Override
    protected Long partTwo(List<String> input) {
        var map = DenseGrid.fromInput(input).getFirst();

        var guardPosition = map.find(this::isGuard);
        var guard = new Guard(guardPosition, getDirection(map.get(guardPosition)));
        var initialPath = getPath(map, guard);

        long options = 0;

        for (var coordinate : initialPath) {
            if (map.get(coordinate) != '.') {
                continue;
            }

            map.put(coordinate, '#');

            guard = new Guard(guardPosition, getDirection(map.get(guardPosition)));
            var seen = new HashSet<Guard>();

            while (map.contains(guard.coordinate())) {
                if (seen.contains(guard)) {
                    ++options;
                    break;
                } else {
                    seen.add(guard);
                }

                var nextPosition = guard.coordinate().move(guard.direction());

                guard = (map.contains(nextPosition) && map.get(nextPosition) == '#')
                        ? new Guard(guard.coordinate(), guard.direction().turnClockwise())
                        : new Guard(guard.coordinate().move(guard.direction()), guard.direction());
            }

            map.put(coordinate, '.');
        }

        return options;
    }

    private Set<Coordinate> getPath(DenseGrid<Character> map, Guard guard) {
        var seen = new HashSet<Coordinate>();

        while (map.contains(guard.coordinate())) {
            seen.add(guard.coordinate());

            var nextPosition = guard.coordinate().move(guard.direction());

            guard = (map.contains(nextPosition) && map.get(nextPosition) == '#')
                    ? new Guard(guard.coordinate(), guard.direction().turnClockwise())
                    : new Guard(guard.coordinate().move(guard.direction()), guard.direction());
        }

        return seen;
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
