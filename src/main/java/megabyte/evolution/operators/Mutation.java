package megabyte.evolution.operators;

import megabyte.evolution.Individual;

public interface Mutation<T> {
    void mutate(Individual<T> ind);
}
