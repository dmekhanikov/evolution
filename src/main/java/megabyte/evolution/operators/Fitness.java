package megabyte.evolution.operators;

import megabyte.evolution.Individual;

public interface Fitness<T extends Individual> {
    double calculate(T ind);
}
