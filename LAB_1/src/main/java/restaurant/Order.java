package restaurant;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private List<MenuItem> items = new ArrayList<>();

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void showOrder(){
        System.out.println("\n###-Your order-###:");
        for(MenuItem item : items){
            System.out.println("-" + item.getDescription());
        }
    }
}
