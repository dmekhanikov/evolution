package megabyte.evolution.salesman;

import megabyte.evolution.Evolution;
import megabyte.evolution.operators.Crossover;
import megabyte.evolution.operators.Fitness;
import megabyte.evolution.operators.Mutation;
import megabyte.evolution.salesman.primitives.Map;
import megabyte.evolution.salesman.primitives.NeighbourhoodPath;
import megabyte.evolution.salesman.primitives.Path;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SalesmanProblem {

    private static final int POPULATION_SIZE = 100;
    private static final int ITERATIONS = 2000;

    public static void main(String... args) throws IOException {
        Map map = Map.read(new File(args[0]));
        Crossover<NeighbourhoodPath> crossover = new HeuristicCrossover(0.5, map);
        Mutation<NeighbourhoodPath> mutation = new PrefixSavingMutation(0.1);
        Fitness<NeighbourhoodPath> fitness = new PathCostFitness(map);
        Evolution<NeighbourhoodPath> evolution = new Evolution<>(crossover, mutation, fitness);
        List<NeighbourhoodPath> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(NeighbourhoodPath.generate(map.size()));
        }
        for (int i = 0; i < ITERATIONS; i++) {
            evolution.evolve(population);
            double bestFitness = population.stream().map(fitness::calculate).max(Double::compare).get();
            System.out.println("Best distance: " + -bestFitness);
        }

        Path best = Collections.max(population, (ind1, ind2) ->
                Double.compare(fitness.calculate(ind1), fitness.calculate(ind2)));
        TourWindow window = new TourWindow(map, best);
        window.setVisible(true);
    }
}
