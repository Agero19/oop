/**
 * Propriétés invariantes.
 * - la durée de vie d'un tamagotchi est strictement positive :
 *       0 < this.lifeTime()
 * - l'âge d'un tamagotchi est compris entre 0 et sa durée de vie :
 *     0 <= this.age() <= this.lifeTime()
 * - l'énergie minimale d'un tamagotchi est strictement positive et strictement
 *   inférieure à son énergie maximale :
 *     0 < this.minEnergy() < this.maxEnergy()
 * - l'énergie d'un tamagotchi est strictement positive :
 *     0 <= this.energy()
 * - un tamagotchi est vivant tant que son énergie ne dépasse pas le maximum,
 *   et que son espérance de vie est strictement supérieure à sa dette
 *   énergétique :
 *     this.isAlive()
 *         <==> this.energy() <= this.maxEnergy()
 *              && this.lifeTime() - this.age() > this.energyDebt()
 * - un tamagotchi est affamé ssi il est vivant et que son énergie est
 *   inférieure ou égale à son minimum d'énergie :
 *     this.isStarved() <==> this.isAlive() && this.energy() <= this.minEnergy()
 */
public class Tamagotchi {

    // ATTRIBUTS STATIQUES

    /**
     *  L'accroissement maximal d'énergie apporté par un repas.
     */
    public static final int ENERGY_GAIN = 5;

    // ATTRIBUTS

    /**
     * La durée de vie de ce tamagotchi.
     */
    private final int maxLifetime;

    /**
     * L'énergie maximale de ce tamagotchi.
     */
    private final int minEnergy;

    /**
     * L'énergie minimale de ce tamagotchi.
     */
    private final int maxEnergy;

    /**
     * L'âge de ce tamagotchi.
     */
    private int age;

    /**
     * La dette énergétique de ce tamagotchi.
     */
    private int energyDebt;

    /**
     * L'énergie de ce tamagotchi.
     */
    private int energy;

    // CONSTRUCTEURS

    /**
     * Un tamagotchi qui vient de naître.
     * En entrée
     * - lifeTime doit être strictement positif : 0 < lifeTime
     * - min doit être strictement positif et strictement plus petit que max :
     *     0 < min < max
     * En sortie
     * - ce tamagotchi a un âge égal à 0 : this.age() == 0
     * - ce tamagotchi a une durée de vie égale à lifeTime :
     *     this.lifeTime() == lifeTime
     * - ce tamagotchi a une énergie minimale égale à min :
     *     this.minEnergy() == min
     * - ce tamagotchi a une énergie maximale égale à max :
     *     this.maxEnergy() == max
     * - ce tamagotchi a une énergie entre min et la moyenne de min et max :
     *     min <= this.energy() <= (min + max) / 2
     * - ce tamagotchi n'a pas de dette énergétique :
     *     energyDebt() == 0
     */
    public Tamagotchi(int lifeTime, int min, int max) {
        if (min <= 0 || min >= max || lifeTime <= 0) {
            throw new AssertionError();
        }
        age = 0;
        maxLifetime = lifeTime;
        maxEnergy = max;
        minEnergy = min;
        energy = randomBetween(min, (min + max) / 2);
        energyDebt = 0;
    }

    // REQUETES

    /**
     * L'âge de ce tamagotchi.
     */
    public int getAge() {
        return age;
    }

    /**
     * L'énergie de ce tamagotchi.
     */
    public int getEnergy() {
        return energy;
    }

    /**
     * La dette énergétique de ce tamagotchi.
     */
    public int getEnergyDebt() {
        return energyDebt;
    }

    /**
     * La durée de vie de ce tamagotchi.
     */
    public int getLifeTime() {
        return maxLifetime;
    }

    /**
     * L'énergie maximale de ce tamagotchi.
     */
    public int getMaxEnergy() {
        return maxEnergy;
    }

    /**
     * L'énergie minimale de ce tamagotchi.
     */
    public int getMinEnergy() {
        return minEnergy;
    }

    /**
     * Indique si ce tamagotchi est bien vivant.
     */
    public boolean isAlive() {
        return energy <= maxEnergy && maxLifetime - age > energyDebt;
    }

    /**
     * Indique si ce tamagotchi est affamé.

     */
    public boolean isStarved() {
        if (!isAlive()) {
            throw new AssertionError(
                "Impossible d'évoluer : le tamagotchi est mort !");
        }
        return energy <= minEnergy;
    }

