import aoc.Day;

import java.util.List;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Day03 extends Day {

    @Override
    protected Long partOne(List<String> input) {
        var pattern = Pattern.compile("mul\\(([0-9]+),([0-9]+)\\)");
        var matcher = pattern.matcher(String.join("", input));

        long result = 0;

        while (matcher.find()) {
            result += Long.parseLong(matcher.group(1)) * Long.parseLong(matcher.group(2));
        }

        return result;
    }

    @Override
    protected Long partTwo(List<String> input) {
        var pattern = Pattern.compile("mul\\(([0-9]+),([0-9]+)\\)|do\\(\\)|don't\\(\\)");
        var matcher = pattern.matcher(String.join("", input));

        boolean enabled = true;
        long result = 0;

        while (matcher.find()) {
            if (matcher.group(0).equals("do()")) {
                enabled = true;
            } else if (matcher.group(0).equals("don't()")) {
                enabled = false;
            } else if (enabled) {
                result += Long.parseLong(matcher.group(1)) * Long.parseLong(matcher.group(2));
            }
        }

        return result;
    }

}
