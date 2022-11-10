import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Classe qui définie un joueur humain.
 * 
 * @author Les Bornés
 */
public class JoueurHumain extends Joueur {
    private static Scanner scanner = new Scanner(System.in);

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
        if (this.getKmParcourus() >= Jeu.LIMITE_METEO && this.getPile(Pile.METEO).size() < 1) {
            this.piocherCarteMeteo(piocheMeteo);
        }

        // Si le joueur a au moins une carte en main.
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
                    estTourTermine = numeroCarteJouee != -1;
                }
            } while (!estTourTermine);
        } else {
            // Le joueur n'a pas de cartes en main et doit passer son tour.
            this.parler("Je n'ai plus de cartes, je passe mon tour.\n");
            aPasseTour = true;
        }
        return aPasseTour;
    }

    public int choisirAction() {
        String messageErreur = "Oops, je me suis trompé(e) !\n\n";
        String input = null;
        String regex = "^[1-2]$";

        do {
            this.parler("Je choisis de 1-[Jouer une Carte] 2-[Passer] : ");
            input = lireChoix(regex, messageErreur);
        } while (input == null);

        return Integer.parseInt(input);
    }

    public int choisirCarte() {
        String messageErreur = "Oops, je me suis trompé(e) !\n\n";
        String input = null;
        String regex = "^[1-" + this.getMain().size() + "]$";

        if (!this.getMain().isEmpty()) {
            do {
                parler("Je choisis la carte n° ");
                input = lireChoix(regex, messageErreur);
            } while (input == null);
        } else {
            parler("Je n'ai pas de cartes en main :(\n");
        }

        return Integer.parseInt(input);
    }

    public int choisirJoueur(ArrayList<Joueur> joueurs) {
        String messageErreur = "Oops, je me suis trompé(e) !\n\n";
        String input = null;
        String regex = "^[";
        String choixDesJoueurs = "";

        // Construire le menu de sélection des joueurs.
        for (Joueur joueur : joueurs) {
            if (joueur.getId() != this.getId()) {
                choixDesJoueurs += (joueur.getId() + 1) + "-[" + joueur.getNom() + "] ";
                regex += (joueur.getId() + 1);
            }
        }
        regex += "]$";

        do {
            this.parler("Je choisis d'attaquer " + choixDesJoueurs + " : ");
            input = lireChoix(regex, messageErreur);
        } while (input == null);

        return Integer.parseInt(input);
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

    /**
     * Lit la une chaîne de caractère saisie dans la console. Les choix possibles
     * doivent vérifier l'expression régulière passée en entrée.
     * 
     * @param regex     Expression régulière qui doit vérifier la saisie du choix.
     * @param msgErreur Le message d'erreur à afficher en cas d'erreur de saisie.
     * @return La chaîne de caratère saisie, ou null si la saisie ne vérifie pas
     *         l'expression régulière.
     */
    private String lireChoix(String regex, String msgErreur) {
        String result = null;

        try {
            String input = scanner.nextLine().trim();

            if (Pattern.matches(regex, input)) {
                result = input;
            } else {
                this.parler(msgErreur);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
