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
     * Mélange la pioche.
     */
    public void melanger() {
        Collections.shuffle(this);
    }

}
