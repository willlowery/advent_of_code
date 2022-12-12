package advent_of_code_22;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProblemReader {

    public List<String> readProblem(String name) {
        try (BufferedReader data = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(name)))) {
            ArrayList<String> strings = new ArrayList<>();
            String line;
            while ((line = data.readLine()) != null) {
                strings.add(line);
            }
            return strings;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }
}
