package instance.reseau;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class Point {
    private final int id;
    private int abscisse;
    private int ordonnee;
    private Map<Point, Route> routes;

    public Point(int id, int x, int y) {
        this.id = id;
        this.abscisse = x;
        this.ordonnee = y;
        this.routes = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public int getAbscisse() {
        return abscisse;
    }

    public int getOrdonnee() {
        return ordonnee;
    }

    /**
     * crée une route à partir du point de destination et l'ajoute aux routes
     * @param destination
     */
    public void ajouterRoute(Point destination) {
        Route route = new Route(this, destination);
        this.routes.put(destination, route);
    }

    /**
     * renvoie le cout de la route à partir du point de destination
     * @param destination
     * @return infini si la route n'existe pas, le cout sinon
     */
    public int getCoutVers(Point destination) {
        Route route = this.routes.get(destination);
        if(route == null) return Integer.MAX_VALUE;
        return route.getCout();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return id == point.id && abscisse == point.abscisse && ordonnee == point.ordonnee;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, abscisse, ordonnee);
    }

    @Override
    public String toString() {
        return "Point{" +
                "id=" + id +
                ", abscisse=" + abscisse +
                ", ordonnee=" + ordonnee +
                '}';
    }

    public static void main(String[] args) {
        Depot p1 = new Depot(1, 2, 3);
        Depot p2 = new Depot(2, 6, 8);
        System.out.println(p1.getCoutVers(p2));
        p1.ajouterRoute(p2);
        System.out.println(p1.getCoutVers(p2));
    }
}
