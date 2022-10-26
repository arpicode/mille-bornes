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

    public static void testPileCarte() {
        ArrayList<Carte> cartes = new ArrayList<Carte>();
        cartes.add(new Carte("25"));
        cartes.add(new Carte("50"));
        cartes.add(new Carte("75"));
        cartes.add(new Carte("100"));
        cartes.add(new Carte("200"));
        cartes.add(new Carte("Crevé"));

        PileCartes pioche = new PileCartes(cartes);
        System.out.print(
                "pioche devrait être égale à [[Étape] 25, [Étape] 50, [Étape] 75, [Étape] 100, [Étape] 200, [Attaque] Crevé] : ");
        System.out.println(pioche.toString()
                .compareTo("[[Étape] 25, [Étape] 50, [Étape] 75, [Étape] 100, [Étape] 200, [Attaque] Crevé]") == 0);

        PileCartes.melanger(pioche);
        System.out.println("pioche après mélange : " + pioche);
    }

    public static void testConfiguration() {
        ArrayList<String> ligneCartes = Configuration.parse("config.txt");
        System.out.println("La config par défaut devrait avoir 22 lignes de cartes : " + (ligneCartes.size() == 22));

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
