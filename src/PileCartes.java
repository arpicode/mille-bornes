import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class PileCartes extends Stack<Carte> {
    public PileCartes() {
        super();
    }

    public PileCartes(ArrayList<Carte> cartes) {
        super();
        alimenter(cartes);
    }

    public void alimenter(ArrayList<Carte> cartes) {
        for (int i = 0; i < cartes.size(); i++) {
            if (cartes.get(i).estValide()) {
                this.push(cartes.get(i));
            }
        }
    }

    public static void melanger(List<Carte> cartes) {
        Collections.shuffle(cartes);
    }

}
