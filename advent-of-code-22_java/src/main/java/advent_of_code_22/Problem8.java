package advent_of_code_22;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Problem8 {
    public static void main(String[] args) {
        List<List<Integer>> gridOfTrees = new ProblemReader()
                .readProblem("Problem8.txt")
                .stream()
                .map(i -> i.split(""))
                .map(i -> Stream.of(i).map(Integer::parseInt).collect(Collectors.toList()))
                .collect(Collectors.toList());

        TreeGrid treeGrid = new TreeGrid(gridOfTrees);
        int count = 0;
        int max = 0;
        for (int i = 0; i < treeGrid.rows(); i++) {
            for (int j = 0; j < treeGrid.rowLength(i); j++) {
                int score = treeGrid.score(i, j);
                if (max < score) {
                    max = score;
                }
                if (treeGrid.isVisible(i, j)) {
                    count++;
                }
            }
        }
        System.out.println(count);
        System.out.println(max);
    }

    public static class TreeGrid {
        private final List<List<Integer>> grid;

        public TreeGrid(List<List<Integer>> grid) {
            this.grid = grid;
        }

        public int rows() {
            return grid.size();
        }

        public int rowLength(int row) {
            return grid.get(row).size();
        }

        public int treeHeight(int rowIndex, int columnIndex) {
            return grid.get(rowIndex).get(columnIndex);
        }

        public boolean isVisible(int rowIndex, int columnIndex) {
            if (rowIndex == 0) return true;
            if (rowIndex + 1 == rows()) return true;
            if (columnIndex == 0) return true;
            if (columnIndex + 1 == grid.get(rowIndex).size()) return true;
            if (scanRow(rowIndex, columnIndex)) return true;
            return scanColumn(rowIndex, columnIndex);
        }

        public int score(int rowIndex, int columnIndex) {
            return scoreVisibility(treeHeight(rowIndex, columnIndex), up(rowIndex, columnIndex))
                    * scoreVisibility(treeHeight(rowIndex, columnIndex),down(rowIndex, columnIndex))
                    * scoreVisibility(treeHeight(rowIndex, columnIndex),left(rowIndex, columnIndex))
                    * scoreVisibility(treeHeight(rowIndex, columnIndex),right(rowIndex, columnIndex))
                    ;

        }

        private int scoreVisibility(int tree, List<Integer> trees) {
            int score = 0;
            for (Integer compare : trees) {
                score++;
                if (compare >= tree) return score;
            }

            return score;
        }

        private List<Integer> left(int rowIndex, int columnIndex) {
            ArrayList<Integer> inRow = new ArrayList<>();
            List<Integer> row = grid.get(rowIndex);
            for (int i = columnIndex - 1; i >= 0; i--) {
                inRow.add(row.get(i));
            }
            return inRow;
        }

        private List<Integer> right(int rowIndex, int columnIndex) {
            ArrayList<Integer> inRow = new ArrayList<>();
            List<Integer> row = grid.get(rowIndex);
            for (int i = columnIndex + 1; i < row.size(); i++) {
                inRow.add(row.get(i));
            }
            return inRow;
        }


        private List<Integer> down(int rowIndex, int columnIndex) {
            ArrayList<Integer> inColumn = new ArrayList<>();

            for (int i = rowIndex + 1; i < rows(); i++) {
                inColumn.add(grid.get(i).get(columnIndex));
            }

            return inColumn;
        }

        private List<Integer> up(int rowIndex, int columnIndex) {
            ArrayList<Integer> inColumn = new ArrayList<>();

            for (int i = rowIndex - 1; i >= 0; i--) {
                inColumn.add(grid.get(i).get(columnIndex));
            }

            return inColumn;
        }

        private boolean scanRow(int rowIndex, int columnIndex) {
            List<Integer> row = grid.get(rowIndex);
            Integer tree = row.get(columnIndex);

            boolean visible = true;
            for (int i = 0; i < columnIndex; i++) {
                visible &= row.get(i) < tree;
            }
            if (visible) return true;

            visible = true;
            for (int i = columnIndex + 1; i < row.size(); i++) {
                visible &= row.get(i) < tree;
            }
            return visible;
        }

        private boolean scanColumn(int rowIndex, int columnIndex) {
            List<Integer> row = grid.get(rowIndex);
            Integer tree = row.get(columnIndex);

            boolean visible = true;
            for (int i = 0; i < rowIndex; i++) {
                Integer toCompare = grid.get(i).get(columnIndex);
                visible &= toCompare < tree;
            }
            if (visible) return true;
            visible = true;
            for (int i = rowIndex + 1; i < grid.size(); i++) {
                Integer toCompare = grid.get(i).get(columnIndex);
                visible &= toCompare < tree;
            }
            return visible;
        }
    }
}
