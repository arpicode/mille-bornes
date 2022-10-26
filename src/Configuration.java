import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
            // TODO créer le fichier par défault
            System.out.println("Création du fichier de configuration...");
        }

        FileInputStream stream = null;
        try {
            stream = new FileInputStream(fullFileName);
        } catch (FileNotFoundException exception) {
            System.out.println(exception.getMessage());
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String currentLine;
        int numLigne = 1;

        // Lire le fichier ligne par ligne
        try {
            while ((currentLine = reader.readLine()) != null) {

                if (!isComment(currentLine)) {
                    currentLine = getCardFromLine(currentLine);
                    if (currentLine != null) {
                        result.add(currentLine);
                    }
                }

                numLigne++;
            }

        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            System.out.println("N° de ligne : " + numLigne);
        }

        return result;
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
        final String regexp = "^(\\d+\\s*;\\s*[a-z0-9'\\- ]+[a-z0-9])";
        final Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
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
