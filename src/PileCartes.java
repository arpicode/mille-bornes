import java.util.Stack;

/**
 * Classe définissant une pile de cartes.
 * 
 * @author Les Bornés
 */
public class PileCartes extends Stack<Carte> {

    /**
     * Constructeur qui permet d'instancier une pile de cartes.
     */
    public PileCartes() {
        super();
    }

    /**
     * Permet de savoir si la pile de carte contient une carte par son nom.
     * 
     * @param nom Nom de la carte.
     * @return
     */
    public boolean contient(String nom) {
        for (Carte carte : this) {
            if (carte.getNom().compareTo(nom) == 0)
                return true;
        }

        return false;
    }

    /**
     * Compte le nombre de cartes présentent dans la pile de cartes d'après son
     * nom.
     * 
     * @param nom Nom de la carte à compter.
     * @return Nombre d'occurences de la carte.
     */
    public int nombreCartes(String nom) {
        int cpt = 0;

        for (Carte carte : this) {
            if (carte.getNom().compareTo(nom) == 0)
                cpt++;
        }

        return cpt;
    }

}
