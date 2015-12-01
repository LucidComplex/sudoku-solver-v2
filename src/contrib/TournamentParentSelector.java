package contrib;

import core.Individual;
import core.ParentSelector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tan on 12/2/15.
 */
public class TournamentParentSelector implements ParentSelector {
    private int k;

    public TournamentParentSelector(int k) {
        this.k = k;
    }

    @Override
    public List<Individual> select(List<Individual> population) {

        List<Individual> selected = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            // select k at random
            List<Individual> participants = selectParticipants(population);
            // take the best
            Collections.sort(participants, new FitnessComparator());
            selected.add(participants.get(0).copy());
        }

        return selected;
    }

    private List<Individual> selectParticipants(List<Individual> population) {
        List<Individual> participants = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            int randomIndex = (int) Math.round(Math.random() * (population.size() - 1));
            participants.add(population.get(randomIndex).copy());
        }
        return participants;
    }
}
