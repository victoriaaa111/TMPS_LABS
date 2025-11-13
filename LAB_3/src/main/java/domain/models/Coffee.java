package domain.models;

import domain.factory.CoffeeShopConfig;
import domain.models.enums.*;
import java.util.List;
import java.util.ArrayList;

public abstract class Coffee {
    protected String name;
    protected double basePrice;
    protected Size size;
    protected MilkType milkType;
    protected List<Extra> extras;
    protected int shots;
    protected boolean iced;
    protected double finalPrice;

    // Coffee type characteristics
    protected boolean requiresMilk;
    protected boolean allowsMilk;
    protected boolean canBeIced;
    protected int minShots;
    protected int maxShots;
    protected int defaultShots;

    public Coffee(String name, double basePrice) {
        this.name = name;
        this.basePrice = basePrice;
        this.extras = new ArrayList<>();
        this.iced = false;

        // Default values (can be overridden)
        this.requiresMilk = false;
        this.allowsMilk = true;
        this.canBeIced = true;
        this.minShots = 1;
        this.maxShots = 4;
        this.defaultShots = 1;
        this.shots = defaultShots;
    }

    public void calculatePrice() {
        CoffeeShopConfig config = CoffeeShopConfig.getInstance();
        finalPrice = basePrice;

        if (size != null) {
            finalPrice += size.getPriceModifier();
        }

        if (milkType != null) {
            finalPrice += milkType.getExtraCharge();
        }

        for (Extra extra : extras) {
            finalPrice += extra.getPrice();
        }

        // Additional shots cost extra
        finalPrice += (shots - defaultShots) * config.getShotPrice();
    }

    public String getDescription() {
        StringBuilder desc = new StringBuilder();
        desc.append(iced ? "Iced " : "Hot ");
        desc.append(size != null ? size.name() + " " : "");
        desc.append(name);

        if (milkType != null) {
            desc.append(" with ").append(milkType.getDisplayName());
        }

        if (shots != defaultShots) {
            desc.append(", ").append(shots).append(" shot").append(shots > 1 ? "s" : "");
        }

        if (!extras.isEmpty()) {
            desc.append(" + ");
            for (int i = 0; i < extras.size(); i++) {
                desc.append(extras.get(i).getDisplayName());
                if (i < extras.size() - 1) desc.append(", ");
            }
        }

        return desc.toString();
    }

    // Getters
    public String getName() { return name; }
    public double getFinalPrice() { return finalPrice; }
    public boolean requiresMilk() { return requiresMilk; }
    public boolean allowsMilk() { return allowsMilk; }
    public boolean canBeIced() { return canBeIced; }
    public int getMinShots() { return minShots; }
    public int getMaxShots() { return maxShots; }
    public int getDefaultShots() { return defaultShots; }

    // Setters
    public void setSize(Size size) { this.size = size; }
    public void setMilkType(MilkType milkType) { this.milkType = milkType; }
    public void setExtras(List<Extra> extras) { this.extras = extras; }
    public void setShots(int shots) { this.shots = shots; }
    public void setIced(boolean iced) { this.iced = iced; }

    // Validation method
    public boolean isValidConfiguration() {
        if (requiresMilk && milkType == null) {
            return false;
        }
        if (!allowsMilk && milkType != null) {
            return false;
        }
        if (!canBeIced && iced) {
            return false;
        }
        if (shots < minShots || shots > maxShots) {
            return false;
        }
        return true;
    }
}