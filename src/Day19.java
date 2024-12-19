import aoc.Day;
import util.Coordinate;
import util.DenseGrid;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class Day18 extends Day {

    private static final int GRID_SIZE = 71;
    private static final Coordinate EXIT = new Coordinate(GRID_SIZE - 1, GRID_SIZE - 1);

    @Override
    protected Long partOne(List<String> input) {
        var corrupted = new HashSet<Coordinate>();
        for (var i = 0; i < 1024; i++) {
            var parts = input.get(i).split(",");
            corrupted.add(new Coordinate(Integer.parseInt(parts[1]), Integer.parseInt(parts[0])));
        }

        var map = new DenseGrid<>(Character.class, GRID_SIZE, GRID_SIZE, '.');

        return findPath(map, corrupted);
    }

    @Override
    protected Long partTwo(List<String> input) {
        int min = 0;
        int max = input.size() - 1;

        var map = new DenseGrid<>(Character.class, GRID_SIZE, GRID_SIZE, '.');

        while (true) {
            if (min >= max - 1) {
                System.out.println(input.get(min));
                return (long) min;
            }

            int stop = min + (max - min) / 2;

            var corrupted = new HashSet<Coordinate>();
            for (var i = 0; i < stop; i++) {
                var parts = input.get(i).split(",");
                corrupted.add(new Coordinate(Integer.parseInt(parts[1]), Integer.parseInt(parts[0])));
            }

            if (findPath(map, corrupted) == -1) {
                max = stop;
            } else {
                min = stop;
            }
        }
    }

    record Step(Coordinate coordinate, long steps) {
    }

    private long findPath(DenseGrid<Character> map, Set<Coordinate> corrupted) {
        var seen = new HashSet<Coordinate>();

        var fringe = new ArrayDeque<Step>();
        fringe.addLast(new Step(new Coordinate(0, 0), 0));

        while (!fringe.isEmpty()) {
            var next = fringe.removeFirst();

            if (next.coordinate().equals(EXIT)) {
                return next.steps();
            }

            if (seen.contains(next.coordinate()) || corrupted.contains(next.coordinate())) {
                continue;
            }
            seen.add(next.coordinate());

            map.crossNeighbors(next.coordinate()).stream()
                    .filter(c -> !seen.contains(c))
                    .filter(c -> !corrupted.contains(c))
                    .forEach(c -> fringe.addLast(new Step(c, next.steps() + 1)));
        }

        return -1L;
    }

}
