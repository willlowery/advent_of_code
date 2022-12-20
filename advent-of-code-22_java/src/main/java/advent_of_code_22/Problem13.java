package advent_of_code_22;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static advent_of_code_22.Problem3.partition;

public class Problem13 {
    public static void main(String[] args) {
        String file = "Problem13.txt";
        p1(file);
        p2(file);
    }

    private static void p2(String file) {
        List<String> lines = new ProblemReader().readProblem(file);

        lines.add("[[2]]");
        lines.add("[[6]]");

        List<List<Object>> lists = lines
                .stream()
                .filter(i -> !i.isBlank())
                .map(Problem13::parseList)
                .sorted(Problem13::compare)
                .toList();

        System.out.println();
        System.out.println((lists.indexOf(List.of(List.of(2))) + 1) * (lists.indexOf(List.of(List.of(6))) + 1));
    }

    public static void p1(String file) {
        List<Integer> item = new ProblemReader()
                .readProblem(file)
                .stream()
                .filter(i -> !i.isBlank())
                .map(Problem13::parseList)
                .collect(partition(2))
                .stream()
                .map(i -> compare(i.get(0), i.get(1))).toList();

        int sum = 0;
        for (int i = 0; i < item.size(); i++) {
            Integer comparison = item.get(i);
            if (comparison <= 0) {
                sum += (i + 1);
            }
        }
        System.out.println(sum);

    }

    public static Integer compare(List<Object> left, List<Object> right) {
        if (left.isEmpty() && right.isEmpty()) return 0;

        for (int i = 0; i < left.size(); i++) {
            if (i >= right.size()) {
                return 1;
            }
            Object leftEntry = left.get(i);
            Object rightEntry = right.get(i);
            if (leftEntry instanceof Integer && rightEntry instanceof Integer) {
                Integer comp = compare((Integer) leftEntry, (Integer) rightEntry);
                if (comp != 0) {
                    return comp;
                }
            } else if (leftEntry instanceof List && rightEntry instanceof Integer) {
                Integer comp = compare((List) leftEntry, (Integer) rightEntry);
                if (comp != 0) {
                    return comp;
                }
            } else if (leftEntry instanceof Integer && rightEntry instanceof List) {
                Integer comp = compare((Integer) leftEntry, (List) rightEntry);
                if (comp != 0) {
                    return comp;
                }
            } else if (leftEntry instanceof List && rightEntry instanceof List) {
                Integer comp = compare((List) leftEntry, (List) rightEntry);
                if (comp != 0) {
                    return comp;
                }
            } else {
                throw new IllegalStateException("Ahhh!!!");
            }
        }
        if (left.size() < right.size())
            return -1;
        else if (left.size() == right.size()) {
            return 0;
        }
        return 1;
    }

    public static Integer compare(Integer left, Integer right) {
        return Integer.compare(left, right);
    }

    public static Integer compare(Integer left, List<Object> right) {
        return compare(Collections.singletonList(left), right);
    }

    public static Integer compare(List<Object> left, Integer right) {
        return compare(left, Collections.singletonList(right));
    }

    public static List<Object> parseList(String line) {
        LinkedList<ArrayList<Object>> stack = new LinkedList<>();
        stack.push(new ArrayList<>());

        String buffer = "";
        for (int i = 1; i < line.length() - 1; i++) {
            if (line.charAt(i) == '[') {
                stack.push(new ArrayList<>());

            } else if (line.charAt(i) == ']') {
                if (!buffer.isBlank()) {
                    stack.peek().add(Integer.parseInt(buffer));
                    buffer = "";
                }
                ArrayList<Object> temp = stack.pop();
                stack.peek().add(temp);
            } else if (line.charAt(i) == ',') {
                if (!buffer.isBlank()) {
                    stack.peek().add(Integer.parseInt(buffer));
                    buffer = "";
                }
            } else {
                buffer += line.charAt(i);
            }
        }
        if (!buffer.isBlank()) {
            stack.peek().add(Integer.parseInt(buffer));
            buffer = "";
        }

        return stack.pop();
    }
}
