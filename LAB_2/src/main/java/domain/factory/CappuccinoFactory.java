package domain.factory;

import domain.builder.Builder;
import domain.builder.DefaultCappuccinoBuilder;
import domain.models.Coffee;
import domain.models.Cappuccino;

public class CappuccinoFactory extends CoffeeFactory {

    @Override
    public Coffee createCoffee() {
        return new Cappuccino();
    }

    @Override
    public Builder getDefaultBuilder() {
        return new DefaultCappuccinoBuilder();
    }
}