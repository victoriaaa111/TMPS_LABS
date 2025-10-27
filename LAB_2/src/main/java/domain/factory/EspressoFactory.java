package domain.factory;

import domain.builder.Builder;
import domain.builder.DefaultEspressoBuilder;
import domain.models.Espresso;
import domain.models.Coffee;

public class EspressoFactory extends CoffeeFactory {
    @Override
    public Coffee createCoffee() {
        return new Espresso();
    }
    @Override
    public Builder getDefaultBuilder() {
        return new DefaultEspressoBuilder();
    }
}
