import java.util.ArrayList;
import java.util.Collections;

/**
 * Classe fille de PileCartes représentant une pioche.
 * 
 * @author Les Bornés
 */
public class Pioche extends PileCartes {

    /**
     * Constructeur permettant d'instancier une pioche.
     */
    public Pioche() {
        super();
    }

    /**
     * Constructeur ajouté pour faciliter les tests.
     * 
     * @param ArrayList<Carte> cartes
     */
    protected Pioche(ArrayList<Carte> cartes) {
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
