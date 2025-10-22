package domain.factory;

import java.util.HashSet;
import java.util.Set;

public class CoffeeShopConfig {
    private static CoffeeShopConfig instance;
    private String shopName;
    private double taxRate;
    private boolean loyaltyProgramEnabled;
    private int maxExtrasPerDrink;
    private Set<String> loyaltyMembers;
    private double loyaltyDiscount;
    private double shotPrice;

    // Private constructor prevents instantiation from other classes
    private CoffeeShopConfig() {
        this.shopName = "Victoria's TMPS Coffee Shop";
        this.taxRate = 0.08;
        this.loyaltyProgramEnabled = true;
        this.maxExtrasPerDrink = 3;
        this.loyaltyMembers = new HashSet<>();
        this.loyaltyDiscount = 0.10; // 10% discount
        this.shotPrice = 0.50;

        // Pre-populate with some test members
        loyaltyMembers.add("068738282");
    }


    public static CoffeeShopConfig getInstance() {
        if (instance == null) {
            synchronized (CoffeeShopConfig.class) {
                if (instance == null) instance = new CoffeeShopConfig();
            }
        }
        return instance;
    }


    public String getShopName() { return shopName; }
    public double getTaxRate() { return taxRate; }
    public boolean isLoyaltyProgramEnabled() { return loyaltyProgramEnabled; }
    public int getMaxExtrasPerDrink() { return maxExtrasPerDrink; }
    public double getLoyaltyDiscount() { return loyaltyDiscount; }
    public double getShotPrice() { return shotPrice; }

    public void setShopName(String shopName) { this.shopName = shopName; }
    public void setTaxRate(double taxRate) { this.taxRate = taxRate; }

    public boolean isMember(String phoneNumber) {
        return loyaltyMembers.contains(phoneNumber);
    }

    public void addMember(String phoneNumber) {
        loyaltyMembers.add(phoneNumber);
        System.out.println("Yayy!!! Successfully enrolled! Your number: " + phoneNumber);
    }

    public double applyLoyaltyDiscount(double price) {
        return price * (1 - loyaltyDiscount);
    }

    public double[] calculateFinalPrice(double price) {
        double tax = price * this.taxRate;
        double finalPrice = price + tax;
        double [] result = {tax, finalPrice};
        return result;
    }

    public void displayLoyaltyInfo() {
        System.out.println("\nLoyalty Program Benefits:");
        System.out.println("   - " + (int)(loyaltyDiscount * 100) + "% discount on all orders");
    }
}