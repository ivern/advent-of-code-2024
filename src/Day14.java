import aoc.Day;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Day14 extends Day {

    private static final Pattern INPUT_FORMAT = Pattern.compile("p=([0-9]+),([0-9]+) v=(-?[0-9]+),(-?[0-9]+)");
    private static final int HEIGHT = 103;
    private static final int WIDTH = 101;
    private static final int SECONDS = 100;

    @Override
    protected Long partOne(List<String> input) {
        long[] counts = new long[5];
        Arrays.fill(counts, 0);

        for (int i = 0; i < input.size(); i++) {
            Matcher matcher = INPUT_FORMAT.matcher(input.get(i));
            if (matcher.matches()) {
                int px = Integer.parseInt(matcher.group(1));
                int py = Integer.parseInt(matcher.group(2));
                int vx = Integer.parseInt(matcher.group(3));
                int vy = Integer.parseInt(matcher.group(4));

                px = (px + vx * SECONDS) % WIDTH;
                if (px < 0) {
                    px += WIDTH;
                }

                py = (py + vy * SECONDS) % HEIGHT;
                if (py < 0) {
                    py += HEIGHT;
                }

                counts[classify(px, py)]++;
            }
        }

        return counts[1] * counts[2] * counts[3] * counts[4];
    }

    @Override
    protected Long partTwo(List<String> input) {
        return null;
    }

    private int classify(int x, int y) {
        if (x == WIDTH / 2 || y == HEIGHT / 2) {
            return 0;
        } else if (y < HEIGHT / 2) {
            return (x < WIDTH / 2) ? 1 : 2;
        } else {
            return (x < WIDTH / 2) ? 3 : 4;
        }
    }

}
