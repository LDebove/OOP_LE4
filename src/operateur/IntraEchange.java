package operateur;

import solution.Tournee;

public class IntraEchange extends OperateurIntraTournee {

    public IntraEchange() {
        super();
    }

    public IntraEchange(Tournee tournee, int positionClient1, int positionClient2) {
        super(tournee, positionClient1, positionClient2);
    }

    @Override
    protected int evalDeltaCout() {
        if(this.tournee == null) return Integer.MAX_VALUE;
        return this.tournee.deltaCoutEchange(this.positionClient1, this.positionClient2);
    }

    @Override
    protected boolean doMouvement() {
        return this.tournee.doEchange(this);
    }

    @Override
    public String toString() {
        return "IntraEchange{" +
                "positionClient1=" + positionClient1 +
                ", positionClient2=" + positionClient2 +
                ", client1=" + client1 +
                ", client2=" + client2 +
                ", tournee=" + tournee +
                ", cout=" + cout +
                '}';
    }
}
