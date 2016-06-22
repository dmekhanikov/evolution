package megabyte.evolution.salesman;

import megabyte.evolution.operators.Fitness;
import megabyte.evolution.salesman.primitives.Map;
import megabyte.evolution.salesman.primitives.NeighbourhoodPath;
import megabyte.evolution.salesman.primitives.Path;

// it should be just Path there, but poor Java generics doesn't allow using Fitness<Path> as Fitness<NeighbourhoodPath>
public class PathCostFitness implements Fitness<NeighbourhoodPath> {

    private Map map;

    public PathCostFitness(Map map) {
        this.map = map;
    }

    @Override
    public double calculate(NeighbourhoodPath path) {
        int prev = -1;
        double cost = 0;
        for (int cur : path) {
            if (prev != -1) {
                cost += map.getDistance(prev, cur);
            }
            prev = cur;
        }
        return -cost;
    }
}
