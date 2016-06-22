package megabyte.evolution.salesman;

import megabyte.evolution.Evolution;
import megabyte.evolution.operators.Crossover;
import megabyte.evolution.operators.Fitness;
import megabyte.evolution.operators.Mutation;
import megabyte.evolution.salesman.primitives.Map;
import megabyte.evolution.salesman.primitives.NeighbourhoodPath;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SalesmanProblem {

    private static final int POPULATION_SIZE = 100;
    private static final int ITERATIONS = 10000;

    public static void main(String... args) throws IOException {
        Map map = Map.read(new File(args[0]));
        Crossover<NeighbourhoodPath> crossover = new HeuristicCrossover(0.5, map);
        Mutation<NeighbourhoodPath> mutation = new PrefixSavingMutation(0.01);
        Fitness<NeighbourhoodPath> fitness = new PathCostFitness(map);
        Evolution<NeighbourhoodPath> evolution = new Evolution<>(crossover, mutation, fitness);
        List<NeighbourhoodPath> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(NeighbourhoodPath.generate(map.size()));
        }
        NeighbourhoodPath best = null;
        for (int i = 0; i < ITERATIONS; i++) {
            evolution.evolve(population);
            NeighbourhoodPath curBest = Collections.max(population, (ind1, ind2) ->
                    Double.compare(fitness.calculate(ind1), fitness.calculate(ind2)));
            if (best == null || fitness.calculate(curBest) >  fitness.calculate(best)) {
                best = curBest;
            }
            System.out.println("Current best distance: " + -fitness.calculate(curBest));
        }

        System.out.println("Best distance:" + -fitness.calculate(best));
        TourWindow window = new TourWindow(map, best);
        window.setVisible(true);
    }
}
