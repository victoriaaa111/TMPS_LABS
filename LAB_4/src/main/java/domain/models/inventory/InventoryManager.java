package domain.models.inventory;

import java.util.HashMap;
import java.util.Map;

public class InventoryManager {
    private static InventoryManager instance;
    private Map<String, InventoryItem> inventory;

    private InventoryManager() {
        this.inventory = new HashMap<>();
        initializeInventory();
    }

    public static InventoryManager getInstance() {
        if (instance == null) {
            instance = new InventoryManager();
        }
        return instance;
    }

    private void initializeInventory() {
        inventory.put("coffee_beans", new InventoryItem("Coffee Beans", 100, 20));
        inventory.put("milk", new InventoryItem("Milk", 50, 10));
        inventory.put("water", new InventoryItem("Water", 200, 30));
        inventory.put("caramel_sauce", new InventoryItem("Caramel Sauce", 30, 5));
        inventory.put("pistachio_paste", new InventoryItem("Pistachio Paste", 25, 5));
        inventory.put("croissant", new InventoryItem("Croissant", 3, 1));
        inventory.put("muffin", new InventoryItem("Muffin", 2, 1));
    }

    public boolean checkAvailability(String itemName, int quantity) {
        InventoryItem item = inventory.get(itemName);
        return item != null && item.hasStock(quantity);
    }

    public void reduceStock(String itemName, int quantity) {
        InventoryItem item = inventory.get(itemName);
        if (item != null) {
            item.reduceStock(quantity);
        }
    }


    public InventoryItem getItem(String itemName) {
        return inventory.get(itemName);
    }
}