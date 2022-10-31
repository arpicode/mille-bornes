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
        clearScreen();
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
        final String messageErreur = "Erreur de saisie ! Veuillez recommencer.\n";
        int nbJoueurs = 0;

        do {
            System.out.printf("Entrez le nombre de joueurs (entre %d et %d) : ",
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
     * Efface la console.
     */
    public static void clearScreen() {
        System.out.println("\033\143");
    }
}
