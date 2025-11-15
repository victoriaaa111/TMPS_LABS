package domain.models.food.source;

public class VendorSource implements FoodSource {

    @Override
    public String getSourceDescription() {
        return "Sourced from trusted local vendor";
    }

    @Override
    public double adjustPrice(double basePrice) {
        return basePrice;
    }

    @Override
    public double getPriceModifier() {
        return 0.0;
    }
}
