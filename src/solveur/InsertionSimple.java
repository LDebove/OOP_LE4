package solveur;

import instance.Instance;
import instance.reseau.Client;
import io.InstanceReader;
import io.exception.ReaderException;
import solution.Solution;

public class InsertionSimple implements Solveur {

    public String getNom() {
        return "Insertion Simple";
    }

    public Solution solve(Instance instance) {
        Solution solution = new Solution(instance);
        for(Client client : instance.getClients()) {
            boolean result = solution.ajouterClientTourneeExistante(client);
            if(!result) {
                solution.ajouterClientNouvelleTournee(client);
            }
        }
        if(!solution.check()) return null;
        return solution;
    }

    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("instances/A-n32-k5.vrp");
            Instance i1 = reader.readInstance();
            InsertionSimple is = new InsertionSimple();
            Solution s1 = is.solve(i1);
            System.out.println(s1);
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
