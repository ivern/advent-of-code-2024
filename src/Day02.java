import aoc.Day;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class Day02 extends Day {

    @Override
    protected Long partOne(List<String> input) {
        long safeCount = 0;

        for (var line : input) {
            int[] levels = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();

            if (isSafe(levels)) {
                ++safeCount;
            }
        }

        return safeCount;
    }

    @Override
    protected Long partTwo(List<String> input) {
        long safeCount = 0;

        for (var line : input) {
            int[] levels = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();

            for (int i = 0; i < levels.length; i++) {
                int[] dampened = dampen(levels, i);
                if (isSafe(dampened)) {
                    ++safeCount;
                    break;
                }
            }
        }

        return safeCount;
    }

    private boolean isSafe(int[] levels) {
        if (levels[0] < levels[1]) {
            for (int i = 0; i < levels.length - 1; i++) {
                if (levels[i] >= levels[i + 1]) {
                    return false;
                }
                if (levels[i] < levels[i + 1] - 3) {
                    return false;
                }
            }
        } else {
            for (int i = 0; i < levels.length - 1; i++) {
                if (levels[i] <= levels[i + 1]) {
                    return false;
                }
                if (levels[i] > levels[i + 1] + 3) {
                    return false;
                }
            }
        }

        return true;
    }

    private int[] dampen(int[] levels, int badIndex) {
        int[] dampened = new int[levels.length - 1];
        for (int i = 0, j = 0; i < levels.length; i++) {
            if (i != badIndex) {
                dampened[j++] = levels[i];
            }
        }
        return dampened;
    }

}
