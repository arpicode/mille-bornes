import java.util.ArrayList;
import java.util.EnumMap;

/**
 * Classe abstraite définissant un joueur.
 * 
 * @author Les Bornés
 */
public abstract class Joueur {
    /**
     * Enumeration des différentes piles contenues dans la zone de jeu d'un
     * joueur.
     */
    public static enum Pile {
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
    private EnumMap<Pile, PileCartes> zoneDeJeu; // La zone de jeu du joueur.
    private int age; // L'âge du joueur.
    private int kmParcourus; // Les kilomètre parcourus du joueur.
    private int score; // Le score final du joueur.
    private boolean estMeteoResolue; // Indique si une carte météo à été résolue.
    private boolean aJoueCoupFourre; // Indique si le joueur vient de jouer un
                                     // Coup-fourré.
    private boolean estGagnant; // Indique si le joueur est le gagnant.

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
        this.aJoueCoupFourre = false;
        this.estGagnant = false;
        nbJoueur++;
    }

    /**
     * Permet de lancer le déroulement du tour de jeu du joueur.
     * 
     * @param joueurs     Les joueurs présents.
     * @param pioche      La pioche normale.
     * @param piocheMeteo La pioche météo.
     * @param defausse    La pile de défausse.
     * @return true si le joueur a passé son tour, false si non.
     */
    public abstract boolean jouerTour(ArrayList<Joueur> joueurs, Pioche pioche, Pioche piocheMeteo, Defausse defausse);

    /**
     * Permet au joueur de choisir son action pour son tour de jeu.
     * 
     * @return Le numéro d'action choisi.
     */
    public abstract int choisirAction();

    /**
     * Permet au joueur de choisir une carte de sa main par son numéro.
     * Les numéros vont de 1 au nombre de cartes en main.
     * 
     * @return Numéro de la carte choisie ou 0 s'il n'y a pas de cartes en main.
     */
    public abstract int choisirCarte();

