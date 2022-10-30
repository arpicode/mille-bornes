/**
 * Classe définissant une carte du jeu.
 * 
 * @author Les Bornés
 */
public class Carte {
    public static final int ETAPE = 0;
    public static final int ATTAQUE = 1;
    public static final int PARADE = 2;
    public static final int BOTTE = 3;
    public static final int METEO = 4;

    private static final String[] typesDesCartes = { "Étape", "Attaque", "Parade", "Botte", "Météo" };

    /*
     * L'ordre des cartes attaques, parade et bottes est important par exemple
     * Stop est à l'indice 0 et sa parade qui est Roulez est aussi à l'indice 0.
     * Il y a un cas particulier avec la botte Prioritaire qui contre à la fois
     * Stop et Limite de Vitesse.
     */
    private static final String[][] nomsDesCartes = new String[][] {
            // nomDesCartes[ETAPE] => tableau avec des Etapes
            { "25", "50", "75", "100", "200" },
            // nomDesCartes[ATTAQUE] => tableau avec des Attaques
            { "Stop", "Limite de Vitesse", "Panne d'Essence", "Crevé", "Accident" },
            // nomDesCartes[PARADE] => tableau avec des Parades
            { "Roulez", "Fin de Limite de Vitesse", "Essence", "Roue de Secours", "Réparation" },
            // nomDesCartes[BOTTE] => tableau avec des Bottes
            { "Prioritaire", "Citerne", "Increvable", "As du Volant" },
            // nomDesCartes[METEO] => tableau avec des Meteos
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

        this.nom = nom;
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

    public String[] getTypesCarte() {
        return typesDesCartes;
    }

    /**
     * @return String représentant la carte.
     */
    public String toString() {
        if (this.estValide()) {
            String couleurCarte = "";
            switch (this.type) {
                case ETAPE:
                    couleurCarte = Affichage.Color.CYAN;
                    break;
                case ATTAQUE:
                    couleurCarte = Affichage.Color.LIGHT_RED;
                    break;
                case PARADE:
                    couleurCarte = Affichage.Color.LIGHT_GREEN;
                    break;
                case BOTTE:
                    couleurCarte = Affichage.Color.LIGHT_MAGENTA;
                    break;
            }
            return couleurCarte + this.nom + Affichage.Color.END;
        }

        return "La carte '" + this.nom + "' n'est pas valide!";
    }
}
