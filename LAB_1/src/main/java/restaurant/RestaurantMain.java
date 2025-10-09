package restaurant;

import java.util.Scanner;

public class RestaurantMain {
    public static void main(String[] args) {

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
        System.out.println("1. " + cocaCola.getDescription());
        System.out.println("2. " + sprite.getDescription());
        System.out.println("3. " + fanta.getDescription());
        System.out.println("4. " + coffee.getDescription());
        System.out.println("5. " + water.getDescription());
        System.out.println("6. " + lemonade.getDescription());
        System.out.println("7. " + mojito.getDescription());
        System.out.println("8. " + aperol.getDescription());

        System.out.println("\n");
    }
}