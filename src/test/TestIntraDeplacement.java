package test;

import instance.Instance;
import instance.reseau.Client;
import instance.reseau.Depot;
import operateur.OperateurLocal;
import operateur.TypeOperateurLocal;
import solution.Tournee;

public class TestIntraDeplacement {

    public static void main(String[] args) {
        int id = 1;
        Depot d = new Depot(id++, 0, 0);
        Instance inst = new Instance("test", 100, d);
        Client c1 = new Client(id++, 5, 0, 10);
        Client c2 = new Client(id++, 10, 0, 10);
        Client c3 = new Client(id++, 40, 0, 10);
        Client c4 = new Client(id++, 5, 0, 10);
        Client c5 = new Client(id++, 20, 0, 10);
        inst.ajouterClient(c1);
        inst.ajouterClient(c2);
        inst.ajouterClient(c3);
        inst.ajouterClient(c4);
        inst.ajouterClient(c5);
        Tournee t = new Tournee(inst);
        t.ajouterClient(c1);
        t.ajouterClient(c2);
        t.ajouterClient(c3);
        t.ajouterClient(c4);
        t.ajouterClient(c5);
        System.out.println(t.deltaCoutDeplacement(-1, 5)); // infini
        System.out.println(t.deltaCoutDeplacement(1, -5)); // infini
        System.out.println(t.deltaCoutDeplacement(1, 6)); // infini
        System.out.println(t.deltaCoutDeplacement(1, 4)); // 0
        System.out.println(t.deltaCoutDeplacement(1, 1)); // infini
        System.out.println(t.deltaCoutDeplacement(1, 2)); // infini
        System.out.println(t.deltaCoutDeplacement(3, 0)); // -30
        System.out.println(t.deltaCoutDeplacement(4, 2)); // -30

        OperateurLocal op1 = OperateurLocal.getOperateur(TypeOperateurLocal.INTRA_DEPLACEMENT);
        OperateurLocal op2 = OperateurLocal.getOperateurIntra(TypeOperateurLocal.INTRA_DEPLACEMENT, t, 1, 4);
        OperateurLocal op3 = OperateurLocal.getOperateurIntra(TypeOperateurLocal.INTRA_DEPLACEMENT, t, 3, 0);
        OperateurLocal op4 = OperateurLocal.getOperateurIntra(TypeOperateurLocal.INTRA_DEPLACEMENT, t, -1, 4);
        System.out.println(op1.isMouvementRealisable()); // 0
        System.out.println(op2.isMouvementRealisable()); // 1
        System.out.println(op3.isMouvementRealisable()); // 1
        System.out.println(op4.isMouvementRealisable()); // 0
        System.out.println(op2.isMouvementAmeliorant()); // 0
        System.out.println(op3.isMouvementAmeliorant()); // 1
        OperateurLocal best = t.getMeilleurOperateurIntra(TypeOperateurLocal.INTRA_DEPLACEMENT);
        System.out.println(best);
        System.out.println(best.doMouvementIfRealisable());
        System.out.println(t);
    }
}
