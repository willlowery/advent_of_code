package advent_of_code_22;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Problem2 {
    public static void main(String[] args) throws Exception {
        ArrayList<Round> rounds = new ArrayList<>();
        try (BufferedReader data = new BufferedReader(new InputStreamReader(Problem2.class.getResourceAsStream("Problem2.txt")))) {
            String line;
            while ((line = data.readLine()) != null) {
                rounds.add(new Round(line));
            }
        }

        System.out.println(rounds.get(0) + " " + rounds.get(0).score());
//        System.out.println(rounds);
        System.out.println(rounds.stream().map(Round::score).reduce(0, Integer::sum));
    }

    static class Round {
        Options other;
        Options mine;

        public Round(String line) {
            other = toOption(line.charAt(0));
            mine = other.findItem(toOutcome(line.charAt(2)));
        }

        private Options toOption(char charAt) {
            if (charAt == 'A') return Options.ROCK;
            if (charAt == 'B') return Options.PAPER;
            if (charAt == 'C') return Options.SCISSOR;

            throw new IllegalStateException("Unknown move: " + charAt);
        }

        private Outcome toOutcome(char charAt) {
            if (charAt == 'X') return Outcome.LOSE;
            if (charAt == 'Y') return Outcome.TIE;
            if (charAt == 'Z') return Outcome.WIN;
            throw new IllegalStateException("Unknown move: " + charAt);
        }

        public int score() {
            return mine.scoreAgainst(other);
        }

        @Override
        public String toString() {
            return "Round{" +
                    "other=" + other +
                    ", mine=" + mine +
                    '}';
        }
    }

    enum Outcome {
        WIN,
        LOSE,
        TIE
    }

    enum Options {
        ROCK(1, 2,1),
        PAPER(2, 0,2),
        SCISSOR(3, 1,0);


        private final int beats;
        private final int loses;
        private final int choiceScore;

        Options(int choiceScore, int beats,int loses) {
            this.choiceScore = choiceScore;
            this.beats = beats;
            this.loses = loses;
        }

        public int getChoiceScore() {
            return choiceScore;
        }

        public Options findItem(Outcome o) {
            if (o.equals(Outcome.WIN)) return Options.values()[this.loses];
            if (o.equals(Outcome.TIE)) return this;
            return Options.values()[this.beats];
        }

        public int scoreAgainst(Options other) {
            int score = choiceScore;
            if (Options.values()[this.beats].equals(other)) {
                score += 6;
            } else if (this.equals(other)) {
                score += 3;
            }
            return score;
        }
    }
}
