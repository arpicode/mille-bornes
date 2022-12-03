import java.util.ArrayList;

/**
 * Classe qui représente le Jeu. Contient la boucle de jeu.
 * 
 * @author Les Bornés
 */
public class Jeu {
    public static final int NB_JOUEURS_MIN = 2;
    public static final int NB_JOUEURS_MAX = 6;

    /**
     * Nombre de carte qu'un joueur peut avoir au maximum à la fin de son tour de
     * jeu. (Il peut être momentanément à TAILLE_MAIN +1 juste après avoir pioché).
     */
    public static final int TAILLE_MAIN = 6;

    /**
     * Nombre de Km nécessaire pour piocher une carte météo.
     */
    public static final int LIMITE_METEO = 200;

    /**
     * Nombre de Km à parcourir pour gagné une manche.
     */
    public static final int NB_KM_MAX = 1000;

    /**
     * Nombre de points à avoir pour gagner une partie.
     */
    public static final int SCORE_MAX = 5000;

    /**
     * Points accordés aux gagnant d'une manche. Les gagnants sont ceux qui
     * ont parcourus le plus de Km.
     */
    public static final int POINTS_BONUS_GAGNANT = 400;

    /**
     * Points accordés aux joueurs qui ont gagné sans jouer d'étapes 200.
     */
    public static final int POINTS_BONUS_GAGNANT_SANS_200 = 200;

    /**
     * Points accordés à tous les joueurs qui ont posé des étapes dans le cas ou
     * au moins un joueur est sans étape à la fin d'une manche.
     */
    public static final int POINTS_BONUS_NON_FANNY = 500;

    /**
     * Points accordés à chaque botte posée.
     */
    public static final int POINTS_BOTTE_POSEE = 100;

    /**
     * Points accordés à chaque Coup-fourré (se cumule avec POINTS_BOTTE_POSEE).
     */
    public static final int POINTS_COUP_FOURRE = 300;

    private int nbJoueurs; // Nombre de joueurs jouant au jeu.
    private int idJoueurCourant; // Id du joueur dont c'est le tour.
    private Pioche pioche; // Pioche avec les cartes normales du jeu.
    private Pioche piocheMeteo; // Pioche des cartes météo.
    private Defausse defausse; // La pile de défausse.
    private ArrayList<String> carteParsees; // Cartes parsée depuis le fichier de configuration.
    private ArrayList<Joueur> joueurs; // Les joueurs jouant au jeu.

    /**
     * Constructeur qui permet d'instancier le jeu.
     * 
     * @param configFileName Fichier de configuration.
     */
    public Jeu(String configFileName) {
        Affichage.clearScreen();
        Affichage.titreJeu();
        this.carteParsees = Configuration.parse(configFileName); // return un ArrayList de string
        this.pioche = new Pioche();
        this.piocheMeteo = new Pioche();
        this.defausse = new Defausse();
        this.joueurs = new ArrayList<Joueur>();

        if (!this.initialiser()) {
            Affichage.message("Impossible d'initialiser le jeu ! Vérifiez le fichier de configuration.");
            System.exit(1);
        }
    }

