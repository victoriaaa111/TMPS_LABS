package domain.builder;

import domain.models.Coffee;
import domain.models.Espresso;
import domain.models.enums.*;

public class DefaultEspressoBuilder implements Builder{

    @Override
    public Coffee build() {
        System.out.println("Building default Espresso configuration...");
        Coffee espresso = new Espresso();

        // Default configuration for Espresso
        espresso.setSize(Size.SMALL);        // Small is traditional
        espresso.setMilkType(null);          // No milk
        espresso.setShots(1);                // Single shot
        espresso.setIced(false);             // Hot only
        espresso.setExtras(new java.util.ArrayList<>());  // No extras

        espresso.calculatePrice();

        System.out.println(" Default Espresso: Small, Hot, 1 shot, no extras");
        return espresso;
    }
}