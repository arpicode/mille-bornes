/**
 * Classe définissant une carte du jeu.
 * 
 * @author Les Bornés
 */
public class Carte {
    public static final int ETAPE = 0;
    public static final int ATTAQUE = 1;
    public static final int DEFENSE = 2;
    public static final int BOTTE = 3;
    public static final int METEO = 4;

    private static final String[] typesDesCartes = { "Étape", "Attaque", "Défense", "Botte", "Météo" };
    private static final String[][] nomsDesCartes = new String[][] {
            // nomDesCartes[ETAPE] => Etapes
            { "25", "50", "75", "100", "200" },
            // nomDesCartes[ATTAQUE] => Attaques
            { "Stop", "Limite de Vitesse", "Panne d'Essence", "Crevé", "Accident" },
            // nomDesCartes[DEFENSE] => Défenses
            { "Roulez", "Fin de Limite de Vitesse", "Essence", "Roue de Secours", "Réparation" },
            // nomDesCartes[BOTTE] => Bottes
            { "Prioritaire", "Citerne", "Increvable", "As du Volant" },
            // nomDesCartes[METEO] => Meteo
            { "Neige", "Beau Temps", "Vent dans le Dos" }
    };

    private String nom;
    private int type;

    public Carte(String nom) {
        initialiserCarte(nom);
    }

    /**
     * Initialise le nom et le type d'une carte. Si le nom en entrée n'est pas
     * un nom de carte qui existe le type de la carte sera -1.
     * 
     * @param nom String nom de la carte insensible à la casse.
     * @return le résultat de l'initialisation.
     */
    private boolean initialiserCarte(String nom) {
        for (int type = 0; type < nomsDesCartes.length; type++) {
            for (int i = 0; i < nomsDesCartes[type].length; i++) {
                if (nom.compareToIgnoreCase(nomsDesCartes[type][i]) == 0) {
                    this.nom = nomsDesCartes[type][i];
                    this.type = type;
                    return true;
                }
            }
        }
        this.type = -1;
        return false;
    }

    /**
     * Teste si la carte est valide ou pas.
     * 
     * @return boolean
     */
    public boolean estValide() {
        return this.type != -1;
    }

    public String getNom() {
        return nom;
    }

    public int getType() {
        return type;
    }

    /**
     * @return String représentant la carte.
     */
    public String toString() {
        if (this.estValide()) {
            return "[" + typesDesCartes[this.type] + "] " + this.nom;
        }

        return "La carte '" + this.nom + "' n'est pas valide!";
    }
}