    /**
     * Permet au joueur de choisir le numéro du joueur à cibler pour une carte
     * attaque.
     * 
     * @return Numéro du joueur ciblé.
     */
    public abstract int choisirJoueur(ArrayList<Joueur> joueurs);

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
            this.parler("Je pioche une carte.\n");
        } else {
            this.parler("La pioche est vide, pas de carte pour moi :(\n");
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
            this.getPile(Pile.METEO).push(carte);
            this.parler("Je pioche une carte météo : [" + carte + "].\n");
        } else {
            this.parler("La pioche est vide, pas de carte pour moi :(\n");
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
     * Permet au joueur de jouer la carte qu'il a choisie.
     *
     * @param numeroCarte Numéro de la carte choisie.
     * @param joueurs     Les joueurs.
     * @param pioche      Pile de pioche.
     * @param defausse    Pile de défausse.
     * @return Le numéro de la carte. -1 si la carte n'a pas pu être jouée.
     */
    public int jouerCarte(int numeroCarte, ArrayList<Joueur> joueurs, PileCartes pioche, Defausse defausse) {
        Carte carte = this.main.get(numeroCarte - 1);

        switch (carte.getType()) {
            case Carte.TYPE_ETAPE:
                return jouerCarteEtape(numeroCarte);

            case Carte.TYPE_ATTAQUE:
                return jouerCarteAttaque(numeroCarte, joueurs, pioche, defausse);

            case Carte.TYPE_PARADE:
                return jouerCarteParade(numeroCarte);

            case Carte.TYPE_BOTTE:
                return jouerCarteBotte(numeroCarte, defausse);
        }

        return -1;
    }

    /**
     * Pose une carte depuis la main du joueur sur une pile de la zone de jeu
     * d'un joueur.
     * 
     * @param numeroCarte Numéro de la carte choisie.
     * @param pile        Pile de la zone de jeu.
     */
    private void poserCarte(int numeroCarte, Joueur joueur, Pile pile) {
        joueur.getPile(pile).push(this.main.remove(numeroCarte - 1));
    }

    /**
     * Permet au joueur de jouer une carte d'étape.
     * 
     * @param numeroCarte Numéro de la carte choisie.
     * @return Numéro de la carte choisie ou -1 si la carte n'est pas
     *         jouable.
     */
    private int jouerCarteEtape(int numeroCarte) {
        Carte carte = this.main.get(numeroCarte - 1);

        if (peutJouerEtape(carte)) {
            // Ajouter l'étape aux km
            kmParcourus += Integer.parseInt(carte.getNom());

            parler("Je joue une Étape [" + this.main.get(numeroCarte - 1) + "].\n");

            // S'il y a une carte météo et quelle n'a pas été résolue.
            if (!getPile(Pile.METEO).isEmpty() && !estMeteoResolue) {
                if (getPile(Pile.METEO).peek().estEgale(Carte.TYPE_METEO, Carte.NEIGE)) {
                    kmParcourus -= 25;
                    this.main.get(numeroCarte - 1).setNom(this.main.get(numeroCarte - 1).getNom() + "(-25)");
                } else if (getPile(Pile.METEO).peek().estEgale(Carte.TYPE_METEO, Carte.VENT_DOS)) {
                    kmParcourus += 25;
                    this.main.get(numeroCarte - 1).setNom(this.main.get(numeroCarte - 1).getNom() + "(+25)");
                }

                estMeteoResolue = true;
            }

            poserCarte(numeroCarte, this, Pile.ETAPE);

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
    public boolean peutJouerEtape(Carte carteEtape) {
        if (peutRouler(this)) {
            if (this.estAttaquePar(Carte.LIMITE_VITESSE) && Integer.parseInt(carteEtape.getNom()) > 50) {
                afficherInfo("Vous ne pouvez pas jouer une carte Étape > 50. Vous avez",
                        new Carte(Carte.TYPE_ATTAQUE, Carte.LIMITE_VITESSE));
                return false;
            } else if (Integer.parseInt(carteEtape.getNom()) == 200 && !peutJouerEtape200()) {
                afficherInfo("Vous ne pouvez pas jouer plus de 2 Étapes",
                        new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_200));
                return false;
            } else if (Integer.parseInt(carteEtape.getNom()) + kmParcourus > 1000) {
                afficherInfo("Vous ne pouvez pas dépasser les 1000 Km.");
                return false;
            } else {
                return true;
            }
        } else {
            if (estAttaque(this)) {
                afficherInfo("Vous ne pouvez pas jouer d'Étape si vous êtes attaqué(e)");
            } else {
                afficherInfo("Vous ne pouvez pas roulez.");
            }
            return false;
        }
    }

    /**
     * Permet au joueur de jouer une carte attaque sur un autre joueur.
     * 
     * @param numeroCarte Numéro de la carte choisie.
     * @param joueurs     Joueur ciblé.
     * @param defausse    La pile de défausse.
     * @return Numéro de la carte choisie ou -1 si la carte n'est pas
     *         jouable.
     */
    private int jouerCarteAttaque(int numeroCarte, ArrayList<Joueur> joueurs, PileCartes pioche, Defausse defausse) {
        // Choisir le joueur à attaquer.
        int numeroJoueur = choisirJoueur(joueurs);
        Joueur joueurCible = joueurs.get(numeroJoueur - 1);

        if (peutAttaquerJoueur(this.main.get(numeroCarte - 1), joueurCible)) {

            parler("J'attaque " + joueurCible.getNom()
                    + " avec [" + this.main.get(numeroCarte - 1) + "].\n");

            // Vérifier si le joueur ciblé peut répondre par un coup-fourré.
            int numeroCarteCoupFourre = joueurCible.chercherCarteCoupFourre(this.main.get(numeroCarte - 1));

            // Si l'attaque est une Limite de Vitesse il faut la poser sur la pile Vitesse.
            if (this.main.get(numeroCarte - 1).estEgale(Carte.TYPE_ATTAQUE, Carte.LIMITE_VITESSE)) {
                poserCarte(numeroCarte, joueurCible, Pile.VITESSE);
            } else {
                poserCarte(numeroCarte, joueurCible, Pile.BATAILLE);
            }

            // Résoudre le coup-fourré s'il y a lieu.
            if (numeroCarteCoupFourre != -1) {
                joueurCible.parler("Coup-fourré !!\n");
                joueurCible.jouerCarteBotte(numeroCarteCoupFourre, defausse);
                joueurCible.mettreAJourScore(Jeu.POINTS_COUP_FOURRE);
                joueurCible.setAJouerCoupFourre(true);
                joueurCible.piocherCarte(pioche);
            }

            return numeroCarte;
        }
        return -1;
    }

    /**
     * Permet de savoir si le joueur peut jouer une carte d'attaque sur un autre
     * joueur.
     * 
     * @param carte  Carte jouée.
     * @param joueur Joueur ciblé.
     * @return true si la carte est jouable, false si non.
     */
    public boolean peutAttaquerJoueur(Carte carte, Joueur joueur) {
        // Un joueur peut attaquer s'il n'est pas déjà attaqué ET qu'il est en train de
        // roulez OU si la carte est une Limite de Vitesse
        if (!estAttaque(joueur) && peutRouler(joueur)
                || carte.estEgale(Carte.TYPE_ATTAQUE, Carte.LIMITE_VITESSE)) {
            // regarder si le joueur est sous limitation de vitesse.
            if (joueur.estAttaquePar(Carte.LIMITE_VITESSE)) {
                // il ne peut pas être attaqué par une limitation de vitesse.
                if (carte.estEgale(Carte.TYPE_ATTAQUE, Carte.LIMITE_VITESSE)) {
                    afficherInfo(joueur.getNom() + " a déjà", carte);
                    return false;
                }
            }
            // regarder si le joueur est protégé par une botte.
            if (!joueur.getPile(Pile.BOTTE).isEmpty()) {
                // Si le joueur à la botte Prioritaire
                if (joueur.getPile(Pile.BOTTE).contient(Carte.getNom(Carte.TYPE_BOTTE, Carte.PRIORITAIRE))) {
                    // il ne peut pas être attaqué par Feu Rouge ou Limite de Vitesse
                    if (carte.estEgale(Carte.TYPE_ATTAQUE, Carte.LIMITE_VITESSE)
                            || carte.estEgale(Carte.TYPE_ATTAQUE, Carte.FEU_ROUGE)) {
                        afficherInfo(joueur.getNom() + " est protégé par",
                                new Carte(Carte.TYPE_BOTTE, Carte.PRIORITAIRE));
                        return false;
                    }
                    // Si le joueur à la botte Citerne
                } else if (joueur.getPile(Pile.BOTTE)
                        .contient(Carte.getNom(Carte.TYPE_BOTTE, Carte.CITERNE))) {
                    // Il ne peut pas être attaqué par Panne d'Essence.
                    if (carte.estEgale(Carte.TYPE_ATTAQUE, Carte.PANNE_ESSENCE)) {
                        afficherInfo(joueur.getNom() + " est protégé par",
                                new Carte(Carte.TYPE_BOTTE, Carte.CITERNE));
                        return false;
                    }
                    // Si le joueur à la botte Increvable.
                } else if (joueur.getPile(Pile.BOTTE)
                        .contient(Carte.getNom(Carte.TYPE_BOTTE, Carte.INCREVABLE))) {
                    // Il ne peut pas être attaqué par Crevé
                    if (carte.estEgale(Carte.TYPE_ATTAQUE, Carte.CREVE)) {
                        afficherInfo(joueur.getNom() + " est protégé par",
                                new Carte(Carte.TYPE_BOTTE, Carte.INCREVABLE));
                        return false;
                    }
                    // Si le joueur à la botte As du Volant
                } else if (joueur.getPile(Pile.BOTTE)
                        .contient(Carte.getNom(Carte.TYPE_BOTTE, Carte.AS_VOLANT))) {
                    // Il ne peut pas être attaqué par Accident
                    if (carte.estEgale(Carte.TYPE_ATTAQUE, Carte.ACCIDENT)) {
                        afficherInfo(joueur.getNom() + " est protégé par",
                                new Carte(Carte.TYPE_BOTTE, Carte.AS_VOLANT));
                        return false;
                    }
                }
            }

            return true;
        } else {
            if (estAttaque(joueur)) {
                afficherInfo(joueur.getNom() + " est déjà attaqué(e) par", joueur.getPile(Pile.BATAILLE).peek());
            }
            afficherInfo(joueur.getNom() + " doit être en train de rouler pour être attaqué(e).");

            return false;
        }
    }

    /**
     * Permet au joueur de jouer une carte parade.
     * 
     * @param numeroCarte Numéro de la carte choisie.
     * @return Numéro de la carte choisie ou -1 si la carte n'est pas
     *         jouable.
     */
    private int jouerCarteParade(int numeroCarte) {
        Carte carteParade = this.main.get(numeroCarte - 1);

        if (peutJouerParade(carteParade)) {

            parler("Je joue [" + carteParade + "].\n");

            // Si la parade est une Fin de Limite de Vitesse il faut la poser sur la pile
            // Vitesse.
            if (this.main.get(numeroCarte - 1).estEgale(Carte.TYPE_PARADE, Carte.FIN_LIMITE_VITESSE)) {
                poserCarte(numeroCarte, this, Pile.VITESSE);
            } else {
                poserCarte(numeroCarte, this, Pile.BATAILLE);
            }
            return numeroCarte;
        }
        return -1;
    }

    /**
     * Détermine si une carte parade est jouable.
     * 
     * @param carteParade Carte parade.
     * @return true si la carte parade est jouable, false si non.
     */
    public boolean peutJouerParade(Carte carteParade) {
        // Cas ou la carte parade est un Feu Vert.
        if (carteParade.getNom().compareTo(Carte.getNom(Carte.TYPE_PARADE, Carte.FEU_VERT)) == 0) {
            // Si la pile Bataille est vide
            // OU le joueur est attaqué par une attaque Feu Rouge
            // OU le joueur ne peut pas roulez ET qu'il n'est pas attaqué.
            if (this.getPile(Pile.BATAILLE).isEmpty()
                    || this.estAttaquePar(Carte.FEU_ROUGE)
                    || (!peutRouler(this) && !estAttaque(this))) {
                return true;
            }
            afficherInfo("Vous ne pouvez pas jouer", carteParade);
            // Si non, si la carte parade est la carte Fin Limite de Vitesse.
        } else if (carteParade.getNom().compareTo(Carte.getNom(Carte.TYPE_PARADE, Carte.FIN_LIMITE_VITESSE)) == 0) {
            // Si le joueur est attaqué par une attaque Limite de Vitesse.
            if (this.estAttaquePar(Carte.LIMITE_VITESSE)) {
                return true;
            }
            afficherInfo("Vous n'être pas attaqué(e) par", new Carte(Carte.TYPE_ATTAQUE, Carte.LIMITE_VITESSE));
            // Si non, si la carte parade est la carte Essence.
        } else if (carteParade.getNom().compareTo(Carte.getNom(Carte.TYPE_PARADE, Carte.ESSENCE)) == 0) {
            // Si le joueur est attaqué par une attaque Panne d'Essence.
            if (this.estAttaquePar(Carte.PANNE_ESSENCE)) {
                return true;
            }
            afficherInfo("Vous n'être pas attaqué(e) par", new Carte(Carte.TYPE_ATTAQUE, Carte.PANNE_ESSENCE));
            // Si non, si la carte parade est une Roue de Secours.
        } else if (carteParade.getNom().compareTo(Carte.getNom(Carte.TYPE_PARADE, Carte.ROUE_SECOURS)) == 0) {
            // Si le joueur est attaqué par une attaque Crevé.
            if (this.estAttaquePar(Carte.CREVE)) {
                return true;
            }
            afficherInfo("Vous n'être pas attaqué(e) par", new Carte(Carte.TYPE_ATTAQUE, Carte.CREVE));
            // Si non, si la carte parade est une Réparation.
        } else if (carteParade.getNom().compareTo(Carte.getNom(Carte.TYPE_PARADE, Carte.REPARATION)) == 0) {
            // Si le joueur est attaqué par une attaque Accident.
            if (this.estAttaquePar(Carte.ACCIDENT)) {
                return true;
            }
            afficherInfo("Vous n'être pas attaqué(e) par", new Carte(Carte.TYPE_ATTAQUE, Carte.ACCIDENT));
        }

        return false;
    }

    /**
     * Permet au joueur de jouer des cartes Botte.
     * 
     * @param numeroCarte Numéro de carte choisie.
     * @param defausse    Pile de défausse.
     * 
     * @return Numéro de la carte jouée.
     */
    private int jouerCarteBotte(int numeroCarte, Defausse defausse) {
        Carte carteBotte = this.main.get(numeroCarte - 1);

        // Il n'y a pas de conditions pour pouvoir jouer une botte.
        parler("Je joue la botte [" + carteBotte + "].\n");

        // Si le joueur joue la botte Prioritaire.
        if (carteBotte.getNom().compareTo(Carte.getNom(Carte.TYPE_BOTTE, Carte.PRIORITAIRE)) == 0) {
            if (this.estAttaquePar(Carte.LIMITE_VITESSE)) {
                parler("J'annule l'attaque [" + defausse.push(getPile(Pile.VITESSE).pop()) + "] !\n");
            }

            if (this.estAttaquePar(Carte.FEU_ROUGE)) {
                parler("J'annule l'attaque [" + defausse.push(getPile(Pile.BATAILLE).pop()) + "] !\n");
            }
            // Si non, si le joueur joue la botte Citerne.
        } else if (carteBotte.getNom().compareTo(Carte.getNom(Carte.TYPE_BOTTE, Carte.CITERNE)) == 0) {
            if (this.estAttaquePar(Carte.PANNE_ESSENCE)) {
                parler("J'annule l'attaque [" + defausse.push(getPile(Pile.BATAILLE).pop()) + "] !\n");
            }
            // Si non, si le joueur joue la botte Increvable.
        } else if (carteBotte.getNom().compareTo(Carte.getNom(Carte.TYPE_BOTTE, Carte.INCREVABLE)) == 0) {
            if (this.estAttaquePar(Carte.CREVE)) {
                parler("J'annule l'attaque [" + defausse.push(getPile(Pile.BATAILLE).pop()) + "] !\n");
            }
            // Si non, si le joueur joue la botte As du Volant.
        } else if (carteBotte.getNom().compareTo(Carte.getNom(Carte.TYPE_BOTTE, Carte.AS_VOLANT)) == 0) {
            if (this.estAttaquePar(Carte.ACCIDENT)) {
                parler("J'annule l'attaque [" + defausse.push(getPile(Pile.BATAILLE).pop()) + "] !\n");
            }
        }

        poserCarte(numeroCarte, this, Pile.BOTTE);
        mettreAJourScore(Jeu.POINTS_BOTTE_POSEE);

        return numeroCarte;
    }

    /**
     * Permet de déterminé si un joueur qui vient d'être attaqué peut répondre par
     * un coup-fourré.
     * 
     * @param carte Carte d'attaque.
     * @return Numéro de la carte botte dans la main du joueur si elle permet le
     *         coup-fourré, -1 si non.
     */
    public int chercherCarteCoupFourre(Carte carte) {
        // Parcourir les cartes de la main du joueur attaqué.
        for (int i = 0; i < this.getMain().size(); i++) {
            Carte carteCourante = this.getMain().get(i);
            // Si la carte courante est une botte
            if (carteCourante.getType() == Carte.TYPE_BOTTE) {
                // Regarder si elle peut contrer l'attaque.
                if ((carteCourante.estEgale(Carte.TYPE_BOTTE, Carte.PRIORITAIRE)
                        && (carte.estEgale(Carte.TYPE_ATTAQUE, Carte.FEU_ROUGE)
                                || carte.estEgale(Carte.TYPE_ATTAQUE, Carte.LIMITE_VITESSE)))
                        || (carteCourante.estEgale(Carte.TYPE_BOTTE, Carte.CITERNE)
                                && carte.estEgale(Carte.TYPE_ATTAQUE, Carte.PANNE_ESSENCE))
                        || (carteCourante.estEgale(Carte.TYPE_BOTTE, Carte.INCREVABLE)
                                && carte.estEgale(Carte.TYPE_ATTAQUE, Carte.CREVE))
                        || (carteCourante.estEgale(Carte.TYPE_BOTTE, Carte.AS_VOLANT)
                                && carte.estEgale(Carte.TYPE_ATTAQUE, Carte.ACCIDENT))) {
                    return i + 1;
                }
            }
        }
        return -1;
    }

    /**
     * Permet de savoir si un joueur est attaqué par autre chose qu'une Limite
     * de Vitesse.
     * 
     * @param joueur Un joueur.
     * @return true si le joueur est attaqué, false si non.
     */
    private boolean estAttaque(Joueur joueur) {
        Boolean estAttaque = !joueur.getZoneDeJeu().get(Pile.BATAILLE).isEmpty()
                && joueur.getZoneDeJeu().get(Pile.BATAILLE).peek().getType() == Carte.TYPE_ATTAQUE;

        return estAttaque;
    }

    /**
     * Permet de savoir si le joueur est attaqué par une carte attaque d'après
     * son indice.
     * 
     * @param indiceCarteAttaque Indice de la carte attaque.
     * 
     * @return true si le joueur est attaqué par la carte, false si non.
     */
    private boolean estAttaquePar(int indiceCarteAttaque) {
        String nomCarteAttaque = Carte.getNom(Carte.TYPE_ATTAQUE, indiceCarteAttaque);

        if (!this.getZoneDeJeu().get(Pile.BATAILLE).isEmpty()) {
            if (this.getPile(Pile.BATAILLE).peek().getNom().compareTo(nomCarteAttaque) == 0) {
                return true;
            }
        }

        if (!this.getZoneDeJeu().get(Pile.VITESSE).isEmpty()) {
            if (this.getPile(Pile.VITESSE).peek().getNom().compareTo(nomCarteAttaque) == 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * Permet de savoir si un joueur peut rouler.
     * 
     * @param joueur Joueur.
     * @return true si le joueur peut rouler, false si non.
     */
    private boolean peutRouler(Joueur joueur) {
        boolean possedeFeuVert = !joueur.getZoneDeJeu().get(Pile.BATAILLE).isEmpty()
                && joueur.getZoneDeJeu().get(Pile.BATAILLE).peek().getNom()
                        .compareTo(Carte.getNom(Carte.TYPE_PARADE, Carte.FEU_VERT)) == 0;
        boolean possedePrioritaire = joueur.getZoneDeJeu().get(Pile.BOTTE)
                .contient(Carte.getNom(Carte.TYPE_BOTTE, Carte.PRIORITAIRE));

        return !estAttaque(joueur) && (possedeFeuVert || possedePrioritaire);
    }

    /**
     * Permet de savoir si le joueur à la possibilité de jouer une étape 200.
     * (On ne peut pas avoir plus de 2 carte d'étape 200)
     * 
     * @return true si possible, false si non.
     */
    private boolean peutJouerEtape200() {
        return this.getZoneDeJeu().get(Pile.ETAPE)
                .nombreCartes(Carte.getNom(Carte.TYPE_ETAPE, Carte.ETAPE_200)) < 2;
    }

    private static void afficherInfo(String message) {
        System.out.println(Affichage.Color.GRAY + message + Affichage.Color.END);
    }

    private static void afficherInfo(String message, Carte carte) {
        System.out.println(Affichage.Color.GRAY + message + Affichage.Color.END
                + " [" + carte + "].");
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

    public void mettreAJourScore(int points) {
        this.score += points;
    }

    public boolean getEstMeteoResolue() {
        return this.estMeteoResolue;
    }

    public boolean aJouerCoupFourre() {
        return this.aJoueCoupFourre;
    }

    public void setAJouerCoupFourre(boolean value) {
        this.aJoueCoupFourre = value;
    }

    public boolean estGagnant() {
        return this.estGagnant;
    }

    public void setEstGagnant(boolean value) {
        this.estGagnant = value;
    }

    public ArrayList<Carte> getMain() {
        return main;
    }

    public EnumMap<Pile, PileCartes> getZoneDeJeu() {
        return zoneDeJeu;
    }

    public PileCartes getPile(Pile pile) {
        return this.zoneDeJeu.get(pile);
    }

    public String toString() {
        String result = "  [Id: " + this.id;
        result += "  Nom: " + this.nom;
        result += "  Âge: " + this.age + "]\n";

        result += "    Main: ";
        for (Carte carte : main) {
            result += "[" + carte + "]" + " ";
        }
        result += "\n    Zone de Jeu: " + zoneDeJeu;
        return result;
    }

    /**
     * Permet d'initialiser la zone de jeu du joueur.
     * 
     * @return La zone de jeu initialisée.
     */
    private EnumMap<Pile, PileCartes> initialiseZoneDeJeu() {
        EnumMap<Pile, PileCartes> result = new EnumMap<Pile, PileCartes>(Pile.class);

        for (Pile z : Pile.values()) {
            result.put(z, new PileCartes());
        }

        // juste pour tester tous les joueurs créés auront les même cartes sur table.
        // result.get(Pile.ETAPE).push(new Carte("75"));
        // result.get(Pile.ETAPE).push(new Carte("200"));
        // result.get(Pile.ETAPE).push(new Carte("100"));
        // result.get(Pile.ETAPE).push(new Carte("50"));

        // result.get(Pile.VITESSE).push(new Carte("Limite de Vitesse"));
        // result.get(Pile.VITESSE).push(new Carte("Fin de Limite de Vitesse"));

        // result.get(Pile.BATAILLE).push(new Carte("Feu Vert"));
        // result.get(Pile.BATAILLE).push(new Carte("Accident"));
        // result.get(Pile.BATAILLE).push(new Carte("Réparation"));
        // result.get(Pile.BATAILLE).push(new Carte("Feu Vert"));
        // result.get(Pile.BATAILLE).push(new Carte("Crevé"));
        // result.get(Pile.BATAILLE).push(new Carte("Roue de Secours"));
        // result.get(Pile.BATAILLE).push(new Carte("Feu Vert"));
        // result.get(Pile.BATAILLE).push(new Carte("Feu Rouge"));
        // result.get(Pile.BATAILLE).push(new Carte("Feu Vert"));
        // result.get(Pile.BATAILLE).push(new Carte("Feu Rouge"));
        // result.get(Pile.BATAILLE).push(new Carte("Feu Vert"));
        // result.get(Pile.BATAILLE).push(new Carte("Panne d'Essence"));
        // result.get(Pile.BATAILLE).push(new Carte("Essence"));

        // result.get(Pile.BOTTE).push(new Carte("as du volant"));

        // result.get(Pile.METEO).push(new Carte("neige"));
        // ------
        return result;
    }
}
