package solveur;

import instance.Instance;
import instance.reseau.Client;
import solution.Solution;

import java.util.LinkedList;

public class InsertionPlusProcheVoisin implements Solveur {

    public String getNom() {
        return "Insertion Plus Proche Voisin";
    }

    public Solution solve(Instance instance) {
        Solution solution = new Solution(instance);
        LinkedList<Client> clients = instance.getClients();
        Client client = clients.getFirst();
        boolean result = false;
        while(!clients.isEmpty()) {
            result = solution.ajouterClientDerniereTournee(client);
            if(!result) {
                if(!solution.ajouterClientNouvelleTournee(client)) return null;
            }
            clients.remove(client);
            client = this.getPlusProcheVoisin(client, clients);
        }
        if(!solution.check()) return null;
        return solution;
    }

    private Client getPlusProcheVoisin(Client client, LinkedList<Client> clients) {
        Client plusProcheVoisin = null;
        int distance = Integer.MAX_VALUE;
        int _distance = Integer.MAX_VALUE;
        for(Client voisin : clients) {
            _distance = voisin.getCoutVers(client);
            if(_distance < distance) {
                plusProcheVoisin = voisin;
                distance = _distance;
            }
        }
        return plusProcheVoisin;
    }
}
