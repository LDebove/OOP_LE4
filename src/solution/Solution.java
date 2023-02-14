package solution;

import instance.Instance;
import instance.reseau.Client;
import io.InstanceReader;
import io.exception.ReaderException;

import java.util.LinkedList;

public class Solution {
    private int coutTotal;
    private Instance instance;
    private LinkedList<Tournee> tournees;

    public Solution(Instance instance) {
        this.instance = instance;
        this.tournees = new LinkedList<>();
        this.coutTotal = 0;
    }

    public void calcSolution(Instance instance) {
        Tournee tournee = new Tournee(instance);
        for(Client client : instance.getClients()) {
            boolean result = tournee.ajouterClient(client);
            if(!result) {
                this.tournees.add(tournee);
                tournee = new Tournee(instance);
                tournee.ajouterClient(client);
            }
        }
        this.tournees.add(tournee);
        this.coutTotal = 0;
        for(Tournee t : this.tournees) {
            this.coutTotal += t.getCoutTotal();
        }
    }

    public int getCoutTotal() {
        return coutTotal;
    }

    public Instance getInstance() {
        return instance;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "coutTotal=" + coutTotal +
                //", instance=" + instance +
                ", tournees=" + tournees +
                '}';
    }

    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("instances/A-n32-k5.vrp");
            Instance i1 = reader.readInstance();
            Solution s1 = new Solution(i1);
            System.out.println(s1);
            s1.calcSolution(i1);
            System.out.println(s1);
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
