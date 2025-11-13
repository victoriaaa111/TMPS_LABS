package client;

import domain.facade.OrderFacade;
import domain.factory.CoffeeShopConfig;
import domain.models.food.Food;
import domain.models.decorator.CaramelSauceDecorator;
import domain.models.decorator.PistachioPasteDecorator;
import domain.models.Coffee;
import domain.models.enums.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserInterface {

    private final Scanner scanner;
    private final CoffeeShopConfig config;
    private final OrderFacade orderFacade;

    public UserInterface(Scanner scanner) {
        this.scanner = scanner;
        this.config = CoffeeShopConfig.getInstance();
        this.orderFacade = new OrderFacade();
    }

    // ===================== BASIC UI =====================

    public void displayWelcome() {
        System.out.println("=".repeat(60));
        System.out.println("           Welcome to " + config.getShopName());
        System.out.println("=".repeat(60));
    }

    public void displayGoodbye() {
        System.out.println("\nThank you for visiting " + config.getShopName() + "!");
        System.out.println("Have a wonderful day! ☕");
    }

    // ===================== LOYALTY PROGRAM =====================

    public boolean handleLoyaltyProgram() {
        if (!config.isLoyaltyProgramEnabled()) {
            System.out.println("\nLoyalty program is currently not available.");
            return false;
        }

        System.out.println("\n" + "-".repeat(60));
        System.out.print("Are you a loyalty program member? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("yes") || response.equals("y")) {
            System.out.print("Please enter your phone number: ");
            String phone = scanner.nextLine().trim();
            if (config.isMember(phone)) {
                System.out.println("Welcome back, valued member! Your discount will be applied.");
                return true;
            } else {
                System.out.println("We couldn't find you in our system.");
                System.out.println("Continuing without loyalty discount...");
                return false;
            }
        } else {
            System.out.println("\nYou are not a member yet.");
            config.displayLoyaltyInfo();
            System.out.print("\nWould you like to join our loyalty program? (yes/no): ");
            String join = scanner.nextLine().trim().toLowerCase();
            if (join.equals("yes") || join.equals("y")) {
                System.out.print("Enter your phone number to register: ");
                String phone = scanner.nextLine().trim();
                config.addMember(phone);
                return true;
            } else {
                System.out.println("No worries! You can join anytime later.");
                return false;
            }
        }
    }

    // ===================== ORDER CREATION =====================

    public Coffee createCustomCoffee() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("               !!! NEW DRINK ORDER !!!");
        System.out.println("=".repeat(60));

        CoffeeType coffeeType = selectCoffeeType();
        boolean useDefaultRecipe = askYesNo("Would you like the DEFAULT recipe for this drink?");

        Size size = null;
        MilkType milkType = null;
        boolean iced = false;
        int shots = 1;
        List<Extra> extras = new ArrayList<>();

        if (!useDefaultRecipe) {
            size = selectSize();

            if (coffeeAllowsMilk(coffeeType)) {
                milkType = selectMilkType(coffeeRequiresMilk(coffeeType));
            }

            if (coffeeCanBeIced(coffeeType)) {
                iced = askYesNo("Would you like it iced?");
            }

            shots = selectShots();
            extras = selectExtras();
        }

        return orderFacade.createCoffeeOrder(
                coffeeType,
                useDefaultRecipe,
                size,
                milkType,
                iced,
                shots,
                extras
        );
    }

    public Food createFoodItem() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("               !!! NEW FOOD ITEM !!!");
        System.out.println("=".repeat(60));

        System.out.println("Select food:");
        System.out.println("1. Croissant");
        System.out.println("2. Muffin");
        System.out.println("3. Sandwich");
        System.out.print("Choose (1-3): ");

        String foodName;
        while (true) {
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    foodName = "croissant";
                    break;
                case "2":
                    foodName = "muffin";
                    break;
                case "3":
                    foodName = "sandwich";
                    break;
                default:
                    System.out.print("Invalid choice. Please select 1-3: ");
                    continue;
            }
            break;
        }

        boolean houseMade = askYesNo("Should it be house-made (instead of vendor-sourced)?");

        Food food = orderFacade.createFoodOrder(foodName, houseMade);

        // ask for food decorators (toppings)
        food = askForFoodToppings(food);

        return food;
    }

    // ===================== FOOD DECORATOR CHOICES =====================

    private Food askForFoodToppings(Food baseFood) {
        System.out.println("\nWould you like to add toppings to your " + baseFood.getName() + "?");
        System.out.println("1. No toppings");
        System.out.println("2. Pistachio paste (+1.20€)");
        System.out.println("3. Caramel drizzle (+0.80€)");
        System.out.println("4. Both pistachio paste and caramel drizzle (+2.00€)");
        System.out.print("Choose option (1-4): ");

        String input = scanner.nextLine().trim();

        switch (input) {
            case "2":
                return new PistachioPasteDecorator(baseFood);
            case "3":
                return new CaramelSauceDecorator(baseFood);
            case "4":
                return new CaramelSauceDecorator(
                        new PistachioPasteDecorator(baseFood)
                );
            default:
                return baseFood;
        }
    }

    // ===================== SELECTION HELPERS =====================

    private CoffeeType selectCoffeeType() {
        System.out.println("\nAvailable Coffee Types:");
        System.out.println("1. Espresso");
        System.out.println("2. Americano");
        System.out.println("3. Cappuccino");
        System.out.println("4. Latte");
        System.out.print("Select coffee type (1-4): ");

        while (true) {
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1": return CoffeeType.ESPRESSO;
                case "2": return CoffeeType.AMERICANO;
                case "3": return CoffeeType.CAPPUCCINO;
                case "4": return CoffeeType.LATTE;
                default:
                    System.out.print("Invalid choice. Please select 1-4: ");
            }
        }
    }

    private Size selectSize() {
        System.out.println("\nSelect size:");
        int i = 1;
        for (Size s : Size.values()) {
            System.out.println(i + ". " + s);
            i++;
        }
        System.out.print("Choose size (1-" + Size.values().length + "): ");

        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 1 && choice <= Size.values().length) {
                    return Size.values()[choice - 1];
                }
            } catch (NumberFormatException ignored) { }
            System.out.print("Invalid choice. Choose size again: ");
        }
    }

    private MilkType selectMilkType(boolean required) {
        System.out.println("\nSelect milk type:");
        int i = 1;
        for (MilkType m : MilkType.values()) {
            System.out.println(i + ". " + m);
            i++;
        }
        if (!required) {
            System.out.println(i + ". No milk");
        }

        System.out.print("Choose option (1-" + (required ? MilkType.values().length : MilkType.values().length + 1) + "): ");

        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 1 && choice <= MilkType.values().length) {
                    return MilkType.values()[choice - 1];
                } else if (!required && choice == MilkType.values().length + 1) {
                    return null; // no milk
                }
            } catch (NumberFormatException ignored) { }
            System.out.print("Invalid choice. Try again: ");
        }
    }

    private int selectShots() {
        System.out.print("\nHow many espresso shots? (1-4): ");
        while (true) {
            try {
                int shots = Integer.parseInt(scanner.nextLine().trim());
                if (shots >= 1 && shots <= 4) {
                    return shots;
                }
            } catch (NumberFormatException ignored) { }
            System.out.print("Invalid number of shots. Enter 1-4: ");
        }
    }

    private List<Extra> selectExtras() {
        List<Extra> extras = new ArrayList<>();

        if (!askYesNo("\nWould you like to add extras?")) {
            return extras;
        }

        while (true) {
            System.out.println("\nAvailable extras:");
            int i = 1;
            for (Extra e : Extra.values()) {
                System.out.println(i + ". " + e);
                i++;
            }
            System.out.println(i + ". Done adding extras");
            System.out.print("Choose extra (1-" + i + "): ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice == i) {
                    break;
                }
                if (choice >= 1 && choice < i) {
                    Extra selected = Extra.values()[choice - 1];
                    extras.add(selected);
                    System.out.println(selected + " added.");
                } else {
                    System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }

            if (!askYesNo("Add another extra?")) {
                break;
            }
        }

        return extras;
    }

    private boolean coffeeAllowsMilk(CoffeeType type) {
        return type == CoffeeType.CAPPUCCINO
                || type == CoffeeType.LATTE
                || type == CoffeeType.AMERICANO;
    }

    private boolean coffeeRequiresMilk(CoffeeType type) {
        return type == CoffeeType.CAPPUCCINO
                || type == CoffeeType.LATTE;
    }

    private boolean coffeeCanBeIced(CoffeeType type) {
        return type == CoffeeType.AMERICANO
                || type == CoffeeType.LATTE;
    }

    // ===================== RECEIPT / SUMMARY =====================

    public void displayDrinkSummary(Coffee coffee, boolean hasLoyaltyDiscount) {
        System.out.println("\n--- Drink Summary ---");
        System.out.println("Item: " + coffee.getName());
        System.out.println("Details: " + coffee.getDescription());
        System.out.println("Base Price: $" + String.format("%.2f", coffee.getFinalPrice()));

        if (hasLoyaltyDiscount) {
            double discounted = config.applyLoyaltyDiscount(coffee.getFinalPrice());
            double savings = coffee.getFinalPrice() - discounted;
            System.out.println("Loyalty Savings: -$" + String.format("%.2f", savings));
            System.out.println("After Loyalty: $" + String.format("%.2f", discounted));
        }
    }

    public void displayFoodSummary(Food food) {
        System.out.println("\n--- Food Summary ---");
        System.out.println("Item: " + food.getName());
        System.out.println("Details: " + food.getDescription());
        System.out.println("Price: $" + String.format("%.2f", food.getFinalPrice()));
    }

    public void displayFinalReceipt(List<Coffee> drinks, List<Food> foods, boolean hasLoyaltyDiscount) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                 FINAL RECEIPT");
        System.out.println("=".repeat(60));

        double subtotal = 0.0;
        int index = 1;

        for (Coffee coffee : drinks) {
            System.out.printf("%2d. %-30s $%6.2f%n",
                    index++,
                    coffee.getDescription(),
                    coffee.getFinalPrice());
            subtotal += coffee.getFinalPrice();
        }

        for (Food food : foods) {
            System.out.printf("%2d. %-30s $%6.2f%n",
                    index++,
                    food.getDescription(),
                    food.getFinalPrice());
            subtotal += food.getFinalPrice();
        }

        System.out.println("-".repeat(60));
        System.out.println("Subtotal:           $" + String.format("%.2f", subtotal));

        if (hasLoyaltyDiscount) {
            double discountedSubtotal = config.applyLoyaltyDiscount(subtotal);
            double savings = subtotal - discountedSubtotal;
            System.out.println("Loyalty Discount:  -$" + String.format("%.2f", savings));
            subtotal = discountedSubtotal;
        }

        double[] result = config.calculateFinalPrice(subtotal);
        double tax = result[0];
        double total = result[1];

        System.out.println("Tax:                $" + String.format("%.2f", tax));
        System.out.println("=".repeat(60));
        System.out.println("TOTAL:              $" + String.format("%.2f", total));
        System.out.println("=".repeat(60));
    }

    // ===================== UTILS =====================

    public boolean askYesNo(String question) {
        System.out.print("\n" + question + " (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("yes") || response.equals("y");
    }
}
