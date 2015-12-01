package core;

import java.util.List;

/**
 * Created by tan on 12/2/15.
 */
public interface SurvivorSelector {
    public List<Individual> select(List<Individual> sample);
}
