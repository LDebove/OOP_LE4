package operateur;

import instance.reseau.Client;
import solution.Tournee;

public abstract class Operateur {
    protected Tournee tournee;
    protected int cout;

    public Operateur() {
        this.cout = Integer.MAX_VALUE;
    }

    public Operateur(Tournee t) {
        this.tournee = t;
        this.cout = Integer.MAX_VALUE;
    }

    public int getDeltaCout() {
        return this.cout;
    }

    public boolean isMouvementRealisable() {
        return this.cout < Integer.MAX_VALUE;
    }

    public boolean isMeilleur(Operateur op) {
        return this.cout < op.getDeltaCout();
    }

    protected abstract int evalDeltaCout();

    protected abstract boolean doMouvement();

    public boolean isMouvementAmeliorant() {
        return this.cout < 0;
    }

    public boolean doMouvementIfRealisable() {
        if(this.isMouvementRealisable()) return doMouvement();
        return false;
    }

    @Override
    public String toString() {
        return "Operateur{" +
                "tournee=" + tournee +
                ", cout=" + cout +
                '}';
    }
}
