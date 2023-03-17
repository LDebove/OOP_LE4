package solveur;

import instance.Instance;
import io.InstanceReader;
import io.exception.ReaderException;
import operateur.OperateurLocal;
import operateur.TypeOperateurLocal;
import solution.Solution;

public class RechercheLocale implements Solveur{

    private Solveur solutionInitiale;

    public RechercheLocale(Solveur solutionInitiale) {
        this.solutionInitiale = solutionInitiale;
    }

    @Override
    public String getNom() {
        return "Recherche Locale";
    }

    @Override
    public Solution solve(Instance instance) {
        Solution solution = this.solutionInitiale.solve(instance);
        boolean improve = true;
        while(improve) {
            improve = false;
            OperateurLocal best = solution.getMeilleurOperateurLocal(TypeOperateurLocal.INTRA_ECHANGE);
            if(best.isMouvementAmeliorant()) {
                improve = true;
                solution.doMouvementRechercheLocale(best);
            }
        }
        return solution;
    }

    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("instances/A-n32-k5.vrp");
            Instance i1 = reader.readInstance();
            RechercheLocale rl = new RechercheLocale(new InsertionSimple());
            Solution s1 = rl.solve(i1);
            System.out.println(s1);
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
