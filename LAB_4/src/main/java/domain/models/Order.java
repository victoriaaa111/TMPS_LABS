package domain.models;

import domain.models.food.Food;
import java.util.ArrayList;
import java.util.List;



public class Order {
    private String orderId;
    private List<Coffee> drinks;
    private List<Food> foods;

    public Order(String orderId) {
        this.orderId = orderId;
        this.drinks = new ArrayList<>();
        this.foods = new ArrayList<>();
    }

    public void addDrink(Coffee drink) {
        drinks.add(drink);
    }

    public void addFood(Food food) {
        foods.add(food);
    }

    public boolean isEmpty() {
        return drinks.isEmpty() && foods.isEmpty();
    }

    public double getTotal() {
        double total = 0.0;

        for (Coffee drink : drinks) {
            total += drink.getFinalPrice();
        }

        for (Food food : foods) {
            total += food.getFinalPrice();
        }

        return total;
    }

    public void displayOrder() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                   ORDER SUMMARY");
        System.out.println("                   Order ID: " + orderId);
        System.out.println("=".repeat(60));

        if (isEmpty()) {
            System.out.println("  (No items in order)");
            System.out.println("=".repeat(60));
            return;
        }

        if (!drinks.isEmpty()) {
            System.out.println("\n DRINKS:");
            for (int i = 0; i < drinks.size(); i++) {
                Coffee drink = drinks.get(i);
                System.out.printf("  %d. %s - $%.2f%n",
                        i + 1, drink.getDescription(), drink.getFinalPrice());
            }
        }

        if (!foods.isEmpty()) {
            System.out.println("\n FOOD:");
            for (int i = 0; i < foods.size(); i++) {
                Food food = foods.get(i);
                System.out.printf("  %d. %s - $%.2f%n",
                        i + 1, food.getDescription(), food.getFinalPrice());
            }
        }

        System.out.println("\n" + "-".repeat(60));
        System.out.printf("SUBTOTAL: $%.2f%n", getTotal());
        System.out.println("=".repeat(60));
    }

    // Getters for observers and validators
    public String getOrderId() {
        return orderId;
    }

    public List<Coffee> getDrinks() {
        return new ArrayList<>(drinks); // Return copy for safety
    }

    public List<Food> getFoods() {
        return new ArrayList<>(foods); // Return copy for safety
    }
}