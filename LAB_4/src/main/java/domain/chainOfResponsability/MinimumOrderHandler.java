package domain.chainOfResponsability;

import domain.models.Order;

public class MinimumOrderHandler extends OrderValidationHandler {
    private static final double MINIMUM_ORDER_AMOUNT = 3.0;

    public MinimumOrderHandler() {
        super("Minimum Order Check");
    }

    @Override
    protected boolean doValidation(Order order) {
        double total = order.getTotal();

        if (total < MINIMUM_ORDER_AMOUNT) {
            System.out.println("    Order below minimum amount!");
            System.out.println("   Current: $" + String.format("%.2f", total));
            System.out.println("   Minimum: $" + String.format("%.2f", MINIMUM_ORDER_AMOUNT));
            return false;
        }

        System.out.println("   ✓ Order meets minimum amount");
        return true;
    }
}
