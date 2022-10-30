/**
 * Classe définissant la zone de jeu d'un joueur.
 * 
 * @author Les Bornés
 */
public class ZoneDeJeu {
    private PileCartes vitesse; // Pile ou sont placées les étapes
    private PileCartes bataille; // Pile ou sont placées les attaques et parades
    private PileCartes botte; // Pile ou sont placées les bottes

    // TODO à implémenter.
    public ZoneDeJeu() {
        this.vitesse = new PileCartes();
        this.bataille = new PileCartes();
        this.botte = new PileCartes();
    }

    public PileCartes getVitesse() {
        return vitesse;
    }

    public PileCartes getBataille() {
        return bataille;
    }

    public PileCartes getBotte() {
        return botte;
    }
}
