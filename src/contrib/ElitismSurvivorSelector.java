package contrib;

import core.Individual;
import core.SurvivorSelector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tan on 12/2/15.
 */
public class ElitismSurvivorSelector implements SurvivorSelector {
    private float survivalRate;

    public ElitismSurvivorSelector(float survivalRate) {
        this.survivalRate = survivalRate;
    }

    @Override
    public List<Individual> select(List<Individual> sample) {
        Collections.sort(sample, new FitnessComparator());
        int toSurvive = Math.round(sample.size() * survivalRate);
        List<Individual> survivors = new ArrayList<>();
        List<Individual> chosenSurvivors = sample.subList(0, toSurvive);
        for (Individual individual : chosenSurvivors) {
            Individual clone = individual.copy();
            survivors.add(clone);
        }
        return survivors;
    }

}
