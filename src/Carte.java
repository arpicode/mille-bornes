/**
 * Classe définissant une carte du jeu.
 * 
 * @author Les Bornés
 */
public class Carte {
    public static final int TYPE_ETAPE = 0;
    public static final int TYPE_ATTAQUE = 1;
    public static final int TYPE_PARADE = 2;
    public static final int TYPE_BOTTE = 3;
    public static final int TYPE_METEO = 4;

    public static final int ETAPE_25 = 0;
    public static final int ETAPE_50 = 1;
    public static final int ETAPE_75 = 2;
    public static final int ETAPE_100 = 3;
    public static final int ETAPE_200 = 4;

    public static final int FEU_ROUGE = 0;
    public static final int LIMITE_VITESSE = 1;
    public static final int PANNE_ESSENCE = 2;
    public static final int CREVE = 3;
    public static final int ACCIDENT = 4;

    public static final int FEU_VERT = 0;
    public static final int FIN_LIMITE_VITESSE = 1;
    public static final int ESSENCE = 2;
    public static final int ROUE_SECOURS = 3;
    public static final int REPARATION = 4;

    public static final int PRIORITAIRE = 0;
    public static final int CITERNE = 1;
    public static final int INCREVABLE = 2;
    public static final int AS_VOLANT = 3;

    public static final int NEIGE = 0;
    public static final int BEAU_TEMPS = 1;
    public static final int VENT_DOS = 2;

    /*
     * L'ordre des cartes attaques, parade et bottes est important par exemple
     * Feu Rouge est à l'indice 0 et sa parade qui est Feu Vert est aussi à
     * l'indice 0. Il y a un cas particulier avec la botte Prioritaire qui
     * contre à la fois Feu Rouge et Limite de Vitesse.
     * 
     * Exemple d'utilisation pour obtenir le nom de la carte "Roue de Secours"
     * => getCartes()[TYPE_PARADE][ROUE_SECOURS]
     */
    private static final String[][] nomsDesCartes = new String[][] {
            // nomDesCartes[TYPE_ETAPE] => tableau avec des Etapes
            { "25", "50", "75", "100", "200" },
            // nomDesCartes[TYPE_ATTAQUE] => tableau avec des Attaques
            { "Feu Rouge", "Limite de Vitesse", "Panne d'Essence", "Crevé", "Accident" },
            // nomDesCartes[TYPE_PARADE] => tableau avec des Parades
            { "Feu Vert", "Fin de Limite de Vitesse", "Essence", "Roue de Secours", "Réparation" },
            // nomDesCartes[TYPE_BOTTE] => tableau avec des Bottes
            { "Prioritaire", "Citerne", "Increvable", "As du Volant" },
            // nomDesCartes[TYPE_METEO] => tableau avec des Meteos
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

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getType() {
        return type;
    }

    public static String[][] getCartes() {
        return nomsDesCartes;
    }

    /**
     * @return String représentant la carte.
     */
    public String toString() {
        if (this.estValide()) {
            String couleurCarte = "";
            switch (this.type) {
                case TYPE_ETAPE:
                    couleurCarte = Affichage.Color.CYAN;
                    break;
                case TYPE_ATTAQUE:
                    couleurCarte = Affichage.Color.LIGHT_RED;
                    break;
                case TYPE_PARADE:
                    couleurCarte = Affichage.Color.LIGHT_GREEN;
                    break;
                case TYPE_BOTTE:
                    couleurCarte = Affichage.Color.LIGHT_MAGENTA;
                    break;
            }
            return couleurCarte + this.nom + Affichage.Color.END;
        }

        return "La carte '" + this.nom + "' n'est pas valide!";
    }
}
