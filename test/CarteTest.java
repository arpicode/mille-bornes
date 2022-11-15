import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CarteTest {
    private final Carte greenLightCard1 = new Carte("feu vert");
    private final Carte greenLightCard2 = new Carte(Carte.TYPE_PARADE, Carte.FEU_VERT);

    private final Carte outOfPetrolCard1 = new Carte("panne d'essence");
    private final Carte outOfPetrolCard2 = new Carte(Carte.TYPE_ATTAQUE, Carte.PANNE_ESSENCE);

    private final Carte badCard1 = new Carte("Dame de Pique");

    @Test
    @DisplayName("it should create instance of card.")
    void testCreateCard() {
        assertAll(
                () -> assertEquals("Feu Vert", greenLightCard1.getNom()),
                () -> assertEquals("Feu Vert", greenLightCard2.getNom()),
                () -> assertEquals(Carte.TYPE_PARADE, greenLightCard1.getType()),
                () -> assertEquals("Panne d'Essence", outOfPetrolCard1.getNom()),
                () -> assertEquals("Panne d'Essence", outOfPetrolCard2.getNom()),
                () -> assertEquals(Carte.TYPE_ATTAQUE, outOfPetrolCard1.getType()),
                () -> assertEquals("Dame de Pique", badCard1.getNom()));
    }

    @Test
    @DisplayName("it should report correctly if a card is valid or not.")
    void testCardIsValid() {
        assertAll(
                () -> assertTrue(greenLightCard1.estValide()),
                () -> assertTrue(greenLightCard2.estValide()),
                () -> assertTrue(outOfPetrolCard1.estValide()),
                () -> assertTrue(outOfPetrolCard2.estValide()),
                () -> assertFalse(badCard1.estValide()));

        assertEquals("La carte 'Dame de Pique' n'est pas valide!", badCard1.toString());
    }
}
