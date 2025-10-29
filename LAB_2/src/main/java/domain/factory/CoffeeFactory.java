package domain.factory;

import domain.builder.Builder;
import domain.models.Coffee;

public abstract class CoffeeFactory {

    // Factory Method - subclasses will override this
    public abstract Coffee createCoffee();

    // Factory Method for default builder, subclasses will override this
    public abstract Builder getDefaultBuilder();

}