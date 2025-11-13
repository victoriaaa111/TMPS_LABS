package domain.builder;

import domain.models.Coffee;
import domain.models.Americano;
import domain.models.enums.*;
import java.util.ArrayList;

public class DefaultAmericanoBuilder implements Builder {

    @Override
    public Coffee build() {
        System.out.println("Building default Americano configuration...");
        Coffee americano = new Americano();

        // Default configuration for Americano
        americano.setSize(Size.SMALL);      // Small is standard
        americano.setMilkType(null);         // No milk
        americano.setShots(2);               // Double shot (default)
        americano.setIced(false);            // Hot by default
        americano.setExtras(new ArrayList<>());

        americano.calculatePrice();

        System.out.println(" Default Americano: Small, Hot, 2 shots, no extras");
        return americano;
    }
}