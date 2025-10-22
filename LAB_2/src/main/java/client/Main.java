package client;

import domain.factory.*;
import domain.models.*;
import domain.models.enums.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static CoffeeShopConfig config;
    private static boolean hasLoyaltyDiscount = false;

    public static void main(String[] args) {
        config = CoffeeShopConfig.getInstance();

        displayWelcome();
        handleLoyaltyProgram();

        boolean ordering = true;
        List<Coffee> order = new ArrayList<>();

        while (ordering) {
            Coffee coffee = createCustomCoffee();
            if (coffee != null) {
                order.add(coffee);
                displayDrinkSummary(coffee);
            }

            System.out.print("\nWould you like to add another drink? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();
            ordering = response.equals("yes") || response.equals("y");
        }

        displayFinalReceipt(order);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("Thank you for visiting " + config.getShopName() + "!");
        System.out.println("=".repeat(60));

        scanner.close();
    }

    private static void displayWelcome() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("        Welcome to " + config.getShopName() + "! ");
        System.out.println("=".repeat(60));
        System.out.println("Tax Rate: " + (config.getTaxRate() * 100) + "%");
    }

    private static void handleLoyaltyProgram() {
        if (!config.isLoyaltyProgramEnabled()) {
            return;
        }

        System.out.println("\n" + "-".repeat(60));
        System.out.print("Are you a loyalty program member? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("yes") || response.equals("y")) {
            // Verify existing membership
            verifyMembership();
        } else {
            // Offer to join
            offerMembership();
        }
    }

    private static void verifyMembership() {
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;

        while (attempts < MAX_ATTEMPTS) {
            System.out.print("Please enter your phone number (9 digits): ");
            String phoneNumber = scanner.nextLine().trim();

            if (config.isMember(phoneNumber)) {
                System.out.println(" Membership verified! Welcome back!");
                config.displayLoyaltyInfo();
                hasLoyaltyDiscount = true;
                return;
            } else {
                attempts++;
                if (attempts < MAX_ATTEMPTS) {
                    System.out.println("!! Number not found. You have " +
                            (MAX_ATTEMPTS - attempts) + " attempt(s) remaining.");
                    System.out.print("Would you like to try again? (yes/no): ");
                    String retry = scanner.nextLine().trim().toLowerCase();
                    if (!retry.equals("yes") && !retry.equals("y")) {
                        break;
                    }
                }
            }
        }

        if (attempts == MAX_ATTEMPTS) {
            System.out.println("! Maximum attempts reached.");
        }
        System.out.println("Continuing without loyalty discount...");
    }

    private static void offerMembership() {
        config.displayLoyaltyInfo();
        System.out.print("\nWould you like to join our loyalty program? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("yes") || response.equals("y")) {
            System.out.print("Great! Please enter your phone number (9 digits): ");
            String phoneNumber = scanner.nextLine().trim();

            if (phoneNumber.matches("\\d{9}")) {
                config.addMember(phoneNumber);
                hasLoyaltyDiscount = true;
                System.out.println("Your discount is now active for this order!");
            } else {
                System.out.println("✗ Invalid phone number format. Continuing without enrollment.");
            }
        } else {
            System.out.println("No problem! You can join anytime.");
        }
    }

    private static Coffee createCustomCoffee() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("               !!! NEW DRINK ORDER !!!");
        System.out.println("=".repeat(60));

        // Select coffee type using Factory Method
        CoffeeFactory factory = selectCoffeeType();
        if (factory == null) return null;

        Coffee baseCoffee = factory.createCoffee();

        // Display coffee characteristics
        displayCoffeeCharacteristics(baseCoffee);

        // Use Builder Pattern to customize
        CoffeeBuilder builder = new CoffeeBuilder(baseCoffee);

        // Customize size
        Size size = selectSize();
        builder.withSize(size);

        // Customize milk type (only if allowed)
        if (baseCoffee.allowsMilk()) {
            MilkType milkType = selectMilkType(baseCoffee.requiresMilk());
            if (milkType != null) {
                builder.withMilkType(milkType);
            }
        } else {
            System.out.println("\n " + baseCoffee.getName() + " does not include milk.");
        }

        // Select if iced (only if allowed)
        if (baseCoffee.canBeIced()) {
            System.out.print("\nWould you like it iced? (yes/no): ");
            String icedResponse = scanner.nextLine().trim().toLowerCase();
            if (icedResponse.equals("yes") || icedResponse.equals("y")) {
                builder.makeIced();
            }
        } else {
            System.out.println("\n " + baseCoffee.getName() + " is served hot only.");
        }

        // Select number of shots (within allowed range)
        int shots = selectShots(baseCoffee);
        builder.withShots(shots);

        // Add extras
        addExtras(builder);

        return builder.build();
    }

    private static void displayCoffeeCharacteristics(Coffee coffee) {
        System.out.println("\n " + coffee.getName() + " Characteristics:");
        System.out.println("   • Shots: " + coffee.getMinShots() + "-" + coffee.getMaxShots() +
                " (default: " + coffee.getDefaultShots() + ")");
        System.out.println("   • Milk: " + (coffee.requiresMilk() ? "Required" :
                (coffee.allowsMilk() ? "Optional" : "Not allowed")));
        System.out.println("   • Temperature: " + (coffee.canBeIced() ? "Hot or Iced" : "Hot only"));
    }

    private static CoffeeFactory selectCoffeeType() {
        System.out.println("\nAvailable Coffee Types:");
        System.out.println("1. Espresso    - $2.50");
        System.out.println("2. Americano   - $3.00");
        System.out.println("3. Cappuccino  - $3.75");
        System.out.println("4. Latte       - $4.25");
        System.out.print("\nSelect coffee type (1-4): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            switch (choice) {
                case 1: return new EspressoFactory();
                case 2: return new AmericanoFactory();
                case 3: return new CappuccinoFactory();
                case 4: return new LatteFactory();
                default:
                    System.out.println("Invalid choice. Please try again.");
                    return selectCoffeeType();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return selectCoffeeType();
        }
    }

    private static Size selectSize() {
        System.out.println("\nSelect Size:");
        System.out.println("1. Small   (+$0.00)");
        System.out.println("2. Medium  (+$0.50)");
        System.out.println("3. Large   (+$1.00)");
        System.out.print("\nYour choice (1-3): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            switch (choice) {
                case 1: return Size.SMALL;
                case 2: return Size.MEDIUM;
                case 3: return Size.LARGE;
                default:
                    System.out.println("Invalid choice. Defaulting to Medium.");
                    return Size.MEDIUM;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Defaulting to Medium.");
            return Size.MEDIUM;
        }
    }

    private static MilkType selectMilkType(boolean required) {
        System.out.println("\n Select Milk Type" + (required ? " (Required)" : " (Optional)") + ":");
        System.out.println("1. Whole Milk      (+$0.00)");
        System.out.println("2. Pistachio Milk       (+$0.90)");
        System.out.println("3. Almond Milk     (+$0.75)");
        System.out.println("4. Oat Milk        (+$0.75)");
        System.out.println("5. Soy Milk        (+$0.50)");
        System.out.println("6. Coconut Milk        (+$0.50)");
        if (!required) {
            System.out.println("7. No milk");
        }
        System.out.print("\nYour choice (1-" + (required ? "5" : "6") + "): ");

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
                    if (!required) {
                        return null;
                    }
                    System.out.println("Milk is required. Defaulting to Whole Milk.");
                    return MilkType.WHOLE;
                default:
                    System.out.println("Invalid choice. " +
                            (required ? "Defaulting to Whole Milk." : "No milk added."));
                    return required ? MilkType.WHOLE : null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. " +
                    (required ? "Defaulting to Whole Milk." : "No milk added."));
            return required ? MilkType.WHOLE : null;
        }
    }

    private static int selectShots(Coffee coffee) {
        System.out.print("\n Number of espresso shots (" + coffee.getMinShots() +
                "-" + coffee.getMaxShots() + ") [default: " + coffee.getDefaultShots() +
                ", +$0.50 each additional]: ");
        try {
            int shots = Integer.parseInt(scanner.nextLine().trim());
            return Math.max(coffee.getMinShots(), Math.min(shots, coffee.getMaxShots()));
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Using default: " + coffee.getDefaultShots() + " shot(s).");
            return coffee.getDefaultShots();
        }
    }

    private static void addExtras(CoffeeBuilder builder) {
        System.out.println("\n Available Extras:");
        System.out.println("1. Whipped Cream    (+$0.50)");
        System.out.println("2. Caramel Syrup    (+$0.60)");
        System.out.println("3. Vanilla Syrup    (+$0.60)");
        System.out.println("4. Chocolate Syrup  (+$0.60)");
        System.out.println("5. Cinnamon         (+$0.25)");
        System.out.println("\nMaximum " + config.getMaxExtrasPerDrink() + " extras allowed.");

        int extrasAdded = 0;
        while (extrasAdded < config.getMaxExtrasPerDrink()) {
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
                    builder.addExtra(extra);
                    extrasAdded++;
                    System.out.println("✓ Added " + extra.getDisplayName());

                    if (extrasAdded < config.getMaxExtrasPerDrink()) {
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

    private static void displayDrinkSummary(Coffee coffee) {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("✓ Drink Added:");
        System.out.println("  " + coffee.getDescription());
        System.out.println("  Base Price: $" + String.format("%.2f", coffee.getFinalPrice()));

        if (hasLoyaltyDiscount) {
            double discounted = config.applyLoyaltyDiscount(coffee.getFinalPrice());
            System.out.println("  After Loyalty Discount: $" + String.format("%.2f", discounted));
        }
        System.out.println("-".repeat(60));
    }

    private static void displayFinalReceipt(List<Coffee> order) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                     RECEIPT");
        System.out.println("=".repeat(60));

        double subtotal = 0.0;

        for (int i = 0; i < order.size(); i++) {
            Coffee coffee = order.get(i);
            System.out.println((i + 1) + ". " + coffee.getDescription());
            System.out.println("   $" + String.format("%.2f", coffee.getFinalPrice()));
            subtotal += coffee.getFinalPrice();
        }

        System.out.println("-".repeat(60));
        System.out.println("Subtotal:           $" + String.format("%.2f", subtotal));

        if (hasLoyaltyDiscount) {
            double discountedSubtotal = config.applyLoyaltyDiscount(subtotal);
            double savings = subtotal - discountedSubtotal;
            System.out.println("Loyalty Discount:  -$" + String.format("%.2f", savings));
            System.out.println("New Subtotal:       $" + String.format("%.2f", discountedSubtotal));
            subtotal = discountedSubtotal;
        }

        double [] result = config.calculateFinalPrice(subtotal);

        System.out.println("Tax (" + (config.getTaxRate() * 100) + "%):         $" +
        String.format("%.2f", result[0]));
        System.out.println("=".repeat(60));
        System.out.println("TOTAL:              $" + String.format("%.2f", result[1]));
        System.out.println("=".repeat(60));
    }

}