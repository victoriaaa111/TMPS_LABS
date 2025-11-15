package domain.models.decorator;
import domain.models.food.Food;

public class PistachioPasteDecorator extends FoodDecorator {

    private static final double PISTACHIO_PRICE = 1.20;

    public PistachioPasteDecorator(Food base) {
        super(base);
    }

    @Override
    public String getDescription() {
        return base.getDescription() + " + pistachio paste";
    }

    @Override
    public double getFinalPrice() {
        return base.getFinalPrice() + PISTACHIO_PRICE;
    }

    @Override
    public double getBasePrice() {
        return PISTACHIO_PRICE;
    }
}
