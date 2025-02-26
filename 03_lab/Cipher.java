import java.util.StringTokenizer;

/**
 * Un chiffreur/déchiffreur de messages.
 *
 * Les algorithmes utilisés sont les suivants :
 * <ul>
 * <li> pour le chiffrage :
 * <ol>
 *     <li> le message est transformé une première fois globalement par
 *         un encodeur circulaire avec un décalage aléatoire à droite ;
 *     </li>
 *     <li> le message transformé est modifié une nouvelle fois mais mot
 *         à mot, à l'aide d'un encodeur circulaire à droite dont le décalage
 *         prend les longueurs des mots successivement rencontrés.
 *     </li>
 * </ol>
 * </li>
 *
 * <li> pour le déchiffrage :
 * <ol>
 *     <li> le message est travaillé une première fois à l'aide d'un encodeur
 *         circulaire à gauche dont le décalage prend en compte la longueur des
 *         mots successifs ;
 *     </li>
 *     <li> pour annuler l'utilisation lors du chiffrage du premier encodeur
 *         circulaire on retrouve le décalage aléatoire utilisé, en s'appuyant
 *         sur le fait que la lettre la plus fréquente dans un texte français
 *         (ou anglais) est le 'e'.
 *     </li>
 * </ol>
 * </li>
 * </ul>
 * @inv <pre>
 *     getClearText() != null
 *     getCipherText() != null
 *     getClearText() est le déchiffré de getCipherText() </pre>
 */
public class Cipher {

    // ATTRIBUTS STATIQUES

    /**
     * Drapeau pour transformWords indiquant que l'on souhaite encoder.
     */
    private static final int ENCODE = 1;

    /**
     * Drapeau pour transformWords indiquant que l'on souhaite décoder.
     */
    private static final int DECODE = 2;

    /**
     * Chaîne contenant la séquence de tous les caractères séparateurs.
     */
    private static final String SEPARATORS = " \t\n\r\f\'\"([-,?;.:!])";

    /**
     * Taille de l'alphabet.
     */
    private static final int ALPHABET_SIZE = SubstCipher.ALPHABET_SIZE;

    // ATTRIBUTS

    /**
     * Le message courant en clair.
     */
    private String clearText;

    /**
     * Le message courant chiffré.
     */
    private String cipherText;

    // CONSTRUCTEURS

    /**
     * Un chiffreur dont les messages getClearText() et getCipherText()
     *  retournent la chaîne vide.
     * @post <pre>
     *     getClearText().equals("")
     *     getCipherText().equals("") </pre>
     */
    public Cipher() {
        this.clearText = "";
        this.cipherText = "";
    }

    // REQUETES

    /**
     * Le message courant en clair. Sa valeur correspond au message décodé de
     *  getCipherText().
     */
    public String getClearText() {
        return clearText;
    }

    /**
     * Le message courant chiffré. Sa valeur correspond au message encodé de
     *  getClearText().
     */
    public String getCipherText() {
        return cipherText;
    }

    // COMMANDES

    /**
     * Modification du message courant en clair.
     * @pre <pre>
     *     text != null </pre>
     * @post <pre>
     *     getClearText().equals(text) </pre>
     */
    public void setClearText(String text) {
         if(text == null){
            throw new AssertionError("la référence est vide");
        }
        this.clearText = text;
        this.cipherText = transformWords(text, ENCODE);
    }

    /**
     * Modification du message courant chiffré.
     * @pre <pre>
     *     text != null </pre>
     * @post <pre>
     *     getCipherText().equals(text) </pre>
     */
    public void setCipherText(String text) {
         if(text == null){
            throw new AssertionError("la référence est vide");
        }
        this.cipherText = text;
        this.clearText = transformWords(text, DECODE);
    }

    // OUTILS

    /**
     * Une représentation sous forme de chaîne de cet encodeur.
     * @post
     *     result.equals("Cipher[clear:" + getClearText()
     *         + ";cipher:" + getCipherText() + "]")
     */
    public String toString() {
        return "Cipher[clear:" + clearText + ";cipher:" + cipherText + "]";
    }

    /**
     * Un nombre aléatoire compris entre a et b (au sens large).
     * @pre <pre>
     *     0 < a <= b </pre>
     * @post <pre>
     *     a <= result <= b </pre>
     */
    private static int alea(int a, int b) {
        assert (a > 0) && (b >= a);

        return a + (int) (Math.random() * (b - a + 1));
    }

    /**
     * indique si le mot word correspond à un séparateur.
     * @pre <pre>
     *     word != null </pre>
     * @post <pre>
     *     result <==>
     *         word.length() == 1
     *         word.charAt(0) est dans SEPARATORS </pre>
     */
    private static boolean isSeparator(String word) {
        assert (word != null);

        return word.length() == 1 && SEPARATORS.indexOf(word.charAt(0)) != -1;
    }

    /**
     * Encodage mot à mot de message avec un décalage donné par la longueur
     *  des mots (décalage à droite si type == ENCODE, décalage à gauche si
     *  type == DECODE).
     * @pre <pre>
     *     message != null </pre>
     * @post <pre>
     *     type == ENCODE
     *         ==> result est l'encodage de message mot à mot
     *     type == DECODE
     *         ==> result est le décodage de message mot à mot </pre>
     */
    private static String transformWords(String message, int type) {
        assert message != null;
        SubstCipher t = new SubstCipher();
        StringTokenizer s = new StringTokenizer(message, SEPARATORS, true);
        StringBuffer res = new StringBuffer("");
        while (s.hasMoreTokens()){
          String currentWord = s.nextToken();
          if(!isSeparator(currentWord)){
             int length = currentWord.length() % ALPHABET_SIZE;
             t.setShift(length);
             if(type == ENCODE){
                 t.ensurePositiveShift();
             }  else {
                 t.ensureNegativeShift();
             }
             t.buildShiftedTextFor(currentWord);
             currentWord = t.getLastShiftedText();
          }
          res.append(currentWord);
        }
        return new String(res);
    }

    // TESTS

    public static void main(String[] args) {
        // dans un sens
        Cipher c = new Cipher();
        String clear = "Les Grecs attaquent par derrière !";
        c.setClearText(clear);
        System.out.println(c);
        // dans l'autre sens (peut ne pas fonctionner)
        String cyphered = c.getCipherText();
        c.setCipherText(cyphered);
        System.out.println(c);
        // un exemple qui ne donne pas le bon résultat !
        c.setCipherText("Cvj zkxvl xqqxnrbkq gri zanneèna !");
        System.out.println(c);
    }
}
