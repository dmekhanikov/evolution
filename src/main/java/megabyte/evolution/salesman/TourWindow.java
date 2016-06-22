package megabyte.evolution.salesman;

import megabyte.evolution.salesman.primitives.City;
import megabyte.evolution.salesman.primitives.Map;
import megabyte.evolution.salesman.primitives.Path;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TourWindow extends Frame {

    private Map map;
    private Path path;

    public TourWindow(Map map, Path path) throws HeadlessException {
        super("Traveling salesman");
        this.map = map;
        this.path = path;

        setSize(800, 800);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Point prev = null;
        for (int n : path) {
            City city = map.getCity(n);
            Point p = new Point(city.getX() * 10, city.getY() * 10);
            drawPoint(g2, p);
            if (prev != null) {
                g2.drawLine(prev.x, prev.y, p.x, p.y);
            }
            prev = p;
        }
    }

    private void drawPoint(Graphics2D g2, Point p) {
        g2.fillOval((int) p.getX() - 3, (int) p.getY() - 3, 6, 6);
    }
}
