package solveur;

import instance.Instance;
import instance.reseau.Client;
import io.InstanceReader;
import io.exception.ReaderException;
import operateur.InsertionClient;
import solution.Solution;

import java.util.LinkedList;
import java.util.List;

public class MeilleureInsertion implements Solveur {

    public String getNom() {
        return "Meilleure Insertion";
    }

    public Solution solve(Instance instance) {
        Solution solution = new Solution(instance);
        LinkedList<Client> clients = instance.getClients();
        while(!clients.isEmpty()) {
            InsertionClient bestInsertion = this.getMeilleureInsertion(solution, clients);
            if(solution.doInsertion(bestInsertion)) {
                clients.remove(bestInsertion.getClient());
            } else {
                Client client = clients.getFirst();
                if(!solution.ajouterClientNouvelleTournee(client)) return null;
                clients.remove(client);
            }
        }
        return solution;
    }

    private InsertionClient getMeilleureInsertion(Solution solution, List<Client> clients) {
        InsertionClient best = new InsertionClient();
        InsertionClient ic;
        for(Client client : clients) {
            ic = solution.getMeilleureInsertion(client);
            if(ic.isMeilleur(best)) {
                best = ic;
            }
        }
        return best;
    }

    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("instances/A-n32-k5.vrp");
            Instance i1 = reader.readInstance();
            MeilleureInsertion mi = new MeilleureInsertion();
            Solution s1 = mi.solve(i1);
            System.out.println(s1);
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
