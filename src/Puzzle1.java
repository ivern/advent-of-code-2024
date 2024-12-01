import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("unused")
public class Puzzle1 {

    public int solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day1.txt"));
        var list1 = new ArrayList<Integer>();
        var list2 = new ArrayList<Integer>();

        for (var line : lines) {
            var numbers = line.split("   ");
            list1.add(Integer.parseInt(numbers[0]));
            list2.add(Integer.parseInt(numbers[1]));
        }

        Collections.sort(list1);
        Collections.sort(list2);

        int totalDistance = 0;

        for (int i = 0; i < list1.size(); i++) {
            totalDistance += Math.abs(list1.get(i) - list2.get(i));
        }

        return totalDistance;
    }

}
