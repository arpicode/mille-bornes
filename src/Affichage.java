import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe qui permet au Jeu d'afficher ses messages aux joueurs.
 * 
 * @author Les Bornés
 */
public class Affichage {
    private static Scanner scanner = new Scanner(System.in, "IBM850");

    private Affichage() {
    }

    /**
     * Classe static interne qui défini les couleurs d'affichage de la console.
     */
    // CHECKSTYLE:OFF
    public static final class Color {
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
    // CHECKSTYLE:ON

    /**
     * Affiche le titre du jeu : Mille Bornes.
     */
    public static void titreJeu() {
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
        final String messageErreur = Color.RED + "Ce n'est pas un nombre entre " + Jeu.NB_JOUEURS_MIN + " et "
                + Jeu.NB_JOUEURS_MAX + " !" + Color.END;
        String input = null;
        String regex = "^[" + Jeu.NB_JOUEURS_MIN + "-" + Jeu.NB_JOUEURS_MAX + "]$";

        do {
            System.out.printf(Color.YELLOW + "Entrez le nombre de joueurs (entre %d et %d) : " + Color.END,
                    Jeu.NB_JOUEURS_MIN,
                    Jeu.NB_JOUEURS_MAX);

            input = lireSaisie(regex, messageErreur);

        } while (input == null);

        return Integer.parseInt(input);
    }

    /**
     * Demander à l'utilisateur de saisir le nom et l'âge d'un joueur jusqu'à ce
     * que la saisie soit valide.
     * 
     * @param numeroJoueur Le numéro d'ordre de saisi du joueur.
     * @return Le nom et l'âge du joueur sous la forme d'une String nom;âge.
     */
    public static String saisieNomEtAgeJoueur(int numeroJoueur) {
        String input = null;
        String regex = "^(\\p{L}+[-]{0,1}\\p{L}+)\\s+(\\d+)$";
        String error = Color.RED + "Erreur de saisie !" + Color.END;

        do {
            System.out.printf(Color.YELLOW + "Entrez le nom et l'âge du joueur n°%d : " + Color.END, numeroJoueur);
            input = lireSaisie(regex, error);

        } while (input == null);

        return input;
    }

    /**
     * Affiche le joueur dont c'est le tour de jouer.
     * 
     * @param joueur Joueur.
     */
    public static void annoncerJoueur(Joueur joueur) {
        System.out.print(Color.YELLOW + "C'est au tour de " + Color.END);
        System.out.print(Color.BOLD + joueur.getNom() + Color.END);
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
     * Affiche le score de la manche de tous les joueurs.
     * 
     * @param joueurs Joueurs trié par Km parcourus décroissant.
     */
    public static void scoreManche(ArrayList<Joueur> joueurs) {
        for (int i = 0; i < joueurs.size(); i++) {
            System.out.print(Color.YELLOW + (i + 1) + Color.END + " - " + joueurs.get(i).getNom() + " :\t");
            System.out.println(Color.BOLD + joueurs.get(i).getScore() + Color.END);
        }
    }

    /**
     * Permet d'afficher un message.
     * 
     * @param message Message à afficher.
     */
    public static void message(String message) {
        System.out.println(Color.YELLOW + message + Color.END);
    }

    /**
     * Affiche un message demandant au joueur d'appuyer sur ENTRER pour
     * continuer.
     * 
     * @param joueur Joueur qui doit agir ou null.
     */
    public static void attendre(Joueur joueur) {
        if (joueur != null) {
            System.out.println(joueur.getNom() + Color.YELLOW + ", appuie sur ENTRER pour continuer..." + Color.END);

        } else {
            System.out.println(Color.YELLOW + "Appuyez sur ENTRER pour continuer..." + Color.END);
        }
        try {
            System.in.read();
            scanner.nextLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche le nom et la zone de jeu d'un joueur.
     * 
     * @param joueur          Joueur dont on affiche la zone de jeu.
     * @param idJoueurCourant L'id du joueur dont c'est le tour.
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

        affichePile(joueur, "Vitesse  ", Joueur.Pile.VITESSE);
        affichePile(joueur, "Battaille", Joueur.Pile.BATAILLE);
        affichePile(joueur, "Botte    ", Joueur.Pile.BOTTE);
        affichePile(joueur, "Météo    ", Joueur.Pile.METEO);
        affichePile(joueur, "Étape    ", Joueur.Pile.ETAPE);
        System.out.println();
    }

    /**
     * Efface la console.
     */
    public static void clearScreen() {
        String emptyLines = "";
        for (int i = 0; i < 60; i++) {
            emptyLines += "\n";
        }
        System.out.println(emptyLines);
        System.out.println("\033\143");
    }

    /**
     * Affiche une pile de carte de la zone de jeu.
     * 
     * @param joueur Joueur.
     * @param nom    Nom de la pile.
     * @param pile   Type de pile.
     */
    private static void affichePile(Joueur joueur, String nom, Joueur.Pile pile) {
        System.out.print("  " + nom + " : ");
        for (Carte carte : joueur.getPile(pile)) {
            System.out.print("[" + carte + "]");
        }
        System.out.println();
    }

    /**
     * Lit la chaîne de caractère saisie dans la console. Elle doit matcher
     * l'expression regulière regex. S'il y a des groupes capturés ils sont
     * retournés dans le résultat séparés par ';'.
     * (i.e. : groupe1;groupe2; ... ;groupeN).
     * 
     * @param regex     Expression régulière que doit vérifier la saisie.
     * @param msgErreur Le message d'erreur à afficher en cas d'erreur de saisie.
     * @return La chaîne de caratère saisie, ou null si la saisie ne vérifie pas
     *         l'expression régulière.
     */
    private static String lireSaisie(String regex, String msgErreur) {
        final Pattern pattern = Pattern.compile(regex);
        String result = "";

        try {
            String input = scanner.nextLine().trim();
            final Matcher matcher = pattern.matcher(input);

            if (matcher.find()) {
                if (matcher.groupCount() == 0) {
                    result = matcher.group(0);
                } else {
                    for (int i = 1; i <= matcher.groupCount(); i++) {
                        result += matcher.group(i);
                        if (i < matcher.groupCount()) {
                            result += ";";
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result.isEmpty()) {
            result = null;
            System.out.println(msgErreur);
        }

        return result;
    }

}
