import java.util.ArrayList;
import java.util.EnumMap;
import java.util.regex.Pattern;

/**
 * Classe abstraite définissant un joueur.
 * 
 * @author Les Bornés
 */
public abstract class Joueur {
    /**
     * Enumeration des différentes zones contenues dans la zone de jeu d'un
     * joueur.
     */
    public static enum Zone {
        VITESSE,
        BATAILLE,
        BOTTE,
        METEO,
        ETAPE,
    };

    private static int nbJoueur; // Compteur du nombre d'instance de Joueur.

    private int id; // Identifiant unique du joueur.
    private String nom; // Nom du joueur.
    private ArrayList<Carte> main; // Les cartes dans la main du joueur.
    private EnumMap<Zone, PileCartes> zoneDeJeu; // La zone de jeu du joueur.
    private int age; // L'âge du joueur.
    private int kmParcourus; // Les kilomètre parcourus du joueur.
    private int score; // Le score final du joueur.
    private boolean estMeteoResolue; // Indique si une carte météo à été résolue.

    /**
     * Constructeur d'un joueur.
     * 
     * @param nom Nom du joueur.
     */
    public Joueur(String nom) {
        this.id = nbJoueur;
        this.nom = nom;
        this.main = new ArrayList<Carte>();
        this.zoneDeJeu = initialiseZoneDeJeu();
        this.kmParcourus = 0;
        this.score = 0;
        this.estMeteoResolue = false;
        nbJoueur++;
    }

    /**
     * Permet de lancer le déroulement du tour de jeu du joueur.
     * 
     * @param joueurs     Les joueurs présents.
     * @param pioche      La pioche normale.
     * @param piocheMeteo La pioche météo.
     * @param defausse    La pile de défausse.
     */
    public abstract void jouerTour(ArrayList<Joueur> joueurs, Pioche pioche, Pioche piocheMeteo, Defausse defausse);

    /**
     * Permet au joueur de choisir son action pour son tour de jeu.
     * 
     * @return Le numéro d'action choisi.
     */
    public abstract int choisirAction();

    /**
     * Permet au joueur d'effectuer l'action de passer son tour.
     * 
     * @param defausse La pile de défausse.
     */
    public abstract void passerTour(Defausse defausse);

    /**
     * Permet au joueur d'afficher un message.
     * 
     * @param message Message.
     */
    public void parler(String message) {
        System.out.print("[" + this.nom + "] " + message);
    }

    /**
     * Pioche une carte depuis la pioche normale et la place dans la main du
     * joueur.
     * 
     * @param pnormale Pioche normale.
     * @return La carte piochée.
     */
    public Carte piocherCarte(PileCartes pnormale) {
        Carte carte = null;
        if (!pnormale.empty()) {
            carte = pnormale.pop();
            this.main.add(carte);
        }

        return carte;
    }

    /**
     * Pioche une carte depuis la pioche météo et la place dans la zone de jeu
     * du joueur.
     * 
     * @param pmeteo Pioche météo.
     * @return La carte piochée.
     */
    public Carte piocherCarteMeteo(PileCartes pmeteo) {
        Carte carte = null;
        if (!pmeteo.empty()) {
            carte = pmeteo.pop();
            this.zoneDeJeu.get(Zone.METEO).push(carte);
        }

        return carte;
    }

    /**
     * Permet de défausser une carte de la main et la placer sur la pile de
     * défausse.
     * 
     * @param numero   numéro de carte à défausser
     * @param defausse pile de défausse
     * @return La carte défausser.
     */
    public Carte defausserCarte(int numero, Defausse defausse) {
        return defausse.push(this.main.remove(numero - 1));
    }

