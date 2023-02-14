package solution;

import instance.Instance;
import instance.reseau.Client;
import instance.reseau.Depot;
import io.InstanceReader;
import io.exception.ReaderException;

import java.util.LinkedList;

public class Tournee {
    private final int capacite; // capacité du véhicule
    private int demandeTotale; // demande cumulée de tous les clients de la tournée
    private int coutTotal; // coût total de toutes les routes de la tournée
    private final Depot depot; // point de départ et d'arrivée
    private LinkedList<Client> clients; // liste ordonnée des clients à livrer durant la tournée

    public Tournee(Instance instance) {
        this.capacite = instance.getCapacite();
        this.depot = instance.getDepot();
        this.clients = new LinkedList<>();
        this.demandeTotale = 0;
        this.coutTotal = 0;
    }

    public boolean ajouterClient(Client clientToAdd) {
        if(this.demandeTotale + clientToAdd.getDemande() > this.capacite) return false;
        this.demandeTotale += clientToAdd.getDemande();
        if(this.clients.size() == 0) { // il n'y a pas encore de client dans la tournée
            this.coutTotal += 2 * clientToAdd.getCoutVers(this.depot); // 2x pour aller-retour
        } else { // il y a déjà un client dans la tournée
            // enlever le cout entre le dernier client et le depot
            // ajouter le cout entre le dernier client et le nouveau client
            // ajouter le cout entre le nouveau client et le dépôt
            this.coutTotal -= this.clients.getLast().getCoutVers(this.depot);
            this.coutTotal += this.clients.getLast().getCoutVers(clientToAdd);
            this.coutTotal += clientToAdd.getCoutVers(this.depot);
        }
        this.clients.add(clientToAdd);
        return true;
    }

    public boolean check() {
        if(!checkDemandeTotale()) {
            System.out.println("Tournée irréalisable: demande totale incorrecte");
            return false;
        }
        if(!this.checkCoutTotal()) {
            System.out.println("Tournée irréalisable: coût total incorrect");
            return false;
        }
        if(this.demandeTotale > this.capacite) {
            System.out.println("Tournée irréalisable: demande totale supérieure à la capacité");
            return false;
        }
        return true;
    }

    private boolean checkCoutTotal() {
        int coutTotal = 0;
        if(this.clients.size() == 1) { // si il n'y a qu'un seul client dans la tournée
            coutTotal += 2 * this.depot.getCoutVers(this.clients.getFirst());
        } else { // si il y a plus d'un client dans la tournée
            coutTotal += this.depot.getCoutVers(this.clients.getFirst());
            for(int i = 1; i < this.clients.size(); i++) {
                coutTotal += this.clients.get(i).getCoutVers(this.clients.get(i - 1));
            }
            coutTotal += this.clients.getLast().getCoutVers(this.depot);
        }
        if(coutTotal != this.coutTotal) return false;
        return true;
    }

    private boolean checkDemandeTotale() {
        int demandeTotale = 0;
        for(Client c : this.clients) {
            demandeTotale += c.getDemande();
        }
        if(demandeTotale != this.demandeTotale) return false;
        return true;
    }

    public int getCapacite() {
        return capacite;
    }

    public int getDemandeTotale() {
        return demandeTotale;
    }

    public int getCoutTotal() {
        return coutTotal;
    }

    public Depot getDepot() {
        return depot;
    }

    public LinkedList<Client> getClients() {
        return (LinkedList<Client>) this.clients.clone();
    }

    @Override
    public String toString() {
        return "Tournee{" +
                "capacite=" + capacite +
                ", demandeTotale=" + demandeTotale +
                ", coutTotal=" + coutTotal +
                ", depot=" + depot +
                ", clients=" + clients +
                '}';
    }

    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("instances/A-n32-k5.vrp");
            Instance i1 = reader.readInstance();
            Tournee t1 = new Tournee(i1);
            for(Client client : i1.getClients()) {
                t1.ajouterClient(client);
            }
            System.out.println(t1.getCoutTotal());
            System.out.println(t1.getDemandeTotale());
            System.out.println(t1);
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
