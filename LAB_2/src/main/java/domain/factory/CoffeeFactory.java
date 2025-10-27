package domain.factory;

import domain.builder.Builder;
import domain.models.Coffee;

public abstract class CoffeeFactory {

    // Factory Method - subclasses will override this
    public abstract Coffee createCoffee();

    // Factory Method for default builder, subclasses will override this
    public abstract Builder getDefaultBuilder();

    // Template method that uses the factory method
    public Coffee orderCoffee() {
        Coffee coffee = createCoffee();
        System.out.println("Preparing: " + coffee.getName());
        return coffee;
    }
}