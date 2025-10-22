package domain.factory;

import domain.models.Coffee;

public abstract class CoffeeFactory {

    // factory Method - subclasses will override this
    public abstract Coffee createCoffee();

    // template method that uses the factory method
    public Coffee orderCoffee() {
        Coffee coffee = createCoffee();
        System.out.println("Preparing: " + coffee.getDescription());
        return coffee;
    }
}
