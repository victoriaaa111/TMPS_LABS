package domain.factory;

import domain.builder.Builder;
import domain.builder.DefaultLatteBuilder;
import domain.models.Latte;
import domain.models.Coffee;

public class LatteFactory extends CoffeeFactory {
    @Override
    public Coffee createCoffee() {
        return new Latte();
    }

    @Override
    public Builder getDefaultBuilder() {
        return new DefaultLatteBuilder();
    }
}
