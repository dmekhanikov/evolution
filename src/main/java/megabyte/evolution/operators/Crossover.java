package megabyte.evolution.operators;

import megabyte.evolution.Individual;

import java.util.List;

public interface Crossover<T extends Individual> {
    List<T> cross(T ind1, T ind2);
}
