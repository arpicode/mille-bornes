import java.util.ArrayList;
import java.util.Collections;

/**
 * Classe fille de PileCartes représentant une pioche.
 * 
 * @author Les Bornés
 */
public class Pioche extends PileCartes {

    public Pioche() {
        super();
    }

    /**
     * Constructeur ajouté pour faciliter les tests.
     * 
     * @param ArrayList<Carte> cartes
     */
    public Pioche(ArrayList<Carte> cartes) {
        super();
        alimenter(cartes);
    }

    /**
     * Mélange la pioche.
     */
    public void melanger() {
        Collections.shuffle(this);
    }

}
