package domain.factory;

import domain.builder.Builder;
import domain.models.Coffee;

public interface CoffeeFactory {
    Coffee createCoffee();
    Builder getDefaultBuilder();
}