    /**
     * Démarre une partie de Mille Bornes.
     */
    public void demarrer() {
        boolean estPartieTerminee = false;
        boolean estMancheTerminee = false;
        boolean aPasseTour = false;
        int toursPassesConsecutifs = 0;
        int nbManchesJouees = 0;

        // Boucle de jeu
        while (!estPartieTerminee) {
            if (nbManchesJouees > 0) {
                initialiserPioches();
                for (Joueur joueur : joueurs) {
                    joueur.reset();
                }
            }

            this.distribuerCartes();
            estMancheTerminee = false;
            Affichage.message("Début de la manche n° " + (nbManchesJouees + 1));
            Affichage.attendre(null);

            while (!estMancheTerminee) {
                Affichage.clearScreen();
                // Annoncer le tour du joueur
                Affichage.annoncerJoueur(joueurs.get(this.idJoueurCourant));

                if (joueurs.get(this.idJoueurCourant) instanceof JoueurHumain) {
                    Affichage.attendre(joueurs.get(this.idJoueurCourant));
                }

                // Afficher les zones de jeu des joueurs
                for (Joueur joueur : joueurs) {
                    Affichage.zoneDeJeu(joueur, this.idJoueurCourant);
                }

                // Donner la main au joueur courant
                aPasseTour = joueurs.get(this.idJoueurCourant).jouerTour(joueurs, pioche, piocheMeteo, defausse);

                // Comptabiliser le nombre de joueurs qui ont passés consécutivement.
                if (this.pioche.size() == 0 && aPasseTour) {
                    toursPassesConsecutifs++;
                } else {
                    toursPassesConsecutifs = 0;
                }

                // Le joueur a fini son tour, regarder s'il a atteint les 1000 Bornes.
                if (joueurs.get(this.idJoueurCourant).getKmParcourus() == Jeu.NB_KM_MAX) {
                    estMancheTerminee = true;
                } else {
                    // S'il n'y a plus de carte dans la pioche ET que tout le monde a passé son
                    // tour.
                    if (this.pioche.size() == 0 && toursPassesConsecutifs == nbJoueurs) {
                        estMancheTerminee = true;
                    } else {
                        if (joueurs.get(this.idJoueurCourant) instanceof JoueurHumain) {
                            Affichage.attendre(joueurs.get(this.idJoueurCourant));
                        }

                        // Si un joueur a joué une botte, c'est à lui de jouer, si non,
                        // passer au joueur suivant.
                        int idJoueurJoueBotte = chercherJoueurJoueBotte();
                        if (idJoueurJoueBotte != -1) {
                            this.idJoueurCourant = idJoueurJoueBotte;
                        } else {
                            this.idJoueurCourant = (this.idJoueurCourant + 1) % nbJoueurs;
                        }
                    }
                }

            }

            if (this.pioche.size() == 0 && toursPassesConsecutifs == nbJoueurs) {
                Affichage.message("\nTout le monde a passé son tour, la manche est terminée !\n");
            } else {
                Affichage.message("\nLa manche est terminée !\n");
            }

            ArrayList<Joueur> joueursClasses = calculerScoresManche();
            estPartieTerminee = joueursClasses.get(0).getScore() >= Jeu.SCORE_MAX;

            if (!estPartieTerminee) {
                Affichage.scoreManche(joueursClasses);
                Affichage.attendre(null);
            } else {
                Affichage.message("\nLa partie est terminée !\n");
                Affichage.scoreManche(joueursClasses);
            }
            nbManchesJouees++;
        }
    }

    /**
     * Calculer le score des joueurs pour la manche.
     * 
     * @return Les joueurs triés par Km parcourus décroissant.
     */
    public ArrayList<Joueur> calculerScoresManche() {
        int nbJoueursSansEtape = 0;
        ArrayList<Joueur> tmpJoueurs = new ArrayList<Joueur>();

        for (Joueur joueur : joueurs) {
            tmpJoueurs.add(joueur);
            // Compter les joueurs sans étapes.
            if (joueur.getPile(Joueur.Pile.ETAPE).isEmpty()) {
                nbJoueursSansEtape++;
            }
        }

        // Tri décroissant des joueurs par leur Km parcourus.
        tmpJoueurs.sort((j1, j2) -> {
            if (j1.getKmParcourus() == j2.getKmParcourus())
                return 0;
            if (j1.getKmParcourus() < j2.getKmParcourus())
                return 1;
            else
                return -1;
        });

        // Calculer le score.
        int plusGrosKm = tmpJoueurs.get(0).getKmParcourus();
        for (Joueur joueur : tmpJoueurs) {
            // Ajouter les KmParcourus au score.
            joueur.mettreAJourScore(joueur.getKmParcourus());

            // Si aucun joueurs n'a d'étapes, il n'y a pas de gagnants.
            if (nbJoueursSansEtape != nbJoueurs) {
                // Ajouter les points pour avoir gagné
                if (joueur.getKmParcourus() == plusGrosKm) {
                    joueur.mettreAJourScore(Jeu.POINTS_BONUS_GAGNANT);
                    // Regarder si le gagnant n'a pas étapes 200.
                    if (!joueur.getPile(Joueur.Pile.ETAPE).contient(Carte.getNom(Carte.TYPE_ETAPE, Carte.ETAPE_200))) {
                        // Ajouter les points pour avoir gagné sans étape 200.
                        joueur.mettreAJourScore(Jeu.POINTS_BONUS_GAGNANT_SANS_200);
                    }
                }
            }

            // S'il y a au moins 1 joueur sans étapes donner les points bonus si
            // le joueur a joué des étapes.
            if (nbJoueursSansEtape > 1) {
                if (!joueur.getPile(Joueur.Pile.ETAPE).isEmpty()) {
                    joueur.mettreAJourScore(Jeu.POINTS_BONUS_NON_FANNY);
                }
            }

        }

        // Les points rapportés pas les bottes et coup-fourré sont déjà comptés
        // au moment où elles sont jouées.

        return tmpJoueurs;
    }

