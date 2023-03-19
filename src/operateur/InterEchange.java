package operateur;

import instance.reseau.Client;
import solution.Tournee;

public class InterEchange extends OperateurInterTournees {

    public InterEchange() {
        super();
    }

    public InterEchange(Tournee tournee, int positionClient1, Tournee tournee2, int positionClient2) {
        super(tournee, positionClient1, tournee2, positionClient2);
    }

    @Override
    protected boolean doMouvement() {
        return this.tournee.doEchange(this);
    }

    @Override
    public int evalDeltaCoutTournee() {
        if(this.tournee == null || this.client2 == null || !this.isInterEchangeValide()) return Integer.MAX_VALUE;
        return this.tournee.deltaCoutRemplacement(positionClient1, client2);
    }

    @Override
    public int evalDeltaCoutTournee2() {
        if(this.tournee2 == null || this.client1 == null || !this.isInterEchangeValide()) return Integer.MAX_VALUE;
        return this.tournee2.deltaCoutRemplacement(positionClient2, client1);
    }

    private boolean isInterEchangeValide() {
        if(tournee.getDemandeTotale() - client1.getDemande() + client2.getDemande() > tournee.getCapacite()
        || tournee2.getDemandeTotale() - client2.getDemande() + client1.getDemande() > tournee2.getCapacite()) return false;
        return true;
    }

    @Override
    public String toString() {
        return "InterEchange{" +
                "tournee2=" + tournee2 +
                ", deltaCoutTournee1=" + deltaCoutTournee1 +
                ", deltaCoutTournee2=" + deltaCoutTournee2 +
                ", positionClient1=" + positionClient1 +
                ", positionClient2=" + positionClient2 +
                ", client1=" + client1 +
                ", client2=" + client2 +
                ", tournee=" + tournee +
                ", cout=" + cout +
                '}';
    }
}
