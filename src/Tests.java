import java.util.ArrayList;

/**
 * Classe permettant de déninir des tests.
 */
public class Tests {
        /**
         * Tests de la classe Carte
         */
        public static void testCarte() {
                Carte carte1 = new Carte("adazd");
                Carte carte2 = new Carte("50");
                Carte carte3 = new Carte("roue de secours");

                afficheTest("carte1 devrait être non null", (carte1 != null));
                afficheTest("carte1 devrait être une carte non valide", (!carte1.estValide()));

                System.out.println("carte2 devrait être églal à 50 : " + carte2.toString());
                afficheTest("carte2 devrait être une carte valide", (carte2.estValide()));

                System.out.println("carte3 devrait être églal à Roue de Secours : " + carte3.toString());
        }

        /**
         * Tests de la classe PileCarte.
         */
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

        /**
         * Tests de la classe Configuration.
         */
        public static void testConfiguration() {
                ArrayList<String> ligneCartes1 = Configuration.parse("config.txt");
                afficheTest("config.txt devrait avoir 22 lignes de cartes", (ligneCartes1.size() == 22));

                ArrayList<String> ligneCartes2 = Configuration.parse("testFiles/config_test2.txt");
                afficheTest("config_test2.txt devrait avoir 7 lignes de cartes", (ligneCartes2.size() == 7));
                System.out.println("Lignes trouvées dans config_test2.txt");
                System.out.println(ligneCartes2);
        }

        /**
         * Tests de la classe Joueur.
         */
        public static void testJoueur() {
                Joueur nicolas = new JoueurHumain("Nicolas", 40);
                Joueur lea = new JoueurHumain("Lea", 12);
                // Joueur tom = new JoueurHumain("Tom", 13);

                Carte feuVert = new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT);
                Carte finLimiteVitesse = new Carte(Carte.TYPE_PARADE, Carte.FIN_LIMITE_VITESSE);
                Carte essence = new Carte(Carte.TYPE_PARADE, Carte.ESSENCE);

                Carte limiteVitesse = new Carte(Carte.TYPE_ATTAQUE, Carte.LIMITE_VITESSE);
                Carte feuRouge = new Carte(Carte.TYPE_ATTAQUE, Carte.FEU_ROUGE);
                Carte panneEssence = new Carte(Carte.TYPE_ATTAQUE, Carte.PANNE_ESSENCE);
                Carte creve = new Carte(Carte.TYPE_ATTAQUE, Carte.CREVE);
                Carte accident = new Carte(Carte.TYPE_ATTAQUE, Carte.ACCIDENT);

