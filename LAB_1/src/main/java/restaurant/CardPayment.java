package restaurant;

public class CardPayment implements PaymentMethod {
    @Override
    public void pay(double amount){
        System.out.println("Paid lei " + amount + " with card. Thank you!");
    }
}
