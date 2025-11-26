package domain.strategy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;


public class CashPaymentStrategy implements PaymentStrategy {
    private final double cashTendered;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final double[] DENOMINATIONS = {20.0, 10.0, 5.0, 1.0, 0.25, 0.10, 0.05, 0.01};
    private static final String[] DENOMINATION_NAMES = {"$20", "$10", "$5", "$1", "$0.25", "$0.10", "$0.05", "$0.01"};

    public CashPaymentStrategy(double cashTendered) {
        this.cashTendered = cashTendered;
    }

    @Override
    public boolean processPayment(double amount) {
        if (!validatePayment(amount)) {
            System.out.println(" Insufficient cash! Need $" + String.format("%.2f", amount) +
                    " but received $" + String.format("%.2f", cashTendered));
            return false;
        }

        System.out.println("\n Processing cash payment...");
        System.out.println("Amount due: $" + String.format("%.2f", amount));
        System.out.println("Cash tendered: $" + String.format("%.2f", cashTendered));

        double change = cashTendered - amount;
        if (change > 0.005) {
            System.out.println("\n Change due: $" + String.format("%.2f", change));
            Map<String, Integer> changeBreakdown = calculateChange(change);

            if (!changeBreakdown.isEmpty()) {
                System.out.println("Change breakdown:");
                changeBreakdown.forEach((denom, count) ->
                        System.out.println("  " + denom + " x " + count)
                );
            }
        } else {
            System.out.println("\n No change due");
        }

        System.out.println(" Cash payment processed successfully!");
        return true;
    }

    @Override
    public String getPaymentReceipt(double amount) {
        double change = cashTendered - amount;

        StringBuilder receipt = new StringBuilder();
        receipt.append("\n").append("=".repeat(40)).append("\n");
        receipt.append("         PAYMENT RECEIPT\n");
        receipt.append("=".repeat(40)).append("\n");
        receipt.append("Payment Method: Cash\n");
        receipt.append("Date: ").append(LocalDateTime.now().format(formatter)).append("\n");
        receipt.append("-".repeat(40)).append("\n");
        receipt.append("Amount Due:     $").append(String.format("%7.2f", amount)).append("\n");
        receipt.append("Cash Tendered:  $").append(String.format("%7.2f", cashTendered)).append("\n");

        if (change > 0.005) {
            receipt.append("Change:         $").append(String.format("%7.2f", change)).append("\n");
        }

        receipt.append("-".repeat(40)).append("\n");
        receipt.append("Status: PAID\n");
        receipt.append("=".repeat(40)).append("\n");
        return receipt.toString();
    }

    @Override
    public String getPaymentMethodName() {
        return "Cash";
    }

    @Override
    public boolean validatePayment(double amount) {
        return cashTendered >= amount;
    }

    private Map<String, Integer> calculateChange(double change) {
        Map<String, Integer> breakdown = new LinkedHashMap<>();
        double remaining = Math.round(change * 100.0) / 100.0;

        for (int i = 0; i < DENOMINATIONS.length; i++) {
            if (remaining >= DENOMINATIONS[i] - 0.001) {
                int count = (int) (remaining / DENOMINATIONS[i]);
                if (count > 0) {
                    breakdown.put(DENOMINATION_NAMES[i], count);
                    remaining = Math.round((remaining - (count * DENOMINATIONS[i])) * 100.0) / 100.0;
                }
            }
        }

        return breakdown;
    }
}