                Carte etape25 = new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_25);
                Carte etape50 = new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_50);
                Carte etape75 = new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_75);
                Carte etape100 = new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_100);
                Carte etape200 = new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_200);

                afficheTest("Nicolas doit pouvoir jouer Feu Vert",
                                nicolas.peutJouerParade(feuVert));

                afficheTest("Nicolas doit pouvoir jouer Limite de Vitesse sur Lea",
                                nicolas.peutAttaquerJoueur(limiteVitesse, lea));
                afficheTest("Nicolas ne doit pas pouvoir jouer Feu Rouge sur Lea",
                                !nicolas.peutAttaquerJoueur(feuRouge, lea));
                afficheTest("Nicolas ne doit pas pouvoir jouer Panne d'Essence sur Lea",
                                !nicolas.peutAttaquerJoueur(panneEssence, lea));
                afficheTest("Nicolas ne doit pas pouvoir jouer Crevé sur Lea",
                                !nicolas.peutAttaquerJoueur(creve, lea));
                afficheTest("Nicolas ne doit pas pouvoir jouer Accident sur Lea",
                                !nicolas.peutAttaquerJoueur(accident, lea));

                afficheTest("Lea ne doit pas pouvoir jouer une étape 25", !lea.peutJouerEtape(etape25));
                afficheTest("Lea ne doit pas pouvoir jouer une étape 50", !lea.peutJouerEtape(etape50));
                afficheTest("Lea ne doit pas pouvoir jouer une étape 75", !lea.peutJouerEtape(etape75));
                afficheTest("Lea ne doit pas pouvoir jouer une étape 100", !lea.peutJouerEtape(etape100));
                afficheTest("Lea ne doit pas pouvoir jouer une étape 200", !lea.peutJouerEtape(etape200));

                lea.getPile(Joueur.Pile.BATAILLE).push(new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT));
                afficheTest("Lea doit pouvoir jouer une étape 25", lea.peutJouerEtape(etape25));
                afficheTest("Lea doit pouvoir jouer une étape 50", lea.peutJouerEtape(etape50));
                afficheTest("Lea doit pouvoir jouer une étape 75", lea.peutJouerEtape(etape75));
                afficheTest("Lea doit pouvoir jouer une étape 100", lea.peutJouerEtape(etape100));
                afficheTest("Lea doit pouvoir jouer une étape 200", lea.peutJouerEtape(etape200));

                afficheTest("Lea ne doit pas pouvoir jouer une Fin de Limite de Vitesse",
                                !lea.peutJouerParade(finLimiteVitesse));
                lea.getPile(Joueur.Pile.VITESSE).push(new Carte(Carte.TYPE_ATTAQUE, Carte.LIMITE_VITESSE));
                afficheTest("Lea doit pouvoir jouer une étape 25", lea.peutJouerEtape(etape25));
                afficheTest("Lea doit pouvoir jouer une étape 50", lea.peutJouerEtape(etape50));
                afficheTest("Lea ne doit pas pouvoir jouer une étape 75", !lea.peutJouerEtape(etape75));
                afficheTest("Lea ne doit pas pouvoir jouer une étape 100", !lea.peutJouerEtape(etape100));
                afficheTest("Lea ne doit pas pouvoir jouer une étape 200", !lea.peutJouerEtape(etape200));

                lea.getPile(Joueur.Pile.BATAILLE).push(new Carte(Carte.TYPE_ATTAQUE, Carte.PANNE_ESSENCE));
                afficheTest("Lea ne doit pas pouvoir jouer une étape 25", !lea.peutJouerEtape(etape25));
                afficheTest("Lea ne doit pas pouvoir jouer une étape 50", !lea.peutJouerEtape(etape50));
                afficheTest("Lea ne doit pas pouvoir jouer une étape 75", !lea.peutJouerEtape(etape75));
                afficheTest("Lea ne doit pas pouvoir jouer une étape 100", !lea.peutJouerEtape(etape100));
                afficheTest("Lea ne doit pas pouvoir jouer une étape 200", !lea.peutJouerEtape(etape200));

                afficheTest("Lea doit pouvoir jouer une Fin de Limite de Vitesse",
                                lea.peutJouerParade(finLimiteVitesse));
                afficheTest("Lea doit pouvoir jouer une Essence", lea.peutJouerParade(essence));

                lea.getPile(Joueur.Pile.VITESSE).pop();
                lea.getPile(Joueur.Pile.BATAILLE).pop();
                lea.getPile(Joueur.Pile.BOTTE).push(new Carte(Carte.TYPE_BOTTE, Carte.CITERNE));
                afficheTest("Nicolas ne doit pas pouvoir jouer Panne d'Essence sur Lea",
                                !nicolas.peutAttaquerJoueur(panneEssence, lea));

                lea.getPile(Joueur.Pile.BATAILLE).pop();
                lea.getPile(Joueur.Pile.BOTTE).pop();
                lea.getPile(Joueur.Pile.BOTTE).push(new Carte(Carte.TYPE_BOTTE, Carte.PRIORITAIRE));
                afficheTest("Lea doit pouvoir jouer une étape 100", lea.peutJouerEtape(etape100));
                afficheTest("Lea ne doit pas pouvoir jouer Feu Vert", lea.peutJouerParade(feuVert));

                afficheTest("Nicolas ne doit pas pouvoir jouer Limite de Vitesse sur Lea",
                                !nicolas.peutAttaquerJoueur(limiteVitesse, lea));
                afficheTest("Nicolas ne doit pas pouvoir jouer Feu Rouge sur Lea",
                                !nicolas.peutAttaquerJoueur(feuRouge, lea));

                lea.getMain().add(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_100));
                lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.PANNE_ESSENCE));
                lea.getMain().add(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_25));
                lea.getMain().add(new Carte(Carte.TYPE_BOTTE, Carte.CITERNE));
                lea.getMain().add(new Carte(Carte.TYPE_BOTTE, Carte.PRIORITAIRE));
                lea.getMain().add(new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT));
                int numeroCarte = lea.chercherCarteCoupFourre(new Carte(Carte.TYPE_ATTAQUE, Carte.LIMITE_VITESSE));
                afficheTest("Lea peut faire un coup-fourré avec sa carte n° 5",
                                numeroCarte == 5);
                numeroCarte = lea.chercherCarteCoupFourre(new Carte(Carte.TYPE_ATTAQUE, Carte.FEU_ROUGE));
                afficheTest("Lea peut faire un coup-fourré avec sa carte n° 5",
                                numeroCarte == 5);
                numeroCarte = lea.chercherCarteCoupFourre(new Carte(Carte.TYPE_ATTAQUE, Carte.PANNE_ESSENCE));
                afficheTest("Lea peut faire un coup-fourré avec sa carte n° 4",
                                numeroCarte == 4);
                numeroCarte = lea.chercherCarteCoupFourre(new Carte(Carte.TYPE_ATTAQUE, Carte.CREVE));
                afficheTest("Lea ne peut pas faire un coup-fourré contre Crevé",
                                numeroCarte == -1);
        }

        private static void afficheTest(String message, boolean test) {
                System.out.print(Affichage.Color.BOLD + message + Affichage.Color.END);
                if (test) {
                        System.out.println(" : " + Affichage.Color.GREEN + test + Affichage.Color.END);
                } else {
                        System.out.println(" : " + Affichage.Color.RED + test + Affichage.Color.END);
                }
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

                System.out.println("--------------------------------");
                System.out.println("Tests de la classe Joueur");
                System.out.println("--------------------------------");
                testJoueur();
                System.out.println();

        }
}
