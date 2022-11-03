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

    /**
     * Lance le déroulement d'un tour de jeu du joueur.
     */
    public void jouerTour(ArrayList<Joueur> joueurs, Pioche pioche, Pioche piocheMeteo, Defausse defausse) {
        // Piocher une carte dans la pioche normale
        if (this.piocherCarte(pioche) != null) {
            this.parler("Je pioche une carte.\n");
        } else {
            this.parler("La pioche est vide, pas de carte pour moi :(\n");
        }

        Affichage.mainJoueur(this);

        // Piocher une carte météo si applicable (kmParcourus >= 500 et qu'on a pas déjà
        // une carte météo)
        if (this.getKmParcourus() >= 500 && this.getZoneDeJeu().get(Zone.METEO).size() < 1) {
            Carte carteMeteo = this.piocherCarteMeteo(piocheMeteo);
            if (carteMeteo != null) {
                this.parler("Je pioche une carte Météo : [" + carteMeteo + "].\n");
            } else {
                this.parler("La pioche est vide, pas de carte pour moi :(\n");
            }
        }

        // Si le joueur à au moins une carte en main
        if (this.getMain().size() > 0) {
            boolean tourFini = false;

            do {
                // Choisir une action : 1 jouer une carte, 2 passer.
                int action = this.choisirAction();

                // Traitement de l'action : 2 Passer.
                if (action == 2) {
                    this.passerTour(defausse);
                    tourFini = true;
                } else if (action == 1) {
                    // Traitement de l'action : 1 Jouer une carte.
                    int numeroCarteJouee = jouerCarte(choisirNumeroCarte());

                    if (numeroCarteJouee != -1) {
                        tourFini = true;
                    }
                }
            } while (!tourFini);
        } else {
            // Le joueur n'a pas de cartes en main et doit passer son tour.
            this.parler("Je n'ai plus de cartes, je passe mon tour.");
        }

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

    public void passerTour(Defausse defausse) {
        this.parler("Je passe mon tour. ");

        if (this.getMain().size() > Jeu.TAILLE_MAIN) {
            System.out.println("Je dois défausser une carte.");
            // Choisir la carte à défausser
            int choix = this.choisirNumeroCarte();
            Carte carte = this.defausserCarte(choix, defausse);

            this.parler("C'était une carte [" + carte + "].\n");
        } else {
            System.out.println();
        }
    }

}
