import aoc.Day;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings("unused")
public class Day05 extends Day {

    @Override
    protected Long partOne(List<String> input) {
        var lessThan = new HashMap<Long, HashSet<Long>>();
        int line = 0;

        while (!input.get(line).isBlank()) {
            var parts = input.get(line++).split("\\|");
            lessThan.computeIfAbsent(Long.parseLong(parts[0]), _k -> new HashSet<>())
                    .add(Long.parseLong(parts[1]));
        }

        long sum = 0;

        while (++line < input.size()) {
            boolean ordered = true;
            var parts = Arrays.stream(input.get(line).split(",")).mapToLong(Long::parseLong).toArray();
            for (int i = 0; i < parts.length && ordered; i++) {
                for (int j = i + 1; j < parts.length && ordered; j++) {
                    if (lessThan.containsKey(parts[j]) && lessThan.get(parts[j]).contains(parts[i])) {
                        ordered = false;
                    }
                }
            }

            if (ordered) {
                sum += parts[parts.length / 2];
            }
        }

        return sum;
    }

    @Override
    protected Long partTwo(List<String> input) {
        var lessThan = new HashMap<Long, HashSet<Long>>();
        int line = 0;

        while (!input.get(line).isBlank()) {
            var parts = input.get(line++).split("\\|");
            lessThan.computeIfAbsent(Long.parseLong(parts[0]), _k -> new HashSet<>())
                    .add(Long.parseLong(parts[1]));
        }

        long sum = 0;

        while (++line < input.size()) {
            boolean ordered = true;
            Long[] parts = Arrays.stream(input.get(line).split(",")).map(Long::parseLong).toArray(Long[]::new);
            for (int i = 0; i < parts.length && ordered; i++) {
                for (int j = i + 1; j < parts.length && ordered; j++) {
                    if (lessThan.containsKey(parts[j]) && lessThan.get(parts[j]).contains(parts[i])) {
                        ordered = false;
                    }
                }
            }

            if (!ordered) {
                Arrays.sort(parts, (x, y) -> {
                    if (lessThan.containsKey(x) && lessThan.get(x).contains(y)) {
                        return -1;
                    } else if (lessThan.containsKey(y) && lessThan.get(y).contains(x)) {
                        return 1;
                    } else {
                        return 0;
                    }
                });
                sum += parts[parts.length / 2];
            }
        }

        return sum;
    }

}
