package domain.factory;

import domain.models.Latte;
import domain.models.Coffee;

public class LatteFactory extends CoffeeFactory {
    @Override
    public Coffee createCoffee() {
        return new Latte();
    }
}
