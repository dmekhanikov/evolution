package megabyte.evolution.salesman;

import megabyte.evolution.Util;
import megabyte.evolution.operators.Mutation;
import megabyte.evolution.salesman.primitives.NeighbourhoodPath;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class PrefixSavingMutation implements Mutation<NeighbourhoodPath> {

    private double probability;
    private Random random = new Random(System.currentTimeMillis());

    public PrefixSavingMutation(double probability) {
        this.probability = probability;
    }

    @Override
    public void mutate(NeighbourhoodPath ind) {
        double d = random.nextDouble();
        if (d < probability) {
            int prefixSize = random.nextInt(ind.getGenome().size() - 1) + 1;
            Set<Integer> used = new HashSet<>();
            int start = random.nextInt(ind.getGenome().size());
            int cur = start;
            for (int i = 0; i < prefixSize - 1; i++) {
                used.add(cur);
                cur = ind.getGenome().get(cur);
            }
            for (int i = 0; i < ind.getGenome().size() - prefixSize; i++) {
                used.add(cur);
                int next = Util.chooseFree(ind.getGenome().size(), used);
                ind.getGenome().set(cur, next);
                cur = next;
            }
            ind.getGenome().set(cur, start);
            assert ind.isCorrect();
        }
    }
}
