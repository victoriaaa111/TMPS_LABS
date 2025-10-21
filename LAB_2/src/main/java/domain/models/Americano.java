package domain.models;

public class Americano extends Coffee {
    public Americano() {
        super("Americano", 3.00);
        this.requiresMilk = false;
        this.allowsMilk = false;     // Americano is espresso + water, no milk
        this.canBeIced = true;       // can be served iced
        this.minShots = 1;
        this.maxShots = 3;
        this.defaultShots = 2;       // Typically comes with 2 shots
        this.shots = defaultShots;
    }
}
