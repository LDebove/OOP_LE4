package instance.reseau;

import java.util.Objects;

public class Route {
    private int cout;
    private Point depart;
    private Point destination;

    public Route(Point depart, Point destination) {
        this.depart = depart;
        this.destination = destination;
        this.cout = calcCout(depart, destination);
    }

    /**
     * calcule le coût d'une route égal à l'arrondi de la valeur euclidienne entre le point de départ et le point de destination
     * @param start point de départ
     * @param end point de destination
     * @return coût calculé
     */
    private int calcCout(Point start, Point end) {
        return Math.toIntExact(Math.round(Math.sqrt(Math.pow(end.getAbscisse() - start.getAbscisse(), 2) + Math.pow(end.getOrdonnee() - start.getOrdonnee(), 2))));
    }

    public int getCout() {
        return cout;
    }

    public Point getDepart() {
        return depart;
    }

    public Point getDestination() {
        return destination;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return depart.equals(route.depart) && destination.equals(route.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(depart, destination);
    }

    @Override
    public String toString() {
        return "Route{" +
                "cout=" + cout +
                ", depart=" + depart +
                ", destination=" + destination +
                '}';
    }

    public static void main(String[] args) {
        Client p1 = new Client(1,2,3, 4);
        Depot p2 = new Depot(1,43,22);
        Route r1 = new Route(p1, p2);
        Route r2 = new Route(p2, p1);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(r1.equals(r2));
    }
}
