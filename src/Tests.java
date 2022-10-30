import java.util.ArrayList;

public class Tests {
    // ----- Tests de la classe Carte
    public static void testCarte() {
        Carte carte1 = new Carte("adazd");
        Carte carte2 = new Carte("50");
        Carte carte3 = new Carte("roue de secours");

        System.out.println("carte1 devrait être non null : " + (carte1 != null));
        System.out.println("carte1 devrait être une carte non valide : " + (!carte1.estValide()));

        System.out.println("carte2 devrait être églal à 50 : " + carte2.toString());
        System.out.println("carte2 devrait être une carte valide : " + (carte2.estValide()));

        System.out.println("carte3 devrait être églal à Roue de Secours : " + carte3.toString());
    }

    public static void testPileCarte() {
        ArrayList<Carte> cartes = new ArrayList<Carte>();
        cartes.add(new Carte("Feu Vert"));
        cartes.add(new Carte("50"));
        cartes.add(new Carte("Citerne"));
        cartes.add(new Carte("100"));
        cartes.add(new Carte("200"));
        cartes.add(new Carte("Crevé"));

        Pioche pioche = new Pioche(cartes);
        System.out.print("pioche devrait être égale à [Feu Vert, 50, Citerne, 100, 200, Crevé] : ");
        System.out.println(pioche.toString());

        pioche.melanger();
        System.out.println("pioche après mélange : " + pioche);
    }

    public static void testConfiguration() {
        ArrayList<String> ligneCartes1 = Configuration.parse("config.txt");
        System.out.println("config.txt devrait avoir 22 lignes de cartes : " + (ligneCartes1.size() == 22));

        ArrayList<String> ligneCartes2 = Configuration.parse("testFiles/config_test2.txt");
        System.out.println("config_test2.txt devrait avoir 7 lignes de cartes : " + (ligneCartes2.size() == 7));
        System.out.println("Lignes trouvées dans config_test2.txt");
        System.out.println(ligneCartes2);
    }

    public static void main(String[] args) {
        System.out.println("------------------------");
        System.out.println("Tests de la classe Carte");
        System.out.println("------------------------");
        testCarte();
        System.out.println();

        System.out.println("-----------------------------");
        System.out.println("Tests de la classe PileCartes");
        System.out.println("-----------------------------");
        testPileCarte();
        System.out.println();

        System.out.println("--------------------------------");
        System.out.println("Tests de la classe Configuration");
        System.out.println("--------------------------------");
        testConfiguration();
        System.out.println();

    }
}
