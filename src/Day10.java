import aoc.Day;
import util.Coordinate;
import util.DenseGrid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class Day10 extends Day {

    @Override
    protected Long partOne(List<String> input) {
        var map = DenseGrid.fromInput(input, Integer.class, Character::getNumericValue).getFirst();
        long count = 0;

        for (int row = 0; row < map.numRows(); row++) {
            for (int col = 0; col < map.numCols(); col++) {
                if (map.get(row, col) == 0) {
                    var seen = new HashSet<Coordinate>();
                    walkTrails(map, new Coordinate(row, col), seen);
                    count += seen.stream().mapToLong(c -> (map.get(c) == 9) ? 1 : 0).sum();
                }
            }
        }

        return count;
    }

    @Override
    protected Long partTwo(List<String> input) {
        var map = DenseGrid.fromInput(input, Integer.class, Character::getNumericValue).getFirst();
        long count = 0;

        for (int row = 0; row < map.numRows(); row++) {
            for (int col = 0; col < map.numCols(); col++) {
                if (map.get(row, col) == 0) {
                    var seen = new HashSet<Coordinate>();
                    count += countTrails(map, new Coordinate(row, col), seen);
                }
            }
        }

        return count;
    }

    private void walkTrails(DenseGrid<Integer> map, Coordinate coordinate, Set<Coordinate> seen) {
        seen.add(coordinate);

        for (var neighbor : map.crossNeighbors(coordinate)) {
            if (!seen.contains(neighbor) && map.get(neighbor) == map.get(coordinate) + 1) {
                walkTrails(map, neighbor, seen);
            }
        }
    }

    private long countTrails(DenseGrid<Integer> map, Coordinate coordinate, Set<Coordinate> seen) {
        if (map.get(coordinate) == 9) {
            return 1;
        }

        seen.add(coordinate);
        long count = 0;

        for (var neighbor : map.crossNeighbors(coordinate)) {
            if (!seen.contains(neighbor) && map.get(neighbor) == map.get(coordinate) + 1) {
                count += countTrails(map, neighbor, seen);
            }
        }

        seen.remove(coordinate);
        return count;
    }

}
