import aoc.Day;
import util.Coordinate;
import util.DenseGrid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings("unused")
public class Day08 extends Day {

    @Override
    protected Long partOne(List<String> input) {
        return solve(input, false);
    }

    @Override
    protected Long partTwo(List<String> input) {
        return solve(input, true);
    }

    private Long solve(List<String> input, boolean keepGoing) {
        var grid = DenseGrid.fromInput(input).getFirst();
        var antennasByFrequency = new HashMap<Character, List<Coordinate>>();

        for (int row = 0; row < grid.numRows(); row++) {
            for (int col = 0; col < grid.numCols(); col++) {
                if (grid.get(row, col) != '.') {
                    antennasByFrequency
                            .computeIfAbsent(grid.get(row, col), _c -> new ArrayList<>())
                            .add(new Coordinate(col, row));
                }
            }
        }

        var antinodes = new HashSet<Coordinate>();

        for (var frequency : antennasByFrequency.keySet()) {
            var antennas = antennasByFrequency.get(frequency);

            if (antennas.size() == 1) {
                break;
            }

            for (int i = 0; i < antennas.size(); ++i) {
                if (keepGoing) {
                    antinodes.add(antennas.get(i));
                }

                for (int j = i + 1; j < antennas.size(); ++j) {
                    int dr = antennas.get(i).row() - antennas.get(j).row();
                    int dc = antennas.get(i).col() - antennas.get(j).col();

                    var c = antennas.get(i);
                    while (true) {
                        c = new Coordinate(c.row() + dr, c.col() + dc);
                        if (grid.contains(c)) {
                            antinodes.add(c);
                        } else {
                            break;
                        }
                        if (!keepGoing) {
                            break;
                        }
                    }

                    c = antennas.get(j);
                    while (true) {
                        c = new Coordinate(c.row() - dr, c.col() - dc);
                        if (grid.contains(c)) {
                            antinodes.add(c);
                        } else {
                            break;
                        }
                        if (!keepGoing) {
                            break;
                        }
                    }
                }
            }
        }

        return (long) antinodes.size();
    }

}
