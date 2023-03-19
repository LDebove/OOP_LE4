package solution;

import instance.Instance;
import instance.reseau.Client;
import instance.reseau.Depot;
import io.InstanceReader;
import io.exception.ReaderException;
import operateur.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Solution {
    private int coutTotal;
    private final Instance instance;
    private LinkedList<Tournee> tournees;

    public Solution(Instance instance) {
        this.instance = instance;
        this.tournees = new LinkedList<>();
        this.coutTotal = 0;
    }

    public boolean ajouterClientNouvelleTournee(Client clientToAdd) {
        Tournee tournee = new Tournee(this.instance);
        boolean result = tournee.ajouterClient(clientToAdd);
        if(!result) return false;
        this.tournees.add(tournee);
        this.coutTotal += tournee.getCoutTotal();
        return true;
    }

    public boolean ajouterClientTourneeExistante(Client clientToAdd) {
        boolean result = false;
        int _coutTotal = 0;
        Tournee tourneeAjout = new Tournee(new Instance("temp", 0, new Depot(0, 0, 0)));
        for(Tournee t : this.tournees) {
            tourneeAjout = t;
            _coutTotal = t.getCoutTotal();
            result = t.ajouterClient(clientToAdd);
            if(result) break;
        }
        if(result) {
            this.coutTotal += tourneeAjout.getCoutTotal() - _coutTotal;
            return true;
        }
        return false;
    }

    public boolean ajouterClientDerniereTournee(Client clientToAdd) {
        if(this.tournees.isEmpty()) return false;
        Tournee last = this.tournees.getLast();
        int _coutTotal = last.getCoutTotal();
        boolean result = last.ajouterClient(clientToAdd);
        if(!result) return false;
        this.coutTotal = this.calcCoutTotal();
        return true;
    }

    public void calcSolution(Instance instance) {
        for(Client client : instance.getClients()) {
            boolean result = this.ajouterClientTourneeExistante(client);
            if(!result) {
                this.ajouterClientNouvelleTournee(client);
            }
        }
    }

    public InsertionClient getMeilleureInsertion(Client clientToInsert) {
        InsertionClient bestInsertion = new InsertionClient();
        for(Tournee t: this.tournees) {
            InsertionClient o = t.getMeilleureInsertion(clientToInsert);
            if(o != null && o.isMouvementRealisable() && o.isMeilleur(bestInsertion)) {
                bestInsertion = o;
            }
        }
        return bestInsertion;
    }

    public boolean doInsertion(InsertionClient infos) {
        boolean result = infos.doMouvementIfRealisable();
        if (result) this.coutTotal += infos.getDeltaCout();
        return result;
    }

    private int calcCoutTotal() {
        int coutTotal = 0;
        for(Tournee t : this.tournees) {
            coutTotal += t.getCoutTotal();
        }
        return coutTotal;
    }

    public boolean check() {
        for(Tournee t : this.tournees) {
            if(!t.check()) { // check de chaque tournee
                System.out.println("Solution irréalisable: tournée invalide");
                return false;
            }
        }
        // coût total de la solution est correctement calculé
        if(!checkCoutTotal()) {
            System.out.println("Solution irréalisable: coût total incorrect");
            return false;
        }
        ArrayList<Client> clientsTournees = this.getClientsTournees();
        // check si tous les clients sont présents dans exactement une seule des tournées de la solution
        if(!checkClients(clientsTournees)) {
            System.out.println("Solution irréalisable: client présent dans 2 tournées");
            return false;
        }
        // check la présence de chaque client dans la solution
        if(!checkPresenceClients(clientsTournees)) {
            System.out.println("Solution irréalisable: client non présent dans la solution");
            return false;
        }
        return true;
    }

    private ArrayList<Client> getClientsTournees() {
        ArrayList<Client> clients = new ArrayList<>();
        for(Tournee t : this.tournees) {
            for(Client c : t.getClients()) {
                clients.add(c);
            }
        }
        return clients;
    }

    private boolean checkCoutTotal() {
        int coutTotal = 0;
        for(Tournee t : this.tournees) {
            coutTotal += t.getCoutTotal();
        }
        if(coutTotal != this.coutTotal) {
            System.out.println("Coût total attendu: " + coutTotal + ", coût total calculé: " + this.coutTotal);
            return false;
        }
        return true;
    }

    private boolean checkClients(ArrayList<Client> clientsTournees) {
        // check si tous les clients sont présents dans exactement une seule des tournées de la solution
        Map<Integer, Client> clients = new HashMap<>();
        for(Client client : clientsTournees) {
            if(clients.get(client.getId()) != null) return false;
            clients.put(client.getId(), client);
        }
        return true;
    }

    private boolean checkPresenceClients(ArrayList<Client> clientsTournees) {
        Map<Integer, Client> totaliteClients = new HashMap<>();
        for(Client client : clientsTournees) {
            totaliteClients.put(client.getId(), client);
        }
        for(Client c : this.instance.getClients()) {
            if(totaliteClients.get(c.getId()) == null) return false;
        }
        return true;
    }

    private OperateurLocal getMeilleurOperateurIntra(TypeOperateurLocal type) {
        OperateurLocal best = OperateurLocal.getOperateur(type);
        for(Tournee tournee: this.tournees) {
            OperateurLocal op = tournee.getMeilleurOperateurIntra(type);
            if(op.isMeilleur(best)) {
                best = op;
            }
        }
        return best;
    }

    private OperateurLocal getMeilleurOperateurInter(TypeOperateurLocal type) {
        OperateurLocal best = OperateurLocal.getOperateur(type);
        for(Tournee tournee1: this.tournees) {
            for(Tournee tournee2: this.tournees) {
                if(tournee1 == tournee2) continue;
                OperateurLocal op = tournee1.getMeilleurOperateurInter(type, tournee2);
                if(op.isMeilleur(best)) {
                    best = op;
                }
            }
        }
        return best;
    }

    public OperateurLocal getMeilleurOperateurLocal(TypeOperateurLocal type) {
        if(OperateurLocal.getOperateur(type) instanceof OperateurIntraTournee) {
            return this.getMeilleurOperateurIntra(type);
        } else if(OperateurLocal.getOperateur(type) instanceof OperateurInterTournees) {
            return this.getMeilleurOperateurInter(type);
        }
        return null;
    }

    public boolean doMouvementRechercheLocale(OperateurLocal infos) {
        if(infos == null) return false;
        boolean result = infos.doMouvementIfRealisable();
        if(result) this.coutTotal += infos.getDeltaCout();
        if(!this.check()) {
            System.out.println(infos);
            System.out.println(this);
            System.exit(-1);
        }
        return result;
    }

    public int getCoutTotal() {
        return coutTotal;
    }

    public Instance getInstance() {
        return instance;
    }

    public LinkedList<Tournee> getTournees() {
        return (LinkedList<Tournee>) this.tournees.clone();
    }

    @Override
    public String toString() {
        return "Solution{" +
                "\n\tcoutTotal=" + coutTotal +
                ", \n\ttournees=" + tournees +
                "\n}";
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
