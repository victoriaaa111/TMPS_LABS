package domain.builder;

import domain.models.Coffee;
import domain.models.Latte;
import domain.models.enums.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DefaultLatteBuilder implements Builder {

    @Override
    public Coffee build() {
        System.out.println("Building default Latte configuration...");
        Coffee latte = new Latte();

        // Default configuration for Latte
        latte.setSize(Size.SMALL);          // Medium is popular
        latte.setMilkType(MilkType.WHOLE);   // Whole milk (required)
        latte.setShots(1);                   // Single shot
        latte.setIced(false);                // Hot by default
        latte.setExtras(new ArrayList<>()); // no extras

        latte.calculatePrice();

        System.out.println(" Default Latte: Small, Hot, Whole Milk, 1 shot, no extras");
        return latte;
    }
}