package advent_of_code_22;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Problem12 {
    public static void main(String[] args) {
        TheTerrain t = new TheTerrain();
        new ProblemReader()
                .readProblem("Problem12.txt")
                .forEach(t::addLine);

        t.replaceStartAndEnd();
        System.out.println("Starting Search");
        int min = Integer.MAX_VALUE;
        for (TheTerrain.Point a : t.findAs()) {
            int r = t.bfs(a, t.end);
            if (min > r) {
                min = r;
            }
        }
        System.out.println(min);

        //Part 1: 425
        //Part 2: 418
    }

     static class TheTerrain {
        private final ArrayList<List<Integer>> elevation = new ArrayList<>();
        private Point start;
        private Point end;

        void addLine(String line) {
            elevation.add(Stream
                    .of(line.split(""))
                    .map(i -> {
                        if (i.charAt(0) == 'S') return -1;
                        if (i.charAt(0) == 'E') return -2;
                        return i.charAt(0) - 'a';
                    })
                    .collect(Collectors.toList())
            );
        }

        public void replaceStartAndEnd() {
            for (int i = 0; i < elevation.size(); i++) {
                for (int j = 0; j < elevation.get(i).size(); j++) {
                    Integer integer = elevation.get(i).get(j);
                    if (integer == -1) {
                        start = new Point(j, i);
                        elevation.get(i).set(j, 0);
                    } else if (integer == -2) {
                        end = new Point(j, i);
                        elevation.get(i).set(j, 'z' - 'a');
                    }
                }
            }
        }


        public int bfs(Point start, Point end) {
            HashSet<Point> visited = new HashSet<>();

            LinkedList<A> queue = new LinkedList<>();
            queue.addLast(new A(start, 0));

            int depth = 0;

            while (!queue.isEmpty()) {
                A current = queue.removeFirst();
                if (visited.contains(current.point)) continue;
                visited.add(current.point);

                if (depth != current.value) {
                    depth = current.value;
                }

                if (current.point.equals(end))
                    return current.value;

                for (Point option : options(current.point)) {
                    queue.addLast(new A(option, current.value + 1));
                }

            }
            return Integer.MAX_VALUE;
        }

        public List<Point> findAs() {
            ArrayList<Point> points = new ArrayList<>();

            for (int i = 0; i < elevation.size(); i++) {
                for (int j = 0; j < elevation.get(i).size(); j++) {
                    Point p = new Point(j, i);
                    if (get(p) == 0)
                        points.add(p);
                }
            }

            return points;
        }

        public static class A {
            Point point;
            int value;

            public A(Point point, int value) {
                this.point = point;
                this.value = value;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                A a = (A) o;
                return Objects.equals(point, a.point);
            }

            @Override
            public int hashCode() {
                return Objects.hash(point);
            }
        }


        public List<Point> options(Point p) {
            Integer startingElevation = get(p);

            ArrayList<Point> points = new ArrayList<>();

            if (p.y - 1 >= 0) {
                Point temp = new Point(p.x, p.y - 1);
                if (startingElevation >= get(temp) - 1)
                    points.add(temp);
            }
            if (p.y + 1 < elevation.size()) {
                Point temp = new Point(p.x, p.y + 1);
                if (startingElevation >= get(temp) - 1)
                    points.add(temp);
            }
            if (p.x - 1 >= 0) {
                Point temp = new Point(p.x - 1, p.y);
                if (startingElevation >= get(temp) - 1)
                    points.add(temp);
            }

            if (p.x + 1 < elevation.get(p.y).size()) {
                Point temp = new Point(p.x + 1, p.y);
                if (startingElevation >= get(temp) - 1)
                    points.add(temp);
            }

            return points;
        }

        public Integer get(Point p) {
            return elevation.get(p.y).get(p.x);
        }

        private List<Point> reconstructPath(Point end, HashMap<Point, Point> parents) {
            LinkedList<Point> points = new LinkedList<>();
            Point current = parents.get(end);
            while (current != null) {
                points.add(current);
                current = parents.get(current);
            }

            return points;
        }

        @Override
        public String toString() {
            return "TheTerrain{" +
                    "elevation=" + elevation +
                    ", start=" + start +
                    ", end=" + end +
                    '}';
        }

        static class Point {
            int x;
            int y;

            public Point(int x, int y) {
                this.x = x;
                this.y = y;
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
}
