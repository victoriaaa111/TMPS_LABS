package domain.chainOfResponsability;

import domain.models.Order;

public abstract class OrderValidationHandler {
    protected OrderValidationHandler nextHandler;
    protected String handlerName;

    public OrderValidationHandler(String handlerName) {
        this.handlerName = handlerName;
    }

    public void setNext(OrderValidationHandler handler) {
        this.nextHandler = handler;
    }

    public boolean validate(Order order) {
        System.out.println(" [" + handlerName + "] Validating...");

        boolean isValid = doValidation(order);

        if (isValid) {
            System.out.println("   ✓ " + handlerName + " - PASSED");
            return passToNext(order);
        } else {
            System.out.println("   ✗ " + handlerName + " - FAILED");
            return false;
        }
    }

    protected abstract boolean doValidation(Order order);

    protected boolean passToNext(Order order) {
        if (nextHandler != null) {
            return nextHandler.validate(order);
        }
        return true;
    }
}