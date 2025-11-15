package domain.models;

public class Cappuccino extends Coffee {
    public Cappuccino() {
        super("Cappuccino", 3.75);
        this.requiresMilk = true;    // Must have milk
        this.allowsMilk = true;
        this.canBeIced = false;
        this.minShots = 1;
        this.maxShots = 2;
        this.defaultShots = 1;
        this.shots = defaultShots;
    }
}
