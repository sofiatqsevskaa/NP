package K2.T18;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

interface Identifier {
    long getId();
    double getDistance(Object o);
}
class Point2D implements Identifier {
    long id;
    float x;
    float y;
    public Point2D(long id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public long getId() {
        return id;
    }

    @Override
    public double getDistance(Object o) {
        Point2D other=(Point2D) o;
        return Math.sqrt((Math.pow(this.getX()-other.getX(),2)+Math.pow((this.getY()-other.getY()),2)));
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}


class Cluster<T extends Identifier> {
    Map<Long,T> clusters;
    long id;

    public Cluster() {
        clusters=new HashMap<>();
    }

    void addItem(T element){
        clusters.put(element.getId(), element);
    }
    void near(long id, int top){
        AtomicInteger i=new AtomicInteger(0);
        clusters.entrySet().stream()
                .filter(c->c.getKey()!=id).sorted(Comparator.comparing(x->x.getValue().getDistance(clusters.get(id))))
                .limit(top).forEach(x-> System.out.println(i.incrementAndGet()+". "+x.getKey()+" -> "+String.format("%.3f",x.getValue().getDistance(clusters.get(id)))));
    }
}


public class ClusterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cluster<Point2D> cluster = new Cluster<>();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            long id = Long.parseLong(parts[0]);
            float x = Float.parseFloat(parts[1]);
            float y = Float.parseFloat(parts[2]);
            cluster.addItem(new Point2D(id, x, y));
        }
        int id = scanner.nextInt();
        int top = scanner.nextInt();
        cluster.near(id, top);
        scanner.close();
    }
}

// your code here