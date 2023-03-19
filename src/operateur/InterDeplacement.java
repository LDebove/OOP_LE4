package operateur;

import instance.reseau.Client;
import solution.Tournee;

public class InterDeplacement extends OperateurInterTournees {

    public InterDeplacement() {
        super();
    }

    public InterDeplacement(Tournee tournee, int positionClient1, Tournee tournee2, int positionClient2) {
        super(tournee, positionClient1, tournee2, positionClient2);
    }

    @Override
    protected boolean doMouvement() {
        return this.tournee.doDeplacement(this);
    }

    @Override
    public int evalDeltaCoutTournee() {
        if(this.tournee == null || !this.isInterDeplacementValide()) return Integer.MAX_VALUE;
        return this.tournee.deltaCoutSupression(positionClient1);
    }

    @Override
    public int evalDeltaCoutTournee2() {
        if(this.tournee2 == null || !this.isInterDeplacementValide()) return Integer.MAX_VALUE;
        return this.tournee2.deltaCoutInsertionInter(positionClient2, this.client1);
    }

    private boolean isInterDeplacementValide() {
        if(tournee2.getDemandeTotale() + client1.getDemande() > tournee2.getCapacite()) return false;
        return true;
    }

    @Override
    public String toString() {
        return "InterDeplacement{" +
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
