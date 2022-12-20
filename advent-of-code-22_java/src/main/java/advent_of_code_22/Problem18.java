package advent_of_code_22;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class Problem18 {
    public static void main(String[] args) {
        Shape shape = new Shape();
        new ProblemReader()
                .readProblem("Problem18.txt")
                .stream()
                .map(i -> Stream.of(i.split(",")).map(Integer::parseInt).collect(Collectors.toList()))
                .forEach(shape::add);

        System.out.println(shape.surfaceArea());
        System.out.println(shape.externalSurfaceArea());

    }

    static class Shape {
        private final HashSet<Point> points = new HashSet<>();

        public void add(List<Integer> p) {
            points.add(new Point(p));
        }

        public int surfaceArea() {
            ArrayList<Point> surfaceArea = surfaceAreaPoints();


            return surfaceArea.size();
        }

        public long externalSurfaceArea() {

            ArrayList<Point> surfaceAreaPoints = surfaceAreaPoints();
            HashSet<Point> c = floodFill(new Point(-1, -1, -1));
            surfaceAreaPoints.retainAll(c);
            return surfaceAreaPoints.size();
        }

        private ArrayList<Point> surfaceAreaPoints() {
            ArrayList<Point> surfaceArea = new ArrayList<>();

            for (Point point : points) {
                if (!points.contains(point.up())) {
                    surfaceArea.add(point.up());
                }
                if (!points.contains(point.down())) {
                    surfaceArea.add(point.down());
                }
                if (!points.contains(point.left())) {
                    surfaceArea.add(point.left());
                }
                if (!points.contains(point.right())) {
                    surfaceArea.add(point.right());
                }
                if (!points.contains(point.forward())) {
                    surfaceArea.add(point.forward());
                }
                if (!points.contains(point.backward())) {
                    surfaceArea.add(point.backward());
                }
            }
            return surfaceArea;
        }

        public HashSet<Point> floodFill(Point p) {
            HashSet<Point> visited = new HashSet<>();

            LinkedList<Point> queue = new LinkedList<>();
            queue.add(p);

            while (!queue.isEmpty()) {
                Point point = queue.removeFirst();


                for (Point current : asList(point.up(), point.down(), point.left(), point.right(), point.forward(), point.backward())) {
                    if (points.contains(current)) continue;
                    if (visited.contains(current)) continue;
                    if (!inBounds(current)) continue;
                    queue.add(current);
                    visited.add(current);
                }
            }
            return visited;
        }

        private boolean inBounds(Point p) {
            return (-1 <= p.x && p.x <= 22) &&
                    (-1 <= p.y && p.y <= 22) &&
                    (-1 <= p.z && p.z <= 22);
        }

        public void outside(Point p, HashSet<Point> visited) {
            if (p.x > 21) return;
            if (p.x < -1) return;
            if (p.y > 21) return;
            if (p.y < -1) return;
            if (p.z > 21) return;
            if (p.z < -1) return;


            for (Point point : asList(p.up(), p.down(), p.left(), p.right(), p.forward(), p.backward())) {
                if (!points.contains(point)) {
                    if (visited.add(point)) {
                        outside(point, visited);
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "Shape{" +
                    "points=" + points +
                    '}';
        }


        static class Point {
            private final int x;
            private final int y;
            private final int z;

            public Point(List<Integer> points) {
                x = points.get(0);
                y = points.get(1);
                z = points.get(2);
            }

            public Point(int x, int y, int z) {
                this.x = x;
                this.y = y;
                this.z = z;
            }

            public Point up() {
                return new Point(x, y, z + 1);
            }

            public Point down() {
                return new Point(x, y, z - 1);
            }

            public Point forward() {
                return new Point(x + 1, y, z);
            }

            public Point backward() {
                return new Point(x - 1, y, z);
            }

            public Point left() {
                return new Point(x, y - 1, z);
            }

            public Point right() {
                return new Point(x, y + 1, z);
            }

            @Override
            public String toString() {
                return "Point{" +
                        "x=" + x +
                        ", y=" + y +
                        ", z=" + z +
                        '}';
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Point point = (Point) o;
                return x == point.x && y == point.y && z == point.z;
            }

            @Override
            public int hashCode() {
                return Objects.hash(x, y, z);
            }
        }
    }
}
