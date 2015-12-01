package core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tan on 12/1/15.
 */
public class GA {
    private int populationSize;
    private int maxGenerations;
    private float survivalRate;
    private float crossoverProb;
    private float mutationProb;

    private SudokuPuzzle puzzle;

    public GA() {
        populationSize = 10;
        maxGenerations = 3000000;
        survivalRate = 0.2f;
        crossoverProb = 0.85f;
        mutationProb = 0.7f;
    }

    public GA(String filename) throws IOException {
        this();
        puzzle = loadPuzzle(filename);
    }

    private SudokuPuzzle loadPuzzle(String filename) throws IOException {
        InputStreamReader in = new InputStreamReader(new FileInputStream(filename));
        BufferedReader reader = new BufferedReader(in);
        String line;
        int size = Integer.parseInt(reader.readLine());
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            // skip blank line
            if (line.trim().length() == 0) {
                continue;
            }
            // strip spaces
            line = line.replaceAll(" ", "");
            sb.append(line);
        }
        return new SudokuPuzzle(sb.toString(), size);
    }

    public void solve() {
        List<Individual> population = new ArrayList<>();
        populate(population);
        puzzle.calculateFitness(population.get(0));
    }

    private void populate(List<Individual> population){
        for(int i = 0; i < populationSize; i++){
            population.add(Individual.random(puzzle.getIndividualLength(), puzzle.dimension));
        }
    }
}
