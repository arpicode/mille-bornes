import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class PileCartesTest {

    @Test
    @DisplayName("it should create a stack of cards.")
    void testCreateCardStack() {
        PileCartes stackOfCards = new PileCartes();

        assumeTrue(stackOfCards != null);

        assertTrue(stackOfCards.isEmpty());
        stackOfCards.push(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_75));
        stackOfCards.push(new Carte(Carte.TYPE_ATTAQUE, Carte.ACCIDENT));
        stackOfCards.push(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_75));

        assertFalse(stackOfCards.isEmpty());
        assertEquals(Carte.getNom(Carte.TYPE_ETAPE, Carte.ETAPE_75), stackOfCards.peek().getNom());
    }

    void testStackContainsCardByName() {
        PileCartes stackOfCards = new PileCartes();

        assumeTrue(stackOfCards != null);

        assertTrue(stackOfCards.isEmpty());
        stackOfCards.push(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_75));
        stackOfCards.push(new Carte(Carte.TYPE_ATTAQUE, Carte.ACCIDENT));
        stackOfCards.push(new Carte(Carte.TYPE_ETAPE, Carte.ETAPE_75));

        assertTrue(stackOfCards.contient(Carte.getNom(Carte.TYPE_ATTAQUE, Carte.ACCIDENT)));
    }

}
