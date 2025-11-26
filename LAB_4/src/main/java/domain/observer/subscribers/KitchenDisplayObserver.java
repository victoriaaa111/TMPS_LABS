package domain.observer.subscribers;


import domain.models.Coffee;
import domain.models.Order;
import domain.models.food.Food;

public class KitchenDisplayObserver implements OrderStatusObserver {
    private String kitchenName;

    public KitchenDisplayObserver(String kitchenName) {
        this.kitchenName = kitchenName;
    }

    @Override
    public void update(String status, Order order) {
        System.out.println("-".repeat(50));
        System.out.println("\n[Kitchen Display - " + kitchenName + " Kitchen]");

        switch (status) {
            case "CONFIRMED":
                this.displayNewOrder(order);
                break;

            case "PREPARING":
                System.out.println(" Preparing order #" + order.getOrderId());
                break;

            case "READY":
                System.out.println(" Order #" + order.getOrderId() + " is READY for pickup!");
                break;

            case "COMPLETED":
                System.out.println(" Order #" + order.getOrderId() + " completed and picked up.");
                break;

            default:
                System.out.println("Order #" + order.getOrderId() + " status: " + status);

        }
    }

    private void displayNewOrder(Order order) {
        System.out.println("NEW ORDER: " + order.getOrderId());
        System.out.println("Items to prepare:");

        if (!order.getDrinks().isEmpty()) {
            System.out.println("  Drinks:");
            for (Coffee drink : order.getDrinks()) {
                System.out.println("    - " + drink.getDescription());
            }
        }

        if (!order.getFoods().isEmpty()) {
            System.out.println("  Food:");
            for (Food food : order.getFoods()) {
                System.out.println("    - " + food.getDescription());
            }
        }

    }

    private void removeFromDisplay(Order order) {
        System.out.println("\n [" + kitchenName + " Kitchen Display]");
        System.out.println("Order " + order.getOrderId() + " completed - Removed from queue");
    }
}