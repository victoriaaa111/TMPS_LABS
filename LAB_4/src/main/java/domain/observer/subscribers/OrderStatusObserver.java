package domain.observer.subscribers;

import domain.models.Order;

public interface OrderStatusObserver {
    void update(String status, Order order);
}