package domain.models.food;
import domain.models.food.bridge.FoodSource;

public class Croissant implements Food {
    private static final double BASE_PRICE = 2.50;

    private final FoodSource source;

    public Croissant(FoodSource source) {
        this.source = source;
    }

    @Override
    public String getName() {
        return "Croissant";
    }

    @Override
    public String getDescription() {
        return "Croissant (" + source.getSourceDescription() + ")";
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
