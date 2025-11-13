package domain.models.food.source;

public interface FoodSource {
    String getSourceDescription();
    double adjustPrice(double basePrice);
}