/**
 * Un encodeur selon le principe du décalage circulaire (aussi appelé technique
 *  de "César").
 * La méthode <code>buildShiftedTextFor</code> permet d'encoder un message
 *  (décalage à droite) ou de le décoder (décalage à gauche) selon la valeur
 *  actuelle du décalage.
 * @inv <pre>
 *     -ALPHABET_SIZE < getShift() < ALPHABET_SIZE
 *     getLastShiftedText() != null </pre>
 */
public class SubstCipher {

    // ATTRIBUTS STATIQUES

    /**
     * Taille de l'alphabet.
     */
    public static final int ALPHABET_SIZE = 26;

    /**
     * Caractère (majuscule) le plus fréquent en français (et en anglais).
     */
    public static final char MOST_FREQUENT_CHAR = 'E';

    // ATTRIBUTS

    /**
     * Le décalage courant de cet encodeur.
     */
    private int currentShift;

    /**
     * Le dernier texte encodé par cet encodeur avec un décalage de getShift().
     */
    private String lastShiftedText;

    // CONSTRUCTEURS

    /**
     * Un encodeur de décalage 0 (i.e. l'identité).
     * @post <pre>
     *     getShift() == 0
     *     getLastShiftedText().equals("") </pre>
     */
    public SubstCipher() {
        this.currentShift = 0;
        this.lastShiftedText = "";
    }

    /**
     * Un encodeur de décalage <code>shift</code>.
     * @pre <pre>
     *     -ALPHABET_SIZE < shift < ALPHABET_SIZE </pre>
     * @post <pre>
     *     getShift() == shift
     *     getLastShiftedText().equals("") </pre>
     */
    public SubstCipher(int shift) {
        if (shift <= -ALPHABET_SIZE || shift >= ALPHABET_SIZE) {
            throw new IllegalArgumentException("Le décalage doit être dans l'intervalle (-" + ALPHABET_SIZE + ", " + ALPHABET_SIZE + ")");
        }
        this.currentShift = shift;
    }


    // REQUETES

    /**
     * Le dernier texte encodé par cet encodeur avec un décalage de getShift().
     */
    public String getLastShiftedText() {
        return lastShiftedText;
    }

    /**
     * Le décalage courant de cet encodeur.
     */
    public int getShift() {
        return currentShift;
    }

    // COMMANDES

    /**
     * Affecte un nouveau décalage à l'encodeur.
     * @pre <pre>
     *     -ALPHABET_SIZE < shift < ALPHABET_SIZE </pre>
     * @post <pre>
     *     getShift() == shift
     *     getLastShiftedText().equals("") </pre>
     */
    public void setShift(int shift) {
        assert (-ALPHABET_SIZE < shift && shift < ALPHABET_SIZE);
        this.currentShift = shift;
        this.lastShiftedText = "";
    }

    /**
     * Construit une chaîne à partir de celle fournie en paramètre en décalant
     *  circulairement les caractères alphabétiques selon le décalage donné par
     *  <code>getShift()</code>.
     * Le décalage se fait à droite si <code>getShift() &geq; 0</code>, ou
     *  à gauche si <code>getShift() &lt; 0</code>.
     * @pre <pre>
     *     text != null </pre>
     * @post <pre>
     *     getLastShiftedText() != null
     *     getLastShiftedText().length() == text.length()
     *     forall i, 0 <= i < text.length() :
     *         Let ci ::= text.charAt(i)
     *             xi ::= getLastShiftedText().charAt(i)
     *         isNonAccentedLetter(ci) ==> xi == ci décalé de getShift()
     *         !isNonAccentedLetter(ci) ==> xi == ci </pre>
     */
    public void buildShiftedTextFor(String text) {
    assert text != null;

    StringBuilder result = new StringBuilder();
    for (char c : text.toCharArray()) {
        result.append(shiftChar(c));
    }

    this.lastShiftedText = result.toString();
    }


    /**
     * Configure cet encodeur pour un encodage.
     * @post <pre>
     *     getShift() == +abs(old getShift())
     *     getLastShiftedText().equals("") </pre>
     */
    public void ensurePositiveShift() {
    this.currentShift = Math.abs(this.currentShift);
    this.lastShiftedText = "";
    }




    /**
     * Configure cet encodeur pour un décodage.
     * @post <pre>
     *     getShift() == -abs(old getShift())
     *     getLastShiftedText().equals("") </pre>
     */
    public void ensureNegativeShift() {
    this.currentShift = -Math.abs(this.currentShift);
    this.lastShiftedText = "";
    }

    // OUTILS
    
      /**
     * Vérifie si un caractère est une lettre non accentuée.
     * @param c le caractère à tester
     * @return true si c est une lettre de l'alphabet anglais
     */
    public static boolean isNonAccentedLetter(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }


