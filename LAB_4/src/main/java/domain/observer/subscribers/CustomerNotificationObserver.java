package domain.observer.subscribers;

import domain.models.Order;

public class CustomerNotificationObserver implements OrderStatusObserver {

    public CustomerNotificationObserver() {
    }

    @Override
    public void update(String status, Order order) {
        switch (status) {
            case "CONFIRMED":
                notifyOrderConfirmed(order);
                break;
            case "PREPARING":
                notifyOrderPreparing(order);
                break;
            case "READY":
                notifyOrderReady(order);
                break;
            case "COMPLETED":
                notifyOrderCompleted(order);
                break;
        }
    }

    private void notifyOrderConfirmed(Order order) {
        System.out.println("\n [Customer Notification]");
        System.out.println("✓ Order Confirmed!");
        System.out.println("Order #: " + order.getOrderId());
        System.out.println("Your order is being processed.");
    }

    private void notifyOrderPreparing(Order order) {
        System.out.println("\n [Customer Notification]");
        System.out.println("Your order #" + order.getOrderId() + " is being prepared.");
    }

    private void notifyOrderReady(Order order) {
        System.out.println("\n [Customer Notification]");
        System.out.println(" Your order #" + order.getOrderId() + " is ready for pickup!");
        System.out.println("Please come to the counter.");
    }

    private void notifyOrderCompleted(Order order) {
        System.out.println("\n [Customer Notification]");
        System.out.println("Thank you! Order #" + order.getOrderId() + " completed.");
        System.out.println("We hope you enjoyed your coffee! ");
    }
}