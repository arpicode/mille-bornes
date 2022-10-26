import java.util.ArrayList;

public class Tests {
    // ----- Tests de la classe Carte
    public static void testCarte() {
        Carte carte1 = new Carte("adazd");
        Carte carte2 = new Carte("50");
        Carte carte3 = new Carte("roue de secours");

        System.out.println("carte1 devrait être non null : " + (carte1 != null));
        System.out.println("carte1 devrait être une carte non valide : " + (!carte1.estValide()));
        System.out.println(
                "carte2 devrait être églal à [Étape] 50 : " + (carte2.toString().compareTo("[Étape] 50") == 0));
        System.out.println("carte2 devrait être une carte valide : " + (carte2.estValide()));
        System.out.println("carte3 devrait être églal à [Défense] Roue de Secours : "
                + (carte3.toString().compareTo("[Défense] Roue de Secours") == 0));

    }

    public static void testPileDeCarte() {
        Carte[] cartes = new Carte[] {
                new Carte("25"),
                new Carte("50"),
                new Carte("75"),
                new Carte("100"),
                new Carte("200"),
                new Carte("Crevé"),
                new Carte("fin de limite de vitesse")
        };

        cartes = PileCartes.melanger(cartes);
        PileCartes pioche = new PileCartes(cartes);

        System.out.println(pioche);
        System.out.println(pioche.peek());
    }

    public static void testConfiguration() {
        ArrayList<String> cartes = Configuration.parse("./testFiles/config_test1.txt");

        for (String carte : cartes) {
            System.out.println(carte);
        }
    }

    public static void main(String[] args) {
        System.out.println("------------------------");
        System.out.println("Tests de la classe Carte");
        System.out.println("------------------------");
        testCarte();
        System.out.println();

        System.out.println("-------------------------------");
        System.out.println("Tests de la classe PileDeCartes");
        System.out.println("-------------------------------");
        testPileDeCarte();
        System.out.println();

        System.out.println("--------------------------------");
        System.out.println("Tests de la classe Configuration");
        System.out.println("--------------------------------");
        testConfiguration();
        System.out.println();

    }
}
