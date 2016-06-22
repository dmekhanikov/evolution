package megabyte.evolution.salesman.primitives;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Map {

    private List<City> cities = new ArrayList<>();

    private Map() {}

    public double getDistance(int i, int j) {
        City c1 = cities.get(i);
        City c2 = cities.get(j);
        return Math.sqrt(
                Math.pow(c1.getX() - c2.getX(), 2) +
                Math.pow(c1.getY() - c2.getY(), 2));

    }

    public City getCity(int i) {
        return cities.get(i);
    }

    public int size() {
        return cities.size();
    }

    public static Map read(File file) throws IOException {
        try (Scanner scanner = new Scanner(file)) {
            Map map = new Map();
            while (scanner.hasNext()) {
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                IOException exception = scanner.ioException();
                if (exception != null) {
                    throw exception;
                }
                City city = new City(x, y);
                map.cities.add(city);
            }
            return map;
        }
    }
}
