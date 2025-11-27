package domain.observer.publisher;

import domain.models.Order;
import domain.observer.subscribers.OrderStatusObserver;

import java.util.ArrayList;
import java.util.List;

public class OrderSubject {
    private List<OrderStatusObserver> observers;
    private String currentStatus;

    public OrderSubject() {
        this.observers = new ArrayList<>();
        this.currentStatus = "CREATED";
    }

    public void attach(OrderStatusObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void detach(OrderStatusObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Order order) {
        System.out.println("\n Notifying " + observers.size() + " observers about status: " + currentStatus);
        System.out.println("-".repeat(50));
        List<OrderStatusObserver> observersCopy = new ArrayList<>(observers);

        for (OrderStatusObserver observer : observersCopy) {
            observer.update(currentStatus, order);
        }

        System.out.println("-".repeat(50));
    }

    public void setStatus(String newStatus, Order order) {
        System.out.println("\n Order status changed: " + currentStatus + " → " + newStatus);
        this.currentStatus = newStatus;
        notifyObservers(order);
    }

    public String getCurrentStatus() {
        return currentStatus;
    }
}