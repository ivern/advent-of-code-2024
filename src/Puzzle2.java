import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("unused")
public class Puzzle2 {

    public int solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day1.txt"));
        var list = new ArrayList<Integer>();
        var frequencies = new HashMap<Integer, Integer>();

        for (var line : lines) {
            var numbers = line.split("   ");
            list.add(Integer.parseInt(numbers[0]));
            frequencies.merge(Integer.parseInt(numbers[1]), 1, Integer::sum);
        }

        int similarityScore = 0;

        for (var element : list) {
            if (frequencies.containsKey(element)) {
                similarityScore += element * frequencies.get(element);
            }
        }

        return similarityScore;
    }

}
