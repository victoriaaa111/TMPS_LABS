package domain.factory;

import domain.models.Coffee;
import domain.models.enums.*;
import java.util.ArrayList;
import java.util.List;

public class CoffeeBuilder {
    private Coffee coffee;
    private Size size;
    private MilkType milkType;
    private List<Extra> extras;
    private int shots;
    private boolean iced;

    public CoffeeBuilder(Coffee coffee) {
        this.coffee = coffee;
        this.size = Size.MEDIUM;
        this.extras = new ArrayList<>();
        this.shots = coffee.getDefaultShots();
        this.iced = false;

        // Set default milk for drinks that require it
        if (coffee.requiresMilk()) {
            this.milkType = MilkType.WHOLE; // Default milk type
        }
    }

    public CoffeeBuilder withSize(Size size) {
        this.size = size;
        System.out.println("Selecting a cup of " + this.size + " size");
        return this;
    }

    public CoffeeBuilder withMilkType(MilkType milkType) {
        if (coffee.allowsMilk()) {
            this.milkType = milkType;
            System.out.println("Pouring " + this.milkType + " milk");
        } else {
            System.out.println("!!! " + coffee.getName() + " cannot have milk. Skipping milk selection.");
        }
        return this;
    }

    public CoffeeBuilder addExtra(Extra extra) {
        CoffeeShopConfig config = CoffeeShopConfig.getInstance();
        if (extras.size() < config.getMaxExtrasPerDrink()) {
            this.extras.add(extra);
            System.out.println("Adding extra: " + extra);
        }
        return this;
    }

    public CoffeeBuilder withShots(int shots) {
        int validShots = Math.max(coffee.getMinShots(),
                Math.min(shots, coffee.getMaxShots()));

        if (shots != validShots) {
            System.out.println("!!! " + coffee.getName() + " supports " +
                    coffee.getMinShots() + "-" + coffee.getMaxShots() +
                    " shots. Setting to " + validShots + ".");
        }

        this.shots = validShots;
        System.out.println("Pouring " + shots + " shots of coffee");
        return this;
    }

    public CoffeeBuilder makeIced() {
        if (coffee.canBeIced()) {
            this.iced = true;
            System.out.println("Adding ice");
        } else {
            System.out.println("!!! " + coffee.getName() + " cannot be served iced. Keeping it hot.");
        }
        return this;
    }

    public Coffee build() {
        coffee.setSize(size);

        // Ensure milk requirement is met
        if (coffee.requiresMilk() && milkType == null) {
            System.out.println("!!! " + coffee.getName() + " requires milk. Using default Whole Milk.");
            milkType = MilkType.WHOLE;
        }

        coffee.setMilkType(milkType);
        coffee.setExtras(extras);
        coffee.setShots(shots);
        coffee.setIced(iced);

        if (!coffee.isValidConfiguration()) {
            throw new IllegalStateException("Invalid coffee configuration");
        }

        coffee.calculatePrice();
        return coffee;
    }

    public Coffee getCoffee() {
        return coffee;
    }
}