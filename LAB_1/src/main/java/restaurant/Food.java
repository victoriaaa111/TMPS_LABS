package restaurant;

public class Food extends MenuItem{
    private boolean vegetarian;

    public Food(String name, double price, boolean vegetarian) {
        super(name, price);
        this.vegetarian = vegetarian;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }
}
