package domain.models.decorator;

import domain.models.food.Food;

public class CaramelSauceDecorator extends FoodDecorator {

    private static final double CARAMEL_PRICE = 0.80;

    public CaramelSauceDecorator(Food base) {
        super(base);
    }

    @Override
    public String getDescription() {
        return base.getDescription() + " + caramel drizzle";
    }

    @Override
    public double getFinalPrice() {
        return base.getFinalPrice() + CARAMEL_PRICE;
    }

    @Override
    public double getBasePrice() {
        return CARAMEL_PRICE;
    }
}
