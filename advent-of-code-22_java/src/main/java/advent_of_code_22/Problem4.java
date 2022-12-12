package advent_of_code_22;

import java.util.Arrays;
import java.util.List;

public class Problem4 {
    // part 1; 511
    // part 2; 821

    public static void main(String[] args) {
        System.out.println(new ProblemReader()
                .readProblem("Problem4.txt")
                .stream()
                .map(i -> i.split(","))
                .map(i -> Arrays.asList(new IntegerRange(i[0]), new IntegerRange(i[1])))
                .map(i -> i.get(0).overlap(i.get(1)) || i.get(1).overlap(i.get(0)))
                .filter(i -> i)
                .count());

        ;
    }

    public static class IntegerRange {
        int start;
        int end;

        public IntegerRange(String pair) {
            String[] split = pair.split("-");
            start = Integer.parseInt(split[0]);
            end = Integer.parseInt(split[1]);
        }

        @Override
        public String toString() {
            return "IntegerRange{" +
                    "start=" + start +
                    ", end=" + end +
                    '}';
        }

        public boolean contains(IntegerRange range) {
            return this.start <= range.start && range.end <= this.end;
        }

        public boolean eitherContains(IntegerRange range) {
            return contains(range) || range.contains(this);
        }

        //            |------|
//                 |------|
//       |------|
        public boolean overlap(IntegerRange range) {
            if (start <= range.start && range.start <= end) {
                return true;
            }
            if (start <= range.end && range.end <= end) {
                return true;
            }

            return false;
        }
    }


}
