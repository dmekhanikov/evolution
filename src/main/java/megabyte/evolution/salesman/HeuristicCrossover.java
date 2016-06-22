package megabyte.evolution.salesman;

import megabyte.evolution.Util;
import megabyte.evolution.operators.Crossover;
import megabyte.evolution.salesman.primitives.*;

import java.util.*;

public class HeuristicCrossover implements Crossover<NeighbourhoodPath> {

    private double probability;
    private megabyte.evolution.salesman.primitives.Map map;
    private Random random = new Random(System.currentTimeMillis());


    public HeuristicCrossover(double probability, megabyte.evolution.salesman.primitives.Map map) {
        this.probability = probability;
        this.map = map;
    }

    @Override
    public List<NeighbourhoodPath> cross(NeighbourhoodPath ind1, NeighbourhoodPath ind2) {
        double d = random.nextDouble();
        if (d < probability) {
            int genomeSize = ind1.getGenome().size();
            int start = random.nextInt(genomeSize);
            int cur = start;
            NeighbourhoodPath path = new NeighbourhoodPath(genomeSize);
            Set<Integer> used = new HashSet<>();
            for (int i = 0; i < genomeSize - 1; i++) {
                used.add(cur);
                int c1 = ind1.getGenome().get(cur);
                int c2 = ind2.getGenome().get(cur);
                int next = getNext(cur, c1, c2, used);
                path.getGenome().set(cur, next);
                cur = next;
            }
            path.getGenome().set(cur, start);
            assert path.isCorrect();
            return Collections.singletonList(path);
        } else {
            return Collections.emptyList();
        }
    }

    private int getNext(int cur, int c1, int c2, Set<Integer> used) {
        if (!used.contains(c1) && !used.contains(c2)) {
            if (map.getDistance(cur, c1) < map.getDistance(cur, c2)) {
                return c1;
            } else {
                return c2;
            }
        } else {
            if (!used.contains(c1) && used.contains(c2)) {
                return c1;
            } else if (used.contains(c1) && !used.contains(c2)) {
                return c2;
            } else {
                return Util.chooseFree(map.size(), used);
            }
        }
    }
}
