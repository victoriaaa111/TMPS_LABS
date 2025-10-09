package restaurant;

public class CashPayment implements PaymentMethod{
    @Override
    public void pay(double amount){
        System.out.println("Paid lei " + amount + " in cash. Thank you!");
    }
}