    // COMMANDES

    /**
     * Nourrit ce tamagotchi en lui donnant mealNb plats qui vont chacun
     *  augmenter son niveau d'énergie d'au plus ENERGY_GAIN.
     * En entrée
     * - ce tamagotchi doit être vivant : this.isAlive()
     * - le nombre de plats doit être strictement positif : mealNb > 0
     * En sortie
     * - l'énergie de ce tamagotchi a augmenté d'une valeur égale à la somme
     *   de mealNb valeurs aléatoires comprises entre 1 et ENERGY_GAIN :
     *     Let n ::= old this.energy()
     *         n + mealNb <= this.energy() <= n + mealNb * ENERGY_GAIN
     * - l'âge, la dette énergétique, la durée de vie et les minimum et maximum
     *   d'énergie n'ont pas changé :
     *     this.age() == old this.age()
     *     this.energyDebt() == old this.energyDebt()
     *     this.lifeTime() == old this.lifeTime()
     *     this.minEnergy() == old this.minEnergy()
     *     this.maxEnergy() == old this.maxEnergy()
     */
    public void eat(int mealNb) {
        if (!isAlive() || mealNb <= 0) {
            throw new AssertionError(
                "Impossible d'évoluer : le tamagotchi est mort !");
        }
        for (int i = 0; i < mealNb; ++i) {
            energy += randomBetween(1, ENERGY_GAIN);
        }
    }

    /**
     * Passe un tour de jeu pour ce tamagotchi, qui reste éveillé sans rien
     *  faire.
     * En entrée
     * - ce tamagotchi doit être vivant : this.isAlive()
     * En sortie
     * - l'âge de ce tamagotchi a augmenté de 1 :
     *     this.age() == old this.age() + 1
     * - ce tamagotchi a perdu une unité d'énergie durant le tour, de plus
     *   s'il avait suffisamment d'énergie pour survivre et que sa dette
     *   énergétique était strictement positive, alors il a remboursé cette
     *   dernière d'autant que son niveau d'énergie le lui a permis :
     *     old this.energy() == 0
     *         ==> this.energy() == 0
     *             this.energyDebt() == old this.energyDebt() + 1
     *     old this.energy() == 1
     *         ==> this.energy() == 0
     *             this.energyDebt() == old this.energyDebt()
     *     old this.energy() >= 2
     *         ==> this.energyDebt() ==
     *                 old (this.energyDebt()
     *                     - min(this.energyDebt(), this.energy() - 1))
     *             this.energy() ==
     *                 old (this.energy() - 1
     *                     - min(this.energyDebt(), this.energy() - 1))
     * - la durée de vie et les minimum et maximum d'énergie n'ont pas changé :
     *     this.lifeTime() == old this.lifeTime()
     *     this.minEnergy() == old this.minEnergy()
     *     this.maxEnergy() == old this.maxEnergy()
     */
    public void evolve() {
        if (!isAlive()) {
            throw new AssertionError(
                "Impossible d'évoluer : le tamagotchi est mort !");
        }
        if (energy == 0) {
            ++energyDebt;
        } else if (energy == 1){
            --energy;
        }
        else{
            if (energy >= energyDebt) {
                energy -= energyDebt;
                energyDebt = 0;
            } else {
                energy = 0;
                energyDebt -= energy;
            }
        }
        ++age;
    }

    // OUTILS

    /**
     * Une description textuelle de ce tamagotchi pour jouer sous BlueJ.
     * En sortie
     * - la valeur retournée est une chaîne de caractères de la forme
     *     Tamagotchi[âge:a(A) ; énergie:e(m/M)]
     *   où a et A représentent l'âge et l'âge maximal du tamagotchi, et m, e,
     *   et M représentent respectivement son énergie minimale, son énergie
     *   et son énergie maximale
     */
    public String toString() {
        return this.getClass().getName()
        + "[age:" + age + "(" + maxLifetime + ") ; énergie:"
        + energy + "(" + minEnergy + "/" + maxEnergy + ")]";
    }

    /**
     * .
     * Entrée:
     * - a <= b
     * Sortie:
     * - un entier aléatoire entre a et b
     */
    private int randomBetween(int a, int b) {
        assert a <= b;
        return a + ((int) Math.random() * (b - a + 1));
    }
}
