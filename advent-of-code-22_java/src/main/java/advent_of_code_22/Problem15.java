package advent_of_code_22;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Problem15 {
    public static void main(String[] args) {
        Pattern linePattern = Pattern.compile("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)");

        World world = new World();

        new ProblemReader()
                .readProblem("Problem15.txt")
                .forEach(i -> {
                    Matcher matcher = linePattern.matcher(i);
                    if (matcher.matches()) {
                        world.addSensorLine(
                                new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))),
                                new Point(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)))
                        );
                    } else {
                        throw new IllegalStateException("Pattern didn't Match: " + i);
                    }
                });

        System.out.println(world.beaconNotPresent(2_000_000));
        Point possibleLocations = world.findPossibleLocation(0, 4000000);
        System.out.println((possibleLocations.getX() * 4000000L) + (long) possibleLocations.getY());
    }

    static class World {
        private final HashSet<Point> beacons = new HashSet<>();
        private final HashSet<Point> sensors = new HashSet<>();
        private final HashMap<Point, Integer> distances = new HashMap<>();
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;

        public void addSensorLine(Point sensor, Point beacon) {
            int dist = sensor.manhattanDistance(beacon);
            minX = Math.min(sensor.getX() - dist, minX);
            maxX = Math.max(sensor.getX() + dist, maxX);
            beacons.add(beacon);
            sensors.add(sensor);
            distances.put(sensor, dist);
        }

        public boolean isCovered(Point p) {
            if (beacons.contains(p)) return false;
            for (Point sensor : sensors) {
                if (sensor.manhattanDistance(p) <= distances.get(sensor))
                    return true;
            }

            return false;
        }

        public Point findPossibleLocation(int lowerBound, int upperBound) {
            for (Point sensor : sensors) {
                //Only scan 1 distance cells one distance further than active sensor towers
                //don't scan out of bounds and don't report a beacon that has already been found
                for (Point point : sensor.atDistance(distances.get(sensor) + 1)) {
                    if (beacons.contains(point)) continue;
                    if (!(lowerBound <= point.getX() && point.getX() <= upperBound)) continue;
                    if (!(lowerBound <= point.getY() && point.getY() <= upperBound)) continue;
                    if (!isCovered(point))
                        return point;
                }
            }
            throw new IllegalStateException("Can't find it!");
        }

        public long beaconNotPresent(int row) {
            long count = 0;
            for (int i = minX; i < maxX; i++) {
                if (isCovered(new Point(i, row))) {
                    count++;
                }
            }

            return count;
        }
    }

    static class Point {
        private final int x;
        private final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int manhattanDistance(Point p) {
            return Math.abs(x - p.x) + Math.abs(y - p.y);
        }

        public HashSet<Point> atDistance(int distance) {

            HashSet<Point> points = new HashSet<>();

            for (int i = 0; i <= distance; i++) {
                points.add(new Point(x + i, y + distance - i));
                points.add(new Point(x + i, y - distance + i));
            }
            for (int i = 0; i <= distance; i++) {
                points.add(new Point(x - i, y + distance - i));
                points.add(new Point(x - i, y - distance + i));
            }

            return points;
        }


        public int getX() {
            return x;
        }

        public int getY() {
            return y;
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
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
