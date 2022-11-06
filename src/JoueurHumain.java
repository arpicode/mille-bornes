import java.util.ArrayList;
import java.util.regex.Pattern;

public class JoueurHumain extends Joueur {

    /**
     * Constructeur d'un joueur humain.
     * 
     * @param nom // Nom du joueur.
     * @param age // Âge du joueur.
     */
    public JoueurHumain(String nom, int age) {
        super(nom);
        this.setAge(age);
    }

    public boolean jouerTour(ArrayList<Joueur> joueurs, Pioche pioche, Pioche piocheMeteo, Defausse defausse) {
        boolean aPasseTour = false;
        // Piocher une carte dans la pioche normale
        this.piocherCarte(pioche);

        // Afficher la main du joueur
        this.regarderMain();

        // Piocher une carte météo si applicable (kmParcourus >= 500 et qu'on a pas déjà
        // une carte météo)
        if (this.getKmParcourus() >= 500 && this.getPile(Pile.METEO).size() < 1) {
            Carte carteMeteo = this.piocherCarteMeteo(piocheMeteo);
            if (carteMeteo != null) {
                this.parler("Je pioche une carte Météo : [" + carteMeteo + "].\n");
            } else {
                this.parler("La pioche est vide, pas de carte pour moi :(\n");
            }
        }

        // Si le joueur à au moins une carte en main
        if (this.getMain().size() > 0) {
            boolean estTourTermine = false;

            do {
                // Choisir une action : 1 jouer une carte, 2 passer.
                int action = this.choisirAction();

                // Traitement de l'action : 2 Passer.
                if (action == 2) {
                    this.passerTour(defausse);
                    aPasseTour = true;
                    estTourTermine = true;
                } else if (action == 1) {
                    // Traitement de l'action : 1 Jouer une carte.
                    int numeroCarteJouee = jouerCarte(choisirCarte(), joueurs, pioche, defausse);

                    if (numeroCarteJouee != -1) {
                        estTourTermine = true;
                    }
                }
            } while (!estTourTermine);
        } else {
            // Le joueur n'a pas de cartes en main et doit passer son tour.
            this.parler("Je n'ai plus de cartes, je passe mon tour.");
        }
        return aPasseTour;
    }

    public int choisirAction() {
        String input;
        int choix = 0;

        do {
            this.parler("Je choisis de 1-[Jouer une Carte] 2-[Passer] : ");
            input = System.console().readLine();

            if (Pattern.matches("^\\s*[1-2]\\s*$", input)) {
                choix = Integer.parseInt(input.trim());
            } else {
                this.parler("Oops je me suis trompé(e) !\n");
            }

        } while (choix == 0);

        return choix;
    }

    public int choisirCarte() {
        int choix = 0;

        if (!this.getMain().isEmpty()) {
            String input;
            String regex = "^\\s*[1-" + this.getMain().size() + "]\\s*$";

            do {
                parler("Je choisis la carte n° ");
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

    public int choisirJoueur(ArrayList<Joueur> joueurs) {
        String input;
        String regex = "^\\s*[";
        String choixDesJoueurs = "";
        int choix = 0;

        // Construire le menu de sélection des joueurs.
        for (Joueur joueur : joueurs) {
            if (joueur.getId() != this.getId()) {
                choixDesJoueurs += (joueur.getId() + 1) + "-[" + joueur.getNom() + "] ";
                regex += (joueur.getId() + 1);
            }
        }
        regex += "]\\s*$";

        do {
            this.parler("Je choisis d'attaquer " + choixDesJoueurs + " : ");
            input = System.console().readLine();

            if (Pattern.matches(regex, input)) {
                choix = Integer.parseInt(input.trim());
            } else {
                this.parler("Oops je me suis trompé(e) !\n");
            }

        } while (choix == 0);

        return choix;
    }

    public void passerTour(Defausse defausse) {
        this.parler("Je passe mon tour. ");

        if (this.getMain().size() > Jeu.TAILLE_MAIN) {
            System.out.println("Je dois défausser une carte.");
            // Choisir la carte à défausser
            int choix = this.choisirCarte();
            Carte carte = this.defausserCarte(choix, defausse);

            this.parler("Je défausse [" + carte + "].\n");
        } else {
            System.out.println();
        }
    }

    /**
     * Affiche la main du joueur.
     */
    private void regarderMain() {
        parler("Je regarde ma main...\n");
        for (int i = 0; i < this.getMain().size(); i++) {
            System.out.print((i + 1) + "-" + "[" + this.getMain().get(i) + "] ");
        }
        System.out.println("\n");
    }

}
