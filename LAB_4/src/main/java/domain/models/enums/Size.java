package domain.models.enums;

public enum Size {
    SMALL(0.0),
    MEDIUM(0.50),
    LARGE(1.00);

    private final double priceModifier;

    Size(double priceModifier) {
        this.priceModifier = priceModifier;
    }

    public double getPriceModifier() {
        return priceModifier;
    }
}