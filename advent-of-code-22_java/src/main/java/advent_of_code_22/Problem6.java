package advent_of_code_22;

import java.util.HashSet;
import java.util.List;

public class Problem6 {
    public static void main(String[] args) {
        List<String> lines = new ProblemReader().readProblem("Problem6.txt");
        String message = lines.get(0);
//        String message = "mjqjpqmgbljsphdztnvjfqwrcgsmlb";
        int s = findStart(message, 14);
        System.out.println(s);
        System.out.println(message.substring(s));
    }

    private static int findStart(String message, int length) {
        for (int i = length; i < message.length(); i++) {
            if (uniqueCharacters(message.substring(i - length, i))) {
                return i;
            }
        }
        return -1;
    }

    public static boolean uniqueCharacters(String str) {
        HashSet<Character> buffer = new HashSet<>();
        for (int i = 0; i < str.length(); i++) {
            buffer.add(str.charAt(i));
        }
        return buffer.size() == str.length();
    }
}
