import java.util.ArrayList;

public class JoueurHumain extends Joueur {
    // TODO à implémenter.
    public JoueurHumain(String nom, int age) {
        super(nom);
        this.setAge(age);
    }

    /**
     * Déroulement d'un tour de jeu du joueur.
     */
    public void jouerTour(ArrayList<Joueur> joueurs) {
        System.out.println("le joueur [" + this.getId() + "] " + this.getNom() + " est en train de jouer.");
    }

}
