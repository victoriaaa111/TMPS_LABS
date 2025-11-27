package domain.strategy;

public interface PaymentStrategy {
    boolean processPayment(double amount);
    String getPaymentReceipt(double amount);
    String getPaymentMethodName();
    boolean validatePayment(double amount);
}