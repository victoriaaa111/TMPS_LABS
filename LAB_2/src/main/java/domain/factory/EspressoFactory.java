package domain.factory;

import domain.models.Espresso;
import domain.models.Coffee;

public class EspressoFactory extends CoffeeFactory {
    @Override
    public Coffee createCoffee() {
        return new Espresso();
    }
}
