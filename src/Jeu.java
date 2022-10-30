import java.util.ArrayList;

public class Jeu {

    private Pioche pioche;
    private Pioche piocheMeteo;
    private Defausse defausse;
    private ArrayList<String> carteParse;

    public Jeu(String configFileName) {
        this.carteParse = Configuration.parse(configFileName); // return un ArrayList de string
        this.pioche = new Pioche();
        this.piocheMeteo = new Pioche();
        this.defausse = new Defausse();
    }

    public void demarrer() {
        Affichage.nomJeu();
    }

    /**
     * Permet d'initialiser les pioches
     * 
     * @param ArrayList<String> cartes
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

    /**
     * @return Pioche la pioche du jeu.
     */
    public Pioche getPioche() {

        return this.pioche;
    }

    /**
     * @return Pioche la pioche des cartes météo jeu.
     */
    public Pioche getPiocheMeteo() {
        return piocheMeteo;
    }

    /**
     * @return Defausse la pile de défausse.
     */
    public Defausse getDefausse() {
        return defausse;
    }

    public static void main(String[] args) {

        Jeu jeu1 = new Jeu("config.txt");
        jeu1.initialiserPioches();
        System.out.println(jeu1.getPioche());
        System.out.println("Nb cartes : " + jeu1.getPioche().size());
    }

}
