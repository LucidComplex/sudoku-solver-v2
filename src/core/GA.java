package core;

import contrib.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by tan on 12/1/15.
 */
public class GA {
    public int restarts;
    public int populationSize;
    public int maxGenerations;
    public float survivalRate;
    public float recombinationProb;
    public float mutationProb;
    public SurvivorSelector survivorSelector;
    public ParentSelector parentSelector;
    public Recombinator recombinator;
    public Mutator mutator;

   public SudokuPuzzle puzzle;
    boolean solutionFound;
    public int generations;
    private Individual best;

    public GA() {
        populationSize = 1000;
        maxGenerations = 3000000;
        survivalRate = 0.2f;
        recombinationProb= 1f;
        mutationProb = 1f;
        restarts = 0;
        generations = 0;
        survivorSelector = new ElitismSurvivorSelector(survivalRate);
        parentSelector = new TournamentParentSelector(3);
        recombinator = new NPointRecombinator(2);
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
        int overallFitness = 0;
        int count = 0;
        while (!solutionFound && iterations < maxGenerations) {

            // update and print fitness
            printAverageFitness(population);
            System.out.println("Iteration " + ++iterations);
            if (solutionFound) {
                System.out.println("Solution Found");
                System.out.println(best.getChromosome());
            }


            if (overallFitness == calculateFitness(population)) {
                count++;
            }
            if (count > 20) {
                populate(population);
                restarts++;
                count = 0;
            }
            overallFitness = calculateFitness(population);


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
//            for (Individual i: population){
//                System.out.println(i.getChromosome());
//            }
        }
        generations = iterations;
    }


    public void saveSolution(long runningTime, String filename){

        String file_name = filename.split("\\s.\\s")[0];
        file_name += ".out";
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file_name, "UTF-8");
            writer.println("GA PARAMETERS");
            writer.println("Representation: " + "Integer");
            writer.println("Population size: " + populationSize);
            writer.println("Max Generation: " + maxGenerations);
            writer.println("Parent Selection: " + parentSelector.toString());
            writer.println("Survivor Selection: " + survivorSelector.toString());
            writer.println("Recombination Method: " + recombinator.toString());
            writer.println("Mutation Method: " + mutator.toString());
            writer.println("Recombination Probabilty: " + recombinationProb);
            writer.println("Mutation Probability: " + mutationProb);
            writer.println("Survival Rate: " + survivalRate);
            writer.println("Generations: " + generations);
            writer.println("Running time: "+ runningTime + " ms");
            writer.println("Restarts: " + restarts);
            writer.println("Best individual (Phenotype): ");
            writer.println(best.getChromosome()+"\n");
            writer.println(puzzle.fillPuzzle(best));
            if(solutionFound)
                writer.println("The sudoku puzzle was solved");
            else
                writer.println("The puzzle wasn't solved. Either the given"
                        + "\n puzzle was wrongly configured or the GA "
                        + "has limitations");
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(GA.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            writer.close();
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
                best = i;
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
