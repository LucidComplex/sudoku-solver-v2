package contrib;

import core.Individual;
import core.Recombinator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tan on 12/2/15.
 */
public class UniformRecombinator implements Recombinator{
    @Override

    public List<Individual> recombine(List<Individual> parents) {
        int geneSize = parents.get(0).getChromosome().length();
        char [] chromosome = new char[geneSize];
        List<Individual> children = new ArrayList<>();

        for(int i=0; i < parents.size(); i++){
            for(int allele = 0; allele < geneSize; allele++){
                double randomNumber = Math.random();
                if(randomNumber>=0.5){
                    chromosome[allele] = parents.get(i).getChromosome().charAt(allele);
                } else {
                    chromosome[allele] = parents.get((i+1)%2).getChromosome().charAt(allele);
                }
            }
            children.add(new Individual(String.valueOf(chromosome)));
        }

        return children;
    }

    public String toString(){
        return "Uniform Crossover";
    }
}