    /**
     * Permet au joueur de choisir une carte de sa main par son numéro.
     * Les numéros vont de 1 au nombre de cartes en main.
     * 
     * @return Numéro de la carte choisie ou 0 s'il n'y a pas de cartes en main.
     */
    public int choisirNumeroCarte() {
        int choix = 0;

        if (!this.main.isEmpty()) {
            String input;
            String regex = "^\\s*[1-" + this.main.size() + "]\\s*$";

            do {
                this.parler("Je choisis la carte n° ");
                input = System.console().readLine();

                if (Pattern.matches(regex, input)) {
                    choix = Integer.parseInt(input.trim());
                } else {
                    this.parler("Oops je me suis trompé(e) !\n");
                }
            } while (choix == 0);
        } else {
            parler("Je n'ai pas de cartes en main :(\n");
        }

        return choix;
    }

    /**
     * Permet au joueur de jouer la carte qu'il a choisie.
     * 
     * @param numeroCarte Numéro de la carte choisie.
     * @return Le numéro de la carte. -1 si la carte n'a pas pu être jouée.
     */
    public int jouerCarte(int numeroCarte) {
        Carte carte = this.main.get(numeroCarte - 1);

        switch (carte.getType()) {
            case Carte.TYPE_ETAPE:
                return jouerCarteEtape(numeroCarte);

            case Carte.TYPE_ATTAQUE:
                System.out.println("TODO... implémenter les Attaques");
                break;

            case Carte.TYPE_PARADE:
                System.out.println("TODO... implémenter les Parades");
                break;

            case Carte.TYPE_BOTTE:
                System.out.println("TODO... implémenter les Bottes");
                break;
        }

        return -1;
    }

    /**
     * Pose une carte depuis la main du joueur sur une pile de la zone de jeu.
     * 
     * @param numeroCarte Numéro de la carte choisie.
     * @param pile        Pile de la zone de jeu.
     */
    private void poserCarte(int numeroCarte, Zone pile) {
        zoneDeJeu.get(pile).push(this.main.remove(numeroCarte - 1));
    }

    /**
     * Permet au joueur de jouer une carte d'étape.
     * 
     * @param numeroCarte Numéro de la carte choisie.
     * @return
     */
    private int jouerCarteEtape(int numeroCarte) {
        Carte carte = this.main.get(numeroCarte - 1);

        if (peutJouerEtape(carte)) {
            // Ajouter l'étape aux km
            kmParcourus += Integer.parseInt(carte.getNom());

            // S'il y a une carte météo et quelle n'a pas été résolue
            if (!zoneDeJeu.get(Zone.METEO).isEmpty() && !estMeteoResolue) {
                if (zoneDeJeu.get(Zone.METEO).peek().getNom()
                        .compareTo(Carte.getCartes()[Carte.TYPE_METEO][Carte.NEIGE]) == 0) {
                    kmParcourus -= 25;
                    this.main.get(numeroCarte - 1).setNom(this.main.get(numeroCarte - 1).getNom() + "(-25)");
                } else if (zoneDeJeu.get(Zone.METEO).peek().getNom()
                        .compareTo(Carte.getCartes()[Carte.TYPE_METEO][Carte.VENT_DOS]) == 0) {
                    kmParcourus += 25;
                    this.main.get(numeroCarte - 1).setNom(this.main.get(numeroCarte - 1).getNom() + "(+25)");
                }

                estMeteoResolue = true;
            }

            poserCarte(numeroCarte, Zone.ETAPE);

            return numeroCarte;
        } else {
            parler("Oops je ne pas jouer cette carte !\n\n");
        }

        return -1;
    }

    /**
     * Détermine si une carte étape peut être jouée.
     * 
     * @param carteEtape Carte étape.
     * @return true si l'étape est jouable, false si non.
     */
    private boolean peutJouerEtape(Carte carteEtape) {
        if (peutRouler()) {
            if (estSousLimitationVitesse() && Integer.parseInt(carteEtape.getNom()) > 50) {
                System.err.println("Sous limitation de vitesse et étape > 50");
                return false;
            } else if (Integer.parseInt(carteEtape.getNom()) == 200 && !peutJouerEtape200()) {
                System.err.println("On ne peut pas jouer plus de 2 étapes 200");
                return false;
            } else if (Integer.parseInt(carteEtape.getNom()) + kmParcourus > 1000) {
                System.err.println("On ne peut pas dépasser les 1000Km");
                return false;
            } else {
                return true;
            }
        } else {
            System.err.println("Il ne faut pas être attaqué et avoir Feu Vert ou Prioritaire.");
            return false;
        }
    }

