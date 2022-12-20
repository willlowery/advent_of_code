package advent_of_code_22;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class Problem14 {
    public static String test() {
        return "Howdy";
    }

    public static void main(String[] args) {

        Room room = new Room();
        new ProblemReader()
                .readProblem("Problem14.txt")
                .stream()
                .map(i -> List.of(i.split(" -> ")))
                .map(i -> i.stream().map(Point::new).toList())
                .forEach(room::add);

        System.out.println(room);
        room.addFloor(2, 510);
//
        int i = 0;
        while (room.addSand()) {
            i++;
//            System.out.println(room);
        }
//        System.out.println(room);
        System.out.println(i);
    }

    public static void print(Supplier<String> get) {
        System.out.println(get);
    }


    static class Room {
        private HashMap<Point, String> room = new HashMap<>();
        private int minX = Integer.MAX_VALUE;
        private int maxX = Integer.MIN_VALUE;
        private int minY = 0;
        private int maxY = Integer.MIN_VALUE;


        public boolean addSand() {
            Point start = new Point(500, 0);
            while (true) {
                Point temp = drop(start);
                if (inBounds(temp)) {
                    if (temp.equals(new Point(500, 0)) && room.containsKey(new Point(500, 0))) {
                        return false;
                    } else if (temp.equals(start)) {
                        room.put(temp, "O");
                        return true;
                    } else {
                        start = temp;
                    }
                } else {
                    return false;
                }
            }
        }

        private Point drop(Point sand) {

            if (!this.room.containsKey(new Point(sand.getX(), sand.getY() + 1)))
                return new Point(sand.getX(), sand.getY() + 1);
            if (!this.room.containsKey(new Point(sand.getX() - 1, sand.getY() + 1)))
                return new Point(sand.getX() - 1, sand.getY() + 1);
            if (!this.room.containsKey(new Point(sand.getX() + 1, sand.getY() + 1)))
                return new Point(sand.getX() + 1, sand.getY() + 1);
            return sand;

        }

        private boolean inBounds(Point sand) {
            return inX(sand) && inY(sand);
        }

        private boolean inX(Point sand) {
            return minX <= sand.getX() && sand.getX() <= maxX;
        }

        private boolean inY(Point sand) {
            return minY <= sand.getY() && sand.getY() <= maxY;
        }

        public void add(List<Point> line) {
            if (line.isEmpty()) return;
            Point start = line.get(0);
            for (int i = 1; i < line.size(); i++) {
                Point point = line.get(i);
                if (point.getX() == start.getX()) {
                    int dir = 1;
                    if (start.getY() > point.getY()) {
                        dir = -1;
                    }
                    for (int j = start.getY(); j != point.getY() + dir; j += dir) {
                        addPoint(start.getX(), j);
                    }
                } else {
                    int dir = 1;
                    if (start.getX() > point.getX()) {
                        dir = -1;
                    }
                    for (int j = start.getX(); j != point.getX() + dir; j += dir) {
                        addPoint(j, start.getY());
                    }
                }
                start = point;
            }
        }

        private void addPoint(int x, int y) {
            minX = Math.min(x, minX);
            maxX = Math.max(x, maxX);
            minY = Math.min(y, minY);
            maxY = Math.max(y, maxY);

            room.put(new Point(x, y), "#");
        }

        public void addFloor(int depth, int width) {
            add(List.of(new Point(minX - width, maxY + depth), new Point(maxX + width, maxY + depth)));
        }

        @Override
        public String toString() {
            String toString = "";
            for (int i = minY; i <= maxY; i++) {
                for (int j = minX; j <= maxX; j++) {
                    if (i == 0 && j == 500) {
                        toString += room.getOrDefault(new Point(j, i), "+");
                    } else
                        toString += room.getOrDefault(new Point(j, i), ".");
                }
                toString += "\n";
            }
            return toString;
        }
    }

    static class Point {
        private final int x;
        private final int y;

        public Point(String commaSep) {
            String[] split = commaSep.split(",");
            x = Integer.parseInt(split[0]);
            y = Integer.parseInt(split[1]);
        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
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

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
