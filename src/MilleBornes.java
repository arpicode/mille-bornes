/**
 * Classe principale du Mille Bornes.
 * Point d'entré du programme.
 */
public class MilleBornes {

    public static void main(String[] args) {
        Jeu jeu = new Jeu("config.txt");
        jeu.demarrer();
    }

}
