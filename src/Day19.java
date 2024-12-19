import aoc.Day;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Day19 extends Day {

    private final Map<String, Long> cache = new HashMap<>();

    @Override
    protected Long partOne(List<String> input) {
        long count = 0;
        var patterns = Arrays.stream(input.getFirst().split(",")).map(String::trim).toList();

        for (int i = 2; i < input.size(); i++) {
            if (countWays(input.get(i), patterns, cache) > 0) {
                ++count;
            }
        }

        return count;
    }

    @Override
    protected Long partTwo(List<String> input) {
        long count = 0;
        var patterns = Arrays.stream(input.getFirst().split(",")).map(String::trim).toList();

        for (int i = 2; i < input.size(); i++) {
            count += countWays(input.get(i), patterns, cache);
        }

        return count;
    }

    private long countWays(String design, List<String> patterns, Map<String, Long> cache) {
        if (design.isEmpty()) {
            return 1L;
        }

        if (!cache.containsKey(design)) {
            long ways = 0;

            for (int i = 0; i < patterns.size(); i++) {
                var pattern = patterns.get(i);
                if (design.startsWith(pattern)) {
                    ways += countWays(design.substring(pattern.length()), patterns, cache);
                }
            }

            cache.put(design, ways);
        }

        return cache.get(design);
    }

}
