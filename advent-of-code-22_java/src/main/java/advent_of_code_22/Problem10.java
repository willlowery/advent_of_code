package advent_of_code_22;

import java.util.LinkedList;
import java.util.List;

public class Problem10 {
    public static void main(String[] args) {
        CPU cpu = new CPU();
        new ProblemReader()
                .readProblem("Problem10.txt")
                .forEach(i -> {
                    if (i.contains("noop")) {
                        cpu.noop();
                    } else {
                        cpu.add(Integer.parseInt(i.split(" ")[1]));
                    }
                });

        cpu.run();
    }

    public static class CPU {
        int cycle = 1;
        int register = 1;

        int sum = 0;

        LinkedList<Op> ops = new LinkedList<>();
        private Op current;

        void noop() {
            ops.add(new Noop());
        }

        void add(int value) {
            ops.add(new Add(value));
        }

        void run() {
            while (!ops.isEmpty()) {
                if (current == null || current.length == 0)
                    current = ops.pop();

                current.length -= 1;

                if ((cycle - 1) % 40 == 0) {
                    System.out.println();
                }
                if (Math.abs(((cycle - 1) % 40) - register) <= 1) {
                    System.out.print("$");
                } else {
                    System.out.print(" ");
                }
                cycle++;
                current.go();
                if (isIntersting(cycle)) {
                    sum += register * cycle;

//                    System.out.printf("Cycle: %s Value: %s Signal: %s, Sum: %s %n", cycle, register, register * cycle, sum);
                }
            }

        }

        private boolean isIntersting(int value) {
            if (value > 220) return false;
            if (value == 20) {
                return true;
            }
            if ((value - 20) % 40 == 0) {
                return true;
            }
            return false;
        }

        private abstract class Op {
            int length;

            public Op(int length) {
                this.length = length;
            }

            abstract void go();
        }

        private class Noop extends Op {

            public Noop() {
                super(1);
            }

            @Override
            void go() {

            }
        }

        private class Add extends Op {

            private final int value;

            public Add(int value) {
                super(2);
                this.value = value;
            }

            @Override
            void go() {
                if (length == 0) {
                    register += value;
                }
            }
        }
    }
}
