import java.util.ArrayList;
import java.util.Stack;

/**
 * Classe définissant une pile de cartes.
 * 
 * @author Les Bornés
 */
public class PileCartes extends Stack<Carte> {

    public PileCartes() {
        super();
    }

    /**
     * Méthode ajoutée pour les tests : alimente une pile de cartes.
     * 
     * @param ArrayList<Carte> cartes
     */
    protected void alimenter(ArrayList<Carte> cartes) {
        for (int i = 0; i < cartes.size(); i++) {
            if (cartes.get(i).estValide()) {
                this.push(cartes.get(i));
            }
        }
    }

}
