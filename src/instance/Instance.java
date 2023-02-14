package instance;

import instance.reseau.Client;
import instance.reseau.Depot;
import io.InstanceReader;
import io.exception.ReaderException;

import java.util.*;

public class Instance {
    private String nom;
    private int capacite;
    private Depot depot;
    private Map<Integer, Client> clients;

    public Instance(String nom, int capacite, Depot depot) {
        this.nom = nom;
        this.capacite = capacite;
        this.depot = depot;
        this.clients = new LinkedHashMap<>();
    }

    /**
     * renvoie le nombre de clients de l'instance
     * @return int
     */
    public int getNbClients() {
        return this.clients.size();
    }


    public Client getClientById(int id) {
        return this.clients.get(id);
    }

    public LinkedList<Client> getClients() {
        return new LinkedList<Client>(this.clients.values());
    }

    public boolean ajouterClient(Client clientToAdd) {
        if(clientToAdd == null) return false;
        for(Map.Entry clientEntry : this.clients.entrySet()) {
            Client client = (Client) clientEntry.getValue();
            clientToAdd.ajouterRoute(client);
            client.ajouterRoute(clientToAdd);
        }
        clientToAdd.ajouterRoute(this.depot);
        this.depot.ajouterRoute(clientToAdd);
        this.clients.put(clientToAdd.getId(), clientToAdd);
        return true;
    }

    //region Getters and Setters
    public String getNom() {
        return nom;
    }

    public int getCapacite() {
        return capacite;
    }

    public Depot getDepot() {
        return depot;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public void setDepot(Depot depot) {
        this.depot = depot;
    }
    //endregion

    //region Overrides
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instance instance = (Instance) o;
        return capacite == instance.capacite && nom.equals(instance.nom) && depot.equals(instance.depot) && clients.equals(instance.clients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, capacite, depot, clients);
    }

    @Override
    public String toString() {
        return "Instance{" +
                "nom='" + nom + '\'' +
                ", capacite=" + capacite +
                ", depot=" + depot +
                ", clients=" + clients +
                '}';
    }
    //endregion

    public static void main(String[] args) {
        Instance i1 = new Instance("truc", 100, new Depot(1,2,3));
        System.out.println(i1);
        Client c1 = new Client(1,2,3,4);
        Client c2 = new Client(2,15,36,7);
        i1.ajouterClient(c1);
        i1.ajouterClient(c2);
        System.out.println(i1.getClients().toString());
        i1.ajouterClient(c2);
        System.out.println(i1.getClients().toString());

        try {
            InstanceReader reader = new InstanceReader("instances/A-n32-k5.vrp");
            Instance i2 = reader.readInstance();
            System.out.println(i2.toString());
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
