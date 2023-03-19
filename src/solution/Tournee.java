package solution;

import instance.Instance;
import instance.reseau.Client;
import instance.reseau.Depot;
import instance.reseau.Point;
import io.InstanceReader;
import io.exception.ReaderException;
import operateur.*;

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
        if(!this.isClientAjoutable(clientToAdd)) return false;
        this.demandeTotale += clientToAdd.getDemande();
        if(this.clients.size() == 0) { // il n'y a pas encore de client dans la tournée
            this.coutTotal += 2 * clientToAdd.getCoutVers(this.depot); // 2x pour aller-retour
        } else { // il y a déjà un client dans la tournée
            // enlever le cout entre le dernier client et le depot
            // ajouter le cout entre le dernier client et le nouveau client
            // ajouter le cout entre le nouveau client et le dépôt
            //this.coutTotal -= this.clients.getLast().getCoutVers(this.depot);
            this.coutTotal += this.deltaCoutInsertion(this.clients.size(), clientToAdd);
        }
        this.clients.add(clientToAdd);
        return true;
    }

    private boolean isClientAjoutable(Client clientToAdd) {
        if(clientToAdd == null) return false;
        if(this.demandeTotale + clientToAdd.getDemande() > this.capacite) return false;
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
            System.out.println("Demande totale: " + this.demandeTotale + ", capacité: " + this.capacite);
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
        if(coutTotal != this.coutTotal) {
            System.out.println("Coût total attendu: " + coutTotal + ", coût total calculé: " + this.coutTotal);
            return false;
        }
        return true;
    }

    private boolean checkDemandeTotale() {
        int demandeTotale = 0;
        for(Client c : this.clients) {
            demandeTotale += c.getDemande();
        }
        if(demandeTotale != this.demandeTotale) {
            System.out.println("Demande totale attendue: " + demandeTotale + ", Demande totale calculée: " + this.demandeTotale);
            return false;
        }
        return true;
    }

    private Point getPrec(int position) {
        if(position == 0) return this.depot;
        return this.clients.get(position - 1);
    }

    private Point getCurrent(int position) {
        if(position == this.clients.size()) return this.depot;
        return this.clients.get(position);
    }

    private Point getNext(int position) {
        if(position == this.clients.size() - 1) return this.depot;
        return this.clients.get(position + 1);
    }

    private boolean isPositionValide(int position) {
        if(position < 0 || position >= this.clients.size()) return false;
        return true;
    }

    private boolean isPositionInsertionValide(int position) {
        if(position < 0 || position > this.clients.size()) return false;
        return true;
    }

    private boolean isDeplacementValide(int positionClient1, int positionClient2) {
        if(!this.isPositionValide(positionClient1) || !this.isPositionValide(positionClient2) || Math.abs(positionClient1 - positionClient2) <= 1) return false;
        return true;
    }

    private boolean isEchangeValide(int positionClient1, int positionClient2) {
        if(!isPositionValide(positionClient1) || !isPositionValide(positionClient2) || positionClient1 >= positionClient2) return false;
        return true;
    }

    public int deltaCoutInsertion(int position, Client clientToAdd) {
        if(!this.isPositionInsertionValide(position)) return Integer.MAX_VALUE;
        Point last = this.getPrec(position);
        Point next = this.getCurrent(position);
        int cout = 0;
        cout += last.getCoutVers(clientToAdd);
        cout += clientToAdd.getCoutVers(next);
        if(last != next) cout -= last.getCoutVers(next);
        return cout;
    }

    public int deltaCoutInsertionInter(int position, Client clientToAdd) {
        if(!isClientAjoutable(clientToAdd)) return Integer.MAX_VALUE;
        return this.deltaCoutInsertion(position, clientToAdd);
    }

    public int deltaCoutSupression(int position) {
        if(!this.isPositionValide(position)) return Integer.MAX_VALUE;
        Point last = this.getPrec(position);
        Point current = this.getCurrent(position);
        Point next = this.getNext(position);
        if(last == next) {
            return - 2 * current.getCoutVers(next);
        }
        return last.getCoutVers(next) - last.getCoutVers(current) - current.getCoutVers(next);
    }

    public int deltaCoutDeplacement(int positionClient1, int positionClient2) {
        if(!isDeplacementValide(positionClient1, positionClient2)) return Integer.MAX_VALUE;
        return this.deltaCoutInsertion(positionClient2, this.getClient(positionClient1)) + this.deltaCoutSupression(positionClient1);
    }

    public int deltaCoutEchange(int positionClient1, int positionClient2) {
        if(!isEchangeValide(positionClient1, positionClient2)) return Integer.MAX_VALUE;
        if(positionClient2 - positionClient1 == 1) return this.deltaCoutEchangeConsecutif(positionClient1);
        return this.deltaCoutRemplacement(positionClient1, this.clients.get(positionClient2)) + this.deltaCoutRemplacement(positionClient2, this.clients.get(positionClient1));
    }

    private int deltaCoutEchangeConsecutif(int position) {
        Point last = this.getPrec(position);
        Point client1 = this.getCurrent(position);
        Point client2 = this.getCurrent(position + 1);
        Point next = this.getNext(position + 1);
        int cout = 0;
        cout -= last.getCoutVers(client1) + client2.getCoutVers(next);
        cout += last.getCoutVers(client2) + client1.getCoutVers(next);
        return cout;
    }

    public int deltaCoutRemplacement(int position, Client client) {
        if(position >= this.clients.size()) return Integer.MAX_VALUE;
        Point last = this.getPrec(position);
        Point current = this.getCurrent(position);
        Point next = this.getNext(position);
        if(client == last || client == next) return Integer.MAX_VALUE;
        int cout = 0;
        cout -= last.getCoutVers(current) + current.getCoutVers(next);
        cout += last.getCoutVers(client) + client.getCoutVers(next);
        return cout;
    }

    public InsertionClient getMeilleureInsertion(Client clientToInsert) {
        if(!this.isClientAjoutable(clientToInsert)) return null;
        InsertionClient bestInsertion = new InsertionClient(this, clientToInsert, 0);
        if(this.clients.size() > 0) {
            for(int pos = 0; pos <= this.clients.size() + 1; pos++) {
                InsertionClient ic = new InsertionClient(this, clientToInsert, pos);
                if(ic.isMouvementRealisable() && ic.isMeilleur(bestInsertion)) {
                    bestInsertion = ic;
                }
            }
        }
        return bestInsertion;
    }

    public OperateurLocal getMeilleurOperateurIntra(TypeOperateurLocal type) {
        OperateurLocal best = OperateurLocal.getOperateur(type);
        for(int positionClient1 = 0; positionClient1 < this.clients.size(); positionClient1++) {
            for(int positionClient2 = 0; positionClient2 < this.clients.size(); positionClient2++) {
                OperateurLocal op = OperateurLocal.getOperateurIntra(type, this, positionClient1, positionClient2);
                if(op.isMeilleur(best)) {
                    best = op;
                }
            }
        }
        return best;
    }

    public OperateurLocal getMeilleurOperateurInter(TypeOperateurLocal type, Tournee tournee2) {
        if(this == tournee2) return null;
        OperateurLocal best = OperateurLocal.getOperateur(type);
        for(int positionClient1 = 0; positionClient1 < this.clients.size(); positionClient1++) {
            for(int positionClient2 = 0; positionClient2 < tournee2.getClients().size(); positionClient2++) {
                OperateurLocal op = OperateurLocal.getOperateurInter(type, this, positionClient1, tournee2, positionClient2);
                if(op.isMeilleur(best)) {
                    best = op;
                }
            }
        }
        return best;
    }

    public boolean doInsertion(InsertionClient infos) {
        if(infos == null) return false;
        this.demandeTotale += infos.getClient().getDemande();
        this.coutTotal += infos.getDeltaCout();
        this.clients.add(infos.getPosition(), infos.getClient());
        if(!this.check()) {
            System.out.println(infos);
            System.out.println(this);
            System.exit(-1);
        }
        return true;
    }

    public boolean doDeplacement(IntraDeplacement infos) {
        if(infos == null) return false;
        this.coutTotal += infos.getDeltaCout();
        this.clients.remove(infos.getPositionClient1());
        if(infos.getPositionClient1() > infos.getPositionClient2()) {
            this.clients.add(infos.getPositionClient2(), infos.getClient1());
        } else {
            this.clients.add(infos.getPositionClient2() - 1, infos.getClient1());
        }
        if(!this.check()) {
            System.out.println(infos);
            System.out.println(this);
            System.exit(-1);
        }
        return true;
    }

    public boolean doDeplacement(InterDeplacement infos) {
        Tournee tournee2 = infos.getTournee2();
        if(infos == null || !tournee2.isClientAjoutable(infos.getClient1())) return false;
        // tournée 1
        this.coutTotal += infos.getDeltaCoutTournee1();
        this.demandeTotale -= infos.getClient1().getDemande();
        this.clients.remove(infos.getPositionClient1());
        // tournée 2
        /*InsertionClient ic = new InsertionClient(tournee2, infos.getClient1(), infos.getPositionClient2());
        tournee2.doInsertion(ic);*/
        tournee2.doDeplacementTournee2(infos);
        if(!this.check() || !tournee2.check()) {
            System.out.println(infos);
            System.out.println(this);
            System.exit(-1);
        }
        return true;
    }

    private void doDeplacementTournee2(InterDeplacement infos) {
        this.coutTotal += infos.getDeltaCoutTournee2();
        this.demandeTotale += infos.getClient1().getDemande();
        this.clients.add(infos.getPositionClient2(), infos.getClient1());
    }

    public boolean doEchange(IntraEchange infos) {
        if(infos == null) return false;
        this.coutTotal += infos.getDeltaCout();
        this.clients.set(infos.getPositionClient1(), infos.getClient2());
        this.clients.set(infos.getPositionClient2(), infos.getClient1());
        if(!this.check()) {
            System.out.println(infos);
            System.out.println(this);
            System.exit(-1);
        }
        return true;
    }

    public boolean doEchange(InterEchange infos) {
        if(infos == null) return false;
        Tournee tournee2 = infos.getTournee2();
        Client client1 = infos.getClient1();
        Client client2 = infos.getClient2();
        // tournée 1
        this.coutTotal += infos.getDeltaCoutTournee1();
        this.demandeTotale += client2.getDemande() - client1.getDemande();
        this.clients.remove(infos.getPositionClient1());
        this.clients.add(infos.getPositionClient1(), client2);
        // tournée 2
        tournee2.doEchangeTournee2(infos);
        if(!this.check() || !tournee2.check()) {
            System.out.println(infos);
            System.out.println(this);
            System.exit(-1);
        }
        return true;
    }

    private void doEchangeTournee2(InterEchange infos) {
        this.coutTotal += infos.getDeltaCoutTournee2();
        this.demandeTotale += infos.getClient1().getDemande() - infos.getClient2().getDemande();
        this.clients.remove(infos.getPositionClient2());
        this.clients.add(infos.getPositionClient2(), infos.getClient1());
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

    public Client getClient(int position) {
        if(!isPositionValide(position)) return null;
        return this.clients.get(position);
    }

    @Override
    public String toString() {
        return "Tournee{" +
                "\n\tcapacite=" + capacite +
                ", \n\tdemandeTotale=" + demandeTotale +
                ", \n\tcoutTotal=" + coutTotal +
                ", \n\tdepot=" + depot +
                ", \n\tclients=" + clients +
                "\n}";
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
