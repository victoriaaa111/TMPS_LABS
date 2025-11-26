package domain.observer.subscribers;

import domain.models.inventory.InventoryManager;
import domain.models.Coffee;
import domain.models.Order;
import domain.models.food.Food;
import domain.observer.publisher.OrderSubject;

public class InventoryObserver implements OrderStatusObserver {
    private InventoryManager inventoryManager;
    private OrderSubject subject;

    public InventoryObserver(OrderSubject subject) {
        this.inventoryManager = InventoryManager.getInstance();
        this.subject = subject;
    }

    @Override
    public void update(String status, Order order) {
        if (status.equals("CONFIRMED")) {
            System.out.println("-".repeat(50));
            updateInventory(order);
            System.out.println("-".repeat(50));

            // Detach after updating inventory - no longer needed
            subject.detach(this);
            System.out.println(" InventoryObserver detached (job complete)");
        }
    }

    private void updateInventory(Order order) {
        System.out.println("\n[Inventory Update for Order " + order.getOrderId() + "]");

        // Reduce stock for each drink
        for (Coffee drink : order.getDrinks()) {
            inventoryManager.reduceStock("coffee_beans", 1);
            inventoryManager.reduceStock("water", 1);

            String description = drink.getDescription().toLowerCase();
            if (description.contains("latte") || description.contains("cappuccino")) {
                inventoryManager.reduceStock("milk", 1);
            }

        }

        // Reduce stock for each food item
        for (Food food : order.getFoods()) {
            String description = food.getDescription().toLowerCase();
            if (description.contains("croissant")) {
                inventoryManager.reduceStock("croissant", 1);
            } else if (description.contains("muffin")) {
                inventoryManager.reduceStock("muffin", 1);
            }

            if (description.contains("caramel")) {
                inventoryManager.reduceStock("caramel_sauce", 1);
            }
            if (description.contains("pistachio")) {
                inventoryManager.reduceStock("pistachio_paste", 1);
            }
        }
        System.out.println(" Inventory updated successfully!");
    }
}