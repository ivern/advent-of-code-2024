import aoc.Day;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public class Day01 extends Day {

    @Override
    protected Long partOne(List<String> input) {
        var list1 = new ArrayList<Integer>();
        var list2 = new ArrayList<Integer>();

        for (var line : input) {
            var numbers = line.split("   ");
            list1.add(Integer.parseInt(numbers[0]));
            list2.add(Integer.parseInt(numbers[1]));
        }

        Collections.sort(list1);
        Collections.sort(list2);

        long totalDistance = 0;

        for (int i = 0; i < list1.size(); i++) {
            totalDistance += Math.abs(list1.get(i) - list2.get(i));
        }

        return totalDistance;
    }

    @Override
    protected Long partTwo(List<String> input) {
        var list = new ArrayList<Integer>();
        var frequencies = new HashMap<Integer, Long>();

        for (var line : input) {
            var numbers = line.split("   ");
            list.add(Integer.parseInt(numbers[0]));
            frequencies.merge(Integer.parseInt(numbers[1]), 1L, Long::sum);
        }

        long similarityScore = 0;

        for (var element : list) {
            if (frequencies.containsKey(element)) {
                similarityScore += element * frequencies.get(element);
            }
        }

        return similarityScore;
    }

}
