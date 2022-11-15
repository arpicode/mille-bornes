import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class JoueurTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private static final Joueur nico = new JoueurHumain("Nico", 34);
    private final Joueur lea = new JoueurHumain("Léa", 12);
    private final Joueur tom = new JoueurHumain("Tom", 13);

    @BeforeEach
    void setUpStream() {
        System.setOut(new PrintStream(outContent));
    }

    @BeforeEach
    void disableInfos() {
        lea.setEstInfosAffichees(false);
    }

    @AfterEach
    void restoreStream() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("it should create an instance of Joueur.")
    void testJoueur() {
        assumeTrue(nico != null);
        assertAll("joueur",
                () -> assertEquals(0, nico.getId()),
                () -> assertEquals("Nico", nico.getNom()),
                () -> assertEquals(34, nico.getAge()),
                () -> assertTrue(nico.getMain().isEmpty()),
                () -> assertTrue(nico.getKmParcourus() == 0),
                () -> assertTrue(nico.getScore() == 0));

        for (Joueur.Pile pile : Joueur.Pile.values()) {
            assertTrue(nico.getPile(pile).isEmpty());
        }
    }

    @Test
    @DisplayName("it should validate being able to play an 'Étape'.")
    void testPeutJouerEtape() {
        lea.getPile(Joueur.Pile.BATAILLE).add(new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT));
        assertAll("joueur",
                () -> assertTrue(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_25))),
                () -> assertTrue(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_50))),
                () -> assertTrue(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_75))),
                () -> assertTrue(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_100))),
                () -> assertTrue(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_200))));
    }

    @Test
    @DisplayName("it should invalidate being able to play an 'Étape'.")
    void testNePeutPasJouerEtape() {
        assertAll("joueur",
                () -> assertFalse(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_25))),
                () -> assertFalse(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_50))),
                () -> assertFalse(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_75))),
                () -> assertFalse(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_100))),
                () -> assertFalse(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_200))));
    }

    @Test
    @DisplayName("it should invalidate being able to play an 'Étape' > 50.")
    void testJouerEtapeSousLimiteDeVitesse() {

        lea.getPile(Joueur.Pile.BATAILLE).add(new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT));
        lea.getPile(Joueur.Pile.VITESSE).add(new Carte(Carte.TYPE_ATTAQUE, Carte.LIMITE_VITESSE));

        assertAll("joueur",
                () -> assertTrue(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_25))),
                () -> assertTrue(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_50))),

                () -> assertFalse(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_75))),
                () -> assertFalse(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_100))),
                () -> assertFalse(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_200))));
    }

    @Test
    @DisplayName("it should invalidate being able to play an 'Étape 200' when two are in play.")
    void testJouerEtapeAboveTwo200() {

        lea.getPile(Joueur.Pile.BATAILLE).add(new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT));
        lea.getPile(Joueur.Pile.ETAPE).add(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_200));
        lea.getPile(Joueur.Pile.ETAPE).add(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_200));

        assertAll("joueur",
                () -> assertTrue(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_25))),
                () -> assertTrue(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_50))),
                () -> assertTrue(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_75))),
                () -> assertTrue(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_100))),

                () -> assertFalse(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_200))));
    }

    @Test
    @DisplayName("it should invalidate being able to play an 'Étape' if mileage goes over 1000.")
    void testJouerEtapeAbove1000() {
        lea.setKmParcourus(975);
        lea.getPile(Joueur.Pile.BATAILLE).add(new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT));

        assertAll("joueur",
                () -> assertTrue(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_25))),

                () -> assertFalse(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_50))),
                () -> assertFalse(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_75))),
                () -> assertFalse(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_100))),
                () -> assertFalse(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_200))));
    }

    @Test
    @DisplayName("it should invalidate being able to play an 'Étape' when being attacked.")
    void testJouerEtapeWhenAttacked() {
        lea.setKmParcourus(975);
        lea.getPile(Joueur.Pile.BATAILLE).add(new Carte(Carte.TYPE_ATTAQUE, Carte.ACCIDENT));

        assertAll("joueur",
                () -> assertFalse(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_25))),
                () -> assertFalse(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_50))),
                () -> assertFalse(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_75))),
                () -> assertFalse(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_100))),
                () -> assertFalse(lea.peutJouerEtape(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_200))));
    }

    @Test
    @DisplayName("it should make player talk.")
    void testParler() {
        lea.parler("Salut !");
        assertEquals("[Léa] Salut !", outContent.toString());
    }

    @Test
    @DisplayName("it should add the top card of the stack to player's hand.")
    void testPiocherCarte() {
        PileCartes pioche = new PileCartes();
        pioche.push(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_75));
        pioche.push(new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT));

        assertEquals(0, lea.getMain().size());

        Carte carte = lea.piocherCarte(pioche);
        assertEquals(1, lea.getMain().size());
        assertEquals(carte.getNom(), Carte.getNom(Carte.TYPE_PARADE, Carte.FEU_VERT));

        carte = lea.piocherCarte(pioche);
        assertEquals(2, lea.getMain().size());
        assertEquals(carte.getNom(), Carte.getNom(Carte.TYPE_ETAPE, Carte.ETAPE_75));
    }

    @Test
    @DisplayName("it should have player say when the drawing stack is empty.")
    void testPiocherCarteVide() {
        PileCartes pioche = new PileCartes();

        assertEquals(0, lea.getMain().size());

        Carte carte = lea.piocherCarte(pioche);
        assertEquals(0, lea.getMain().size());
        assertNull(carte);

        assertEquals("[Léa] La pioche est vide, pas de carte pour moi :(\n", outContent.toString());
    }

    @Test
    @DisplayName("it should add the top card of the stack on top of player's 'Météo' stack.")
    void testPiocherCarteMeteo() {
        PileCartes piocheMeteo = new PileCartes();
        piocheMeteo.push(new Carte(Carte.TYPE_METEO, Carte.NEIGE));

        assertEquals(0, lea.getMain().size());
        assertTrue(lea.getPile(Joueur.Pile.METEO).isEmpty());
        assertFalse(lea.estMeteoResolue());

        Carte carte = lea.piocherCarteMeteo(piocheMeteo);
        assertEquals(0, lea.getMain().size());
        assertEquals(carte.getNom(), lea.getPile(Joueur.Pile.METEO).peek().getNom());
    }

    @Test
    @DisplayName("it should have player say when the stack 'Météo' is empty.")
    void testPiocherCarteMeteoVide() {
        PileCartes piocheMeteo = new PileCartes();

        assertEquals(0, lea.getMain().size());
        assertTrue(lea.getPile(Joueur.Pile.METEO).isEmpty());

        Carte carte = lea.piocherCarteMeteo(piocheMeteo);
        assertEquals(0, lea.getMain().size());
        assertNull(carte);

        assertEquals("[Léa] La pioche est vide, pas de carte pour moi :(\n", outContent.toString());
    }

    @Test
    @DisplayName("it should discard a card from the hand onto the top of the discard stack.")
    void testDefausserCarte() {
        Defausse defausse = new Defausse();
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.ACCIDENT));

        assertTrue(defausse.isEmpty());
        assertEquals(1, lea.getMain().size());

        lea.defausserCarte(1, defausse);
        assertFalse(defausse.isEmpty());
        assertTrue(defausse.peek().estEgale(Carte.TYPE_ATTAQUE, Carte.ACCIDENT));
    }

    @Test
    @DisplayName("it should put a card from the hand onto the top of a pile of the player's game area.")
    void testPoserCarte() {
        lea.getMain().add(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_100));
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.LIMITE_VITESSE));
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.ACCIDENT));
        lea.getMain().add(new Carte(Carte.TYPE_PARADE, Carte.ROUE_SECOURS));
        lea.getMain().add(new Carte(Carte.TYPE_BOTTE, Carte.CITERNE));

        assertEquals(5, lea.getMain().size());

        lea.poserCarte(5, lea, Joueur.Pile.BOTTE);
        assertEquals(4, lea.getMain().size());
        assertTrue(lea.getPile(Joueur.Pile.BOTTE).peek().estEgale(Carte.TYPE_BOTTE, Carte.CITERNE));

        lea.poserCarte(4, lea, Joueur.Pile.BATAILLE);
        assertEquals(3, lea.getMain().size());
        assertTrue(lea.getPile(Joueur.Pile.BATAILLE).peek().estEgale(Carte.TYPE_PARADE, Carte.ROUE_SECOURS));

        lea.poserCarte(3, lea, Joueur.Pile.BATAILLE);
        assertEquals(2, lea.getMain().size());
        assertTrue(lea.getPile(Joueur.Pile.BATAILLE).peek().estEgale(Carte.TYPE_ATTAQUE, Carte.ACCIDENT));

        lea.poserCarte(2, lea, Joueur.Pile.VITESSE);
        assertEquals(1, lea.getMain().size());
        assertTrue(lea.getPile(Joueur.Pile.VITESSE).peek().estEgale(Carte.TYPE_ATTAQUE, Carte.LIMITE_VITESSE));

        lea.poserCarte(1, lea, Joueur.Pile.ETAPE);
        assertEquals(0, lea.getMain().size());
        assertTrue(lea.getPile(Joueur.Pile.ETAPE).peek().estEgale(Carte.TYPE_ETAPE, Carte.ETAPE_100));
    }

    @Test
    @DisplayName("it should correctly handle a player able to play a card 'Étape'")
    void testAbleJouerCarte() {
        lea.getMain().add(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_25));
        lea.getMain().add(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_50));
        lea.getMain().add(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_75));
        lea.getMain().add(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_100));
        lea.getMain().add(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_200));
        lea.getPile(Joueur.Pile.BATAILLE).push(new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT));

        assertEquals(5, lea.getMain().size());

        for (int i = 5; i >= 1; i--) {
            Carte currentCard = lea.getMain().get(i - 1);
            int num = lea.jouerCarte(i, null, null, null);

            assertEquals(i, num);
            assertEquals(currentCard.getNom(), lea.getPile(Joueur.Pile.ETAPE).peek().getNom());
            assertEquals("[Léa] Je joue une Étape [" + currentCard + "].\n", outContent.toString());
            outContent.reset();
        }

        assertEquals(0, lea.getMain().size());
    }

    @Test
    @DisplayName("it should correctly handle a player able to play a card 'Étape' during weather event 'Neige'.")
    void testAbleJouerCarteDuringNeige() {
        lea.getMain().add(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_50));
        lea.getMain().add(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_75));
        lea.getPile(Joueur.Pile.BATAILLE).push(new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT));
        lea.getPile(Joueur.Pile.METEO).push(new Carte(Carte.TYPE_METEO, Carte.NEIGE));

        assertEquals(2, lea.getMain().size());
        Carte currentCard = lea.getMain().get(2 - 1);
        String currentCardName = currentCard.getNom();

        int num = lea.jouerCarte(2, null, null, null);

        assertEquals(2, num);
        assertEquals(currentCard.getNom(), lea.getPile(Joueur.Pile.ETAPE).peek().getNom());
        assertEquals(currentCardName + "(-25)", lea.getPile(Joueur.Pile.ETAPE).peek().getNom());
        assertEquals("[Léa] Je joue une Étape [" + new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_75) + "].\n",
                outContent.toString());
        assertEquals(1, lea.getMain().size());
        assertTrue(lea.estMeteoResolue());

        outContent.reset();

        // Météo has resolved
        currentCard = lea.getMain().get(1 - 1);
        currentCardName = currentCard.getNom();

        num = lea.jouerCarte(1, null, null, null);

        assertEquals(1, num);
        assertEquals(currentCard.getNom(), lea.getPile(Joueur.Pile.ETAPE).peek().getNom());
        assertEquals(currentCardName, lea.getPile(Joueur.Pile.ETAPE).peek().getNom());
        assertEquals("[Léa] Je joue une Étape [" + new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_50) + "].\n",
                outContent.toString());
        assertEquals(0, lea.getMain().size());
    }

    @Test
    @DisplayName("it should correctly handle a player able to play a card 'Étape' during weather event 'Vent dans le Dos'.")
    void testAbleJouerCarteDuringVentDos() {
        lea.getMain().add(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_50));
        lea.getMain().add(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_100));
        lea.getPile(Joueur.Pile.BATAILLE).push(new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT));
        lea.getPile(Joueur.Pile.METEO).push(new Carte(Carte.TYPE_METEO, Carte.VENT_DOS));

        assertEquals(2, lea.getMain().size());
        Carte currentCard = lea.getMain().get(2 - 1);
        String currentCardName = currentCard.getNom();

        int num = lea.jouerCarte(2, null, null, null);

        assertEquals(2, num);
        assertEquals(currentCard.getNom(), lea.getPile(Joueur.Pile.ETAPE).peek().getNom());
        assertEquals(currentCardName + "(+25)", lea.getPile(Joueur.Pile.ETAPE).peek().getNom());
        assertEquals("[Léa] Je joue une Étape [" + new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_100) + "].\n",
                outContent.toString());
        assertEquals(1, lea.getMain().size());
        outContent.reset();

        // Météo has resolved
        currentCard = lea.getMain().get(1 - 1);
        currentCardName = currentCard.getNom();

        num = lea.jouerCarte(1, null, null, null);

        assertEquals(1, num);
        assertEquals(currentCard.getNom(), lea.getPile(Joueur.Pile.ETAPE).peek().getNom());
        assertEquals(currentCardName, lea.getPile(Joueur.Pile.ETAPE).peek().getNom());
        assertEquals("[Léa] Je joue une Étape [" + new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_50) + "].\n",
                outContent.toString());
        assertEquals(0, lea.getMain().size());
    }

    @Test
    @DisplayName("it should correctly handle a player not being able to play a card 'Étape'")
    void testNotAbleJouerCarte() {
        lea.getMain().add(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_25));
        lea.getMain().add(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_50));
        lea.getMain().add(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_75));
        lea.getMain().add(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_100));
        lea.getMain().add(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_200));

        assertEquals(5, lea.getMain().size());

        for (int i = 5; i >= 1; i--) {
            int num = lea.jouerCarte(i, null, null, null);

            assertEquals(-1, num);
            assertTrue(lea.getPile(Joueur.Pile.ETAPE).isEmpty());
            assertEquals("[Léa] Oops je ne pas jouer cette carte !\n\n", outContent.toString());
            outContent.reset();
            lea.getMain().remove(i - 1);
        }

        assertEquals(0, lea.getMain().size());
    }

    @Test
    @DisplayName("it should validate a player being able to attack an other player.")
    void testPlayerCanAttack() {
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.ACCIDENT));
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.CREVE));
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.FEU_ROUGE));
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.LIMITE_VITESSE));
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.PANNE_ESSENCE));

        tom.getPile(Joueur.Pile.BATAILLE).push(new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT));

        for (Carte carte : lea.getMain()) {
            assertTrue(lea.peutAttaquerJoueur(carte, tom));
        }

        tom.getPile(Joueur.Pile.BATAILLE).push(new Carte(Carte.TYPE_ATTAQUE, Carte.ACCIDENT));
        assertTrue(lea.peutAttaquerJoueur(lea.getMain().get(3), tom));
    }

    @Test
    @DisplayName("it should invalidate a player being able to attack an other player.")
    void testPlayerCantAttack() {
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.ACCIDENT));
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.CREVE));
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.FEU_ROUGE));
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.LIMITE_VITESSE));
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.PANNE_ESSENCE));

        tom.getPile(Joueur.Pile.BATAILLE).push(new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT));
        tom.getPile(Joueur.Pile.BATAILLE).push(new Carte(Carte.TYPE_ATTAQUE, Carte.ACCIDENT));
        tom.getPile(Joueur.Pile.VITESSE).push(new Carte(Carte.TYPE_ATTAQUE, Carte.LIMITE_VITESSE));

        for (Carte carte : lea.getMain()) {
            assertFalse(lea.peutAttaquerJoueur(carte, tom));
        }

        tom.getPile(Joueur.Pile.BATAILLE).pop();
        tom.getPile(Joueur.Pile.VITESSE).pop();
        tom.getPile(Joueur.Pile.BOTTE).push(new Carte(Carte.TYPE_BOTTE, Carte.AS_VOLANT));
        tom.getPile(Joueur.Pile.BOTTE).push(new Carte(Carte.TYPE_BOTTE, Carte.CITERNE));
        tom.getPile(Joueur.Pile.BOTTE).push(new Carte(Carte.TYPE_BOTTE, Carte.INCREVABLE));
        tom.getPile(Joueur.Pile.BOTTE).push(new Carte(Carte.TYPE_BOTTE, Carte.PRIORITAIRE));

        for (Carte carte : lea.getMain()) {
            assertFalse(lea.peutAttaquerJoueur(carte, tom));
        }
    }

    @Test
    @DisplayName("it should correctly handle a player able to attack an other player.")
    void testPlayerCanPlayAttackCard() {
        PileCartes drawPile = new Pioche();
        PileCartes discardPile = new Defausse();

        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.FEU_ROUGE));
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.CREVE));
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.PANNE_ESSENCE));
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.ACCIDENT));
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.LIMITE_VITESSE));

        tom.getPile(Joueur.Pile.BATAILLE).push(new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT));

        assertEquals(5, lea.getMain().size());

        assertEquals(5, lea.jouerCarteAttaque(5, tom, drawPile, discardPile));
        assertTrue(tom.getPile(Joueur.Pile.VITESSE).peek().estEgale(Carte.TYPE_ATTAQUE, Carte.LIMITE_VITESSE));
        assertEquals(4, lea.getMain().size());

        for (int cardNum = lea.getMain().size(); cardNum >= 1; cardNum--) {
            Carte currentCard = lea.getMain().get(cardNum - 1);

            assertEquals(cardNum, lea.jouerCarteAttaque(cardNum, tom, drawPile, discardPile));
            assertEquals(currentCard.getNom(), tom.getPile(Joueur.Pile.BATAILLE).peek().getNom());
            assertEquals(cardNum - 1, lea.getMain().size());
            tom.getPile(Joueur.Pile.BATAILLE).pop();
        }
    }

    @Test
    @DisplayName("it should correctly handle a player not being able to attack an other player.")
    void testPlayerCantPlayAttackCard() {
        PileCartes drawPile = new Pioche();
        PileCartes discardPile = new Defausse();

        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.FEU_ROUGE));
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.CREVE));
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.PANNE_ESSENCE));
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.ACCIDENT));
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.LIMITE_VITESSE));

        tom.getPile(Joueur.Pile.VITESSE).push(new Carte(Carte.TYPE_ATTAQUE, Carte.LIMITE_VITESSE));
        tom.getPile(Joueur.Pile.BATAILLE).push(new Carte(Carte.TYPE_ATTAQUE, Carte.ACCIDENT));

        assertEquals(5, lea.getMain().size());

        for (int cardNum = 1; cardNum <= lea.getMain().size(); cardNum++) {
            assertEquals(-1, lea.jouerCarteAttaque(cardNum, tom, drawPile, discardPile));
            assertEquals(5, lea.getMain().size());
        }
    }

    @Test
    @DisplayName("it should correctly handle a player responding with a dirty trick when attacked.")
    void testPlayerRespondingWithDirtyTrick() {
        PileCartes drawPile = new Pioche();
        PileCartes discardPile = new Defausse();

        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.CREVE));
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.PANNE_ESSENCE));
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.ACCIDENT));
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.FEU_ROUGE));
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.LIMITE_VITESSE));

        tom.getMain().add(new Carte(Carte.TYPE_BOTTE, Carte.CITERNE));
        tom.getMain().add(new Carte(Carte.TYPE_BOTTE, Carte.AS_VOLANT));
        tom.getMain().add(new Carte(Carte.TYPE_BOTTE, Carte.INCREVABLE));
        tom.getMain().add(new Carte(Carte.TYPE_BOTTE, Carte.PRIORITAIRE));

        tom.getPile(Joueur.Pile.BATAILLE).push(new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT));

        assertTrue(discardPile.isEmpty());
        lea.jouerCarteAttaque(5, tom, drawPile, discardPile);
        assertTrue(tom.getPile(Joueur.Pile.VITESSE).isEmpty());
        assertEquals(Carte.getNom(Carte.TYPE_ATTAQUE, Carte.LIMITE_VITESSE), discardPile.peek().getNom());
        assertEquals(4, lea.getMain().size());
        discardPile.pop();
        tom.getPile(Joueur.Pile.BOTTE).pop();

        tom.getMain().add(new Carte(Carte.TYPE_BOTTE, Carte.PRIORITAIRE));
        assertTrue(discardPile.isEmpty());
        lea.jouerCarteAttaque(4, tom, drawPile, discardPile);
        assertEquals(Carte.getNom(Carte.TYPE_PARADE, Carte.FEU_VERT),
                tom.getPile(Joueur.Pile.BATAILLE).peek().getNom());
        assertEquals(Carte.getNom(Carte.TYPE_ATTAQUE, Carte.FEU_ROUGE), discardPile.peek().getNom());
        assertEquals(3, lea.getMain().size());
        discardPile.pop();

        assertTrue(discardPile.isEmpty());
        lea.jouerCarteAttaque(3, tom, drawPile, discardPile);
        assertEquals(Carte.getNom(Carte.TYPE_PARADE, Carte.FEU_VERT),
                tom.getPile(Joueur.Pile.BATAILLE).peek().getNom());
        assertEquals(Carte.getNom(Carte.TYPE_ATTAQUE, Carte.ACCIDENT), discardPile.peek().getNom());
        assertEquals(2, lea.getMain().size());
        discardPile.pop();

        assertTrue(discardPile.isEmpty());
        lea.jouerCarteAttaque(2, tom, drawPile, discardPile);
        assertEquals(Carte.getNom(Carte.TYPE_PARADE, Carte.FEU_VERT),
                tom.getPile(Joueur.Pile.BATAILLE).peek().getNom());
        assertEquals(Carte.getNom(Carte.TYPE_ATTAQUE, Carte.PANNE_ESSENCE), discardPile.peek().getNom());
        assertEquals(1, lea.getMain().size());
        discardPile.pop();

        assertTrue(discardPile.isEmpty());
        lea.jouerCarteAttaque(1, tom, drawPile, discardPile);
        assertEquals(Carte.getNom(Carte.TYPE_PARADE, Carte.FEU_VERT),
                tom.getPile(Joueur.Pile.BATAILLE).peek().getNom());
        assertEquals(Carte.getNom(Carte.TYPE_ATTAQUE, Carte.CREVE), discardPile.peek().getNom());
        assertEquals(0, lea.getMain().size());
        discardPile.pop();

        assertTrue(tom.aJouerBotte());
    }

    @Test
    @DisplayName("it should validate a player being able to play a safety card.")
    void testCanPlaySafety() {
        assertTrue(lea.peutJouerParade(new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT)));

        lea.getPile(Joueur.Pile.VITESSE).push(new Carte(Carte.TYPE_ATTAQUE, Carte.LIMITE_VITESSE));
        assertTrue(lea.peutJouerParade(new Carte(Carte.TYPE_PARADE, Carte.FIN_LIMITE_VITESSE)));

        lea.getPile(Joueur.Pile.BATAILLE).push(new Carte(Carte.TYPE_ATTAQUE, Carte.PANNE_ESSENCE));
        assertTrue(lea.peutJouerParade(new Carte(Carte.TYPE_PARADE, Carte.ESSENCE)));

        lea.getPile(Joueur.Pile.BATAILLE).push(new Carte(Carte.TYPE_ATTAQUE, Carte.CREVE));
        assertTrue(lea.peutJouerParade(new Carte(Carte.TYPE_PARADE, Carte.ROUE_SECOURS)));

        lea.getPile(Joueur.Pile.BATAILLE).push(new Carte(Carte.TYPE_ATTAQUE, Carte.ACCIDENT));
        assertTrue(lea.peutJouerParade(new Carte(Carte.TYPE_PARADE, Carte.REPARATION)));

        lea.getPile(Joueur.Pile.BATAILLE).push(new Carte(Carte.TYPE_ATTAQUE, Carte.FEU_ROUGE));
        assertTrue(lea.peutJouerParade(new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT)));

        lea.getPile(Joueur.Pile.BATAILLE).push(new Carte(Carte.TYPE_PARADE, Carte.ESSENCE));
        assertTrue(lea.peutJouerParade(new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT)));
    }

    @Test
    @DisplayName("it should invalidate a player being able to play a safety card.")
    void testCantPlaySafety() {
        lea.getMain().add(new Carte(Carte.TYPE_PARADE, Carte.ROUE_SECOURS));
        lea.getMain().add(new Carte(Carte.TYPE_PARADE, Carte.FIN_LIMITE_VITESSE));
        lea.getMain().add(new Carte(Carte.TYPE_PARADE, Carte.ESSENCE));
        lea.getMain().add(new Carte(Carte.TYPE_PARADE, Carte.REPARATION));

        lea.getPile(Joueur.Pile.BATAILLE).push(new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT));
        assertFalse(lea.peutJouerParade(new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT)));

        for (Carte carte : lea.getMain()) {
            assertFalse(lea.peutJouerParade(carte));
        }

        lea.getPile(Joueur.Pile.BATAILLE).pop();
        lea.getPile(Joueur.Pile.BOTTE).push(new Carte(Carte.TYPE_BOTTE, Carte.PRIORITAIRE));
        assertFalse(lea.peutJouerParade(new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT)));
    }

    @Test
    @DisplayName("it should correctly handle a player able to play a safety card.")
    void testPlayerCanPlaySafety() {
        lea.getMain().add(new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT));
        lea.getMain().add(new Carte(Carte.TYPE_PARADE, Carte.ESSENCE));
        lea.getMain().add(new Carte(Carte.TYPE_PARADE, Carte.REPARATION));
        lea.getMain().add(new Carte(Carte.TYPE_PARADE, Carte.ROUE_SECOURS));
        lea.getMain().add(new Carte(Carte.TYPE_PARADE, Carte.FIN_LIMITE_VITESSE));
        lea.getMain().add(new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT));

        assertEquals(6, lea.getMain().size());

        assertEquals(6, lea.jouerCarte(6, null, null, null));
        assertEquals(Carte.getNom(Carte.TYPE_PARADE, Carte.FEU_VERT),
                lea.getPile(Joueur.Pile.BATAILLE).peek().getNom());
        assertEquals(5, lea.getMain().size());

        lea.getPile(Joueur.Pile.VITESSE).push(new Carte(Carte.TYPE_ATTAQUE, Carte.LIMITE_VITESSE));
        assertEquals(5, lea.jouerCarte(5, null, null, null));
        assertEquals(Carte.getNom(Carte.TYPE_PARADE, Carte.FIN_LIMITE_VITESSE),
                lea.getPile(Joueur.Pile.VITESSE).peek().getNom());
        assertEquals(4, lea.getMain().size());

        lea.getPile(Joueur.Pile.BATAILLE).push(new Carte(Carte.TYPE_ATTAQUE, Carte.FEU_ROUGE));
        lea.getPile(Joueur.Pile.BATAILLE).push(new Carte(Carte.TYPE_ATTAQUE, Carte.PANNE_ESSENCE));
        lea.getPile(Joueur.Pile.BATAILLE).push(new Carte(Carte.TYPE_ATTAQUE, Carte.ACCIDENT));
        lea.getPile(Joueur.Pile.BATAILLE).push(new Carte(Carte.TYPE_ATTAQUE, Carte.CREVE));

        for (int cardNum = lea.getMain().size(); cardNum >= 1; cardNum--) {
            Carte currentCard = lea.getMain().get(cardNum - 1);

            assertEquals(cardNum, lea.jouerCarte(cardNum, null, null, null));
            assertEquals(currentCard.getNom(), lea.getPile(Joueur.Pile.BATAILLE).peek().getNom());
            assertEquals(cardNum - 1, lea.getMain().size());
            lea.getPile(Joueur.Pile.BATAILLE).pop();
            lea.getPile(Joueur.Pile.BATAILLE).pop();
        }

        lea.getMain().add(new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT));
        lea.getPile(Joueur.Pile.BATAILLE).push(new Carte(Carte.TYPE_PARADE, Carte.ESSENCE));
        assertEquals(1, lea.jouerCarte(1, null, null, null));
        assertEquals(Carte.getNom(Carte.TYPE_PARADE, Carte.FEU_VERT),
                lea.getPile(Joueur.Pile.BATAILLE).peek().getNom());
    }

    @Test
    @DisplayName("it should reset a player in order to start a new round.")
    void testResetPlayer() {
        lea.getPile(Joueur.Pile.BATAILLE).push(new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT));
        lea.getMain().add(new Carte(Carte.TYPE_ATTAQUE, Carte.ACCIDENT));
        lea.getMain().add(new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT));
        lea.mettreAJourScore(500);
        lea.setKmParcourus(400);

        lea.reset();

        assertTrue(lea.getPile(Joueur.Pile.BATAILLE).isEmpty());
        assertTrue(lea.getMain().isEmpty());
        assertEquals(0, lea.getKmParcourus());
        assertEquals(500, lea.getScore());
    }
}
