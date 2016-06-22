package megabyte.evolution.salesman.primitives;

import megabyte.evolution.Util;

import java.util.*;

public class NeighbourhoodPath implements Path {

    private List<Integer> neighbours;

    public NeighbourhoodPath(int size) {
        this.neighbours = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            this.neighbours.add(-1);
        }
    }

    public static NeighbourhoodPath generate(int size) {
        int cur = 0;
        NeighbourhoodPath path = new NeighbourhoodPath(size);
        Set<Integer> used = new HashSet<>();
        for (int i = 0; i < size - 1; i++) {
            used.add(cur);
            int next = Util.chooseFree(size, used);
            path.getGenome().set(cur, next);
            cur = next;
        }
        path.getGenome().set(cur, 0);
        assert path.isCorrect();
        return path;
    }

    public boolean isCorrect() {
        int cnt = 0;
        for (int n : this) {
            cnt++;
        }
        return cnt == neighbours.size() + 1;
    }

    @Override
    public List<Integer> getGenome() {
        return neighbours;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new PathIterator();
    }

    private class PathIterator implements Iterator<Integer> {
        private int i = 0;
        private int cnt = 0;
        boolean finish = false;

        @Override
        public boolean hasNext() {
            if (i == 0 && cnt != 0) {
                finish = true;
                return true;
            } else {
                return !finish;
            }
        }

        @Override
        public Integer next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int res = i;
            i = neighbours.get(i);
            cnt++;
            return res;
        }
    }
}
