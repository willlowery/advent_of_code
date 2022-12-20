package advent_of_code_22;

import java.util.*;
import java.util.function.Supplier;

public class Problem17 {
    public static void main(String[] args) {
        String line = new ProblemReader().readProblem("Problem17_Sample.txt").get(0);

        LinkedList<Direction> shifts = new LinkedList<>();
        for (String s : line.split("")) {
            for (Direction d : Direction.values()) {
                if (d.getSymbol().equals(s)) {
                    shifts.add(d);
                }
            }
        }


        LinkedList<Supplier<Shape>> shapes = new LinkedList<>();
        shapes.addLast(Shape::horizontal);
        shapes.addLast(Shape::plus);
        shapes.addLast(Shape::backwardsEl);
        shapes.addLast(Shape::vertical);
        shapes.addLast(Shape::square);

        long last = 0;
        Room room = new Room(shifts);
        for (long i = 0; i < 2000; i++) {
            Supplier<Shape> supplier = shapes.removeFirst();
            room.add(supplier.get(), i);
            shapes.addLast(supplier);
            long tallestPart = room.findTallestPart();
            System.out.println(i + "\t" + tallestPart + "\t" + (tallestPart - last));
            last = tallestPart;
        }


//        1,514
//        1,513
//        1,512

//        System.out.println(room);
//        System.out.println(room.findTallestPart());
    }

    public static class Room {
        int width = 7;
        long currentHeight = 4;
        long minHeight = 1;
        private final Set<Point> parts = new HashSet<>();
        private final LinkedList<Direction> shifts;
        private final HashMap<Point, Long> pointsInShapes = new HashMap<>();
        private final HashMap<Long, Shape> shapes = new HashMap<>();

        public Room(LinkedList<Direction> shifts) {
            this.parts.addAll(Arrays.asList(
                    new Point(0, 0),
                    new Point(1, 0),
                    new Point(2, 0),
                    new Point(3, 0),
                    new Point(4, 0),
                    new Point(5, 0),
                    new Point(6, 0)
            ));
            this.shifts = shifts;
        }

        public void add(Shape shape, long shapeNumber) {
            Shape e = shape
                    .shift(Direction.RIGHT, 2)
                    .shift(Direction.UP, currentHeight + shape.maxY - shape.minY);

            while (true) {
                Direction direction = shifts.removeFirst();
                if (e.canShiftLeftRight(direction, 1, 0, width - 1) && doesNotCollide(e.shift(direction, 1)))
                    e = e.shift(direction, 1);
                shifts.addLast(direction);

                if (e.canShiftDown(1) && doesNotCollide(e.shift(Direction.DOWN, 1))) {
                    e = e.shift(Direction.DOWN, 1);
                } else break;
            }


            parts.addAll(e.points);
            shapes.put(shapeNumber, e);
            for (Point point : e.points) {
                pointsInShapes.put(point, shapeNumber);
            }

            currentHeight = Math.max(-e.points.stream().map(i -> i.y).min(Comparator.naturalOrder()).get() + 4, currentHeight);

        }

        private boolean doesNotCollide(Shape shift) {
            return parts.stream().noneMatch(shift.points::contains);
        }


        public long findTallestPart() {
            return -parts.stream().map(i -> i.y).min(Comparator.naturalOrder()).get();
        }

        @Override
        public String toString() {
            String line = "";
            for (long i = -currentHeight; i < 0; i++) {
                line = line(-i);
            }
            line += "+-------+";
            return line;
        }

        private String line(long i) {
            String line = "|";
            for (int j = 0; j < 7; j++) {
                if (parts.contains(new Point(j, -i)))
                    line += "#";
                else
                    line += ".";
            }
            line += "|\n";
            return line;
        }

        public HashSet<Long> shapesOnALine(long line) {
            HashSet<Long> shapes = new HashSet<>();
            for (int i = 0; i < 7; i++) {
                shapes.add(pointsInShapes.get(new Point(i, -line)));
            }
            shapes.remove(null);
            return shapes;
        }
    }

    static class Shape {
        protected final List<Point> points = new ArrayList<>();
        long minX = 0;
        long maxX = 0;
        long minY = 0;
        long maxY = 0;

        public boolean canShiftLeftRight(Direction direction, int value, long minX, long maxX) {
            Shape shifted = this.shift(direction, value);

            return minX <= shifted.minX && shifted.maxX <= maxX;
        }

        public boolean canShiftDown(int value) {
            Shape shifted = this.shift(Direction.DOWN, value);

            return shifted.minY < 0;
        }

        public Shape shift(Direction direction, long value) {
            Shape s = new Shape();
            points
                    .stream()
                    .map(i -> i.shift(direction, value))
                    .forEach(s::add);
            return s;
        }

        @Override
        public String toString() {
            String output = "";
            for (long i = minY; i <= maxY; i++) {
                for (long j = minX; j <= maxX; j++) {
                    if (points.contains(new Point(j, i))) {
                        output += "#";
                    } else {
                        output += ".";
                    }
                }
                output += "\n";
            }

            return output;
        }

        public void add(Point point) {
            maxX = Math.max(point.x, maxX);
            minX = Math.min(point.x, minX);
            maxY = Math.max(point.y, maxY);
            minY = Math.min(point.y, minY);
            this.points.add(point);
        }

        static Shape square() {
            Shape shape = new Shape();
            shape.add(new Point(0, 0));
            shape.add(new Point(0, 1));
            shape.add(new Point(1, 0));
            shape.add(new Point(1, 1));
            return shape;
        }

        static Shape horizontal() {
            Shape shape = new Shape();
            shape.add(new Point(0, 0));
            shape.add(new Point(1, 0));
            shape.add(new Point(2, 0));
            shape.add(new Point(3, 0));
            return shape;
        }

        static Shape vertical() {
            Shape shape = new Shape();
            shape.add(new Point(0, 0));
            shape.add(new Point(0, 1));
            shape.add(new Point(0, 2));
            shape.add(new Point(0, 3));
            return shape;
        }

        static Shape plus() {
            Shape shape = new Shape();
            shape.add(new Point(1, 0));
            shape.add(new Point(0, 1));
            shape.add(new Point(1, 1));
            shape.add(new Point(2, 1));
            shape.add(new Point(1, 2));
            return shape;
        }

        static Shape backwardsEl() {
            Shape shape = new Shape();
            shape.add(new Point(2, 0));
            shape.add(new Point(2, 1));
            shape.add(new Point(2, 2));
            shape.add(new Point(1, 2));
            shape.add(new Point(0, 2));
            return shape;
        }
    }

    static class Point {
        final long x;
        final long y;

        public Point(long x, long y) {
            this.x = x;
            this.y = y;
        }

        public Point shift(Direction direction, long distance) {
            return new Point(
                    x + (direction.getX() * distance),
                    y + (direction.getY() * distance)
            );
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    enum Direction {
        UP("U", 0, -1),
        DOWN("D", 0, 1),
        LEFT("<", -1, 0),
        RIGHT(">", 1, 0);
        String symbol;
        int x;
        int y;

        Direction(String symbol, int x, int y) {
            this.symbol = symbol;
            this.x = x;
            this.y = y;
        }

        public String getSymbol() {
            return symbol;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
