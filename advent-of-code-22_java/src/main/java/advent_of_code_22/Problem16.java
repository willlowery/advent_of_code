package advent_of_code_22;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Problem16 {

    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("Valve (\\w+) has flow rate=(\\d+); (tunnel|tunnels) (lead|leads) to (valve|valves)(( (\\w+)|, (\\w+))*)");
        G graph = new G();

        new ProblemReader()
                .readProblem("Problem16.txt")
                .forEach(i -> {
                    Matcher matcher = pattern.matcher(i);
                    if (!matcher.matches()) throw new IllegalStateException("Bad Pattern: " + i);

                    String label = matcher.group(1);
                    graph.addVertex(label, Integer.parseInt(matcher.group(2)));

                    for (String s : matcher.group(6).trim().split(", ")) {
                        graph.addEdge(label, s);
                    }
                });

        graph.buildDistances();

        CurrentState state = new CurrentState();
        for (String s : graph.vertices.keySet()) {
            state.add(s, graph.findFlow(s));
        }


        List<String[]> pairs = new ArrayList<>();
        List<String> a = state.findNonZeroFlow();
        for (int i = 0, aSize = a.size(); i < aSize; i++) {
            for (int j = 0, size = a.size(); j < size; j++) {
                String first = a.get(i);
                String second = a.get(j);

                pairs.add(new String[]{first, second});
            }
        }


//        graph.partOneAttemptTwo("AA", state.findNonZeroFlow(), state, 0);
        graph.partTwoAttemptOne("AA", 0, "AA", 0, pairs, state);

    }


    static class CurrentState {
        HashMap<String, Integer> state = new HashMap<>();
        HashMap<String, String> setBy = new HashMap<>();
        HashMap<String, Integer> flow = new HashMap<>();

        private void add(String label, int flowRate) {
            state.put(label, Integer.MAX_VALUE);
            flow.put(label, flowRate);
        }

        public void on(String label, int turn) {
            state.put(label, turn);
        }

        public void on(String person, String label, int turn) {
            state.put(label, turn);
            setBy.put(label, person);
        }

        public boolean on(String label) {
            return state.get(label) != Integer.MAX_VALUE;
        }

        public void off(String label) {
            state.put(label, Integer.MAX_VALUE);
            setBy.remove(label);
        }

        public long countOn() {
            return state.values().stream().filter(i -> i != Integer.MAX_VALUE).count();
        }

        public boolean allOn() {
            return !state.containsValue(Integer.MAX_VALUE);
        }


        public int score(int upperBound) {
            int score = 0;
            for (int i = 0; i <= upperBound; i++) {
                for (Map.Entry<String, Integer> entry : state.entrySet()) {
                    String key = entry.getKey();
                    Integer value = entry.getValue();
                    if (value < i) {
                        score += flow.get(key);
                    }
                }
            }
            return score;
        }

        public List<String> findOff() {
            return state.entrySet().stream().filter(i -> i.getValue() == Integer.MAX_VALUE).map(Map.Entry::getKey).collect(Collectors.toList());
        }

        public List<String> findNonZeroFlow() {
            return state
                    .entrySet()
                    .stream()
                    .filter(i -> i.getValue() == Integer.MAX_VALUE)
                    .map(Map.Entry::getKey)
                    .filter(i -> flow.get(i) > 0)
                    .collect(Collectors.toList());
        }
    }

    static class G {
        int maxScore = 0;
        HashMap<String, Vertex> vertices = new HashMap<>();
        HashMap<String, List<Edge>> edges = new HashMap<>();

        HashMap<String, HashMap<String, Integer>> distances = new HashMap<>();

        public void addVertex(String label, Integer data) {
            vertices.put(label, new Vertex(label, data));
            edges.put(label, findEdges(label));
        }

        public int findFlow(String label) {
            return vertices.get(label).getFlow();
        }

        public void addEdge(String vertex, String label) {
            List<Edge> edges = findEdges(vertex);
            edges.add(new Edge(label));
            this.edges.put(vertex, edges);
        }

        public List<Edge> findEdges(String label) {
            return this.edges.getOrDefault(label, new ArrayList<>());
        }

        public void buildDistances() {
            for (String label : vertices.keySet()) {
                buildDistances(label);
            }
        }

        private void buildDistances(String label) {
            HashSet<String> discovered = new HashSet<>();
            LinkedList<Pair> queue = new LinkedList<>();
            queue.add(new Pair(label, 0));
            distances.computeIfAbsent(label, (k) -> new HashMap<>()).put(label, 0);
            while (!queue.isEmpty()) {
                Pair toSearch = queue.pop();
                discovered.add(toSearch.label);

                for (Edge edge : findEdges(toSearch.label)) {
                    if (!discovered.contains(edge.to)) {
                        queue.addLast(new Pair(edge.to, toSearch.distance + 1));
                        distances.computeIfAbsent(label, (k) -> new HashMap<>()).put(edge.to, toSearch.distance + 1);
                    }
                }
            }
        }

        public Integer distance(String start, String end) {
            return distances.get(start).get(end);
        }

        @Override
        public String toString() {
            return "G{" +
                    "vertices=" + vertices +
                    ", edges=" + edges +
                    '}';
        }

        public void partOneAttemptTwo(String start, List<String> toVisit, CurrentState state, int depth) {
            for (int i = 0; i < toVisit.size(); i++) {
                String end = toVisit.get(i);
                if (state.on(end)) continue;
                int dist = depth + 1 + distance(start, end);
                if (dist > 30) continue;

                state.on(end, dist);
                partOneAttemptTwo(end, toVisit, state, dist);
                state.off(end);
            }

            System.out.println(state.score(30));
        }

        public void partTwoAttemptOne(String one, int depthOne, String two, int depthTwo, List<String[]> toVisit, CurrentState state) {
            int score = state.score(26);
            if (maxScore < score) {
                maxScore = score;
                System.out.println(maxScore);
            }

            if (depthOne > 26 || depthTwo > 26) return;

            for (String[] pairs : toVisit) {
                String first = pairs[0];
                String second = pairs[1];

                if (state.on(first)) continue;
                if (state.on(second)) continue;

                int distOne = depthOne + 1 + distance(one, first);
                int distTwo = depthTwo + 1 + distance(two, second);

                state.on("One", first, distOne);
                state.on("Two", second, distTwo);


                partTwoAttemptOne(first, distOne, second, distTwo, toVisit, state);


                state.off(first);
                state.off(second);
            }
        }

        static class Edge {
            String to;

            public Edge(String to) {
                this.to = to;
            }

            @Override
            public String toString() {
                return "Edge{" +
                        "to='" + to + '\'' +
                        '}';
            }
        }

        static class Vertex {
            private final String label;
            private final Integer flow;

            public Vertex(String label, Integer data) {
                this.flow = data;
                this.label = label;
            }

            public String getLabel() {
                return label;
            }

            public Integer getFlow() {
                return flow;
            }

            @Override
            public String toString() {
                return "Vertex{" +
                        "label='" + label + '\'' +
                        ", data=" + flow +
                        '}';
            }
        }

        static class Pair {
            String label;
            Integer distance;

            public Pair(String label, Integer distance) {
                this.label = label;
                this.distance = distance;
            }

            @Override
            public String toString() {
                return "Pair{" +
                        "label='" + label + '\'' +
                        ", distance=" + distance +
                        '}';
            }
        }
    }
}
