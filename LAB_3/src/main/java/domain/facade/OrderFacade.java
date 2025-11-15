package domain.facade;

import domain.builder.Builder;
import domain.builder.CoffeeBuilder;
import domain.factory.*;
import domain.models.Coffee;
import domain.models.enums.Size;
import domain.models.enums.MilkType;
import domain.models.enums.Extra;
import domain.models.food.Food;
import domain.models.food.Croissant;
import domain.models.food.Muffin;
import domain.models.food.bridge.FoodSource;
import domain.models.food.bridge.HouseMadeSource;
import domain.models.food.bridge.VendorSource;
import domain.models.decorator.CaramelSauceDecorator;
import domain.models.decorator.PistachioPasteDecorator;

import java.util.List;

public class OrderFacade {
    private final CoffeeShopConfig config;

    public OrderFacade() {
        this.config = CoffeeShopConfig.getInstance();
    }

    // ============ COFFEE FACTORY & BUILDER METHODS ============

    public Coffee createDefaultCoffee(String coffeeTypeName) {
        CoffeeFactory factory = getFactoryForType(coffeeTypeName);
        Builder defaultBuilder = factory.getDefaultBuilder();
        return defaultBuilder.build();
    }

    public Coffee createBaseCoffee(String coffeeTypeName) {
        CoffeeFactory factory = getFactoryForType(coffeeTypeName);
        return factory.createCoffee();
    }

    public Coffee buildCustomCoffee(
            String coffeeTypeName,
            Size size,
            MilkType milkType,
            boolean iced,
            int shots,
            List<Extra> extras
    ) {
        Coffee baseCoffee = createBaseCoffee(coffeeTypeName);
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

    public Coffee createCoffeeOrder(
            String coffeeTypeName,
            boolean useDefaultRecipe,
            Size size,
            MilkType milkType,
            boolean iced,
            int shots,
            List<Extra> extras
    ) {
        if (useDefaultRecipe) {
            return createDefaultCoffee(coffeeTypeName);
        }
        return buildCustomCoffee(coffeeTypeName, size, milkType, iced, shots, extras);
    }

    private CoffeeFactory getFactoryForType(String type) {
        return switch (type.toLowerCase()) {
            case "espresso" -> new EspressoFactory();
            case "americano" -> new AmericanoFactory();
            case "cappuccino" -> new CappuccinoFactory();
            case "latte" -> new LatteFactory();
            default -> throw new IllegalArgumentException("Unsupported coffee type: " + type);
        };
    }

    // ============ COFFEE TYPE RESTRICTIONS ============

    public int getMinShots(String coffeeTypeName) {
        Coffee temp = createBaseCoffee(coffeeTypeName);
        return temp.getMinShots();
    }

    public int getMaxShots(String coffeeTypeName) {
        Coffee temp = createBaseCoffee(coffeeTypeName);
        return temp.getMaxShots();
    }

    public int getDefaultShots(String coffeeTypeName) {
        Coffee temp = createBaseCoffee(coffeeTypeName);
        return temp.getDefaultShots();
    }

    public boolean allowsMilk(String coffeeTypeName) {
        Coffee temp = createBaseCoffee(coffeeTypeName);
        return temp.allowsMilk();
    }

    public boolean requiresMilk(String coffeeTypeName) {
        Coffee temp = createBaseCoffee(coffeeTypeName);
        return temp.requiresMilk();
    }

    public boolean canBeIced(String coffeeTypeName) {
        Coffee temp = createBaseCoffee(coffeeTypeName);
        return temp.canBeIced();
    }

    // ============ PRICING INFORMATION ============

    public double getCoffeeBasePrice(String coffeeTypeName) {
        CoffeeFactory factory = getFactoryForType(coffeeTypeName);
        Coffee temp = factory.createCoffee();
        return temp.getBasePrice();
    }

    public double getSizePrice(Size size) {
        return size.getPriceModifier();
    }

    public double getMilkPrice(MilkType milkType) {
        if (milkType == null) return 0.0;
        return milkType.getExtraCharge();
    }

    public double getExtraPrice(Extra extra) {
        return extra.getPrice();
    }

    public double getShotPrice() {
        return config.getShotPrice();
    }

    public double getFoodBasePrice(String foodName) {
        Food temp = createFoodOrder(foodName, false);
        return temp.getBasePrice();
    }

    // ============ SHOP CONFIGURATION ============

    public String getShopName() {
        return config.getShopName();
    }

    public double getTaxRate() {
        return config.getTaxRate();
    }

    public int getMaxExtrasPerDrink() {
        return config.getMaxExtrasPerDrink();
    }

    public double getLoyaltyDiscount() {
        return config.getLoyaltyDiscount();
    }

    public boolean isLoyaltyProgramEnabled() {
        return config.isLoyaltyProgramEnabled();
    }

    public void setShopName(String shopName) {
        config.setShopName(shopName);
    }

    public void setTaxRate(double taxRate) {
        config.setTaxRate(taxRate);
    }

    // ============ LOYALTY PROGRAM OPERATIONS ============

    public boolean isMember(String phoneNumber) {
        return config.isMember(phoneNumber);
    }

    public void addMember(String phoneNumber) {
        config.addMember(phoneNumber);
    }

    public double applyLoyaltyDiscount(double price) {
        return config.applyLoyaltyDiscount(price);
    }

    public void displayLoyaltyInfo() {
        config.displayLoyaltyInfo();
    }

    // ============ PRICE CALCULATIONS ============

    public double[] calculateFinalPrice(double price) {
        return config.calculateFinalPrice(price);
    }

    // ============ FOOD CREATION (BRIDGE) ============

    public Food createFoodOrder(String foodName, boolean houseMade) {
        FoodSource source = houseMade ? new HouseMadeSource() : new VendorSource();

        return switch (foodName.toLowerCase()) {
            case "croissant" -> new Croissant(source);
            case "muffin" -> new Muffin(source);
            default -> throw new IllegalArgumentException("Unknown food item: " + foodName);
        };
    }

    public double getFoodSourcePriceModifier(int choice) {
        FoodSource source = choice == 1 ? new HouseMadeSource() : new VendorSource();
        return source.getPriceModifier();
    }

    // ============ FOOD DECORATION (DECORATOR PATTERN) ============

    public Food decorateWithCaramel(Food food) {
        return new CaramelSauceDecorator(food);
    }

    public Food decorateWithPistachio(Food food) {
        return new PistachioPasteDecorator(food);
    }

    public double getCaramelDecoratorPrice() {
        Food tempFood = new Croissant(new VendorSource());
        Food decorated = new CaramelSauceDecorator(tempFood);
        return decorated.getBasePrice();
    }

    public double getPistachioDecoratorPrice() {
        // create temporary decorator to get its base price
        Food tempFood = new Croissant(new VendorSource());
        Food decorated = new PistachioPasteDecorator(tempFood);
        return decorated.getBasePrice();
    }

}
