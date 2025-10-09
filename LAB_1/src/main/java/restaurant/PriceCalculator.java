package restaurant;

import java.util.Scanner;

public class PriceCalculator {
    private static final double STUDENT_DISCOUNT = 0.10;

    public double calculateTotal(Order order, String discountCode){
        double total = 0;
        for(MenuItem item: order.getItems()){
            total += item.getPrice();
        }
        if(discountCode != null && discountCode.equalsIgnoreCase("STUDENT")){
            System.out.println("Student discount applied: 10% off!!!");
            total -= total*STUDENT_DISCOUNT;
        }
        return total;
    }

    public boolean isValidCode(String code) {
        return code.equalsIgnoreCase("STUDENT") || code.isBlank();
    }
}
