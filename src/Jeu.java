import java.util.ArrayList;

public class Jeu {
    public static final int NB_JOUEURS_MIN = 2;
    public static final int NB_JOUEURS_MAX = 6;

    private int nbJoueurs;
    private int idJoueurCourant;
    private Pioche pioche;
    private Pioche piocheMeteo;
    private Defausse defausse;
    private ArrayList<String> carteParsees;
    private ArrayList<Joueur> joueurs;
    private boolean estTermine;

    public Jeu(String configFileName) {
        this.carteParsees = Configuration.parse(configFileName); // return un ArrayList de string
        this.pioche = new Pioche();
        this.piocheMeteo = new Pioche();
        this.defausse = new Defausse();
        this.joueurs = new ArrayList<Joueur>();
        this.estTermine = false;
    }

    public void demarrer() {
        this.initialiser();
        this.distribuerCartes();

        // this.afficherDonnees(); // Affiche l'état actuel du jeu pour débugger

        // Boucle de jeu
        int i = 0;
        while (!this.estTermine) {
            // donner la main au joueur courant
            joueurs.get(this.idJoueurCourant).jouerTour(joueurs);
            // mettre à jour le joueur courant
            this.idJoueurCourant = (this.idJoueurCourant + 1) % nbJoueurs;

            if (++i >= 10)
                estTermine = true;
        }

    }

    /**
     * Initialise les attributs du jeu
     */
    private void initialiser() {
        this.nbJoueurs = Affichage.saisieNombreJoueurs();

        // Initialiser les joueurs.
        for (int i = 0; i < this.nbJoueurs; i++) {
            String joueurSaisi = Affichage.saisieNomEtAgeJoueur(i + 1);
            String[] tmp = joueurSaisi.split(";");
            joueurs.add(new JoueurHumain(tmp[0], Integer.parseInt(tmp[1])));
        }

        // Initialiser le joueur courant en fonction de l'âge des joueurs.
        this.idJoueurCourant = idDuPlusJeuneJoueur();

        // Initialiser les pioches.
        this.initialiserPioches();
    }

    /**
     * Distribuer la main de départ de 6 cartes à tous les joueurs.
     */
    private void distribuerCartes() {
        for (int i = 0; i < 6; i++) {
            for (Joueur joueur : joueurs) {
                joueur.getMain().add(this.pioche.pop());
            }
        }
    }

    /**
     * Cherche l'id du plus jeune des joueurs.
     * 
     * @return id du plus jeune joueur.
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
     * @param ArrayList<String> cartes
     */
    private void initialiserPioches() {
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
                    if (carte.getType() != Carte.METEO) {
                        this.pioche.push(carte);
                    } else {
                        this.piocheMeteo.push(carte);
                    }
                }
            }
        }

        this.pioche.melanger();
        this.piocheMeteo.melanger();
    }

    /**
     * Affichier les données du jeu. Utilisé pour tester la classe.
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
     * @return Pioche la pioche du jeu.
     */
    public Pioche getPioche() {

        return this.pioche;
    }

    /**
     * @return Pioche la pioche des cartes météo jeu.
     */
    public Pioche getPiocheMeteo() {
        return piocheMeteo;
    }

    /**
     * @return Defausse la pile de défausse.
     */
    public Defausse getDefausse() {
        return defausse;
    }

    public static void main(String[] args) {

        Jeu jeu1 = new Jeu("config.txt");
        jeu1.initialiserPioches();
        System.out.println(jeu1.getPioche());
        System.out.println("Nb cartes : " + jeu1.getPioche().size());
    }

}
