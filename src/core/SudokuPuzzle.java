package core;

/**
 * Created by tan on 12/1/15.
 */
public class SudokuPuzzle {
    public String puzzle;
    public int dimension;

    public SudokuPuzzle(String puzzle, int dimension) {
        this.puzzle = puzzle;
        this.dimension = dimension;
    }

    public int getIndividualLength() {
        // count 0's
        int count = 0;
        for (int i = 0; i < puzzle.length(); i++) {
            if (puzzle.charAt(i) == '0') count++;
        }
        return count;
    }

    public int calculateFitness(Individual individual) {
        int fitness = 0;
        // fill up a sudoku puzzle
        String filled = fillPuzzle(individual);

        // calculate horizontal
        for (int i = 0; i < dimension; i++) {
            String cut = filled.substring(i * dimension, i * dimension + dimension);
            System.out.println(cut);
            fitness += countMissing(cut);
        }
        System.out.println();

        // calculate vertical
        for (int i = 0; i < dimension; i++) {
            String cut = getColumn(filled, i);
            fitness += countMissing(cut);
        }

        // calculate blocks
        for (int i = 0; i < dimension; i++) {
            String cut = getBlock(filled, i);
            fitness += countMissing(cut);
        }
        System.out.println(fitness);
        return fitness;
    }

    private int[] getBlockDimension() {
        if (dimension < 9) {
            return new int[]{2, dimension / 2};
        } else {
            return new int[]{3, 3};
        }
    }

    private String getBlock(String filled, int index) {
        StringBuilder builder = new StringBuilder();
        int[] blockDimension = getBlockDimension();
        int quotient = index / blockDimension[0];
        index = dimension * quotient + index % blockDimension[0];
        String cut = filled.substring(index * blockDimension[1], index * blockDimension[1] + blockDimension[1]);
        builder.append(cut);
        for (int i = 1; i < blockDimension[0]; i++) {
            cut = filled.substring((index * blockDimension[1]) + (i * dimension), (index * blockDimension[1] + blockDimension[1]) + (i * dimension));
            builder.append(cut);
        }
        System.out.println(builder);
        return builder.toString();
    }

    private String getColumn(String filled, int index) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < dimension; i++) {
            char c = filled.charAt(index + i * dimension);
            builder.append(c);
        }
        return builder.toString();
    }

    private int countMissing(String cut) {
        int miss = 0;
        for (char c = '1'; c <= '0' + dimension; c++) {
            if (!cut.contains(String.valueOf(c))) {
                miss++;
            }
        }
        return miss;
    }

    private String fillPuzzle(Individual individual) {
        String filled = puzzle;
        for (char c : individual.chromosome.toCharArray()) {
            filled = filled.replaceFirst("0", String.valueOf(c));
        }
        return filled;
    }

}
