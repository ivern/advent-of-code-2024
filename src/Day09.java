import aoc.Day;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class Day09 extends Day {

    @Override
    protected Long partOne(List<String> input) {
        var blocks = expand(input.getFirst());

        while (true) {
            int next = getNextEmptyBlock(blocks);
            if (next == -1) {
                break;
            }

            blocks.set(next, blocks.getLast());
            blocks.removeLast();
        }

        return checksum(blocks);
    }

    private List<Long> expand(String input) {
        var list = new ArrayList<Long>();
        boolean even = true;
        long id = 0;

        for (char c : input.toCharArray()) {
            long count = Long.parseLong(String.valueOf(c));
            for (int i = 0; i < count; i++) {
                list.add(even ? id : -1);
            }
            if (even) {
                ++id;
            }
            even = !even;
        }

        return list;
    }

    private int getNextEmptyBlock(List<Long> blocks) {
        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i) == -1) {
                return i;
            }
        }
        return -1;
    }

    private long checksum(List<Long> blocks) {
        long checksum = 0;
        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i) != -1) {
                checksum += i * blocks.get(i);
            }
        }
        return checksum;
    }

    @Override
    protected Long partTwo(List<String> input) {
        var files = files(input.getFirst());
        var ids = files.stream()
                .sorted(Comparator.comparingLong(File::id))
                .map(File::id)
                .filter(id -> id != -1)
                .toList();

        for (int i = ids.size() - 1; i >= 0; i--) {
            var id = ids.get(i);

            int fileIndex = findIndex(files, f -> f.id() == id);
            var file = files.get(fileIndex);

            int holeIndex = findIndex(files, f -> f.id() == -1 && f.size() >= file.size());
            if (holeIndex == -1 || holeIndex >= fileIndex) {
                continue;
            }

            var newFiles = new ArrayList<File>();
            for (int j = 0; j < holeIndex; ++j) {
                newFiles.add(files.get(j));
            }
            newFiles.add(file);
            if (files.get(holeIndex).size() > file.size()) {
                newFiles.add(new File(-1, files.get(holeIndex).size() - file.size()));
            }
            for (int j = holeIndex + 1; j < files.size(); ++j) {
                if (j != fileIndex) {
                    newFiles.add(files.get(j));
                } else {
                    newFiles.add(new File(-1, files.get(j).size()));
                }
            }

            files = newFiles;
        }

        return checksum(files.stream().flatMap(file -> {
            var blocks = new ArrayList<Long>();
            for (int i = 0; i < file.size(); i++) {
                blocks.add(file.id());
            }
            return blocks.stream();
        }).toList());
    }

    private int findIndex(List<File> files, Predicate<File> predicate) {
        for (int i = 0; i < files.size(); i++) {
            if (predicate.test(files.get(i))) {
                return i;
            }
        }
        return -1;
    }

    private List<File> files(String input) {
        var list = new ArrayList<File>();
        boolean even = true;
        long id = 0;

        for (char c : input.toCharArray()) {
            int count = Integer.parseInt(String.valueOf(c));
            list.add(new File(even ? id : -1, count));
            if (even) {
                ++id;
            }
            even = !even;
        }

        return list;
    }

    private record File(long id, int size) {
    }

}
