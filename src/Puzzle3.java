import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@SuppressWarnings("unused")
public class Puzzle3 {

    public long solve() {
        try (var lines = Files.lines(Paths.get("./data/day2.txt"))) {
            return lines.filter(this::isSafe).count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isSafe(String text) {
        int[] levels = Arrays.stream(text.split(" ")).mapToInt(Integer::parseInt).toArray();

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

}
