package contrib;

import core.Individual;
import core.Recombinator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tan on 12/2/15.
 */
public class NPointRecombinator implements Recombinator {
    private int n;

    public NPointRecombinator(int n) {
        this.n = n;
    }

    @Override
    public List<Individual> recombine(List<Individual> parents) {
        int geneSize = parents.get(0).getChromosome().length();
        int[] points = randomizePoints(geneSize);
        StringBuilder[] builders = new StringBuilder[]{new StringBuilder(), new StringBuilder()};
        // alternate construction
        int i = 0;
        for (; i < n; i++) {
            builders[0].append(parents.get((i + 1) % 2).getChromosome().substring(points[i], points[i + 1]));
            builders[1].append(parents.get(i % 2).getChromosome().substring(points[i], points[i + 1]));
        }
        builders[0].append(parents.get((i + 1) % 2).getChromosome().substring(points[i]));
        builders[1].append(parents.get(i % 2).getChromosome().substring(points[i]));

        List<Individual> children = new ArrayList<>();
        for (StringBuilder builder : builders) {
            children.add(new Individual(builder.toString()));
        }
        return children;
    }

    private int[] randomizePoints(int geneSize) {
        int[] points = new int[n + 1];
        points[0] = 0;
        points[1] = (int) (Math.random() * (geneSize));
        for (int i = 2; i < points.length; i++) {
            points[i] = points[i - 1] + (int) (Math.random() * (geneSize - points[i - 1]));
        }
        return points;
    }
}
