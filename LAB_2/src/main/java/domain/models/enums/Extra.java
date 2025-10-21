package domain.models.enums;

public enum Extra {
    WHIPPED_CREAM("Whipped Cream", 0.50),
    CARAMEL("Caramel Syrup", 0.60),
    VANILLA("Vanilla Syrup", 0.60),
    CHOCOLATE("Chocolate Syrup", 0.60),
    COCONUT("Coconut Syrup", 0.60),
    CINNAMON("Cinnamon", 0.25);

    private final String displayName;
    private final double price;

    Extra(String displayName, double price) {
        this.displayName = displayName;
        this.price = price;
    }

    public String getDisplayName() { return displayName; }
    public double getPrice() { return price; }
}