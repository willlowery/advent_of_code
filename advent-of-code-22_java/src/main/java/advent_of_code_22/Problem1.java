package advent_of_code_22;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;

public class Problem1 {
    public static void main(String[] args) throws Exception {

        ArrayList<Elf> elves = new ArrayList<>();
        Elf current = new Elf();

        try (BufferedReader data = new BufferedReader(new InputStreamReader(Problem1.class.getResourceAsStream("Problem1.txt")))) {
            String line;
            while ((line = data.readLine()) != null) {
                if (line.isBlank()) {
                    current = new Elf();
                    elves.add(current);
                } else {
                    current.giveSnack(Integer.parseInt(line));
                }
            }
        }
        elves.add(current);
        System.out.println(elves.stream().map(Elf::totalSnackValue).max(Comparator.naturalOrder()).orElse(0));

        System.out.println(elves
                .stream()
                .map(Elf::totalSnackValue)
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .reduce(0, Integer::sum)
        );
    }

    private static class Elf {
        ArrayList<Integer> snack = new ArrayList<>();

        public void giveSnack(int value) {
            snack.add(value);
        }

        public int totalSnackValue() {
            return snack.stream().reduce(0, Integer::sum);
        }

        @Override
        public String toString() {
            return "Elf{" +
                    "snack=" + snack +
                    '}';
        }
    }
}
