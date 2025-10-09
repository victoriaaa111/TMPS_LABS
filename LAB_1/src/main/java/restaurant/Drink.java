package restaurant;

public class Drink extends MenuItem {
    private boolean alcoholic;

    public Drink(String name, double price, boolean alcoholic) {
        super(name, price);
        this.alcoholic = alcoholic;
    }

    @Override
    public String getDescription() {
        return getName() + " - lei " + getPrice() + (alcoholic ? " (Alcoholic)" : "");
    }

    public boolean isAlcoholic() {
        return alcoholic;
    }

    public void setAlcoholic(boolean alcoholic) {
        this.alcoholic = alcoholic;
    }
}
