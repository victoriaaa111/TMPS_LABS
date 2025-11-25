package client;

import domain.models.food.Food;
import domain.models.Coffee;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserInterface ui = new UserInterface(scanner);

        ui.displayWelcome();

        List<Coffee> drinks = new ArrayList<>();
        List<Food> foods = new ArrayList<>();

        boolean done = false;
        while (!done) {
            System.out.println("\nWhat would you like to add?");
            System.out.println("1. Coffee drink");
            System.out.println("2. Food item");
            System.out.println("3. Finish order");
            System.out.print("Choose (1-3): ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1": {
                    Coffee drink = ui.createCustomCoffee();
                    if (drink != null) {
                        drinks.add(drink);
                        ui.displayDrinkSummary(drink);
                    }
                    break;
                }
                case "2": {
                    Food food = ui.createFoodOrder();
                    if (food != null) {
                        foods.add(food);
                        ui.displayFoodSummary(food);
                    }
                    break;
                }
                case "3":
                    done = true;
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1-3.");
            }
        }

        ui.displayReceipt(drinks, foods);

        ui.displayGoodbye();
        scanner.close();
    }
}
