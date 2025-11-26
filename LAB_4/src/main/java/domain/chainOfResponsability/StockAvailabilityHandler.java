package domain.chainOfResponsability;


import domain.models.Coffee;
import domain.models.food.Food;
import domain.models.inventory.InventoryManager;
import domain.models.Order;

public class StockAvailabilityHandler extends OrderValidationHandler {
    private InventoryManager inventoryManager;

    public StockAvailabilityHandler() {
        super("Stock Availability Check");
        this.inventoryManager = InventoryManager.getInstance();
    }

    @Override
    protected boolean doValidation(Order order) {
        System.out.println("Checking inventory availability...");

        for (Coffee drink : order.getDrinks()) {
            if (!checkDrinkStock(drink)) {
                System.out.println(" Insufficient stock for: " + drink.getDescription());
                return false;
            }
        }

        // Check food items
        for (Food food : order.getFoods()) {
            if (!checkFoodStock(food)) {
                System.out.println(" Insufficient stock for: " + food.getDescription());
                return false;
            }
        }


        System.out.println("   ✓ All items available in stock");
        return true;
    }

    private boolean checkDrinkStock(Coffee drink) {
        // Check basic ingredients
        if (!inventoryManager.checkAvailability("coffee_beans", 1)) return false;
        if (!inventoryManager.checkAvailability("water", 1)) return false;

        String description = drink.getDescription().toLowerCase();

        // Check for milk-based drinks
        if ((description.contains("latte") || description.contains("cappuccino"))
                && !inventoryManager.checkAvailability("milk", 1)) {
            return false;
        }

        return true;
    }

    private boolean checkFoodStock(Food food) {
        String description = food.getDescription().toLowerCase();

        // Check base food
        if (description.contains("croissant") && !inventoryManager.checkAvailability("croissant", 1)) {
            return false;
        }
        if (description.contains("muffin") && !inventoryManager.checkAvailability("muffin", 1)) {
            return false;
        }

        // Check decorators
        if (description.contains("caramel") && !inventoryManager.checkAvailability("caramel_sauce", 1)) {
            return false;
        }
        if (description.contains("pistachio") && !inventoryManager.checkAvailability("pistachio_paste", 1)) {
            return false;
        }

        return true;
    }
}