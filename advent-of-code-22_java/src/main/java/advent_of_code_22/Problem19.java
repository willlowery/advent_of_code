package advent_of_code_22;

import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Problem19 {
    public static void main(String[] args) {


        new ProblemReader()
                .readProblem("Problem19_Sample.txt")
                .stream()
                .map( Blueprint::new)
                .forEach(System.out::println);
    }

    static class Blueprint {
        Integer id;
        Cost robotCost;
        Cost clayRobotCost;
        Cost obsidianRobotCost;
        Cost geodeRobotCost;

        public Blueprint(String line) {
            Pattern pattern = Pattern.compile("Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.");
            Matcher matcher = pattern.matcher(line);
            if (!matcher.matches()) {
                throw new IllegalStateException("Pattern doesn't match: " + line);
            }
            id = Integer.parseInt(matcher.group(1));
            robotCost = new Cost().add(Resource.ORE, Integer.parseInt(matcher.group(2)));
            clayRobotCost = new Cost().add(Resource.ORE, Integer.parseInt(matcher.group(3)));
            obsidianRobotCost = new Cost().add(Resource.ORE, Integer.parseInt(matcher.group(4))).add(Resource.CLAY, Integer.parseInt(matcher.group(5)));
            geodeRobotCost = new Cost().add(Resource.ORE, Integer.parseInt(matcher.group(6))).add(Resource.OBSIDIAN, Integer.parseInt(matcher.group(7)));
        }

        @Override
        public String toString() {
            return "Blueprint{" +
                    "id=" + id +
                    ", robotCost=" + robotCost +
                    ", clayRobotCost=" + clayRobotCost +
                    ", obsidianRobotCost=" + obsidianRobotCost +
                    ", geodeRobotCost=" + geodeRobotCost +
                    '}';
        }
    }

    static class Cost {
        private final HashMap<Resource, Integer> values = new HashMap<>();

        public Cost add(Resource resource, Integer value) {
            values.put(resource, value);
            return this;
        }

        @Override
        public String toString() {
            return "Cost{" +
                    "values=" + values +
                    '}';
        }
    }

    enum Resource {
        ORE,
        CLAY,
        OBSIDIAN,
        GEODE
    }
}
