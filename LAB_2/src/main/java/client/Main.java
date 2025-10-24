package client;

import domain.factory.CoffeeShopConfig;
import domain.models.Coffee;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserInterface ui = new UserInterface(scanner);

        ui.displayWelcome();
        boolean hasLoyaltyDiscount = ui.handleLoyaltyProgram();

        List<Coffee> order = new ArrayList<>();
        boolean ordering = true;

        while (ordering) {
            Coffee coffee = ui.createCustomCoffee();
            if (coffee != null) {
                order.add(coffee);
                ui.displayDrinkSummary(coffee, hasLoyaltyDiscount);
            }
            ordering = ui.askYesNo("Would you like to add another drink?");
        }

        ui.displayFinalReceipt(order, hasLoyaltyDiscount);
        ui.displayGoodbye();

        scanner.close();
    }
}