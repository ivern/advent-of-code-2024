import aoc.Result;
import util.Pair;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final boolean RUN_ALL = true;

    public static void main(String[] args) throws ClassNotFoundException {
        if (RUN_ALL) {
            findPuzzleSolutionsInOrder(new File("./src")).forEach(Main::solve);
        } else {
            solve(findPuzzleSolutionsInOrder(new File("./src")).getLast());
        }
    }

    private static <T> void solve(Class<T> klass) {
        try {
            T instance = klass.getDeclaredConstructor().newInstance();
            Method solver = klass.getMethod("solve");

            @SuppressWarnings("unchecked")
            Pair<Result, Result> result = (Pair<Result, Result>) solver.invoke(instance);

            System.out.println(klass.getName() + " Part 1: " + result.first().result() + " (" + result.first().time() + "ms)");
            System.out.println(klass.getName() + " Part 2: " + result.second().result() + " (" + result.second().time() + "ms)");
        } catch (Exception e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    private static List<Class<?>> findPuzzleSolutionsInOrder(File directory) throws ClassNotFoundException {
        var classes = new ArrayList<Class<?>>();
        if (!directory.exists()) {
            return classes;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return classes;
        }

        for (File file : files) {
            if (file.getName().startsWith("Day") && file.getName().endsWith(".java")) {
                classes.add(Class.forName(file.getName().substring(0, file.getName().length() - 5)));
            }
        }

        classes.sort((a, b) -> {
            int aNum = Integer.parseInt(a.getName().substring("Day".length()));
            int bNum = Integer.parseInt(b.getName().substring("Day".length()));
            return aNum - bNum;
        });

        return classes;
    }

}