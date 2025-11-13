package domain.builder;

import domain.models.Coffee;
import domain.models.Cappuccino;
import domain.models.enums.*;
import java.util.ArrayList;

public class DefaultCappuccinoBuilder implements Builder {

    @Override
    public Coffee build() {
        System.out.println("Building default Cappuccino configuration...");
        Coffee cappuccino = new Cappuccino();

        // Default configuration for Cappuccino
        cappuccino.setSize(Size.SMALL);          // Small is traditional
        cappuccino.setMilkType(MilkType.WHOLE);  // Whole milk (required)
        cappuccino.setShots(1);                  // Single shot
        cappuccino.setIced(false);               // Hot by default
        cappuccino.setExtras(new ArrayList<>());

        cappuccino.calculatePrice();

        System.out.println(" Default Cappuccino: Small, Hot, Whole Milk, 1 shot, no extras");
        return cappuccino;
    }
}