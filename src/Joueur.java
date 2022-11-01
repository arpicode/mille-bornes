import java.util.ArrayList;
import java.util.EnumMap;

/**
 * Classe abstraite définissant un joueur.
 * 
 * @author Les Bornés
 */
public abstract class Joueur {
    public enum Zone {
        VITESSE,
        BATAILLE,
        BOTTE,
        METEO,
        ETAPE,
    };

    private static int nbJoueur; // Compteur du nombre d'instance de Joueur

    private int id;
    private String nom;
    private ArrayList<Carte> main;
    private EnumMap<Zone, PileCartes> zoneDeJeu;
    private int age;

    public Joueur(String nom) {
        this.id = nbJoueur;
        this.nom = nom;
        this.main = new ArrayList<Carte>();
        this.zoneDeJeu = initialiseZoneDeJeu();
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

    public EnumMap<Zone, PileCartes> getZoneDeJeu() {
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
        result += "\n    zones de jeu: " + zoneDeJeu;
        return result;
    }

    private EnumMap<Zone, PileCartes> initialiseZoneDeJeu() {
        EnumMap<Zone, PileCartes> result = new EnumMap<Zone, PileCartes>(Zone.class);

        for (Zone z : Zone.values()) {
            result.put(z, new PileCartes());
        }

        // ------ juste pour tester tous les joueurs créés auront la même main.
        result.get(Zone.ETAPE).push(new Carte("25"));
        result.get(Zone.ETAPE).push(new Carte("100"));
        result.get(Zone.ETAPE).push(new Carte("50"));

        result.get(Zone.VITESSE).push(new Carte("Limite de Vitesse"));

        result.get(Zone.BATAILLE).push(new Carte("Feu Rouge"));
        result.get(Zone.BATAILLE).push(new Carte("Feu Vert"));
        result.get(Zone.BATAILLE).push(new Carte("Crevé"));

        result.get(Zone.BOTTE).push(new Carte("as du volant"));
        // ------
        return result;
    }
}
