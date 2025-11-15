// Espresso.java
package domain.models;

public class Espresso extends Coffee {
    public Espresso() {
        super("Espresso", 2.50);
        this.requiresMilk = false;
        this.allowsMilk = false;    // Espresso doesn't have milk
        this.canBeIced = false;      // Traditional espresso is hot only
        this.minShots = 1;
        this.maxShots = 2;
        this.defaultShots = 1;
        this.shots = defaultShots;
    }
}

