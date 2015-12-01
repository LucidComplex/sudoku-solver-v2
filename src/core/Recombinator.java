package core;

import java.util.List;

/**
 * Created by tan on 12/2/15.
 */
public interface Recombinator {
    public List<Individual> recombine(List<Individual> parents);
}
