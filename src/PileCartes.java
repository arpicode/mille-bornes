import java.util.Stack;

public class PileCartes extends Stack<Carte> {
    public PileCartes(Carte[] cartes) {
        super();
        alimenter(cartes);
    }

    public void alimenter(Carte[] cartes) {
        for (int i = 0; i < cartes.length; i++) {
            if (cartes[i].estValide()) {
                this.push(cartes[i]);
            }
        }
    }

    // TODO à placer dans la classe fille Pioche
    public static Carte[] melanger(Carte[] cartes) {
        for (int lastIndex = cartes.length - 1; lastIndex > 0; lastIndex--) {
            int pickedIndex = (int) Math.floor(Math.random() * (lastIndex + 1));
            Carte tmp = cartes[lastIndex];
            cartes[lastIndex] = cartes[pickedIndex];
            cartes[pickedIndex] = tmp;
        }
        return cartes;
    }

}
