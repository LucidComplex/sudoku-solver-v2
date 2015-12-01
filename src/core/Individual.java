package core;

import java.util.StringJoiner;

/**
 * Created by tan on 12/2/15.
 */
public class Individual {
    public String chromosome;
    public int fitness;

    public Individual(String chromosome) {
        this.chromosome = chromosome;
        this.fitness = -1;
    }

    public static Individual random(int length, int dimensions) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            // generate a random character from 1 to dimension
            char c = (char) ('1' + (Math.random() * dimensions));
            builder.append(c);
        }
        return new Individual(builder.toString());
    }

}
