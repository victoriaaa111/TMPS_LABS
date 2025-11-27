package domain.models.food.bridge;

public interface FoodSource {
    String getSourceDescription();
    double adjustPrice(double basePrice);
    double getPriceModifier();
}