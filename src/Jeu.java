import java.util.ArrayList;

/**
 * Classe qui représente le Jeu. Contient la boucle de jeu.
 * 
 * @author Les Bornés
 */
public class Jeu {
    public static final int NB_JOUEURS_MIN = 2;
    public static final int NB_JOUEURS_MAX = 6;
    public static final int TAILLE_MAIN = 6;

    public static final int POINTS_BONUS_GAGNANT = 400;
    public static final int POINTS_BONUS_GAGNANT_SANS_200 = 200;
    public static final int POINTS_BONUS_NON_FANNY = 500;
    public static final int POINTS_BOTTE_POSEE = 100;
    public static final int POINTS_COUP_FOURRE = 300;

    private int nbJoueurs;
    private int idJoueurCourant;
    private Pioche pioche;
    private Pioche piocheMeteo;
    private Defausse defausse;
    private ArrayList<String> carteParsees;
    private ArrayList<Joueur> joueurs;
    private boolean estTermine;

    /**
     * Constructeur qui permet d'instancier le jeu.
     * 
     * @param configFileName // Fichier de configuration.
     */
    public Jeu(String configFileName) {
        Affichage.clearScreen();
        Affichage.nomJeu();
        this.carteParsees = Configuration.parse(configFileName); // return un ArrayList de string
        this.pioche = new Pioche();
        this.piocheMeteo = new Pioche();
        this.defausse = new Defausse();
        this.joueurs = new ArrayList<Joueur>();
        this.estTermine = false;

        if (!this.initialiser()) {
            Affichage.message("Impossible d'initialiser le jeu ! Vérifiez le fichier de configuration.");
            System.exit(1);
        }
    }

    /**
     * Démarre une partie de Mille Bornes.
     */
    public void demarrer() {
        boolean aPasseTour;
        int toursPassesConsecutifs = 0;

        this.distribuerCartes();

        // Boucle de jeu
        while (!this.estTermine) {
            Affichage.clearScreen();
            // Annoncer le tour du joueur
            Affichage.annoncerJoueur(joueurs.get(this.idJoueurCourant));

            if (joueurs.get(this.idJoueurCourant) instanceof JoueurHumain) {
                Affichage.attendreJoueur(joueurs.get(this.idJoueurCourant));
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

            // Le joueur a fini son tour, regarder s'il a atteind les 1000 Bornes
            if (joueurs.get(this.idJoueurCourant).getKmParcourus() == 1000) {
                estTermine = true;
            } else {
                // S'il n'y a plus de carte dans la pioche ET que tout le monde a passé son
                // tour
                if (this.pioche.size() == 0 && toursPassesConsecutifs == nbJoueurs) {
                    estTermine = true;
                } else {
                    if (joueurs.get(this.idJoueurCourant) instanceof JoueurHumain) {
                        Affichage.attendreJoueur(joueurs.get(this.idJoueurCourant));
                    }

                    // Passer au joueur suivant. Si un joueur a joué une botte, c'est à lui de
                    // jouer.
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
            Affichage.message("\nTout le monde a passé son tour, la partie est terminée !\n");
        } else {
            Affichage.message("\nLa partie est terminée !\n");
        }

        ArrayList<Joueur> joueursClasses = calculerScoresFinaux();
        Affichage.scoreFinalManche(joueursClasses);
    }

    /**
     * Calculer le score final des joueurs et déterminer les gagnants. Les
     * gagnants sont ceux avec le plus grand Km parcourus par forcement ceux
     * avec le plus gros score.
     * 
     * @return Les joueurs triés par Km parcourus décroissant.
     */
    public ArrayList<Joueur> calculerScoresFinaux() {
        ArrayList<Joueur> tmp = new ArrayList<Joueur>();
        for (Joueur joueur : joueurs) {
            tmp.add(joueur);
        }

        tmp.sort((j1, j2) -> { // tri décroissant des joueurs par leur score.
            if (j1.getKmParcourus() == j2.getKmParcourus())
                return 0;
            if (j1.getKmParcourus() < j2.getKmParcourus())
                return 1;
            else
                return -1;
        });

        int plusGrosKm = tmp.get(0).getKmParcourus();
        int nbJoueursSansEtape = 0;
        for (Joueur joueur : tmp) {
            // Regarder si le joueur est "fanny" (pas d'étape)
            if (joueur.getPile(Joueur.Pile.ETAPE).isEmpty()) {
                nbJoueursSansEtape++;
            }

            // Ajouter les KmParcourus au score.
            joueur.mettreAJourScore(joueur.getKmParcourus());

            // Si aucun joueurs n'a d'étapes, il n'y a pas de gagnants.
            if (nbJoueursSansEtape != nbJoueurs) {
                // Ajouter les points pour avoir gagné
                if (joueur.getKmParcourus() == plusGrosKm) {
                    joueur.setEstGagnant(true);
                    joueur.mettreAJourScore(Jeu.POINTS_BONUS_GAGNANT);
                    // Regarder si le gagnant n'a pas étapes 200.
                    if (!joueur.getPile(Joueur.Pile.ETAPE).contient(Carte.getNom(Carte.TYPE_ETAPE, Carte.ETAPE_200))) {
                        // Ajouter les points pour avoir gagné sans étape 200.
                        joueur.mettreAJourScore(Jeu.POINTS_BONUS_GAGNANT_SANS_200);
                    }
                }
            }
        }

        // S'il y a au moins 1 joueur sans étapes donner des points bonus a tous
        // les autres joueurs avec des étapes
        if (nbJoueursSansEtape > 1) {
            for (Joueur joueur : tmp) {
                if (!joueur.getPile(Joueur.Pile.ETAPE).isEmpty()) {
                    joueur.mettreAJourScore(Jeu.POINTS_BONUS_NON_FANNY);
                }
            }
        }

        // Les points rapportés pas les bottes et coup-fourré sont déjà comptés
        // au moment où elles sont jouées.

        return tmp;
    }

    /**
     * Initialise les attributs du jeu
     */
    private boolean initialiser() {
        boolean estInitialise = true;
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
        this.idJoueurCourant = idDuPlusJeuneJoueur();

        // Initialiser les pioches.
        estInitialise &= this.initialiserPioches();

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
    private int idDuPlusJeuneJoueur() {
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

    /**
     * Afficher les données du jeu. Utilisée pour tester la classe.
     */
    public void afficherDonnees() {
        // Nombre de joueurs
        System.out.println("Nombre de joueur: " + this.nbJoueurs);
        System.out.println();

        // Pioche normale
        System.out.println("Pioche (" + this.pioche.size() + " cartes):");
        System.out.println(this.pioche);
        System.out.println();

        // Pioche Météo
        System.out.println("Pioche Météo (" + this.piocheMeteo.size() + " cartes):");
        System.out.println(this.piocheMeteo);
        System.out.println();

        // Défausse
        System.out.println("Défausse (" + this.defausse.size() + " cartes):");
        System.out.println(this.defausse);
        System.out.println();

        // Joueurs
        System.out.println("Joueurs: ");
        for (Joueur joueur : joueurs) {
            System.out.println(joueur);
        }
        System.out.println();

        // Joueur courant
        System.out.println("Id du Joueur courant: " + this.idJoueurCourant);
        System.out.println();
    }

}
