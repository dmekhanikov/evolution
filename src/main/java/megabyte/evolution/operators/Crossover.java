package megabyte.evolution.operators;

import megabyte.evolution.Individual;

import java.util.List;

public interface Crossover<T> {
    List<Individual<T>> cross(Individual<T> ind1, Individual<T> ind2);
}
