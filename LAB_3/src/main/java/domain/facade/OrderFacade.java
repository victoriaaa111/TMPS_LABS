package domain.facade;

import domain.builder.Builder;
import domain.builder.CoffeeBuilder;
import domain.factory.*;
import domain.models.Coffee;
import domain.models.enums.CoffeeType;
import domain.models.enums.Size;
import domain.models.enums.MilkType;
import domain.models.enums.Extra;

import domain.models.food.Food;
import domain.models.food.Croissant;
import domain.models.food.Muffin;
import domain.models.food.source.FoodSource;
import domain.models.food.source.HouseMadeSource;
import domain.models.food.source.VendorSource;

import java.util.List;

public class OrderFacade {



    public OrderFacade() {
        //no dependencies
    }

    // ============ COFFEE CREATION ============

    public Coffee createCoffeeOrder(
            CoffeeType coffeeType,
            boolean useDefaultRecipe,
            Size size,
            MilkType milkType,
            boolean iced,
            int shots,
            List<Extra> extras
    ) {
        CoffeeFactory factory = getFactoryForType(coffeeType);

        if (useDefaultRecipe) {
            Builder defaultBuilder = factory.getDefaultBuilder();
            return defaultBuilder.build();
        }

        Coffee baseCoffee = factory.createCoffee();
        CoffeeBuilder builder = new CoffeeBuilder(baseCoffee);

        if (size != null) {
            builder.withSize(size);
        }

        if (baseCoffee.allowsMilk() && milkType != null) {
            builder.withMilkType(milkType);
        }

        if (baseCoffee.canBeIced() && iced) {
            builder.makeIced();
        }

        builder.withShots(shots);

        if (extras != null) {
            for (Extra extra : extras) {
                builder.addExtra(extra);
            }
        }

        return builder.build();
    }

    private CoffeeFactory getFactoryForType(CoffeeType type) {
        switch (type) {
            case ESPRESSO:
                return new EspressoFactory();
            case AMERICANO:
                return new AmericanoFactory();
            case CAPPUCCINO:
                return new CappuccinoFactory();
            case LATTE:
                return new LatteFactory();
            default:
                throw new IllegalArgumentException("Unsupported coffee type: " + type);
        }
    }

    // ============ FOOD CREATION (BRIDGE) ============

    public Food createFoodOrder(String foodName, boolean houseMade) {
        FoodSource source = houseMade ? new HouseMadeSource() : new VendorSource();

        switch (foodName.toLowerCase()) {
            case "croissant":
                return new Croissant(source);
            case "muffin":
                return new Muffin(source);
            default:
                throw new IllegalArgumentException("Unknown food item: " + foodName);
        }
    }
}
