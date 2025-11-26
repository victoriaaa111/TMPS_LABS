package domain.chainOfResponsability;

import domain.models.Order;

public class EmptyOrderHandler extends OrderValidationHandler {

    public EmptyOrderHandler() {
        super("Empty Order Check");
    }

    @Override
    protected boolean doValidation(Order order) {
        if (order.isEmpty()) {
            System.out.println("    Order is empty!");
            return false;
        }

        System.out.println("   ✓ Order contains items");
        return true;
    }
}