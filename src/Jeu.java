import java.util.ArrayList;

public class Jeu {
    public static final int NB_JOUEURS_MIN = 2;
    public static final int NB_JOUEURS_MAX = 6;
    public static final int TAILLE_MAIN = 6;

    private int nbJoueurs;
    private int idJoueurCourant;
    private Pioche pioche;
    private Pioche piocheMeteo;
    private Defausse defausse;
    private ArrayList<String> carteParsees;
    private ArrayList<Joueur> joueurs;
    private boolean estTermine;

    /**
     * Constructeur du jeu.
     * 
     * @param configFileName // Fichier de configuration.
     */
    public Jeu(String configFileName) {
        this.carteParsees = Configuration.parse(configFileName); // return un ArrayList de string
        this.pioche = new Pioche();
        this.piocheMeteo = new Pioche();
        this.defausse = new Defausse();
        this.joueurs = new ArrayList<Joueur>();
        this.estTermine = false;

        if (!this.initialiser()) {
            System.out.println("Impossible d'initialiser le jeu !");
            System.exit(1);
        }
    }

    /**
     * Démarre la partie de Mille Bornes.
     */
    public void demarrer() {
        boolean aPasseTour;
        int toursPassesConsecutifs = 0;

        this.distribuerCartes();

        // this.afficherDonnees(); // Affiche l'état actuel du jeu pour débugger

        // Boucle de jeu
        while (!this.estTermine) {
            // Annoncer le tour du joueur
            Affichage.annoncerJoueur(joueurs.get(this.idJoueurCourant));

            if (joueurs.get(this.idJoueurCourant) instanceof JoueurHumain) {
                System.out.println(joueurs.get(this.idJoueurCourant).getNom()
                        + ", appuie ENTRER pour commencer ton tour...");
                System.console().readLine();
            }

            System.err.print("Pioche: " + pioche.size());
            for (Joueur j : joueurs) {
                System.err.print(" " + j.getNom() + "(" + j.getKmParcourus() + ")");
            }
            System.err.println();
            // Afficher les zones de jeu des joueurs
            for (Joueur joueur : joueurs) {
                Affichage.zoneDeJeu(joueur, this.idJoueurCourant);
            }

            // Donner la main au joueur courant
            aPasseTour = joueurs.get(this.idJoueurCourant).jouerTour(joueurs, pioche, piocheMeteo, defausse);
            if (this.pioche.size() == 0 && aPasseTour) {
                toursPassesConsecutifs++;
            }
            // else {
            // toursPassesConsecutifs = 0;
            // }

            // Le joueur a fini son tour, regarder si il a atteind les 1000 Bornes
            if (joueurs.get(this.idJoueurCourant).getKmParcourus() > 1000) {
                estTermine = true;
            } else {
                // Si il n'y a plus de carte dans la pioche ET que tout le monde a passé son
                // tour
                if (this.pioche.size() == 0 && toursPassesConsecutifs >= nbJoueurs) {
                    estTermine = true;
                } else {
                    if (joueurs.get(this.idJoueurCourant) instanceof JoueurHumain) {
                        System.out.println(joueurs.get(this.idJoueurCourant).getNom()
                                + ", appuie sur ENTRER pour passer la main...");
                        System.console().readLine();
                    }

                    // Passer au joueur suivant.
                    this.idJoueurCourant = (this.idJoueurCourant + 1) % nbJoueurs;
                }
            }

        }

        if (this.pioche.size() == 0 && toursPassesConsecutifs >= nbJoueurs) {
            System.out.println("Tout le monde a passé son tour, la partie est terminée !");
        }

        System.out.println("TODO... afficher la fin de jeu (gagnant, scores...)");

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
         * joueurs en conséquence. Le nom et age des ordi pourrais être générés
         * aléatoirement.
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
     * Distribuer la main de départ de 6 cartes à tous les joueurs.
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
     * Permet d'initialiser les pioches.
     * 
     * @param ArrayList<String> Cartes lues depuis le fichier de configuration.
     * @return true si les pioches sont valides, false si non.
     */
    private boolean initialiserPioches() {
        boolean estPiochesValide = true;

        // parcour le fichier ctrl+espace
        for (int i = 0; i < this.carteParsees.size(); i++) {
            String[] tempresult = this.carteParsees.get(i).split(";");
            int nombreCarte = Integer.parseInt(tempresult[0].trim()); // trim() retourne la chaine de caractère sans
                                                                      // espace devant et derière
            String nomCarte = tempresult[1].trim();

            for (int c = 0; c < nombreCarte; c++) {

                Carte carte = new Carte(nomCarte);

                // ajouter la carte au dessus de la pile correspondante si elle est valide
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

        if (estPiochesValide) {
            this.pioche.melanger();
            this.piocheMeteo.melanger();
        }

        return estPiochesValide;
    }

    /**
     * Afficher les données du jeu. Utilisé pour tester la classe.
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

    /**
     * @return La pioche normale.
     */
    public Pioche getPioche() {

        return this.pioche;
    }

    /**
     * @return La pioche météo.
     */
    public Pioche getPiocheMeteo() {
        return piocheMeteo;
    }

    /**
     * @return La pile de défausse.
     */
    public Defausse getDefausse() {
        return defausse;
    }

}
