package operateur;

import instance.reseau.Client;
import solution.Tournee;

public class InsertionClient extends Operateur {

    private int position;
    private Client client;

    public InsertionClient() {
        super();
    }

    public InsertionClient(Tournee tournee, Client client, int position) {
        super(tournee);
        this.position = position;
        this.client = client;
        this.cout = evalDeltaCout();
    }

    public Client getClient() {
        return client;
    }

    public int getPosition() {
        return position;
    }

    @Override
    protected int evalDeltaCout() {
        if(this.tournee == null) return Integer.MAX_VALUE;
        return this.tournee.deltaCoutInsertion(this.position, this.client);
    }

    @Override
    protected boolean doMouvement() {
        if(this.tournee == null) return false;
        return this.tournee.doInsertion(this);
    }

    @Override
    public String toString() {
        return "InsertionClient{" +
                "position=" + position +
                ", client=" + client +
                ", tournee=" + tournee +
                ", cout=" + cout +
                '}';
    }
}
