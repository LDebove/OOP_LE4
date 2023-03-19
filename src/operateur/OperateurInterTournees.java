package operateur;

import solution.Tournee;

public abstract class OperateurInterTournees extends OperateurLocal {
    protected Tournee tournee2;
    protected int deltaCoutTournee1;
    protected int deltaCoutTournee2;

    public OperateurInterTournees() {
        super();
        this.tournee2 = null;
        this.deltaCoutTournee1 = 0;
        this.deltaCoutTournee2 = 0;
    }

    public OperateurInterTournees(Tournee tournee, int positionClient1, Tournee tournee2, int positionClient2) {
        super(tournee, positionClient1, positionClient2);
        this.tournee2 = tournee2;
        this.deltaCoutTournee1 = 0;
        this.deltaCoutTournee2 = 0;
        this.client2 = tournee2.getClient(positionClient2);
        this.cout = this.evalDeltaCout();
    }

    public int evalDeltaCout() {
        this.deltaCoutTournee1 = this.evalDeltaCoutTournee();
        this.deltaCoutTournee2 = this.evalDeltaCoutTournee2();
        if(this.deltaCoutTournee1 == Integer.MAX_VALUE || this.deltaCoutTournee2 == Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return this.deltaCoutTournee1 + this.deltaCoutTournee2;
    }

    public abstract int evalDeltaCoutTournee();
    public abstract int evalDeltaCoutTournee2();

    public Tournee getTournee2() {
        return tournee2;
    }

    public int getDeltaCoutTournee1() {
        return deltaCoutTournee1;
    }

    public int getDeltaCoutTournee2() {
        return deltaCoutTournee2;
    }

    @Override
    public String toString() {
        return "OperateurInterTournees{" +
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
