package domain.models.food.bridge;


public class HouseMadeSource implements FoodSource {

    @Override
    public String getSourceDescription() {
        return "Freshly made in our coffee shop";
    }

    @Override
    public double adjustPrice(double basePrice) {
        // slightly more expensive due to quality
        return basePrice + 0.50;
    }

    @Override
    public double getPriceModifier() {
        return 0.50;
    }
}
