package domain.chainOfResponsability;


import domain.models.Order;
import java.time.LocalTime;

public class WorkingHoursHandler extends OrderValidationHandler {
    private LocalTime openTime;
    private LocalTime closeTime;

    public WorkingHoursHandler() {
        super("Working Hours Check");
        this.openTime = LocalTime.of(0, 0);
        this.closeTime = LocalTime.of(23, 58);
    }

    @Override
    protected boolean doValidation(Order order) {
        LocalTime now = LocalTime.now();
        boolean isOpen = now.isAfter(openTime) && now.isBefore(closeTime);

        if (!isOpen) {
            System.out.println("    Shop is closed!");
            System.out.println("   Working hours: " + openTime + " - " + closeTime);
            System.out.println("   Current time: " + now);
        }

        return isOpen;
    }
}