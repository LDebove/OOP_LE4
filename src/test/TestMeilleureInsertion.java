package test;

import instance.Instance;
import instance.reseau.Client;
import instance.reseau.Depot;
import instance.reseau.Point;
import operateur.InsertionClient;
import operateur.Operateur;
import solution.Tournee;

public class TestMeilleureInsertion {
    public static void main(String[] args) {
        int id = 1;
        Depot d = new Depot(id++, 0, 0);
        Instance inst = new Instance("test", 100, d);
        Client c1 = new Client(id++, 5, 0, 10);
        Client c2 = new Client(id++, 10, 0, 10);
        Client c3 = new Client(id++, 20, 0, 10);
        Client cIns = new Client(id++, 15, 0, 10);
        inst.ajouterClient(c1);
        inst.ajouterClient(c2);
        inst.ajouterClient(c3);
        inst.ajouterClient(cIns);
        Tournee t = new Tournee(inst);
        t.ajouterClient(c1);
        t.ajouterClient(c2);
        t.ajouterClient(c3);
        InsertionClient ic = t.getMeilleureInsertion(cIns);
        System.out.println("mouvement réalisable ? " + ic.doMouvementIfRealisable());

        System.out.println(t);
    }
}
