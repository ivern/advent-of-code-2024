import aoc.Day;
import util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class Day07 extends Day {

    @Override
    protected Long partOne(List<String> input) {
        return solve(input, false);
    }

    @Override
    protected Long partTwo(List<String> input) {
        return solve(input, true);
    }

    private long solve(List<String> input, boolean canConcatenate) {
        long total = 0;

        for (String line : input) {
            var equation = parse(line);

            if (isValid(equation.first(), equation.second(), canConcatenate)) {
                total += equation.first();
            }
        }

        return total;
    }

    private boolean isValid(long result, List<Long> operands, boolean canConcatenate) {
        if (operands.isEmpty()) {
            return false;
        }

        if (operands.size() == 1) {
            return result == operands.getFirst();
        }

        var newOperands = new ArrayList<>(operands);
        var summing = newOperands.get(0) + newOperands.get(1);
        var multiplying = newOperands.get(0) * newOperands.get(1);
        var concatenating = Long.parseLong(String.format("%d%d", newOperands.get(0), newOperands.get(1)));

        newOperands.removeFirst();
        newOperands.removeFirst();

        newOperands.addFirst(summing);
        if (isValid(result, newOperands, canConcatenate)) {
            return true;
        }
        newOperands.removeFirst();

        newOperands.addFirst(multiplying);
        if (isValid(result, newOperands, canConcatenate)) {
            return true;
        }

        if (canConcatenate) {
            newOperands.removeFirst();
            newOperands.addFirst(concatenating);
            if (isValid(result, newOperands, canConcatenate)) {
                return true;
            }
        }

        return false;
    }

    private Pair<Long, List<Long>> parse(String line) {
        var parts = line.split(": ");
        var operands = parts[1].split(" ");

        return new Pair<>(
                Long.parseLong(parts[0]),
                Arrays.stream(operands).map(Long::parseLong).toList());
    }

}
