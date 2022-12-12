package advent_of_code_22;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Problem7 {

    public static final Pattern COMMAND_PATTERN = Pattern.compile("\\$ (\\w+)\s?(.+)?");
    public static final Pattern LS_RESULT_PATTERN = Pattern.compile("(.+) (.+)");

    public static void main(String[] args) {
        FileSys sys = new FileSys();

        new ProblemReader()
                .readProblem("Problem7.txt")
                .forEach(i -> {
                    Matcher commandMatcher = COMMAND_PATTERN.matcher(i);
                    Matcher lsResultMatcher = LS_RESULT_PATTERN.matcher(i);
                    if (commandMatcher.matches()) {
                        String command = commandMatcher.group(1);
                        String argument = commandMatcher.group(2);
                        if (command.equals("cd")) {
                            sys.cd(argument);
                        } else if (command.equals("ls")) {
                            //ignored
                        } else {
                            throw new IllegalStateException("Unknown Command: " + command);
                        }
                    } else if (lsResultMatcher.matches()) {
                        String sizeOrDir = lsResultMatcher.group(1);
                        String name = lsResultMatcher.group(2);
                        if (sizeOrDir.equals("dir")) {
                            sys.addDirectory(name);
                        } else {
                            sys.addFile(name, Long.parseLong(sizeOrDir));
                        }
                    } else {
                        System.out.println("Unknown Line");
                    }
                });

        long totalSize = 70_000_000L;
        long requiredSize = 30_000_000L;
        long used = sys.root.size();
        long freeSpace = totalSize - used;
        long needed = requiredSize - freeSpace;

        System.out.printf("Total: [%d] used: [%d] Free: [%d] Needed [%d]%n", totalSize, used, freeSpace, needed);
        System.out.println(scan(sys.root));
        System.out.println(scanForDirectories(sys.root, needed).stream().min(Comparator.naturalOrder()));
    }

    public static List<Long> scanForDirectories(Dir dir, long needed) {
        LinkedList<Long> sizes = new LinkedList<>();
        if (dir.size() >= needed) {
            sizes.add(dir.size());
        }
        for (Dir d : dir.dirs) {
            sizes.addAll(scanForDirectories(d, needed));
        }

        return sizes;
    }

    public static Long scan(Dir dir) {
        Long sum = 0L;
        if (dir.size() <= 100_000) {
            sum += dir.size();
        }
        for (Dir d : dir.dirs) {
            sum += scan(d);
        }

        return sum;
    }

    public static class File {
        private final String name;
        public final long size;

        public File(String name, long size) {
            this.name = name;
            this.size = size;
        }

        @Override
        public String toString() {
            return "File{" +
                    "name='" + name + '\'' +
                    ", size=" + size +
                    '}';
        }
    }

    public static class Dir {

        private final List<File> files = new ArrayList<>();
        private final List<Dir> dirs = new ArrayList<>();
        private final Dir parent;
        private final String name;

        public Dir(Dir parent, String name) {
            this.parent = parent;
            this.name = name;
        }

        public long size() {
            return files.stream().map(i -> i.size).reduce(0L, Long::sum) +
                    dirs.stream().map(Dir::size).reduce(0L, Long::sum);
        }

        @Override
        public String toString() {
            return "Dir{" +
                    "files=" + files +
                    ", dirs=" + dirs +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public static class FileSys {
        private Dir root = new Dir(null, "/");
        private Dir current = root;

        public void cd(String name) {
            if (name.equalsIgnoreCase("/")) {
                current = root;
            } else if (name.equalsIgnoreCase("..")) {
                current = current.parent;
            } else {
                for (Dir dir : current.dirs) {
                    if (dir.name.equalsIgnoreCase(name)) {
                        current = dir;
                        return;
                    }
                }
                throw new IllegalStateException("Unable to cd to: " + name);
            }
        }

        public void addFile(String name, long size) {
            current.files.add(new File(name, size));
        }

        public void addDirectory(String name) {
            current.dirs.add(new Dir(current, name));
        }
    }
}
