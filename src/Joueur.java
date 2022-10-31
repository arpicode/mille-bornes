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
    private int age;

    public Joueur(String nom) {
        this.id = nbJoueur;
        this.nom = nom;
        this.main = new ArrayList<Carte>();
        this.zoneDeJeu = new ZoneDeJeu();
        nbJoueur++;
    }

    public abstract void jouerTour(ArrayList<Joueur> joueurs);

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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
        String result = "  [id: " + this.id;
        result += "  nom: " + this.nom;
        result += "  âge: " + this.age + "]\n";

        result += "    main: ";
        for (Carte carte : main) {
            result += "[" + carte + "]" + " ";
        }
        return result;
    }
}
