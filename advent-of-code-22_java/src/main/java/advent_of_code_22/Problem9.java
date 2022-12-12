package advent_of_code_22;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Problem9 {
    public static void main(String[] args) {
        RopeWorld ropeWorld = new RopeWorld(10);

        Pattern line = Pattern.compile("([URLD]) (\\d+)");

        new ProblemReader()
                .readProblem("Problem9.txt")
                .forEach(i -> {
                    Matcher matcher = line.matcher(i);
                    if (matcher.matches()) {

                        ropeWorld.move(
                                RopeWorld.Direction.find(matcher.group(1)),
                                Integer.parseInt(matcher.group(2))
                        );
                    } else {
                        throw new IllegalStateException("Bad Input: " + i);
                    }
                });
        ;

        System.out.println(ropeWorld.tailPositions());
    }

    public static class RopeWorld {

        ArrayList<Point> knots = new ArrayList<>();
        HashSet<Point> tailPositions = new HashSet<>();

        public RopeWorld(int knots) {
            for (int i = 0; i < knots; i++) {
                this.knots.add(new Point(0, 0));
            }

            tailPositions.add(new Point(0, 0));
        }

        public void move(Direction direction) {
            knots.set(0, knots.get(0).move(direction));

            for (int i = 1; i < knots.size(); i++) {
                Point last = knots.get(i - 1);
                Point current = knots.get(i);

                if (!last.touches(current)) {
                    knots.set(i, current.move(current.directionTo(last)));
                    if (i == knots.size() - 1) {
                        tailPositions.add(knots.get(i));
                    }
                }
            }
        }

        public void move(Direction direction, int count) {
            for (int i = 0; i < count; i++) {
                move(direction);

            }
        }

        public int tailPositions() {
            return tailPositions.size();
        }

        public static class Point {
            private final Integer x;
            private final Integer y;

            public Point(Integer x, Integer y) {
                this.x = x;
                this.y = y;
            }

            public boolean inLineHorizontal(Point other) {
                return x.equals(other.x);
            }

            public boolean inLineVertical(Point other) {
                return y.equals(other.y);
            }

            public Point move(Direction direction) {
                return new Point(x + direction.x, y + direction.y);
            }

            public boolean touches(Point other) {
                return Math.abs(x - other.x) <= 1 && Math.abs(y - other.y) <= 1;
            }


            public Direction directionTo(Point other) {
                return Direction.find(
                        scaleToOne(other.x - x),
                        scaleToOne(other.y - y)
                );
            }

            private int scaleToOne(int i) {
                return Integer.compare(i, 0);
            }

            @Override
            public String toString() {
                return "Point{" +
                        "x=" + x +
                        ", y=" + y +
                        '}';
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Point point = (Point) o;
                return Objects.equals(x, point.x) && Objects.equals(y, point.y);
            }

            @Override
            public int hashCode() {
                return Objects.hash(x, y);
            }
        }

        enum Direction {
            NORTH(0, 1, "U"),
            NORTH_EAST(1, 1, "X"),
            NORTH_WEST(-1, 1, "X"),

            EAST(1, 0, "R"),
            WEST(-1, 0, "L"),

            SOUTH_EAST(1, -1, "X"),
            SOUTH(0, -1, "D"),
            SOUTH_WEST(-1, -1, "X"),
            NONE(0, 0, "X");

            private final int x;
            private final int y;
            private final String ind;

            Direction(int x, int y, String ind) {
                this.x = x;
                this.y = y;
                this.ind = ind;
            }

            public static Direction find(int x, int y) {
                for (Direction d : values()) {
                    if (d.x == x && d.y == y) return d;
                }

                return NONE;
            }

            public static Direction find(String ind) {
                for (Direction d : values()) {
                    if (d.ind.equals(ind)) return d;
                }

                return NONE;
            }
        }
    }
}
