package test;

import instance.Instance;
import instance.reseau.Client;
import instance.reseau.Depot;
import operateur.OperateurLocal;
import operateur.TypeOperateurLocal;
import solution.Tournee;

public class TestInterEchange {
    public static void main(String[] args) {
        int id = 1;
        Depot d = new Depot(id++, 0, 0);
        Instance inst = new Instance("test", 100, d);
        Client c1 = new Client(id++, 5, 0, 10);
        Client c2 = new Client(id++, 10, 0, 10);
        Client c3 = new Client(id++, 10, 0, 10);
        Client c4 = new Client(id++, 15, 0, 10);
        Client c5 = new Client(id++, 0, 10, 60);
        Client c6 = new Client(id++, 10, 10, 10);
        Client c7 = new Client(id++, 15, 10, 10);
        inst.ajouterClient(c1);
        inst.ajouterClient(c2);
        inst.ajouterClient(c3);
        inst.ajouterClient(c4);
        inst.ajouterClient(c5);
        inst.ajouterClient(c6);
        inst.ajouterClient(c7);
        Tournee t = new Tournee(inst);
        t.ajouterClient(c1);
        t.ajouterClient(c2);
        t.ajouterClient(c6);
        t.ajouterClient(c3);
        t.ajouterClient(c4);
        Tournee u = new Tournee(inst);
        u.ajouterClient(c5);
        u.ajouterClient(c7);

        OperateurLocal op1 = OperateurLocal.getOperateur(TypeOperateurLocal.INTER_ECHANGE);
        OperateurLocal op2 = OperateurLocal.getOperateurInter(TypeOperateurLocal.INTER_ECHANGE, t, 1, u, 4); // TODO réalisable car deltaCout = inf - 4
        OperateurLocal op3 = OperateurLocal.getOperateurInter(TypeOperateurLocal.INTER_ECHANGE, t, 3, u, 0);
        OperateurLocal op4 = OperateurLocal.getOperateurInter(TypeOperateurLocal.INTER_ECHANGE, t, -1, u, 4);

        System.out.println("réalisable ? " + op1.isMouvementRealisable());
        System.out.println("réalisable ? " + op2.isMouvementRealisable());
        System.out.println("réalisable ? " + op3.isMouvementRealisable());
        System.out.println("réalisable ? " + op4.isMouvementRealisable());
        System.out.println("réalisable ? " + op1.isMouvementAmeliorant());
        System.out.println("réalisable ? " + op2.isMouvementAmeliorant());

        OperateurLocal best = t.getMeilleurOperateurInter(TypeOperateurLocal.INTER_ECHANGE, u);
        System.out.println("best deltaCout : " + best.getDeltaCout());
        System.out.println("best réalisé ? " + best.doMouvementIfRealisable());
    }
}
