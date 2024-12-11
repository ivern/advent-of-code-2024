import aoc.Day;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class Day11 extends Day {

    @Override
    protected Long partOne(List<String> input) {
        return solve(input, 25);
    }

    @Override
    protected Long partTwo(List<String> input) {
        return solve(input, 75);
    }

    private Long solve(List<String> input, int blinks) {
        var cache = new HashMap<StoneBlinks, Long>();
        return Arrays.stream(input.getFirst().split(" "))
                .map(Long::parseLong)
                .mapToLong(stone -> count(new StoneBlinks(stone, blinks), cache))
                .sum();
    }

    private Long count(StoneBlinks stoneBlinks, Map<StoneBlinks, Long> cache) {
        if (stoneBlinks.blinks() == 0) {
            return 1L;
        }

        if (!cache.containsKey(stoneBlinks)) {
            int nextBlinks = stoneBlinks.blinks() - 1;
            cache.put(stoneBlinks,
                    evolve(stoneBlinks.stone())
                            .mapToLong(stone -> count(new StoneBlinks(stone, nextBlinks), cache))
                            .sum());
        }

        return cache.get(stoneBlinks);
    }

    private Stream<Long> evolve(Long stone) {
        if (stone == 0) {
            return Stream.of(1L);
        }
        
        var digits = stone.toString();
        if (digits.length() % 2 == 0) {
            var middle = digits.length() / 2;
            return Stream.of(
                    Long.parseLong(digits.substring(0, middle)),
                    Long.parseLong(digits.substring(middle)));
        }

        return Stream.of(stone * 2024);
    }

    private record StoneBlinks(Long stone, int blinks) {
    }

}
