package aoc;

import util.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public abstract class Day {

    @SuppressWarnings("unused")
    public Pair<Result, Result> solve() {
        var input = getInput();

        long partOneStartTime = System.nanoTime();
        Long partOneResult = partOne(input);
        long partOneEndTime = System.nanoTime();

        long partTwoStartTime = System.nanoTime();
        Long partTwoResult = partTwo(input);
        long partTwoEndTime = System.nanoTime();

        return new Pair<>(
                new Result(partOneResult, (partOneEndTime - partOneStartTime) / 1_000_000),
                new Result(partTwoResult, (partTwoEndTime - partTwoStartTime) / 1_000_000));
    }

    protected Long partOne(List<String> input) {
        return null;
    }

    protected Long partTwo(List<String> input) {
        return null;
    }

    private List<String> getInput() {
        try (var lines = Files.lines(Paths.get("./data/" + getClass().getName().toLowerCase() + ".txt"))) {
            return lines.toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
