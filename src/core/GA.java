package core;

import contrib.ElitismSurvivorSelector;
import contrib.NPointRecombinator;
import contrib.ResetMutator;
import contrib.TournamentParentSelector;

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
    private float recombinationProb;
    private float mutationProb;
    private SurvivorSelector survivorSelector;
    private ParentSelector parentSelector;
    private Recombinator recombinator;
    private Mutator mutator;

    private SudokuPuzzle puzzle;
    boolean solutionFound;
    private Individual solution;

    public GA() {
        populationSize = 10;
        maxGenerations = 3000000;
        survivalRate = 0.1f;
        recombinationProb= 1f;
        mutationProb = 0.18f;
        survivorSelector = new ElitismSurvivorSelector(survivalRate);
        parentSelector = new TournamentParentSelector(3);
        recombinator = new NPointRecombinator(3);
    }

    public GA(String filename) throws IOException {
        this();
        puzzle = loadPuzzle(filename);
        mutator = new ResetMutator(mutationProb, puzzle.dimension);
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
        // initially populate
        populate(population);
        int iterations = 0;
        while (!solutionFound && iterations < maxGenerations) {
            System.out.println("Iteration " + ++iterations);
            // update and print fitness
            printAverageFitness(population);
            if (solutionFound) {
                System.out.println("Solution Found");
                System.out.println(solution.getChromosome());
            }

            // select survivors
            List<Individual> survivors = survivorSelector.select(population);
            // select parents
            List<Individual> parents = generateParents(population);
            // generate children
            List<Individual> children = generateChildren(parents);

            population.clear();
            population.addAll(cloneIndividualList(survivors));
            population.addAll(cloneIndividualList(children));

            population = mutator.mutate(population);
        }

    }

    private List<Individual> generateChildren(List<Individual> parents) {
        int childrenToGenerate = Math.round(populationSize * (1 - survivalRate));
        List<Individual> children = new ArrayList<>();
        for (int i = 0; i < (childrenToGenerate + 1) / 2; i++) {
            if (recombinationProb >= Math.random()) {
                children.addAll(recombinator.recombine(cloneIndividualList(parents.subList(i * 2, i * 2 + 2))));
            } else {
                children.addAll(cloneIndividualList(parents.subList(i * 2, i * 2 + 2)));
            }
        }
        if (childrenToGenerate % 2 == 1) {
            children.remove(0);
        }
        return children;
    }

    private List<Individual> cloneIndividualList(List<Individual> list) {
        List<Individual> newList = new ArrayList<>();
        for (Individual i : list) {
            newList.add(i.copy());
        }
        return newList;
    }

    private List<Individual> generateParents(List<Individual> population) {
        int childrenToGenerate = Math.round(populationSize * (1 - survivalRate));
        int pairsToGenerate = (childrenToGenerate + 1) / 2;
        List<Individual> parents = new ArrayList<>();
        for (int i = 0; i < pairsToGenerate; i++) {
            parents.addAll(parentSelector.select(population));
        }
        return parents;

    }

    private void printAverageFitness(List<Individual> population) {
        int totalFitness = calculateFitness(population);
        System.out.println("Average fitness: " + (float) totalFitness / population.size());
    }

    private int calculateFitness(List<Individual> population) {
        int total = 0;
        for (Individual i : population) {
            int fitness = puzzle.calculateFitness(i);
            if (fitness == 0) {
                solutionFound = true;
                solution = i;
            }
            total += puzzle.calculateFitness(i);
        }
        return total;
    }

    private void populate(List<Individual> population){
        for(int i = 0; i < populationSize; i++){
            population.add(Individual.random(puzzle.getIndividualLength(), puzzle.dimension));
        }
    }
}
