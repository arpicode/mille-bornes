import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe responsable de la configuration du jeu. Elle permet la lecture du
 * fichier de configuration mais n'est pas responsable de la validité des cartes
 * présentes dans le fichier.
 * 
 * @author Les Bornés
 */
public class Configuration {

    // Expression régulière qui match une ligne qui commence avec une paire de
    // valeurs possibles : nombre_de_cartes;nom_de_la_carte. (Il est permis
    // d'avoir des espaces devant la ligne et autour du ';'.)
    public static final String cardRegexp = "^(\\s*\\d+\\s*;\\s*[\\d\\p{L}\\-' ]+[\\d\\p{L}])";
    private static final Pattern cardPattern = Pattern.compile(cardRegexp);

    /**
     * Parse le fichier de configuration.
     * 
     * @return ArrayList contenant les lignes qui correspondent à une
     *         paire de : nombre_de_cartes;nom_de_la_carte.
     */
    public static ArrayList<String> parse(String fullFileName) {
        ArrayList<String> result = new ArrayList<String>();

        // Regarder si le fichier de configuration existe
        if (!exists(fullFileName)) {
            System.out.println("Création du fichier de configuration par défaut...");
            createDefault(fullFileName);
        }

        try {
            System.out.println("Lecture du fichier de configuration...");
            List<String> lines = Files.readAllLines(Paths.get(fullFileName), StandardCharsets.UTF_8);

            for (String currentLine : lines) {
                // Ignorer les lignes de commentaire
                if (!isComment(currentLine)) {
                    // Récupérer ce qui pourrait être une carte
                    currentLine = getCardFromLine(currentLine);
                    if (currentLine != null) {
                        result.add(currentLine);
                    }
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            System.err.println("Une erreur est survenue pendant la lecture du fichier de configuration.");
        }
        System.out.println("Terminée.\n");

        if (result.size() == 0) {
            System.out.println("Le fichier de configuration n'a pas de cartes !");
            System.exit(1);
        }

        return result;
    }

    /**
     * Crée le fichier de configuration par défaut.
     * 
     * @param fileName nom du fichier créé.
     */
    private static void createDefault(String fileName) {
        List<String> lines = Arrays.asList(
                "#  __  __ _ _ _        ____",
                "# |  \\/  (_) | | ___  | __ )  ___  _ __ _ __   ___  ___",
                "# | |\\/| | | | |/ _ \\ |  _ \\ / _ \\| '__| '_ \\ / _ \\/ __|",
                "# | |  | | | | |  __/ | |_) | (_) | |  | | | |  __/\\__ \\",
                "# |_|  |_|_|_|_|\\___| |____/ \\___/|_|  |_| |_|\\___||___/",
                "#",
                "# Bienvenu dans le fichier de configuration du Mille Bornes !",
                "#",
                "# Ici vous avez la possibilité de changer les quantités des",
                "# différentes cartes du jeu. Le symbole '#' vous permet",
                "# d'écrire un commentaire qui sera ignoré.",
                "#",
                "# Par exemple :",
                "# Si vous voulez jouer avec 5 cartes 'Accident' au lieu de 3,",
                "# vous pouvez modifier la ligne '3;Accident' en '5;Accident'.",
                "# Une autre possibilité serait de créer une nouvelle ligne en",
                "# indiquant '2;Accident' et ces 2 cartes seraient ajoutées aux",
                "# 3 présentes par défaut.",
                "#",
                "# Pour recréer la configuration par défaut vous pouvez toujours",
                "# supprimer ce fichier et relancer le jeu.",
                "#",
                "",
                "",
                "# Étapes",
                "10;" + Carte.getNom(Carte.TYPE_ETAPE, Carte.ETAPE_25),
                "10;" + Carte.getNom(Carte.TYPE_ETAPE, Carte.ETAPE_50),
                "10;" + Carte.getNom(Carte.TYPE_ETAPE, Carte.ETAPE_75),
                "12;" + Carte.getNom(Carte.TYPE_ETAPE, Carte.ETAPE_100),
                "4;" + Carte.getNom(Carte.TYPE_ETAPE, Carte.ETAPE_200),
                "",
                "# Attaques",
                "5;" + Carte.getNom(Carte.TYPE_ATTAQUE, Carte.FEU_ROUGE),
                "4;" + Carte.getNom(Carte.TYPE_ATTAQUE, Carte.LIMITE_VITESSE),
                "3;" + Carte.getNom(Carte.TYPE_ATTAQUE, Carte.PANNE_ESSENCE),
                "3;" + Carte.getNom(Carte.TYPE_ATTAQUE, Carte.CREVE),
                "3;" + Carte.getNom(Carte.TYPE_ATTAQUE, Carte.ACCIDENT),
                "",
                "# Parades",
                "14;" + Carte.getNom(Carte.TYPE_PARADE, Carte.FEU_VERT),
                "6;" + Carte.getNom(Carte.TYPE_PARADE, Carte.FIN_LIMITE_VITESSE),
                "6;" + Carte.getNom(Carte.TYPE_PARADE, Carte.ESSENCE),
                "6;" + Carte.getNom(Carte.TYPE_PARADE, Carte.ROUE_SECOURS),
                "6;" + Carte.getNom(Carte.TYPE_PARADE, Carte.REPARATION),
                "",
                "# Bottes",
                "1;" + Carte.getNom(Carte.TYPE_BOTTE, Carte.PRIORITAIRE),
                "1;" + Carte.getNom(Carte.TYPE_BOTTE, Carte.CITERNE),
                "1;" + Carte.getNom(Carte.TYPE_BOTTE, Carte.INCREVABLE),
                "1;" + Carte.getNom(Carte.TYPE_BOTTE, Carte.AS_VOLANT),
                "",
                "# Météo",
                "3;" + Carte.getNom(Carte.TYPE_METEO, Carte.NEIGE),
                "3;" + Carte.getNom(Carte.TYPE_METEO, Carte.BEAU_TEMPS),
                "3;" + Carte.getNom(Carte.TYPE_METEO, Carte.VENT_DOS));

        try {
            Files.write(Paths.get(fileName), lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
        } catch (IOException exception) {
            System.err.println("Une erreur est survenue pendant la création du fichier de configuration.");
        }
    }

    /**
     * Teste si le fichier de configuration existe.
     * 
     * @return true s'il existe, false si non.
     */
    public static boolean exists(String fullFileName) {
        return Files.exists(Paths.get(fullFileName));
    }

    /**
     * Vérifie si la ligne contient une chaîne de caractère qui correspond au
     * format nombre_de_cartes;nom_de_la_carte. (Cette paire de valeur ne
     * représente pas forcement une carte valide.)
     * 
     * @param line String ligne à vérifier.
     * @return String correspondant à une paire de valeurs au format
     *         nombre_de_cartes;nom_de_la_carte, ou null si le motif n'est
     *         pas présent.
     */
    private static String getCardFromLine(String line) {
        String tmp = line.trim();
        final Matcher matcher = cardPattern.matcher(tmp);

        if (matcher.find()) {
            return matcher.group(0);
        }

        return null;
    }

    /**
     * Teste si la ligne est un commentaire ou vide.
     * 
     * @param line Une ligne lue dans le fichier de configuration.
     * @return true si c'est un commentaire ou vide, false si non.
     */
    private static boolean isComment(String line) {
        return line.trim().isEmpty() || line.trim().charAt(0) == '#';
    }
}
