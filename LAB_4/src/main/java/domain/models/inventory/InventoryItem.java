package domain.models.inventory;


public class InventoryItem {
    private String itemName;
    private int quantity;
    private int lowStockThreshold;

    public InventoryItem(String itemName, int initialQuantity, int lowStockThreshold) {
        this.itemName = itemName;
        this.quantity = initialQuantity;
        this.lowStockThreshold = lowStockThreshold;
    }

    public boolean hasStock(int required) {
        return quantity >= required;
    }

    public void reduceStock(int amount) {
        if (amount <= quantity) {
            quantity -= amount;
            System.out.println(" " + itemName + ": " + quantity + " remaining");

            if (quantity <= lowStockThreshold) {
                System.out.println("  LOW STOCK WARNING: " + itemName);
            }
        }
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

}