    /**
     * Permet de savoir si le joueur est attaqué.
     * 
     * @return true si le joueur est attaqué, false si non.
     */
    private boolean estAttaque() {
        return !this.getZoneDeJeu().get(Zone.BATAILLE).isEmpty()
                && this.getZoneDeJeu().get(Zone.BATAILLE).peek().getType() == Carte.TYPE_ATTAQUE;
    }

    /**
     * Permet de savoir si le joueur peut rouler.
     * 
     * @return true si le joueur peut rouler, false si non.
     */
    private boolean peutRouler() {
        boolean possedeFeuVert = this.getZoneDeJeu().get(Zone.BATAILLE).peek()
                .getNom().compareTo(Carte.getCartes()[Carte.TYPE_PARADE][Carte.FEU_VERT]) == 0;
        boolean possedePrioritaire = this.getZoneDeJeu().get(Zone.BOTTE)
                .contient(Carte.getCartes()[Carte.TYPE_BOTTE][Carte.PRIORITAIRE]);

        return !estAttaque() && (possedeFeuVert || possedePrioritaire);
    }

    /**
     * Permet de savoir si le joueur est sous l'effet de Limite de Vitesse.
     * 
     * @return true si le joueur est sous Limite de Vitesse, false si non.
     */
    private boolean estSousLimitationVitesse() {
        return !this.getZoneDeJeu().get(Zone.VITESSE).isEmpty() && this.getZoneDeJeu().get(Zone.VITESSE).peek()
                .getNom().compareTo(Carte.getCartes()[Carte.TYPE_ATTAQUE][Carte.LIMITE_VITESSE]) == 0;
    }

    /**
     * Permet de savoir si je joueur à la possibilité de jouer une étape 200.
     * (On ne peut pas avoir plus de 2 carte d'étape 200)
     * 
     * @return true si possible, false si non.
     */
    private boolean peutJouerEtape200() {
        return this.getZoneDeJeu().get(Zone.ETAPE)
                .nombreCartes(Carte.getCartes()[Carte.TYPE_ETAPE][Carte.ETAPE_200]) < 2;
    }

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

    public int getKmParcourus() {
        return kmParcourus;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean getEstMeteoResolue() {
        return this.estMeteoResolue;
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

    /**
     * Permet d'initialiser la zone de jeu du joueur.
     * 
     * @return La zone de jeu initialisée.
     */
    private EnumMap<Zone, PileCartes> initialiseZoneDeJeu() {
        EnumMap<Zone, PileCartes> result = new EnumMap<Zone, PileCartes>(Zone.class);

        for (Zone z : Zone.values()) {
            result.put(z, new PileCartes());
        }

        // ------ juste pour tester tous les joueurs créés auront la même main.
        result.get(Zone.ETAPE).push(new Carte("25"));
        result.get(Zone.ETAPE).push(new Carte("200"));
        result.get(Zone.ETAPE).push(new Carte("200"));

        // result.get(Zone.VITESSE).push(new Carte("Limite de Vitesse"));

        result.get(Zone.BATAILLE).push(new Carte("Feu Rouge"));
        result.get(Zone.BATAILLE).push(new Carte("Feu Vert"));
        // result.get(Zone.BATAILLE).push(new Carte("Crevé"));

        result.get(Zone.BOTTE).push(new Carte("as du volant"));

        result.get(Zone.METEO).push(new Carte("neige"));
        // ------
        return result;
    }
}
