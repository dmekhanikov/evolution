package megabyte.evolution;

import java.util.Random;
import java.util.Set;

public class Util {
    private static Random random = new Random(System.currentTimeMillis());

    public static int chooseFree(int max, Set<Integer> used) {
        int n = random.nextInt(max - used.size());
        for (int i = 0; i < max; i++) {
            if (!used.contains(i)) {
                if (n == 0) {
                    return i;
                } else {
                    n--;
                }
            }
        }
        throw new IllegalStateException("Should not get here");
    }
}
