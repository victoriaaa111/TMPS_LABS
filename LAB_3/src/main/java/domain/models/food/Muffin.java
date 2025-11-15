package domain.models.food;

import domain.models.food.bridge.FoodSource;

public class Muffin implements Food {

    private static final double BASE_PRICE = 2.00;

    private final FoodSource source;

    public Muffin(FoodSource source) {
        this.source = source;
    }

    @Override
    public String getName() {
        return "Muffin";
    }

    @Override
    public String getDescription() {
        return "Muffin (" + source.getSourceDescription() + ")";
    }

    @Override
    public double getFinalPrice() {
        return source.adjustPrice(BASE_PRICE);
    }
    @Override
    public double getBasePrice() {
        return BASE_PRICE;
    }
}
