package operateur;

import solution.Tournee;

public abstract class OperateurIntraTournee extends OperateurLocal {

    public OperateurIntraTournee() {
        super();
    }

    public OperateurIntraTournee(Tournee tournee, int positionClient1, int positionClient2) {
        super(tournee, positionClient1, positionClient2);
        this.cout = this.evalDeltaCout();
    }
}
