package restaurant;

import java.util.Scanner;

public class RestaurantMain {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        //Create menu items
        MenuItem soup = new Food("Cream soup", 50, true);
        MenuItem lasagna = new Food("Lasagna", 70, false);
        MenuItem saladCaesar = new Food("Caesar salad", 80, false);
        MenuItem potatoes = new Food("Fried potatoes with sausages", 90, false);
        MenuItem pizza = new Food("Pizza Margherita", 100, false);
        MenuItem salad = new Food("Salad with tomatoes and cucumbers", 60, true);

        MenuItem cocaCola = new Drink("Coca Cola", 15, false);
        MenuItem sprite = new Drink("Sprite", 15, false);
        MenuItem fanta = new Drink("Fanta", 15, false);
        MenuItem coffee = new Drink("Coffee", 25, false);
        MenuItem water = new Drink("Water", 12, false);
        MenuItem lemonade = new Drink("Lemonade", 65, false);
        MenuItem mojito = new Drink("Mojito", 70, true);
        MenuItem aperol = new Drink("Aperol Spritz", 90, true);

        System.out.println("\n");
        System.out.println("=== MENU ===");
        System.out.println("\n");
        System.out.println("###-FOOD-###");
        System.out.println("1. " + soup.getDescription());
        System.out.println("2. " + lasagna.getDescription());
        System.out.println("3. " + saladCaesar.getDescription());
        System.out.println("4. " + salad.getDescription());
        System.out.println("5. " + potatoes.getDescription());
        System.out.println("6. " + pizza.getDescription());

        System.out.println("\n");
        System.out.println("###-DRINKS-###");
        System.out.println("7. " + cocaCola.getDescription());
        System.out.println("8. " + sprite.getDescription());
        System.out.println("9. " + fanta.getDescription());
        System.out.println("10. " + coffee.getDescription());
        System.out.println("11. " + water.getDescription());
        System.out.println("12. " + lemonade.getDescription());
        System.out.println("13. " + mojito.getDescription());
        System.out.println("14. " + aperol.getDescription());

        System.out.println("\n");

        Order order = new Order();
        System.out.println("Enter item numbers (1-14). Enter 0 to finish:");




        while (true) {
            if (!sc.hasNextInt()) {
                System.out.println("Please enter a number between 1-14 (Enter 0 to finish).");
                sc.nextLine(); // clear invalid input
                continue;
            }
            int choice = sc.nextInt();
            if (choice == 0) break;
            switch (choice) {
                case 1 -> order.addItem(soup);
                case 2 -> order.addItem(lasagna);
                case 3 -> order.addItem(saladCaesar);
                case 4 -> order.addItem(salad);
                case 5 -> order.addItem(potatoes);
                case 6 -> order.addItem(pizza);
                case 7 -> order.addItem(cocaCola);
                case 8 -> order.addItem(sprite);
                case 9 -> order.addItem(fanta);
                case 10 -> order.addItem(coffee);
                case 11 -> order.addItem(water);
                case 12 -> order.addItem(lemonade);
                case 13 -> order.addItem(mojito);
                case 14 -> order.addItem(aperol);
                default -> System.out.println("Invalid choice.");
            }
        }
        order.showOrder();

        PriceCalculator calculator = new PriceCalculator();
        sc.nextLine(); // consume leftover newline

        String discountCode;
        while (true) {
            System.out.print("\nEnter discount code (or press Enter to skip): ");
            discountCode = sc.nextLine();

            if (discountCode.isBlank()) {
                discountCode = "";
                break;
            }

            if (calculator.isValidCode(discountCode)) {
                break;
            } else {
                System.out.println("Invalid code. Please try again.");
            }
        }

        double total = calculator.calculateTotal(order, discountCode);
        System.out.println("Final total: lei " + total);



    }
}