package domain.facade;

import domain.builder.Builder;
import domain.builder.CoffeeBuilder;
import domain.chainOfResponsability.*;
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
import domain.observer.publisher.OrderSubject;
import domain.observer.subscribers.CustomerNotificationObserver;
import domain.observer.subscribers.InventoryObserver;
import domain.observer.subscribers.KitchenDisplayObserver;
import domain.strategy.CardPaymentStrategy;
import domain.strategy.CashPaymentStrategy;
import domain.strategy.MobilePaymentStrategy;
import domain.strategy.PaymentStrategy;
import domain.models.Order;
import java.util.List;

public class OrderFacade {
    private final CoffeeShopConfig config;

    // Current order being built
    private Order currentOrder;

    // Observer Pattern
    private OrderSubject orderSubject;

    // Chain of Responsibility
    private OrderValidationHandler validationChain;
    public OrderFacade() {
        this.config = CoffeeShopConfig.getInstance();
        initializeNewOrder();
        setupValidationChain();
    }

    private void initializeNewOrder() {
        String orderId = generateOrderId();
        this.currentOrder = new Order(orderId);
        this.orderSubject = new OrderSubject();
        setupObservers();
    }

    private void setupObservers() {
        orderSubject.attach(new KitchenDisplayObserver("Main"));
        orderSubject.attach(new CustomerNotificationObserver());
        orderSubject.attach(new InventoryObserver(orderSubject));
    }

    private void setupValidationChain() {
        // Create handlers
        OrderValidationHandler emptyOrderHandler = new EmptyOrderHandler();
        OrderValidationHandler workingHoursHandler = new WorkingHoursHandler();
        OrderValidationHandler stockHandler = new StockAvailabilityHandler();
        OrderValidationHandler minimumOrderHandler = new MinimumOrderHandler();

        // Chain them together
        emptyOrderHandler.setNext(workingHoursHandler);
        workingHoursHandler.setNext(stockHandler);
        stockHandler.setNext(minimumOrderHandler);

        // Set the first handler as the chain entry point
        validationChain = emptyOrderHandler;
    }

    private String generateOrderId() {
        return "ORD-" + System.currentTimeMillis();
    }

    // ============ ORDER MANAGEMENT ============

    public void addDrinkToOrder(Coffee coffee) {
        currentOrder.addDrink(coffee);
        System.out.println("" +
                " Added to order: " + coffee.getDescription());
    }

    public void addFoodToOrder(Food food) {
        currentOrder.addFood(food);
        System.out.println(" Added to order: " + food.getDescription());
    }

    public void displayCurrentOrder() {
        currentOrder.displayOrder();
    }

    public void clearCurrentOrder() {
        System.out.println("\n  Clearing order...");
        initializeNewOrder();
    }

    public boolean isOrderEmpty() {
        return currentOrder.isEmpty();
    }

    public double getCurrentOrderTotal() {
        return currentOrder.getTotal();
    }

    public String getCurrentOrderId() {
        return currentOrder.getOrderId();
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

    // ============ PAYMENT (STRATEGY) ============

    public String payWithCash(double amount, double cashTendered) {

        return processPayment(amount, new CashPaymentStrategy(cashTendered));

    }

    public String payWithCard(double amount,
                              String cardNumber,
                              String cardHolderName,
                              String cvv,
                              String expiryDate) {

        return processPayment(amount,
                new CardPaymentStrategy(cardNumber, cardHolderName, cvv, expiryDate));

    }

    public String payWithMobile(double amount,
                                String phoneNumber,
                                String provider,
                                String deviceId) {
        return processPayment(amount,
                new MobilePaymentStrategy(phoneNumber, provider, deviceId));
    }


    private String processPayment(double amount, PaymentStrategy paymentStrategy) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           PROCESSING PAYMENT");
        System.out.println("=".repeat(50));
        System.out.println("Order ID: " + currentOrder.getOrderId());
        System.out.println("Total Amount: $" + String.format("%.2f", amount));
        System.out.println("Payment Method: " + paymentStrategy.getPaymentMethodName());
        System.out.println("=".repeat(50));

        // STRATEGY PATTERN
        if (!paymentStrategy.processPayment(amount)) {
            System.out.println("\n Payment processing failed!");
            return null;
        }

        String receipt = paymentStrategy.getPaymentReceipt(amount);
        System.out.println(receipt);

        // OBSERVER PATTERN - notify after successful payment
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           NOTIFYING OBSERVERS");
        System.out.println("=".repeat(50));

        orderSubject.setStatus("CONFIRMED", currentOrder);
        simulateOrderPreparation();

        System.out.println("\n Thank you for your order!");

        // Clear order after successful payment
        initializeNewOrder();

        return receipt;
    }

    // =========== OBSERVER ============
    private void simulateOrderPreparation() {
        try {
            Thread.sleep(1000);
            orderSubject.setStatus("PREPARING", currentOrder);

            Thread.sleep(2000);
            orderSubject.setStatus("READY", currentOrder);

            Thread.sleep(1000);
            orderSubject.setStatus("COMPLETED", currentOrder);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    //=========== CHAIN OF RESPONSABILITY ============

    public boolean validateOrder() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           ORDER VALIDATION");
        System.out.println("=".repeat(50));


        boolean isValid = validationChain.validate(currentOrder);

        if (!isValid) {
            System.out.println("\n Validation failed!");
            return false;
        }

        System.out.println("\n All validations passed!");
        return true;
    }

}
