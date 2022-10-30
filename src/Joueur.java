import java.util.ArrayList;

/**
 * Classe abstraite définissant un joueur.
 * 
 * @author Les Bornés
 */
public abstract class Joueur {
    private static int nbJoueur; // Compteur du nombre d'instance de Joueur
    private int id;
    private String nom;
    private ArrayList<Carte> main;
    private ZoneDeJeu zoneDeJeu;

    public Joueur(int id, String nom) {
        nbJoueur++;
        this.id = id;
        this.nom = nom;
        this.main = new ArrayList<Carte>();
        this.zoneDeJeu = new ZoneDeJeu();
    }

    public int getId() {
        return id;
    }

    public static int getNbJoueurs() {
        return nbJoueur;
    }

    public ArrayList<Carte> getMain() {
        return main;
    }

    public ZoneDeJeu getZoneDeJeu() {
        return zoneDeJeu;
    }

    public String toString() {
        return this.nom;
    }
}
