import aoc.Day;
import util.Coordinate;
import util.DenseGrid;
import util.Direction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day12 extends Day {

    @Override
    protected Long partOne(List<String> input) {
        var grid = DenseGrid.fromInput(input).getFirst();
        var seen = new HashSet<Coordinate>();
        long price = 0;

        for (int row = 0; row < grid.numRows(); row++) {
            for (int col = 0; col < grid.numCols(); col++) {
                var sides = flood(grid, new Coordinate(row, col), seen);

                long area = sides.stream().map(Side::coordinate)
                        .collect(Collectors.toSet())
                        .size();
                long perimeter = sides.stream().filter(s -> s.direction() != null).count();

                price += area * perimeter;
            }
        }

        return price;
    }

    @Override
    protected Long partTwo(List<String> input) {
        var grid = DenseGrid.fromInput(input).getFirst();
        var seen = new HashSet<Coordinate>();
        long price = 0;

        for (int row = 0; row < grid.numRows(); row++) {
            for (int col = 0; col < grid.numCols(); col++) {
                var sides = flood(grid, new Coordinate(row, col), seen);

                long area = sides.stream().map(Side::coordinate)
                        .collect(Collectors.toSet())
                        .size();
                long numSides = countMergedSides(
                        sides.stream()
                                .filter(s -> s.direction() != null)
                                .sorted(Comparator.<Side>comparingInt(s -> s.coordinate().row()).thenComparingInt(s -> s.coordinate().col()))
                                .collect(Collectors.toList()));

                price += area * numSides;
            }
        }

        return price;
    }

    private static final int[] DX = new int[]{0, 1, 0, -1};
    private static final int[] DY = new int[]{1, 0, -1, 0};
    private static final Direction[] DIR = new Direction[]{Direction.DN, Direction.RT, Direction.UP, Direction.LT};

    private Set<Side> flood(DenseGrid<Character> grid, Coordinate start, Set<Coordinate> seen) {
        var sides = new HashSet<Side>();

        if (!seen.contains(start)) {
            var plant = grid.get(start);
            grid = grid.clone();

            grid.floodFill(start, '.',
                    (g, r, c) -> g.get(r, c) != plant,
                    seen::add);

            for (int y = 0; y < grid.numRows(); y++) {
                for (int x = 0; x < grid.numCols(); x++) {
                    var pos = new Coordinate(y, x);

                    if (grid.get(pos) == '.') {
                        sides.add(new Side(pos, null));

                        for (int i = 0; i < 4; ++i) {
                            int y2 = y + DY[i];
                            int x2 = x + DX[i];
                            if (!grid.contains(y2, x2) || grid.get(y2, x2) != '.') {
                                sides.add(new Side(pos, DIR[i]));
                            }
                        }
                    }
                }
            }
        }

        return sides;
    }

    private Long countMergedSides(List<Side> sides) {
        sides = new ArrayList<>(sides);
        long count = 0;

        while (!sides.isEmpty()) {
            var merged = new ArrayList<Side>();
            merged.add(sides.stream().findFirst().get());
            ++count;

            for (var side : sides) {
                if (!merged.contains(side) && isAdjacent(side, merged)) {
                    merged.add(side);
                }
            }

            sides.removeAll(merged);
        }

        return count;
    }

    private boolean isAdjacent(Side side, List<Side> merged) {
        if (merged.stream().findFirst().get().direction() != side.direction()) {
            return false;
        }

        for (var mergedSide : merged) {
            if (side.coordinate().isAdjacentTo(mergedSide.coordinate())) {
                return true;
            }
        }

        return false;
    }

    private record Side(Coordinate coordinate, Direction direction) {
    }

}