    /**
     * Initialise les attributs du jeu
     */
    private boolean initialiser() {
        boolean estInitialise = false;
        this.nbJoueurs = Affichage.saisieNombreJoueurs();

        /*
         * TODO si on implémente le JoueurOrdinateur on demande ici si le joueur
         * veut joueur contre des ordi. Si oui il sera seul et il y aura
         * (nbJoueurs - 1) ordinateurs. Il faudra modifier l'initialisation des
         * joueurs en conséquence. Le nom et age des ordi pourraient être
         * générés aléatoirement.
         */

        // Initialiser les joueurs.
        for (int i = 0; i < this.nbJoueurs; i++) {
            String joueurSaisi = Affichage.saisieNomEtAgeJoueur(i + 1);
            String[] tmp = joueurSaisi.split(";");
            joueurs.add(new JoueurHumain(tmp[0], Integer.parseInt(tmp[1])));
        }

        // Initialiser le joueur courant en fonction de l'âge des joueurs.
        this.idJoueurCourant = idPlusJeuneJoueur();

        // Initialiser les pioches.
        estInitialise = this.initialiserPioches();

        return estInitialise;
    }

    /**
     * Distribuer la main de départ de Jeu.TAILLE_MAIN cartes à tous les joueurs.
     */
    private void distribuerCartes() {
        for (int i = 0; i < Jeu.TAILLE_MAIN; i++) {
            for (Joueur joueur : joueurs) {
                joueur.getMain().add(this.pioche.pop());
            }
        }
    }

    /**
     * Cherche l'id du plus jeune des joueurs.
     * 
     * @return Id du plus jeune joueur.
     */
    private int idPlusJeuneJoueur() {
        int ageMin = Integer.MAX_VALUE;
        int id = 0;
        for (int i = 0; i < this.joueurs.size(); i++) {
            if (this.joueurs.get(i).getAge() <= ageMin) {
                ageMin = this.joueurs.get(i).getAge();
                id = this.joueurs.get(i).getId();
            }
        }

        return id;
    }

    /**
     * Cherche l'id du joueur qui vient de jouer une botte.
     * 
     * @return Id du joueur ou -1 si aucun joueur vient de jouer une botte.
     */
    private int chercherJoueurJoueBotte() {
        for (Joueur joueur : joueurs) {
            if (joueur.aJouerBotte()) {
                joueur.setAJouerBotte(false);
                return joueur.getId();
            }
        }
        return -1;
    }

    /**
     * Permet d'initialiser les pioches.
     * 
     * @param ArrayList<String> Cartes lues depuis le fichier de configuration.
     * @return true si les pioches sont valides, false si non.
     */
    private boolean initialiserPioches() {
        boolean estPiochesValide = true;

        // Parcourir les lignes parsées
        for (int i = 0; i < this.carteParsees.size(); i++) {
            String[] tempresult = this.carteParsees.get(i).split(";");
            int nombreCarte = Integer.parseInt(tempresult[0].trim()); // trim() retourne la chaine de caractère sans
                                                                      // espace devant et derière
            String nomCarte = tempresult[1].trim();

            for (int c = 0; c < nombreCarte; c++) {

                Carte carte = new Carte(nomCarte);

                // Ajouter la carte au dessus de la pile correspondante si elle est valide.
                if (carte.estValide()) {
                    if (carte.getType() != Carte.TYPE_METEO) {
                        this.pioche.push(carte);
                    } else {
                        this.piocheMeteo.push(carte);
                    }
                } else {
                    System.out.println("La carte " + carte.getNom() + " n'est pas valide !");
                    estPiochesValide = false;
                }
            }
        }

        // Vérifier qu'il y a assez de cartes pour tous les joueurs.
        if (this.pioche.size() < Jeu.TAILLE_MAIN * this.nbJoueurs) {
            System.out.println("Pas assez de cartes. Il faut au moins pouvoir distribuer "
                    + Jeu.TAILLE_MAIN + " cartes à tous les joueurs !");
            estPiochesValide = false;
        }

        if (estPiochesValide) {
            this.pioche.melanger();
            this.piocheMeteo.melanger();
        }

        return estPiochesValide;
    }

}
