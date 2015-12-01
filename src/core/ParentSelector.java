package core;

import java.util.List;

/**
 * Created by tan on 12/2/15.
 */
public interface ParentSelector {
    public List<Individual> select(List<Individual> population);
}
