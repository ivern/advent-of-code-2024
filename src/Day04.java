import aoc.Day;

import java.util.List;

@SuppressWarnings("unused")
public class Day04 extends Day {

    @Override
    protected Long partOne(List<String> input) {
        int rows = input.size();
        int cols = input.get(0).length();

        long wordCount = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (checkOne(input, row, col, rows, cols, 0, 1)) ++wordCount;
                if (checkOne(input, row, col, rows, cols, 0, -1)) ++wordCount;
                if (checkOne(input, row, col, rows, cols, 1, 0)) ++wordCount;
                if (checkOne(input, row, col, rows, cols, -1, 0)) ++wordCount;
                if (checkOne(input, row, col, rows, cols, 1, 1)) ++wordCount;
                if (checkOne(input, row, col, rows, cols, 1, -1)) ++wordCount;
                if (checkOne(input, row, col, rows, cols, -1, 1)) ++wordCount;
                if (checkOne(input, row, col, rows, cols, -1, -1)) ++wordCount;
            }
        }

        return wordCount;
    }

    @Override
    protected Long partTwo(List<String> input) {
        int rows = input.size();
        int cols = input.get(0).length();

        long wordCount = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (checkTwo(input, row, col, rows, cols, 1, 1)) ++wordCount;
                if (checkTwo(input, row, col, rows, cols, 1, -1)) ++wordCount;
                if (checkTwo(input, row, col, rows, cols, -1, 1)) ++wordCount;
                if (checkTwo(input, row, col, rows, cols, -1, -1)) ++wordCount;
            }
        }

        return wordCount;
    }

    private boolean checkOne(List<String> input, int row, int col, int rows, int cols, int dr, int dc) {
        char[] xmas = "XMAS".toCharArray();

        for (int i = 0; i < xmas.length; i++) {
            if (row < 0 || col < 0 || row >= rows || col >= cols) return false;
            if (input.get(row).charAt(col) != xmas[i]) return false;
            row += dr;
            col += dc;
        }

        return true;
    }

    private boolean checkTwo(List<String> input, int row, int col, int rows, int cols, int d1, int d2) {
        if (row < 0 || col < 0 || row >= rows - 2 || col >= cols - 2) return false;

        if (input.get(row + 1).charAt(col + 1) != 'A') return false;
        if (d1 == 1) {
            if (input.get(row).charAt(col) != 'M' || input.get(row + 2).charAt(col + 2) != 'S') return false;
        } else {
            if (input.get(row).charAt(col) != 'S' || input.get(row + 2).charAt(col + 2) != 'M') return false;
        }
        if (d2 == 1) {
            if (input.get(row + 2).charAt(col) != 'M' || input.get(row).charAt(col + 2) != 'S') return false;
        } else {
            if (input.get(row + 2).charAt(col) != 'S' || input.get(row).charAt(col + 2) != 'M') return false;
        }

        return true;
    }
    
}