    /**
     * Une représentation sous forme de chaîne de cet encodeur.
     * @post
     *     result.equals(
     *         "SubstCipher[shift:" + getShift() + "\nlast shifted text:"
     *         + getLastShiftedText() + "]")
     */
    public String toString() {
        return "SubstCipher[shift:" + currentShift + "\nlast shifted text:"
                + lastShiftedText + "]";
    }

    /**
     * Le caractère c décalé circulairement de shift positions dans l'alphabet.
     * @pre <pre>
     *     isNonAccentedLetter(c) </pre>
     * @post <pre>
     *     currentShift >= 0
     *         ==> result == décalé vers la droite de c
     *     currentShift < 0
     *         ==> result == décalé vers la gauche de c </pre>
     */
    private char shiftChar(char c) {
        if (!isNonAccentedLetter(c)){
            return c;
        }
        char base;
        if (Character.isUpperCase(c)){
            base = 'A';
        }else{
            base = 'a';
        }
        return (char) (base + (c - base + currentShift + ALPHABET_SIZE) % ALPHABET_SIZE);
    }

        /**
     * renvoie la position du caractère c.
     * @pre:
     * - isNonAccentedLetter() == true
     * @post:
     * - 0 <= alphaPos(c) && pos(c) < ALPHABET_SIZE
     */
    private static int alphaPos(char c) {
    if (!isNonAccentedLetter(c)) {
        throw new AssertionError("la lettre doit être non accentuée");
    }
    return Character.toLowerCase(c) - 'a';
    }

    
    
    /**
     * Calcule un décalage à partir du message donné en paramètre selon
     *  l'algorithme qui suit.
     * <ul>
     *   <li>compter l'occurrence de chaque lettre non accentuée (minuscule ou
     *       majuscule) du message ;</li>
     *   <li>déterminer un décalage pour encoder MOST_FREQUENT_CHAR par la
     *       lettre apparaissant le plus souvent dans le message.</li>
     * </ul>
     * En cas d'égalité de fréquence de plusieurs lettres du message, la plus
     *  petite parmi ces lettres (pour l'ordre alphabétique) est retournée.
     * Pour un message chiffré égal à la chaîne vide, la valeur retournée est 0.
     * @pre <pre>
     *     text != null </pre>
     * @post <pre>
     *     text.equals("") ==> result == 0
     *     !text.equals("")
     *         ==> Let f ::= la lettre la plus fréquente du message
     *             result ==
     *                     ((f - MOST_FREQUENT_CHAR) + ALPHABET_SIZE)
     *                     % ALPHABET_SIZE </pre>
     */
    public static int guessShiftFrom(String text) {
        if (text == null) {
            throw new AssertionError();
        }

        if (text.equals("")) {
            return 0;
        }
        return guessShiftFromNonEmptyMessage(text);
    }

    /**
     * Calcule un décalage à partir du message (non vide) donné en paramètre
     *  selon l'algorithme qui suit.
     * <ul>
     *   <li>compter l'occurrence de chaque lettre non accentuée (minuscule ou
     *       majuscule) du message ;</li>
     *   <li>déterminer un décalage pour encoder MOST_FREQUENT_CHAR par la
     *       lettre apparaissant le plus souvent dans le message.</li>
     * </ul>
     * En cas d'égalité de fréquence de plusieurs lettres du message, la plus
     *  petite parmi ces lettres (pour l'ordre alphabétique) est retournée.
     * @pre <pre>
     *     text != null && !text.equals("")</pre>
     * @post <pre>
     *     Let f ::= la lettre la plus fréquente du message
     *     result == ((f - MOST_FREQUENT_CHAR) + ALPHABET_SIZE) % ALPHABET_SIZE
     * </pre>
     */
        private static int guessShiftFromNonEmptyMessage(String text) {
        int t[] = charOcc(text);
        int m = max(t);
        return m - alphaPos(MOST_FREQUENT_CHAR);
    }
    
    /**
     * renvoie la position du maximum du tableau donné en paramètre.
     */
    private static int max(int[] t){
        int m = 0;
        for(int i = 1; i < ALPHABET_SIZE; ++i){
            if(t[i] > t[m]){
                m = i;
            }
        }
        return m;
    }
    /**
     * renvoie un tableau contenant le nombre d'occurence de chaque lettre non
     *  accentuée de la chaine de caractère donnée en paramètre.
     */
    private  static int[] charOcc(String s){
        int t[] = new int[ALPHABET_SIZE];
        for(int i = 0; i < s.length(); ++i){
            char c = s.charAt(i);
            if(isNonAccentedLetter(c)){
                t[alphaPos(c)] += 1;
            }
        }
        return t;
    }

}
