package restaurant;

public class Food extends MenuItem{
    private boolean vegetarian;

    public Food(String name, double price, boolean vegetarian) {
        super(name, price);
        this.vegetarian = vegetarian;
    }

    @Override
    public String getDescription() {
        return getName() + " - lei " + getPrice() + (vegetarian ? " (Vegetarian)" : "");
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }
}
