import aoc.Day;
import util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Day13 extends Day {

    @Override
    protected Long partOne(List<String> input) {
        long minTokens = 0;

        for (int nextMachine = 0; nextMachine < input.size(); nextMachine += 4) {
            var machine = parseMachine(input, nextMachine, 0);
            long best = minTokens(machine, 0, 0, 0, 0, new HashMap<>());
            if (best != -1) {
                minTokens += best;
            }
        }

        return minTokens;
    }

    /*
    px = a * ax + b * bx
    py = a * ay + b * by

    a = (px - b * bx) / ax
    b = (py - ((px - b * bx) / ax) * ay) / by
     */

    @Override
    protected Long partTwo(List<String> input) {
        long minTokens = 0;

        for (int nextMachine = 0; nextMachine < input.size(); nextMachine += 4) {
            var machine = parseMachine(input, nextMachine, 10000000000000L);
            var best = minTokens2(machine, 0, 0, 0, 0, new HashMap<>());
            if (best.first() != -1) {
                minTokens += best.first() * best.second();
            }
            System.out.println(best.first() + " " + best.second());
        }

        return minTokens;
    }

    private long minTokens(Machine machine, int x, int y, int amoves, int bmoves, Map<State, Long> cache) {
        if (amoves > 100 || bmoves > 100 || x > machine.px() || y > machine.py()) {
            return -1;
        } else if (x == machine.px() && y == machine.py()) {
            return 0;
        }

        var state = new State(machine, x, y, amoves, bmoves);

        if (!cache.containsKey(state)) {
            long a = minTokens(machine, x + machine.ax(), y + machine.ay(), amoves + 1, bmoves, cache);
            long b = minTokens(machine, x + machine.bx(), y + machine.by(), amoves, bmoves + 1, cache);
            long best = -1;

            if (a != -1 && b != -1) {
                best = Math.min(3 + a, 1 + b);
            } else if (a != -1) {
                best = 3 + a;
            } else if (b != -1) {
                best = 1 + b;
            }

            cache.put(state, best);
        }

        return cache.get(state);
    }

    private Pair<Long, Long> minTokens2(Machine machine, int x, int y, int amoves, int bmoves, Map<State, Pair<Long, Long>> cache) {
        if (x > machine.px() || y > machine.py()) {
            return new Pair<>(-1L, -1L);
        } else if (x != 0 && y != 0 && machine.px() % x == 0 && machine.py() % y == 0 && machine.px() / x == machine.py() / y) {
            return new Pair<>(1L, machine.px() / x);
        }

        var state = new State(machine, x, y, amoves, bmoves);

        if (!cache.containsKey(state)) {
            var a = minTokens2(machine, x + machine.ax(), y + machine.ay(), amoves + 1, bmoves, cache);
            var b = minTokens2(machine, x + machine.bx(), y + machine.by(), amoves, bmoves + 1, cache);
            var best = new Pair<>(-1L, -1L);

            if (a.first() != -1 && b.first() != -1) {
                if (3 + a.first() * a.second() < 1 + b.first() * b.second()) {
                    best = new Pair<>(3 + a.first(), a.second());
                } else {
                    best = new Pair<>(1 + b.first(), b.second());
                }
            } else if (a.first() != -1) {
                best = new Pair<>(3 + a.first(), a.second());
            } else if (b.first() != -1) {
                best = new Pair<>(1 + b.first(), b.second());
            }

            cache.put(state, best);
        }

        return cache.get(state);
    }

    private record State(Machine machine, int x, int y, int amoves, int bmoves) {
    }

    private record Machine(int ax, int ay, int bx, int by, long px, long py) {
    }

    private static final Pattern BUTTON_PARSER = Pattern.compile("Button [AB]: X\\+([0-9]+), Y\\+([0-9]+)");
    private static final Pattern PRIZE_PARSER = Pattern.compile("Prize: X=([0-9]+), Y=([0-9]+)");

    private Machine parseMachine(List<String> input, int startingLine, long prizeFix) {
        var buttonMatcherA = BUTTON_PARSER.matcher(input.get(startingLine));
        var buttonMatcherB = BUTTON_PARSER.matcher(input.get(startingLine + 1));
        var prizeMatcher = PRIZE_PARSER.matcher(input.get(startingLine + 2));

        if (!buttonMatcherA.find() || !buttonMatcherB.find() || !prizeMatcher.find()) {
            throw new RuntimeException("Invalid machine input");
        }

        return new Machine(
                Integer.parseInt(buttonMatcherA.group(1)),
                Integer.parseInt(buttonMatcherA.group(2)),
                Integer.parseInt(buttonMatcherB.group(1)),
                Integer.parseInt(buttonMatcherB.group(2)),
                Long.parseLong(prizeMatcher.group(1)) + prizeFix,
                Long.parseLong(prizeMatcher.group(2)) + prizeFix);
    }

}
