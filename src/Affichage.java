import java.util.ArrayList;
import java.util.Scanner;

public class Affichage {
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Classe static interne qui défini les couleurs d'affichage de la console.
     */
    public static class Color {
        public static final String BLACK = "\033[30m";
        public static final String GRAY = "\033[90m";
        public static final String RED = "\033[31m";
        public static final String LIGHT_RED = "\033[91m";
        public static final String GREEN = "\033[32m";
        public static final String LIGHT_GREEN = "\033[92m";
        public static final String YELLOW = "\033[33m";
        public static final String LIGHT_YELLOW = "\033[93m";
        public static final String BLUE = "\033[34m";
        public static final String LIGHT_BLUE = "\033[94m";
        public static final String MAGENTA = "\033[35m";
        public static final String LIGHT_MAGENTA = "\033[95m";
        public static final String CYAN = "\033[36m";
        public static final String LIGHT_CYAN = "\033[96m";
        public static final String WARNING = "\033[30m\033[43m";
        public static final String END = "\033[0m";
        public static final String BOLD = "\033[1m";
    }

    /**
     * Affiche le nom du jeu : Mille Bornes.
     */
    public static void nomJeu() {
        System.out.println(Color.CYAN + " __  __ _ _ _" + Color.LIGHT_RED
                + "        ____");
        System.out.println(Color.CYAN + "|  \\/  (_) | | ___  " + Color.LIGHT_RED
                + "| __ )  ___  _ __ _ __   ___  ___");
        System.out.println(Color.CYAN + "| |\\/| | | | |/ _ \\" + Color.LIGHT_RED
                + " |  _ \\ / _ \\| '__| '_ \\ / _ \\/ __|");
        System.out.println(Color.CYAN + "| |  | | | | |  __/ " + Color.LIGHT_RED
                + "| |_) | (_) | |  | | | |  __/\\__ \\");
        System.out.println(Color.CYAN + "|_|  |_|_|_|_|\\___| " + Color.LIGHT_RED
                + "|____/ \\___/|_|  |_| |_|\\___||___/"
                + Color.END);
        System.out.println();
    }

    /**
     * Demander à l'utilisateur de saisir le nombre de joueurs jusqu'à ce que la
     * saisie soit valide.
     * 
     * @return le nombre de joueurs.
     */
    public static int saisieNombreJoueurs() {
        final String messageErreur = Color.RED + "Erreur de saisie ! Veuillez recommencer.\n" + Color.END;
        int nbJoueurs = 0;

        do {
            System.out.printf(Color.YELLOW + "Entrez le nombre de joueurs (entre %d et %d) : " + Color.END,
                    Jeu.NB_JOUEURS_MIN,
                    Jeu.NB_JOUEURS_MAX);

            // Vérifier si la prochaine valeur est un entier
            if (scanner.hasNextInt()) {
                nbJoueurs = scanner.nextInt();

                // Vérifier si l'entier saisi est inclus dans [NB_JOUEURS_MIN;NB_JOUEURS_MAX]
                if (nbJoueurs < Jeu.NB_JOUEURS_MIN || nbJoueurs > Jeu.NB_JOUEURS_MAX) {
                    System.out.println(messageErreur);
                }
            } else {
                // L'utilisateur n'a pas saisi un entier
                System.out.println(messageErreur);
                scanner.nextLine();
            }
        } while (nbJoueurs < Jeu.NB_JOUEURS_MIN || nbJoueurs > Jeu.NB_JOUEURS_MAX);

        return nbJoueurs;
    }

