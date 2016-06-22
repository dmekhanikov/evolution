package megabyte.evolution;

public interface Fitness<T> {
    double calculate(Individual<T> ind);
}
