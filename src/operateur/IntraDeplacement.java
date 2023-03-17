package operateur;

import solution.Tournee;

public class IntraDeplacement extends OperateurIntraTournee {

    public IntraDeplacement() {
        super();
    }

    public IntraDeplacement(Tournee tournee, int positionClient1, int positionClient2) {
        super(tournee, positionClient1, positionClient2);
    }

    @Override
    protected int evalDeltaCout() {
        if(this.tournee == null) return Integer.MAX_VALUE;
        return this.tournee.deltaCoutDeplacement(this.positionClient1, this.positionClient2);
    }

    @Override
    protected boolean doMouvement() {
        return this.tournee.doDeplacement(this);
    }

    @Override
    public String toString() {
        return "IntraDeplacement{" +
                "\n\tpositionClient1=" + positionClient1 +
                ", \n\tpositionClient2=" + positionClient2 +
                ", \n\tclient1=" + client1 +
                ", \n\tclient2=" + client2 +
                ", \n\ttournee=" + tournee +
                ", \n\tcout=" + cout +
                '}';
    }
}