    /**
     * Demander à l'utilisateur de saisir le nom et l'âge d'un joueur jusqu'à ce
     * que la saisie soit valide.
     * 
     * @return Le nom et l'âge du joueur sous la forme <nom>;<âge>.
     */
    public static String saisieNomEtAgeJoueur(int numeroJoueur) {
        String nomEtAge = "";
        boolean estValide = false;

        do {
            System.out.print(Color.YELLOW);
            System.out.printf("Entrez le nom (sans accents) et l'âge du joueur n°%d :\n", numeroJoueur);
            System.out.print(Color.GRAY);
            System.out.println(
                    "(Un nom doit contenir 3 caractères minimum : des lettres et '-'. Exemple : Jean-Pierre 14)");
            System.out.print(Color.END);

            // Vérifier si la prochaine valeur est un nom valide
            if (scanner.hasNext("\\p{L}[\\p{L}\\-]+\\p{L}")) {
                nomEtAge += scanner.next();
                estValide = true;

                // Vérifier si la prochaine valeur est un entier
                if (scanner.hasNextInt()) {
                    nomEtAge += ";" + scanner.nextInt();
                } else {
                    // L'utilisateur n'a pas saisi un âge valide
                    System.out.print(Color.RED);
                    System.out.println("Erreur de saisie ! l'âge n'est pas valide.\n");
                    System.out.print(Color.END);
                    estValide = false;
                    nomEtAge = "";
                    scanner.nextLine();

                }
            } else {
                // L'utilisateur n'a pas saisi un nom valide
                System.out.print(Color.RED);
                System.out.println("Erreur de saisie ! le nom n'est pas valide.\n");
                System.out.print(Color.END);
                scanner.nextLine();
            }
        } while (!estValide);

        return nomEtAge;
    }

    public static void annoncerJoueur(Joueur joueur) {
        clearScreen();
        System.out.print(Color.YELLOW + "C'est au tour de " + Color.END);
        System.out.print(joueur.getNom());
        System.out.println(Color.YELLOW + " de jouer !\n" + Color.END);
    }

    /**
     * Affiche la main d'un joueur.
     * 
     * @param joueur joueur
     */
    public static void mainJoueur(Joueur joueur) {
        System.out.print(Color.YELLOW + "\nMain de " + Color.END);
        System.out.println(joueur.getNom() + Color.YELLOW + " : " + Color.END);

        for (int i = 0; i < joueur.getMain().size(); i++) {
            System.out.print((i + 1) + "-" + "[" + joueur.getMain().get(i) + "] ");
        }

        System.out.println("\n");
    }

    /**
     * Affiche le score final de la manche de tous les joueurs.
     * 
     * @param joueurs Joueurs trié par Km parcourus décroissant.
     */
    public static void scoreFinalManche(ArrayList<Joueur> joueurs) {
        for (int i = 0; i < joueurs.size(); i++) {
            System.out.print(Color.YELLOW + (i + 1) + Color.END + " - " + joueurs.get(i).getNom() + " ");
            if (joueurs.get(i).estGagnant()) {
                System.out.print(Color.LIGHT_GREEN + "Gagnant ! " + Color.END);
            }
            System.out.print(":\t");
            System.out.println(Color.BOLD + joueurs.get(i).getScore() + Color.END);
        }
    }

    public static void message(String message) {
        System.out.println(Color.YELLOW + message + Color.END);
    }

    /**
     * Affiche la zone de jeu d'un joueur.
     * 
     * @param joueur joueur.
     */
    public static void zoneDeJeu(Joueur joueur, int idJoueurCourant) {
        // Affiche le nom du joueur courant en blanc et les autres en gris.
        System.out.print(Color.YELLOW + "Zone de jeu de ");
        if (joueur.getId() == idJoueurCourant) {
            System.out.print(Color.END);
        } else {
            System.out.print(Color.GRAY);
        }
        System.out.print(joueur.getNom());
        System.out.println(Color.YELLOW + " : " + Color.END);

        // Afficher la zone VITESSE
        affichePile(joueur, "Vitesse  ", Joueur.Pile.VITESSE);
        // Afficher la zone BATTAILLE
        affichePile(joueur, "Battaille", Joueur.Pile.BATAILLE);
        // Afficher la zone BOTTE
        affichePile(joueur, "Botte    ", Joueur.Pile.BOTTE);
        // Afficher la zone METEO
        affichePile(joueur, "Météo    ", Joueur.Pile.METEO);
        // Afficher la zone BATTAILLE
        affichePile(joueur, "Étape    ", Joueur.Pile.ETAPE);
        System.out.println();
    }

    /**
     * Affiche une pile de carte de la zone de jeu
     * 
     * @param joueur joueur
     * @param pile   nom de la pile
     * @param zone   type de zone de la pile
     */
    private static void affichePile(Joueur joueur, String pile, Joueur.Pile zone) {
        System.out.print("  " + pile + " : ");
        for (Carte carte : joueur.getZoneDeJeu().get(zone)) {
            System.out.print("[" + carte + "]");
        }
        System.out.println();
    }

    /**
     * Efface la console.
     */
    public static void clearScreen() {
        System.out.println("\033\143");
    }
}
