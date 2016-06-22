package megabyte.evolution;

import megabyte.evolution.operators.Crossover;
import megabyte.evolution.operators.Fitness;
import megabyte.evolution.operators.Mutation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Evolution<T extends Individual> {

    private Crossover<T> crossover;
    private Mutation<T> mutation;
    private Fitness<T> fitness;

    private Random random = new Random(System.currentTimeMillis());

    public Evolution(Crossover<T> crossover, Mutation<T> mutation, Fitness<T> fitness) {
        this.crossover = crossover;
        this.mutation = mutation;
        this.fitness = fitness;
    }

    private List<Double> makeRoulette(List<T> population) {
        double minFitness = population.stream().map(fitness::calculate).min(Double::compare).get();
        double sumFitness = population.stream().map(ind -> fitness.calculate(ind) - minFitness)
                .reduce(0.0, (a, b) -> a + b);
        List<Double> roulette = new ArrayList<>();
        if (sumFitness == 0) {
            for (int i = 0; i < population.size(); i++) {
                roulette.add((double) (i + 1) / population.size());
            }
        } else {
            roulette.addAll(population.stream()
                    .map(ind -> (fitness.calculate(ind) - minFitness) / sumFitness)
                    .collect(Collectors.toList()));
            double curCell = roulette.get(0);
            for (int i = 1; i < population.size(); i++) {
                double curFit = roulette.get(i);
                curCell += curFit;
                roulette.set(i, curCell);
            }
        }
        roulette.set(population.size() - 1, 1.0);
        return roulette;
    }

    private int select(List<Double> roulette) {
        double r = random.nextDouble();
        for (int i = 0; i < roulette.size(); i++) {
            if (roulette.get(i) >= r) {
                return i;
            }
        }
        throw new IllegalStateException("Should never get here");
    }

    private List<T> selectParents(List<T> population) {
        List<Double> roulette = makeRoulette(population);
        List<T> parents = new ArrayList<>();
        for (int i = 0; i < population.size(); i++) {
            int p = select(roulette);
            T ind = population.get(p);
            parents.add(ind);
        }
        return parents;
    }

    private <E> void shuffle(List<E> l) {
        for (int i = 0; i < l.size(); i++) {
            int j = random.nextInt(i + 1);
            Collections.swap(l, i, j);
        }
    }

    public List<T> evolve(List<T> population) {
        int popSize = population.size();
        List<T> parents = selectParents(population);
        shuffle(population);
        population.clear();
        while (population.size() < popSize) {
            int i = random.nextInt(parents.size() / 2);
            T ind1 = parents.get(i * 2);
            T ind2 = parents.get(i * 2 + 1);
            population.addAll(crossover.cross(ind1, ind2));
        }
        return population;
    }
}
