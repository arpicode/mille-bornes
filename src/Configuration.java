import java.io.File;
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

public class Configuration {

    /**
     * Parse le fichier de configuration.
     * 
     * @return ArrayList<String> contenant les lignes qui correspondent à une
     *         paire de : <nombre_de_cartes>;<nom_de_la_carte>.
     */
    public static ArrayList<String> parse(String fullFileName) {
        ArrayList<String> result = new ArrayList<String>();

        // Regarder si le fichier de configuration existe
        if (!exists(fullFileName)) {
            // TODO l'affichage du message devra être fait par la classe Affichage
            System.out.println("Création du fichier de configuration par défaut...");
            createDefault(fullFileName);
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get(fullFileName), StandardCharsets.UTF_8);

            for (String currentLine : lines) {
                // Ignorer les lignes de commentaire
                if (!isComment(currentLine)) {
                    // Récuperer ce qui pourrait être une carte
                    currentLine = getCardFromLine(currentLine);
                    if (currentLine != null) {
                        result.add(currentLine);
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return result;
    }

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
                "",
                "# Étapes",
                "10;25",
                "10;50",
                "10;75",
                "12;100",
                "4;200",
                "",
                "# Attaques",
                "3;Accident",
                "3;Panne d'Essence",
                "3;Crevé",
                "4;Limitation de Vitesse",
                "5;Stop",
                "",
                "# Défense",
                "6;Réparations",
                "6;Essence",
                "6;Roue de Secours",
                "6;Fin de Limitation de Vitesse",
                "14;Roulez",
                "",
                "# Bottes",
                "1;As du volant",
                "1;Citerne",
                "1;Increvable",
                "1;Prioritaire",
                "",
                "# Météo",
                "3;Neige",
                "3;Beau Temps",
                "3;Vent dans le Dos");

        try {
            Files.write(Paths.get(fileName), lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
        } catch (IOException exeption) {
            exeption.printStackTrace();
        }
    }

    /**
     * Teste si le fichier de configuration existe.
     * 
     * @return true s'il existe, false si non.
     */
    private static boolean exists(String fullFileName) {
        return new File(fullFileName).isFile();
    }

    /**
     * Vérifie si la ligne contient une chaîne de caractère qui correspond au
     * format <nombre_de_cartes>;<nom_de_la_carte>.
     * 
     * @param line
     * @return String correspondant à une paire
     *         <nombre_de_cartes>;<nom_de_la_carte>, ou null si le motif n'est pas
     *         présent.
     */
    private static String getCardFromLine(String line) {
        String tmp = line.trim();

        // Expression régulière qui match une ligne qui commence avec une paire
        // possible <nombre_de_cartes>;<nom_de_la_carte>
        final String regexp = "^(\\d+\\s*;\\s*[a-z0-9é'\\- ]+[a-z0-9é])";
        final Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(tmp);

        if (matcher.find()) {
            return matcher.group(0);
        }

        return null;
    }

    /**
     * Teste si la ligne est un commentaire.
     * 
     * @param line
     * @return true si c'est un commentaire, false si non.
     */
    private static boolean isComment(String line) {
        if (line.trim().isEmpty())
            return true;
        return line.trim().charAt(0) == '#';
    }
}
