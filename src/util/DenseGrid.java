package util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class DenseGrid<T> {

    private final Class<T> klass;
    private final T[][] grid;
    private final int numRows;
    private final int numCols;

    @SuppressWarnings("unchecked")
    public DenseGrid(Class<T> klass, int numRows, int numCols) {
        this.klass = klass;
        this.numRows = numRows;
        this.numCols = numCols;

        this.grid = (T[][]) Array.newInstance(klass.arrayType(), numRows);
        for (int i = 0; i < numRows; ++i) {
            this.grid[i] = (T[]) Array.newInstance(klass, numCols);
        }
    }

    public DenseGrid(Class<T> klass, int numRows, int numCols, T defaultValue) {
        this(klass, numRows, numCols);

        for (int i = 0; i < numRows; ++i) {
            Arrays.fill(this.grid[i], defaultValue);
        }
    }

    public static List<DenseGrid<Character>> fromInput(List<String> data) {
        return fromInput(data, Character.class, Function.identity());
    }

    public static <T> List<DenseGrid<T>> fromInput(List<String> data, Class<T> klass, Function<Character, T> valueFn) {
        List<DenseGrid<T>> grids = new ArrayList<>();
        int nextLine = 0;

        while (nextLine < data.size()) {
            while (nextLine < data.size() && data.get(nextLine).isBlank()) {
                ++nextLine;
            }
            int startLine = nextLine;

            while (nextLine < data.size() && !data.get(nextLine).isBlank()) {
                ++nextLine;
            }

            int numRows = nextLine - startLine;
            int numCols = data.get(startLine).length();
            DenseGrid<T> grid = new DenseGrid<>(klass, numRows, numCols);

            for (int row = startLine; row < nextLine; ++row) {
                String line = data.get(row);
                for (int col = 0; col < numCols; ++col) {
                    grid.put(row, col, valueFn.apply(line.charAt(col)));
                }
            }

            grids.add(grid);
        }

        return grids;
    }

    public int numRows() {
        return numRows;
    }

    public int numCols() {
        return numCols;
    }

    public T put(int row, int col, T value) {
        T oldValue = grid[row][col];
        grid[row][col] = value;
        return oldValue;
    }

    public T put(Coordinate coordinate, T value) {
        return put(coordinate.row(), coordinate.col(), value);
    }

    public T get(int row, int col) {
        return grid[row][col];
    }

    public T get(Coordinate coordinate) {
        return get(coordinate.row(), coordinate.col());
    }

    public boolean contains(int row, int col) {
        return col >= 0 && col < numCols && row >= 0 && row < numRows;
    }

    public boolean contains(Coordinate coordinate) {
        return contains(coordinate.row(), coordinate.col());
    }

    public Coordinate find(Predicate<T> predicate) {
        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numCols; ++col) {
                if (predicate.test(grid[row][col])) {
                    return new Coordinate(row, col);
                }
            }
        }

        return null;
    }

    public DenseGrid<T> transpose() {
        DenseGrid<T> transposed = new DenseGrid<>(klass, numCols, numRows);

        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numCols; ++col) {
                transposed.grid[row][col] = grid[col][row];
            }
        }

        return transposed;
    }

    public <U> U mapReduce(Function<T, U> mapper, BiFunction<U, U, U> reducer, U initialValue) {
        return mapReduce((grid, row, col) -> mapper.apply(grid.get(row, col)), reducer, initialValue);
    }

    public <U> U mapReduce(CellMapper<T, U> mapper, BiFunction<U, U, U> reducer, U initialValue) {
        U result = initialValue;

        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numCols; ++col) {
                result = reducer.apply(result, mapper.map(this, row, col));
            }
        }

        return result;
    }

    public <U> DenseGrid<U> map(CellMapper<T, U> mapper, Class<U> newKlass) {
        DenseGrid<U> newGrid = new DenseGrid<>(newKlass, numRows, numCols);

        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numCols; ++col) {
                newGrid.grid[row][col] = mapper.map(this, row, col);
            }
        }

        return newGrid;
    }

    public void floodFill(int row, int col, T value, CellPredicate<T> boundary, Consumer<Coordinate> visitor) {
        if (!contains(row, col)) {
            return;
        }

        Deque<Coordinate> fringe = new LinkedList<>();
        fringe.addLast(new Coordinate(row, col));

        while (!fringe.isEmpty()) {
            Coordinate next = fringe.removeFirst();

            if (boundary.test(this, next.row(), next.col())) {
                continue;
            }

            visitor.accept(next);
            grid[next.row()][next.col()] = value;
            fringe.addAll(crossNeighbors(next.row(), next.col()));
        }
    }

    public void floodFill(Coordinate coordinate, T value, CellPredicate<T> boundary, Consumer<Coordinate> visitor) {
        floodFill(coordinate.row(), coordinate.col(), value, boundary, visitor);
    }

    public List<Coordinate> crossNeighbors(int row, int col) {
        return mapCrossNeighbors(row, col, (_g, r, c) -> new Coordinate(r, c));
    }

    public List<Coordinate> crossNeighbors(Coordinate coordinate) {
        return crossNeighbors(coordinate.row(), coordinate.col());
    }

    public <U> List<U> mapCrossNeighbors(int row, int col, CellMapper<T, U> mapper) {
        List<U> values = new ArrayList<>();

        final int[] drow = new int[]{1, 0, -1, 0};
        final int[] dcol = new int[]{0, 1, 0, -1};

        for (int i = 0; i < 4; ++i) {
            int newRow = row + drow[i];
            int newCol = col + dcol[i];

            if (contains(newRow, newCol)) {
                values.add(mapper.map(this, newRow, newCol));
            }
        }

        return values;
    }

    public List<Coordinate> allNeighbors(int row, int col) {
        return mapAllNeighbors(row, col, (_g, r, c) -> new Coordinate(r, c));
    }

    public <U> List<U> mapAllNeighbors(int row, int col, CellMapper<T, U> mapper) {
        List<U> values = new ArrayList<>();

        for (int dcol = -1; dcol <= 1; ++dcol) {
            for (int drow = -1; drow <= 1; ++drow) {
                if (dcol != 0 || drow != 0) {
                    int newRow = row + drow;
                    int newCol = col + dcol;

                    if (contains(newRow, newCol)) {
                        values.add(mapper.map(this, newRow, newCol));
                    }
                }
            }
        }

        return values;
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public DenseGrid<T> clone() {
        DenseGrid<T> clone = new DenseGrid<>(klass, numRows, numCols);

        for (int row = 0; row < numRows; ++row) {
            System.arraycopy(grid[row], 0, clone.grid[row], 0, numCols);
        }

        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DenseGrid<?> denseGrid = (DenseGrid<?>) o;
        return numRows == denseGrid.numRows && numCols == denseGrid.numCols && Objects.equals(klass, denseGrid.klass)
                && Arrays.deepEquals(grid, denseGrid.grid);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(klass, numRows, numCols);
        result = 31 * result + Arrays.deepHashCode(grid);
        return result;
    }

    @Override
    public String toString() {
        boolean first = true;
        StringBuilder builder = new StringBuilder();

        for (int row = 0; row < numRows; ++row) {
            if (first) {
                first = false;
            } else {
                builder.append("\n");
            }

            for (int col = 0; col < numCols; ++col) {
                builder.append(grid[row][col]);
            }
        }

        return builder.toString();
    }

    @FunctionalInterface
    public interface CellMapper<T, U> {
        U map(DenseGrid<T> grid, int row, int col);
    }

    @FunctionalInterface
    public interface CellPredicate<T> {
        boolean test(DenseGrid<T> grid, int row, int col);
    }

}
