package advent_of_code_22;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static advent_of_code_22.Problem3.partition;

public class Problem11 {
    static Pattern namePattern = Pattern.compile("Monkey (\\d+):");
    static Pattern testPattern = Pattern.compile("\\s+Test: divisible by (\\d+)");
    static Pattern throwToPattern = Pattern.compile("\\s+If (true|false): throw to monkey (\\d+)");

    public static void main(String[] args) {
        List<Monkey> monkeys = new ProblemReader()
                .readProblem("Problem11.txt")
                .stream()
                .collect(partition(7))
                .stream()
                .map(Monkey::new)
                .collect(Collectors.toList());

        Function<Long, Long> partOneReduction = (a) -> a / 3;

        Integer divisor = monkeys.stream().map(i -> i.test).reduce(1, (a, b) -> a * b);
        Function<Long, Long> partTwoReduction = (a) -> a % divisor;

        ThoseCrazyMonkeys thoseCrazyMonkeys = new ThoseCrazyMonkeys(monkeys, partTwoReduction);
        for (int i = 0; i < 10_000; i++) {
            thoseCrazyMonkeys.round();
        }

        System.out.println(thoseCrazyMonkeys);
        System.out.println(thoseCrazyMonkeys.monkeyBusiness());
    }

    static class ThoseCrazyMonkeys {
        private final List<Monkey> monkeys;
        private final Function<Long, Long> reduction;

        ThoseCrazyMonkeys(List<Monkey> monkeys, Function<Long, Long> reduction) {
            this.monkeys = monkeys;
            this.reduction = reduction;
        }

        public void round() {
            for (Monkey monkey : monkeys) {
                List<Throw> turn = monkey.turn(reduction);
                for (Throw aThrow : turn) {
                    monkeys.get(aThrow.to).catchItem(aThrow.item);
                }
            }
        }

        @Override
        public String toString() {
            return "ThoseCrazyMonkeys{" +
                    "monkeys=" + monkeys +
                    '}';
        }

        public Long monkeyBusiness() {
            return monkeys
                    .stream()
                    .map(i -> (long) i.inspections)
                    .sorted(Comparator.reverseOrder())
                    .limit(2)
                    .reduce(1L, (a, b) -> a * b);
        }
    }

    static class Monkey {
        Integer inspections = 0;
        Integer name;
        LinkedList<Long> items;
        Expr operation;
        Integer test;
        Integer onTrue;
        Integer onFalse;

        public Monkey(List<String> lines) {
            name = findInt(namePattern, lines.get(0), 1);
            items = new LinkedList<>(findItems(lines.get(1)));
            operation = findExpression(lines.get(2));
            test = findInt(testPattern, lines.get(3), 1);
            onTrue = findInt(throwToPattern, lines.get(4), 2);
            onFalse = findInt(throwToPattern, lines.get(5), 2);
        }

        public List<Throw> turn(Function<Long, Long> stressDivisor) {
            ArrayList<Throw> result = new ArrayList<>();
            while (!items.isEmpty()) {
                inspections++;
                Long item = stressDivisor.apply(operation.apply(items.pop()));
                if (item % test == 0) {
                    result.add(new Throw(item, onTrue));
                } else {
                    result.add(new Throw(item, onFalse));
                }
            }

            return result;
        }

        public void catchItem(Long value) {

            items.addLast(value);
        }

        @Override
        public String toString() {
            return "Monkey{" +
                    "name=" + name +
                    ", inspections=" + inspections +
                    ", items=" + items +
                    ", operation=" + operation +
                    ", test=" + test +
                    ", onTrue=" + onTrue +
                    ", onFalse=" + onFalse +
                    '}';
        }
    }

    static class Throw {
        Long item;
        Integer to;

        public Throw(Long item, Integer to) {
            this.item = item;
            this.to = to;
        }

        @Override
        public String toString() {
            return "Throw{" +
                    "item=" + item +
                    ", to=" + to +
                    '}';
        }
    }

    public static Integer findInt(Pattern pattern, String line, int group) {
        Matcher matcher = pattern.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalStateException("Pattern doesn't match!: " + line);
        }
        return Integer.parseInt(matcher.group(group));
    }

    public static List<Long> findItems(String line) {
        return Stream.of(line
                        .replaceAll("\\s+Starting items:\\s", "")
                        .split(", ")
                )
                .map(Long::parseLong)
                .collect(Collectors.toList())
                ;
    }

    public static Expr findExpression(String line) {
        String[] parts = line
                .replaceAll("\\s+Operation: new = ", "")
                .split(" ");

        Expr left = constantOrOld(parts[0]);
        Expr right = constantOrOld(parts[2]);

        if (parts[1].equals("*")) {
            return new Mul(left, right);
        } else if (parts[1].equals("+")) {
            return new Add(left, right);
        }
        throw new IllegalStateException("Unknown operator: " + line);
    }

    private static Expr constantOrOld(String part) {
        if (part.equals("old")) {
            return new Old();
        } else {
            return new Constant(Long.parseLong(part));
        }
    }

    interface Expr {
        Long apply(Long value);
    }

    static class Old implements Expr {
        public Long apply(Long apply) {
            return apply;
        }

        @Override
        public String toString() {
            return "old";
        }
    }

    static class Constant implements Expr {

        private final Long value;

        Constant(Long value) {
            this.value = value;
        }

        @Override
        public Long apply(Long value) {
            return this.value;
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    static class Mul implements Expr {
        private final Expr left;
        private final Expr right;

        Mul(Expr left, Expr right) {
            this.left = left;
            this.right = right;
        }


        @Override
        public Long apply(Long value) {
            return (left.apply(value) * right.apply(value));
        }

        @Override
        public String toString() {
            return left + " * " + right;
        }
    }

    static class Add implements Expr {
        private final Expr left;
        private final Expr right;

        Add(Expr left, Expr right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public Long apply(Long value) {
            return (left.apply(value) + right.apply(value));
        }

        @Override
        public String toString() {
            return left + " + " + right;
        }
    }
}
