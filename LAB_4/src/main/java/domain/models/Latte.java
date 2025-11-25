package domain.models;

public class Latte extends Coffee {
    public Latte() {
        super("Latte", 4.25);
        this.requiresMilk = true;    // Must have milk
        this.allowsMilk = true;
        this.canBeIced = true;       // Can be served iced
        this.minShots = 1;
        this.maxShots = 2;
        this.defaultShots = 1;
        this.shots = defaultShots;
    }
}