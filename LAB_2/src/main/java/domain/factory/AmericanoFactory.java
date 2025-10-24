package domain.factory;

import domain.builder.Builder;
import domain.builder.DefaultAmericanoBuilder;
import domain.models.Americano;
import domain.models.Coffee;

public class AmericanoFactory extends CoffeeFactory {
    @Override
    public Coffee createCoffee() {
        return new Americano();
    }
    @Override
    public Builder getDefaultBuilder() {
        return new DefaultAmericanoBuilder();
    }
}
