package operateur;

import instance.reseau.Client;
import solution.Tournee;

public abstract class OperateurLocal extends Operateur {
    protected int positionClient1;
    protected int positionClient2;
    protected Client client1;
    protected Client client2;

    public OperateurLocal() {
        super();
        this.client1 = null;
        this.client2 = null;
        this.positionClient1 = 1;
        this.positionClient2 = 1;
    }

    public OperateurLocal(Tournee tournee, int positionClient1, int positionClient2) {
        super(tournee);
        this.positionClient1 = positionClient1;
        this.positionClient2 = positionClient2;
        this.client1 = tournee.getClient(positionClient1);
        this.client2 = tournee.getClient(positionClient2);
    }

    public static OperateurLocal getOperateur(TypeOperateurLocal type) {
        switch (type) {
            case INTRA_DEPLACEMENT -> {
                return new IntraDeplacement();
            }

            case INTRA_ECHANGE -> {
                return new IntraEchange();
            }

            default -> {
                return null;
            }
        }
    }

    public static OperateurLocal getOperateurInter(TypeOperateurLocal type, Tournee tournee1, int positionClient1, Tournee tournee2, int positionClient2) {
        switch (type) {
            default -> {
                return null;
            }
        }
    }

    public static OperateurLocal getOperateurIntra(TypeOperateurLocal type, Tournee tournee, int positionClient1, int positionClient2) {
        switch (type) {
            case INTRA_DEPLACEMENT -> {
                return new IntraDeplacement(tournee, positionClient1, positionClient2);
            }

            case INTRA_ECHANGE -> {
                return new IntraEchange(tournee, positionClient1, positionClient2);
            }

            default -> {
                return null;
            }
        }
    }

    public int getPositionClient1() {
        return positionClient1;
    }

    public int getPositionClient2() {
        return positionClient2;
    }

    public Client getClient1() {
        return client1;
    }

    public Client getClient2() {
        return client2;
    }

    @Override
    public String toString() {
        return "OperateurLocal{" +
                "positionClient1=" + positionClient1 +
                ", positionClient2=" + positionClient2 +
                ", client1=" + client1 +
                ", client2=" + client2 +
                ", tournee=" + tournee +
                ", cout=" + cout +
                '}';
    }
}
