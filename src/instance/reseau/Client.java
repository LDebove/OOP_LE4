package instance.reseau;

public class Client extends Point {

    private int demande;
    public Client(int id, int x, int y, int demande) {
        super(id, x, y);
        if(demande > 0) {
            this.demande = demande;
        }
    }

    public int getDemande() {
        return demande;
    }

    @Override
    public String toString() {
        return "Client{" +
                "demande=" + demande +
                super.toString() +
                '}';
    }

    public static void main(String[] args) {
        Client c1 = new Client(1,2,3, 1);
        Client c2 = new Client(2,3,4, 2);
        System.out.println(c1);
    }
}
