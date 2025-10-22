package domain.factory;

import domain.models.Coffee;
import domain.models.Cappuccino;

public class CappuccinoFactory extends CoffeeFactory {

    @Override
    public Coffee createCoffee() {
        return new Cappuccino();
    }
}