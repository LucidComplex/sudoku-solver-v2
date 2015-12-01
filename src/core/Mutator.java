package core;

import java.util.List;

/**
 * Created by tan on 12/2/15.
 */
public interface Mutator {
    public List<Individual> mutate(List<Individual> individuals);
}
