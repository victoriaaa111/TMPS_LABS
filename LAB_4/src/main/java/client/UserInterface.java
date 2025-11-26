package client;

import domain.facade.OrderFacade;
import domain.models.Coffee;
import domain.models.enums.*;
import domain.models.food.Food;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private final Scanner scanner;
    private final OrderFacade orderFacade;
    private boolean isLoyaltyMember;

    public UserInterface(Scanner scanner) {
        this.scanner = scanner;
        this.orderFacade = new OrderFacade();
        this.isLoyaltyMember = false;
    }

    // ============ DISPLAY METHODS ============

    public void displayWelcome() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("        Welcome to " + orderFacade.getShopName() + "!");
        System.out.println("=".repeat(60));
        System.out.println("Tax Rate: " + (orderFacade.getTaxRate() * 100) + "%");

        if (orderFacade.isLoyaltyProgramEnabled()) {
            System.out.println("\n* Loyalty Program Available!");
            orderFacade.displayLoyaltyInfo();
            checkLoyaltyMembership();
        }
    }

    public void displayGoodbye() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Thank you for visiting " + orderFacade.getShopName() + "!");
        if (isLoyaltyMember) {
            System.out.println("Your loyalty discount was applied to this order!");
        }
        System.out.println("=".repeat(60));
    }

    public void displayCoffeeCharacteristics(String coffeeTypeName) {
        Coffee temp = orderFacade.createBaseCoffee(coffeeTypeName);

        System.out.println("\n* " + coffeeTypeName.toUpperCase() + " Characteristics:");
        System.out.println("   - Base Price: $" + String.format("%.2f", orderFacade.getCoffeeBasePrice(coffeeTypeName)));
        System.out.println("   - Shots: " + temp.getMinShots() + "-" + temp.getMaxShots() +
                " (default: " + temp.getDefaultShots() + ")");
        System.out.println("   - Additional shots: $" + String.format("%.2f", orderFacade.getShotPrice()) + " each");
        System.out.println("   - Milk: " + (temp.requiresMilk() ? "Required" :
                (temp.allowsMilk() ? "Optional" : "Not allowed")));
        System.out.println("   - Temperature: " + (temp.canBeIced() ? "Hot or Iced" : "Hot only"));
    }

    public void displayDrinkSummary(Coffee coffee) {
        double price = coffee.getFinalPrice();

        System.out.println("\n" + "-".repeat(60));
        System.out.println("+ Drink Added:");
        System.out.println("  " + coffee.getDescription());
        System.out.println("  Base Price: $" + String.format("%.2f", price));

        if (isLoyaltyMember) {
            double discountedPrice = orderFacade.applyLoyaltyDiscount(price);
            System.out.println("  Loyalty Discount: -$" + String.format("%.2f", price - discountedPrice));
            System.out.println("  Discounted Price: $" + String.format("%.2f", discountedPrice));
            price = discountedPrice;
        }

        double[] priceBreakdown = orderFacade.calculateFinalPrice(price);
        System.out.println("  Tax: $" + String.format("%.2f", priceBreakdown[0]));
        System.out.println("  Final Price: $" + String.format("%.2f", priceBreakdown[1]));
        System.out.println("-".repeat(60));
    }

    public void displayFoodSummary(Food food) {
        double price = food.getFinalPrice();

        System.out.println("\n" + "-".repeat(60));
        System.out.println("✓ Food Added:");
        System.out.println("  " + food.getDescription());
        System.out.println("  Base Price: $" + String.format("%.2f", price));

        if (isLoyaltyMember) {
            double discountedPrice = orderFacade.applyLoyaltyDiscount(price);
            System.out.println("  Loyalty Discount: -$" + String.format("%.2f", price - discountedPrice));
            System.out.println("  Discounted Price: $" + String.format("%.2f", discountedPrice));
            price = discountedPrice;
        }

        double[] priceBreakdown = orderFacade.calculateFinalPrice(price);
        System.out.println("  Tax: $" + String.format("%.2f", priceBreakdown[0]));
        System.out.println("  Final Price: $" + String.format("%.2f", priceBreakdown[1]));
        System.out.println("-".repeat(60));
    }

    // ============ LOYALTY PROGRAM ============

    private void checkLoyaltyMembership() {
        System.out.print("\nAre you a loyalty member? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("yes") || response.equals("y")) {
            System.out.print("Enter your phone number: ");
            String phone = scanner.nextLine().trim();

            if (orderFacade.isMember(phone)) {
                isLoyaltyMember = true;
                System.out.println("+ Welcome back! Your " + (int)(orderFacade.getLoyaltyDiscount() * 100) + "% discount will be applied.");
            } else {
                System.out.println("x Phone number not found in our system.");
                offerLoyaltySignup();
            }
        } else {
            offerLoyaltySignup();
        }
    }

    private void offerLoyaltySignup() {
        System.out.print("\nWould you like to sign up for our loyalty program? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("yes") || response.equals("y")) {
            System.out.print("Enter your phone number to register: ");
            String phone = scanner.nextLine().trim();

            if (!phone.isEmpty()) {
                orderFacade.addMember(phone);
                isLoyaltyMember = true;
                System.out.println("+ You can now enjoy " + (int)(orderFacade.getLoyaltyDiscount() * 100) + "% off your order!");
            }
        }
    }

    // ============ COFFEE CREATION ============

    public Coffee createCustomCoffee() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("               !!! NEW DRINK ORDER !!!");
        System.out.println("=".repeat(60));

        String coffeeTypeName = selectCoffeeType();
        if (coffeeTypeName == null) return null;

        displayCoffeeCharacteristics(coffeeTypeName);

        System.out.print("\nWould you like the default configuration? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("yes") || response.equals("y")) {
            return orderFacade.createCoffeeOrder(coffeeTypeName, true, null, null, false,
                    orderFacade.getDefaultShots(coffeeTypeName), null);
        }

        return buildCustomCoffee(coffeeTypeName);
    }

    private Coffee buildCustomCoffee(String coffeeTypeName) {
        Size size = selectSize();
        MilkType milkType = null;
        boolean iced = false;
        int shots;
        List<Extra> extras = new ArrayList<>();

        if (orderFacade.allowsMilk(coffeeTypeName)) {
            milkType = selectMilkType(orderFacade.requiresMilk(coffeeTypeName));
        } else {
            System.out.println("\n★ " + coffeeTypeName.toUpperCase() + " does not include milk.");
        }

        if (orderFacade.canBeIced(coffeeTypeName)) {
            System.out.print("\nWould you like it iced? (yes/no): ");
            String icedResponse = scanner.nextLine().trim().toLowerCase();
            iced = icedResponse.equals("yes") || icedResponse.equals("y");
        } else {
            System.out.println("\n★ " + coffeeTypeName.toUpperCase() + " is served hot only.");
        }

        shots = selectShots(coffeeTypeName);
        addExtras(extras);

        return orderFacade.createCoffeeOrder(coffeeTypeName, false, size, milkType, iced, shots, extras);
    }

    // ============ FOOD CREATION ============

    public Food createFoodOrder() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("               !!! NEW FOOD ORDER !!!");
        System.out.println("=".repeat(60));

        String foodName = selectFoodType();
        if (foodName == null) return null;

        boolean houseMade = selectFoodSource();

        Food food = orderFacade.createFoodOrder(foodName, houseMade);

        // Apply decorators
        food = addFoodDecorators(food);

        return food;
    }

    private Food addFoodDecorators(Food baseFood) {
        Food decoratedFood = baseFood;

        System.out.println("\nWould you like to add any toppings?");

        if (askYesNo("Add caramel drizzle? (+$" +
                String.format("%.2f", orderFacade.getCaramelDecoratorPrice()) + ")")) {
            decoratedFood = orderFacade.decorateWithCaramel(decoratedFood);
            System.out.println("✓ Added caramel drizzle");
        }

        if (askYesNo("Add pistachio paste? (+$" +
                String.format("%.2f", orderFacade.getPistachioDecoratorPrice()) + ")")) {
            decoratedFood = orderFacade.decorateWithPistachio(decoratedFood);
            System.out.println("✓ Added pistachio paste");
        }

        return decoratedFood;
    }


    // ============ INPUT SELECTION METHODS ============

    private String selectCoffeeType() {
        System.out.println("\nAvailable Coffee Types:");
        System.out.println("1. Espresso    - $" + String.format("%.2f", orderFacade.getCoffeeBasePrice("espresso")));
        System.out.println("2. Americano   - $" + String.format("%.2f", orderFacade.getCoffeeBasePrice("americano")));
        System.out.println("3. Cappuccino  - $" + String.format("%.2f", orderFacade.getCoffeeBasePrice("cappuccino")));
        System.out.println("4. Latte       - $" + String.format("%.2f", orderFacade.getCoffeeBasePrice("latte")));
        System.out.print("\nSelect coffee type (1-4): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            return switch (choice) {
                case 1 -> "espresso";
                case 2 -> "americano";
                case 3 -> "cappuccino";
                case 4 -> "latte";
                default -> {
                    System.out.println("Invalid choice. Please try again.");
                    yield selectCoffeeType();
                }
            };
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return selectCoffeeType();
        }
    }

    private Size selectSize() {
        System.out.println("\nSelect Size:");
        System.out.println("1. Small   (+$" + String.format("%.2f", orderFacade.getSizePrice(Size.SMALL)) + ")");
        System.out.println("2. Medium  (+$" + String.format("%.2f", orderFacade.getSizePrice(Size.MEDIUM)) + ")");
        System.out.println("3. Large   (+$" + String.format("%.2f", orderFacade.getSizePrice(Size.LARGE)) + ")");
        System.out.print("\nYour choice (1-3): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            return switch (choice) {
                case 1 -> Size.SMALL;
                case 2 -> Size.MEDIUM;
                case 3 -> Size.LARGE;
                default -> {
                    System.out.println("Invalid choice. Defaulting to Medium.");
                    yield Size.MEDIUM;
                }
            };
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Defaulting to Medium.");
            return Size.MEDIUM;
        }
    }

    private MilkType selectMilkType(boolean required) {
        System.out.println("\n* Select Milk Type" + (required ? " (Required)" : " (Optional)") + ":");
        System.out.println("1. Whole Milk      (+$" + String.format("%.2f", orderFacade.getMilkPrice(MilkType.WHOLE)) + ")");
        System.out.println("2. Pistachio Milk  (+$" + String.format("%.2f", orderFacade.getMilkPrice(MilkType.PISTACHIO)) + ")");
        System.out.println("3. Almond Milk     (+$" + String.format("%.2f", orderFacade.getMilkPrice(MilkType.ALMOND)) + ")");
        System.out.println("4. Oat Milk        (+$" + String.format("%.2f", orderFacade.getMilkPrice(MilkType.OAT)) + ")");
        System.out.println("5. Soy Milk        (+$" + String.format("%.2f", orderFacade.getMilkPrice(MilkType.SOY)) + ")");
        System.out.println("6. Coconut Milk    (+$" + String.format("%.2f", orderFacade.getMilkPrice(MilkType.COCONUT)) + ")");
        if (!required) {
            System.out.println("7. No milk");
        }
        System.out.print("\nYour choice (1-" + (required ? "6" : "7") + "): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            switch (choice) {
                case 1: return MilkType.WHOLE;
                case 2: return MilkType.PISTACHIO;
                case 3: return MilkType.ALMOND;
                case 4: return MilkType.OAT;
                case 5: return MilkType.SOY;
                case 6: return MilkType.COCONUT;
                case 7:
                    if (!required) return null;
                    System.out.println("Milk is required. Defaulting to Whole Milk.");
                    return MilkType.WHOLE;
                default:
                    System.out.println("Invalid choice. " + (required ? "Defaulting to Whole Milk." : "No milk added."));
                    return required ? MilkType.WHOLE : null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. " + (required ? "Defaulting to Whole Milk." : "No milk added."));
            return required ? MilkType.WHOLE : null;
        }
    }

    private int selectShots(String coffeeTypeName) {
        int min = orderFacade.getMinShots(coffeeTypeName);
        int max = orderFacade.getMaxShots(coffeeTypeName);
        int defaultShots = orderFacade.getDefaultShots(coffeeTypeName);

        System.out.print("\n* Number of espresso shots (" + min + "-" + max +
                ") [Press Enter for default: " + defaultShots + "],  additional shots: $ " + String.format("%.2f", orderFacade.getShotPrice()) + " each): ");

        try {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Using default: " + defaultShots + " shot(s).");
                return defaultShots;
            }

            int shots = Integer.parseInt(input);
            if (shots >= min && shots <= max) {
                return shots;
            }
            System.out.println("Invalid range. Using default: " + defaultShots + " shot(s).");
            return defaultShots;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Using default: " + defaultShots + " shot(s).");
            return defaultShots;
        }
    }

    private void addExtras(List<Extra> extras) {
        System.out.println("\n* Available Extras:");
        System.out.println("1. Whipped Cream    (+$" + String.format("%.2f", orderFacade.getExtraPrice(Extra.WHIPPED_CREAM)) + ")");
        System.out.println("2. Caramel Syrup    (+$" + String.format("%.2f", orderFacade.getExtraPrice(Extra.CARAMEL)) + ")");
        System.out.println("3. Vanilla Syrup    (+$" + String.format("%.2f", orderFacade.getExtraPrice(Extra.VANILLA)) + ")");
        System.out.println("4. Chocolate Syrup  (+$" + String.format("%.2f", orderFacade.getExtraPrice(Extra.CHOCOLATE)) + ")");
        System.out.println("5. Cinnamon         (+$" + String.format("%.2f", orderFacade.getExtraPrice(Extra.CINNAMON)) + ")");
        System.out.println("\nMaximum " + orderFacade.getMaxExtrasPerDrink() + " extras allowed.");

        int maxExtras = orderFacade.getMaxExtrasPerDrink();
        int extrasAdded = 0;

        while (extrasAdded < maxExtras) {
            System.out.print("\nAdd extra (1-5) or 0 to skip: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                if (choice == 0) break;

                Extra extra = null;
                switch (choice) {
                    case 1: extra = Extra.WHIPPED_CREAM; break;
                    case 2: extra = Extra.CARAMEL; break;
                    case 3: extra = Extra.VANILLA; break;
                    case 4: extra = Extra.CHOCOLATE; break;
                    case 5: extra = Extra.CINNAMON; break;
                    default:
                        System.out.println("Invalid choice.");
                        continue;
                }

                if (extra != null) {
                    extras.add(extra);
                    extrasAdded++;
                    System.out.println("+ Added " + extra.name());

                    if (extrasAdded < maxExtras) {
                        System.out.print("Add another extra? (yes/no): ");
                        String more = scanner.nextLine().trim().toLowerCase();
                        if (!more.equals("yes") && !more.equals("y")) break;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }
    }

    private String selectFoodType() {
        System.out.println("\nAvailable Food Items:");
        System.out.println("1. Croissant - Base $" + "(+$" + String.format("%.2f", orderFacade.getFoodBasePrice("croissant")) + ")");
        System.out.println("2. Muffin    - Base $" + "(+$" + String.format("%.2f", orderFacade.getFoodBasePrice("muffin")) + ")");
        System.out.print("\nSelect food (1-2): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            return switch (choice) {
                case 1 -> "croissant";
                case 2 -> "muffin";
                default -> {
                    System.out.println("Invalid choice.");
                    yield null;
                }
            };
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return null;
        }
    }

    private boolean selectFoodSource() {
        System.out.println("\nSelect Source:");
        System.out.println("1. House-made " + "(+$" + String.format("%.2f", orderFacade.getFoodSourcePriceModifier(1)) + ")");
        System.out.println("2. Vendor " + "(+$" + String.format("%.2f", orderFacade.getFoodSourcePriceModifier(2)) + ")");
        System.out.print("\nYour choice (1-2): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            return choice == 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Defaulting to Vendor.");
            return false;
        }
    }

    public boolean askYesNo(String question) {
        System.out.print("\n" + question + " (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("yes") || response.equals("y");
    }

    public void addDrinkToOrder(Coffee coffee) {
        orderFacade.addDrinkToOrder(coffee);
    }


    public void addFoodToOrder(Food food) {
        orderFacade.addFoodToOrder(food);
    }



    public void proceedToCheckout() {
        if (orderFacade.isOrderEmpty()) {
            System.out.println("\n Your order is empty. Please add items first.");
            return;
        }

        // Display current order
        orderFacade.displayCurrentOrder();

        // Calculate total with loyalty discount
        double subtotal = orderFacade.getCurrentOrderTotal();
        double finalSubtotal = subtotal;

        if (isLoyaltyMember) {
            double discountedAmount = orderFacade.applyLoyaltyDiscount(subtotal);
            double discount = subtotal - discountedAmount;
            finalSubtotal = discountedAmount;

            System.out.println("\n Loyalty Discount Applied:");
            System.out.printf("   Subtotal: $%.2f%n", subtotal);
            System.out.printf("   Discount (%.0f%%): -$%.2f%n",
                    orderFacade.getLoyaltyDiscount() * 100, discount);
            System.out.printf("   Discounted Subtotal: $%.2f%n", finalSubtotal);
        }

        double[] priceBreakdown = orderFacade.calculateFinalPrice(finalSubtotal);
        double tax = priceBreakdown[0];
        double total = priceBreakdown[1];

        System.out.printf("\n Tax (%.0f%%): $%.2f%n", orderFacade.getTaxRate() * 100, tax);
        System.out.printf(" TOTAL TO PAY: $%.2f%n", total);

        if (!orderFacade.validateOrder()) {
            System.out.println("\n Order validation failed! Cannot proceed to payment.");
            return;
        }
        // Proceed to payment
        handlePayment(total);
    }

    private void handlePayment(double total) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                 PAYMENT");
        System.out.println("=".repeat(60));
        System.out.printf("Amount to pay: $%.2f%n", total);

        boolean paid = false;

        while (!paid) {
            System.out.println("\nSelect payment method:");
            System.out.println("1. Cash");
            System.out.println("2. Card");
            System.out.println("3. Mobile payment");
            System.out.print("Your choice (1-3): ");

            String choice = scanner.nextLine().trim();
            String receipt = null;

            switch (choice) {
                case "1":
                    receipt = handleCashPayment(total);
                    break;
                case "2":
                    receipt = handleCardPayment(total);
                    break;
                case "3":
                    receipt = handleMobilePayment(total);
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
                    continue;
            }

            if (receipt == null) {
                System.out.println("\n Payment failed or validation error.");
                if (!askYesNo("Would you like to try a different payment method?")) {
                    System.out.println("Order cancelled. Items returned to inventory.");
                    orderFacade.clearCurrentOrder();
                    return;
                }
            } else {
                paid = true;
                System.out.println("\n Payment successful! Order confirmed.");
            }
        }
    }

    private String handleCashPayment(double total) {
        while (true) {
            System.out.print("\nEnter cash amount given by customer: ");
            String input = scanner.nextLine().trim();
            try {
                double cash = Double.parseDouble(input);
                // Facade creates and uses CashPaymentStrategy internally
                return orderFacade.payWithCash(total, cash);
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please enter a valid number.");
            }
        }
    }

    private String handleCardPayment(double total) {
        System.out.println("\n--- Card Payment ---");

        System.out.print("Cardholder Name: ");
        String cardHolder = scanner.nextLine().trim();

        System.out.print("Card Number (numbers only or with spaces): ");
        String cardNumber = scanner.nextLine().trim();

        System.out.print("CVV (3-4 digits): ");
        String cvv = scanner.nextLine().trim();

        System.out.print("Expiry Date (MM/YY): ");
        String expiry = scanner.nextLine().trim();

        // Facade creates CardPaymentStrategy and processes everything
        return orderFacade.payWithCard(total, cardNumber, cardHolder, cvv, expiry);
    }

    private String handleMobilePayment(double total) {
        System.out.println("\n--- Mobile Payment ---");

        System.out.print("Phone number (e.g. 063 333 333): ");
        String phone = scanner.nextLine().trim();

        System.out.print("Provider (e.g. Apple Pay, Google Pay, Revolut): ");
        String provider = scanner.nextLine().trim();

        System.out.print("Device ID: ");
        String deviceId = scanner.nextLine().trim();

        // Facade creates MobilePaymentStrategy and processes everything
        return orderFacade.payWithMobile(total, phone, provider, deviceId);
    }



}
