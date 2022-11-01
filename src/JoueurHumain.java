import java.util.ArrayList;
import java.util.regex.Pattern;

public class JoueurHumain extends Joueur {

    public JoueurHumain(String nom, int age) {
        super(nom);
        this.setAge(age);
    }

    /**
     * Déroulement d'un tour de jeu du joueur.
     */
    public void jouerTour(ArrayList<Joueur> joueurs, Pioche pioche, Pioche piocheMeteo, Defausse defausse) {
        // Piocher une carte dans la pioche normale
        if (this.piocherCarte(pioche) != null) {
            this.parler("Je pioche une carte.\n");
        } else {
            this.parler("La pioche est vide, pas de carte pour moi :(");
        }
        Affichage.mainJoueur(this);

        // Piocher une carte météo si applicable (kmParcourus >= 500 et qu'on a pas déjà
        // une carte météo)
        if (this.getKmParcourus() >= 500 && this.getZoneDeJeu().get(Zone.METEO).size() < 1) {
            Carte carteMeteo = this.piocherCarteMeteo(piocheMeteo);
            if (carteMeteo != null) {
                this.parler("Je pioche une carte Météo : " + carteMeteo + ".\n");
            } else {
                this.parler("La pioche est vide, pas de carte pour moi :(");
            }
        }

        // Choisir une action : 1 jouer une carte, 2 passer.
        int action = this.choisirAction();

        // Traitement de l'action : 2 Passer.
        if (action == 2) {
            this.passerTour(defausse);
        } else {
            // Traitement de l'action : 1 Jouer une carte.
            System.out.println("TODO...");
        }
    }

    public int choisirAction() {
        String input;
        int choix = 0;
        boolean estChoixValide = false;

        do {
            this.parler("Je choisis de 1-[Jouer une Carte] 2-[Passer] : ");
            input = System.console().readLine();

            if (Pattern.matches("^\s*[1|2]\s*$", input)) {
                choix = Integer.parseInt(input.trim());
                estChoixValide = true;
            } else {
                this.parler("Oops je me suis trompé(e) !\n");
            }

        } while (!estChoixValide);

        return choix;
    }

    public void passerTour(Defausse defausse) {
        this.parler("Je passe mon tour.\n");

        if (this.getMain().size() > Jeu.TAILLE_MAIN) {
            // Choisir la carte à défausser
            String input;
            int choix = 0;
            boolean estChoixValide = false;

            do {
                this.parler("Je défausse la carte n° ");
                input = System.console().readLine();

                if (Pattern.matches("^\s*[1-7]\s*$", input)) {
                    choix = Integer.parseInt(input.trim());
                    estChoixValide = true;
                } else {
                    this.parler("Oops je me suis trompé(e) !\n");
                }

            } while (!estChoixValide);

            Carte carte = this.defausserCarte(choix, defausse);
            this.parler("C'était une carte " + carte + ".\n");
        }
    }

}
