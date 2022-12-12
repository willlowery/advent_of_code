package advent_of_code_22;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Problem5 {
    //part 1: SHQWSRBDL
    //part 2: CDTQZHBRS
    public static void main(String[] args) {

        List<String> lines = new ProblemReader().readProblem("Problem5_Sample.txt");
        Containers container = build(findDrawing(lines));
        List<Move> moves = parseMoves(findInstructions(lines));
        for (Move move : moves) {
            container.moveBetter(move.getFrom(), move.getTo(), move.getCount());
        }

        System.out.println(container.getTopLine());
    }

    public static List<String> findDrawing(List<String> input) {
        for (int i = 0; i < input.size(); i++) {
            if (input.get(i).isBlank())
                return input.subList(0, i);
        }
        throw new IllegalStateException("Busted!!!");
    }

    public static List<Move> parseMoves(List<String> lines) {
        Pattern pattern = Pattern
                .compile("move (\\d+) from (\\d+) to (\\d+)");

        ArrayList<Move> moves = new ArrayList<>();

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (!matcher.matches()) continue;


            moves.add(new Move(
                    Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3))
            ));
        }

        return moves;
    }

    public static List<String> findInstructions(List<String> input) {
        for (int i = 0; i < input.size(); i++) {
            if (input.get(i).isBlank())
                return input.subList(i, input.size());
        }
        throw new IllegalStateException("Busted!!!");
    }

    public static Containers build(List<String> drawing) {
        Containers containers = new Containers();

        String firstLine = drawing.get(drawing.size() - 1);
        String[] headers = firstLine.trim().split("\s+");

        for (int i = drawing.size() - 2; i >= 0; i--) {
            String line = drawing.get(i);
            for (int j = 0; j < headers.length; j++) {
                int index = (j * 4) + 1;
                if (index > line.length()) continue;
                char c = line.charAt(index);
                if (!Character.isSpaceChar(c)) {
                    containers.add((j + 1), "" + c);
                }
            }

        }
        return containers;
    }

    public static class Containers {
        private final TreeMap<Integer, LinkedList<String>> items = new TreeMap<>();

        public void add(Integer stack, String item) {
            items
                    .computeIfAbsent(stack, (a) -> new LinkedList<>())
                    .addLast(item);
        }

        public void addAll(Integer stack, List<String> item) {
            items
                    .computeIfAbsent(stack, (a) -> new LinkedList<>())
                    .addAll(item);
        }

        public String remove(Integer stack) {
            return items
                    .computeIfAbsent(stack, (a) -> new LinkedList<>())
                    .removeLast();
        }

        public List<String> remove(Integer stack, int count) {
            LinkedList<String> strings = items.get(stack);
            LinkedList<String> a = new LinkedList<>();
            for (int i = 0; i < count; i++) {
                a.addFirst(strings.removeLast());
            }
            return a;
        }

        public void move(Integer stackFrom, Integer stackTwo, int count) {
            for (int i = 0; i < count; i++) {
                add(stackTwo, remove(stackFrom));
            }
        }

        public void moveBetter(Integer stackFrom, Integer stackTwo, int count) {
            addAll(stackTwo, remove(stackFrom, count));
        }

        public String getTopLine() {
            String line = "";
            for (Integer id : items.keySet()) {
                LinkedList<String> it = items.get(id);
                line += it.getLast();
            }

            return line;
        }

        @Override
        public String toString() {
            return "Containers{" +
                    "items=" + items +
                    '}';
        }
    }

    static class Move {
        Integer count;
        Integer from;
        Integer to;

        public Move(Integer count, Integer from, Integer to) {
            this.count = count;
            this.from = from;
            this.to = to;
        }

        public Integer getCount() {
            return count;
        }

        public Integer getFrom() {
            return from;
        }

        public Integer getTo() {
            return to;
        }
    }
}
