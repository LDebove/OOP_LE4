package instance.reseau;

public class Depot extends Point {
    public Depot(int id, int x, int y) {
        super(id, x, y);
    }

    @Override
    public String toString() {
        return "Depot{" +
                super.toString() +
                "}";
    }

    public static void main(String[] args) {
        Depot d1 = new Depot(1, 2,3);
        Depot d2 = new Depot(2, 6,8);
        System.out.println(d1);
    }
}
