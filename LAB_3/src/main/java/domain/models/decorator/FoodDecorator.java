package domain.models.decorator;

import domain.models.food.Food;

public abstract class FoodDecorator implements Food {

    protected final Food base;

    protected FoodDecorator(Food base) {
        this.base = base;
    }

    @Override
    public String getName() {
        return base.getName();
    }

    @Override
    public String getDescription() {
        return base.getDescription();
    }

    @Override
    public double getFinalPrice() {
        return base.getFinalPrice();
    }
}
