/**
 * Classe principale du Mille Bornes.
 * Point d'entré du programme.
 * 
 * @author Les Bornés
 */
public class MilleBornes {
    private MilleBornes() {
    }

    public static void main(String[] args) {
        Jeu jeu = new Jeu("config.txt");
        jeu.demarrer();
    }

}
