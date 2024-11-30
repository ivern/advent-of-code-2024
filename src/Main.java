import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final boolean RUN_ALL = false;

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

            long start = System.nanoTime();
            Object result = solver.invoke(instance);
            long end = System.nanoTime();

            System.out.println(klass.getName() + ": " + result + " (" + ((end - start) / 1_000_000.0) + "ms)");
        } catch (Exception e) {
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
            if (file.getName().startsWith("Puzzle") && file.getName().endsWith(".java")) {
                classes.add(Class.forName(file.getName().substring(0, file.getName().length() - 5)));
            }
        }

        classes.sort((a, b) -> {
            int aNum = Integer.parseInt(a.getName().substring("Puzzle".length()));
            int bNum = Integer.parseInt(b.getName().substring("Puzzle".length()));
            return aNum - bNum;
        });

        return classes;
    }

}