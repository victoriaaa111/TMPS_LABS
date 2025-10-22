package domain.factory;

import domain.models.Americano;
import domain.models.Coffee;

public class AmericanoFactory extends CoffeeFactory {
    @Override
    public Coffee createCoffee() {
        return new Americano();
    }
}
