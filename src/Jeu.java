import java.util.ArrayList;

public class Jeu {

    private PileCartes pioche;
    private PileCartes piocheMeteo;
    private PileCartes defausse;
    private ArrayList<String> carteParse;

    public Jeu(String configFileName) {

        this.carteParse = Configuration.parse(configFileName); // return un ArrayList de string
        this.pioche = new PileCartes();
        this.piocheMeteo = new PileCartes();
        this.defausse = new PileCartes();
    }

    /**
     * @return this.pioche
     */
    public PileCartes getPioche() {

        return this.pioche;
    }

    /**
     * Permet d'initialiser les pioche
     * 
     * @param ArrayList<String>cartes
     */
    public void initialiserPioches() {

        // parcour le fichier ctrl+espace
        for (int i = 0; i < this.carteParse.size(); i++) {
            String[] tempresult = this.carteParse.get(i).split(";");
            int nombreCarte = Integer.parseInt(tempresult[0].trim()); // trim() retourne la chaine de caractère sans
                                                                      // espace devant et derière
            String nomCarte = tempresult[1].trim();
            System.out.println(nomCarte);

            for (int c = 0; c < nombreCarte; c++) {

                Carte carte = new Carte(nomCarte);

                // ajouter la carte au dessus de la pile correspondante si elle est valide
                if (carte.estValide()) {
                    if (carte.getType() != Carte.METEO) {
                        this.pioche.push(carte);
                    } else {
                        this.piocheMeteo.push(carte);
                    }
                }
            }
        }

    }

    public static void main(String[] args) {

        Jeu jeu1 = new Jeu("config.txt");
        jeu1.initialiserPioches();
        System.out.println(jeu1.getPioche());
    }

}
