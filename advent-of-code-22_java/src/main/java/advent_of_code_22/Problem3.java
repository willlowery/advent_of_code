package advent_of_code_22;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collector;

public class Problem3 {
    public static void main(String[] args) {
        partOne();
        partTwo();
    }

    private static void partTwo() {
        List<String> lines = new ProblemReader().readProblem("Problem3.txt");
        lines
                .stream()
                .collect(partition(3))
                .stream()
                .map(Problem3::common)
                .flatMap(i -> i.stream().map(Problem3::priority))
                .reduce(Integer::sum)
                .ifPresent(System.out::println);
    }

    private static void partOne() {
        new ProblemReader()
                .readProblem("Problem3.txt")
                .stream()
                .map(i -> {
                    int mid = i.length() / 2;
                    return Arrays.asList(i.substring(0, mid), i.substring(mid));
                })
                .map(Problem3::common)
                .flatMap(i -> i.stream().map(Problem3::priority))
                .reduce(Integer::sum)
                .ifPresent(System.out::println);
    }


    public static Integer priority(char c) {
        if (Character.isLowerCase(c)) {
            return c - 'a' + 1;
        } else {
            return c - 'A' + 1 + 26;
        }
    }

    public static HashSet<Character> common(List<String> items) {
        return items
                .stream()
                .map(i -> {
                    HashSet<Character> c = new HashSet<>();
                    for (int j = 0; j < i.length(); j++) {
                        c.add(i.charAt(j));
                    }
                    return c;
                })
                .reduce((left, right) -> {
                    left.retainAll(right);
                    return left;
                }).orElse(new HashSet<>());
    }

    public static <Type> Collector<Type, List<List<Type>>, List<List<Type>>> partition(int size) {
        return Collector.of(
                ArrayList::new,
                (list, value) -> {
                    List<Type> partition;
                    if (list.isEmpty()) {
                        partition = new ArrayList<>();
                        list.add(partition);
                    } else
                        partition = list.get(list.size() - 1);

                    if (partition.size() == size) {
                        partition = new ArrayList<>();
                        list.add(partition);
                    }

                    partition.add(value);
                },
                (l, r) -> {
                    throw new IllegalStateException("Unsupported operation");
                }
        );
    }
}
