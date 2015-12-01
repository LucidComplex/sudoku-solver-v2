package contrib;

import core.Individual;
import core.Mutator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tan on 12/2/15.
 */
public class ResetMutator implements Mutator {
    private float mutationProb;
    private int dimension;

    public ResetMutator(float mutateProb, int dimension) {
        mutationProb = mutateProb;
        this.dimension = dimension;
    }
    @Override
    public List<Individual> mutate(List<Individual> individuals) {
        List<Individual> newList = new ArrayList<>();
        for (Individual i : individuals) {
            if (mutationProb >= Math.random()) {
                newList.add(mutateIndividual(i));
            } else {
                newList.add(i.copy());
            }
        }
        return newList;
    }

    private Individual mutateIndividual(Individual i) {
        StringBuilder builder = new StringBuilder();
        for (char c : i.getChromosome().toCharArray()) {
            if (0.5 >= Math.random()) {
                char temp = (char) ('1' + (int) Math.round(Math.random() * (dimension - 1)));
                builder.append(temp);
            } else {
                builder.append(c);
            }
        }
        return new Individual(builder.toString());
    }


}
