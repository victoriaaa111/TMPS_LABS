package domain.strategy;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class CardPaymentStrategy implements PaymentStrategy {
    private String cardNumber;
    private String cardHolderName;
    private String cvv;
    private String expiryDate;
    private CardType cardType;
    private String authorizationCode;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public CardPaymentStrategy(String cardNumber, String cardHolderName, String cvv, String expiryDate) {
        this.cardNumber = cardNumber.replaceAll("\\s", "");
        this.cardHolderName = cardHolderName;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
        this.cardType = detectCardType(this.cardNumber);
    }

    @Override
    public boolean processPayment(double amount) {
        if (!validatePayment(amount)) {
            return false;
        }

        System.out.println("\n Processing card payment...");
        System.out.println("Card Type: " + cardType.getName());
        System.out.println("Cardholder: " + cardHolderName);
        System.out.println("Card: " + getMaskedCardNumber());
        System.out.println("Amount: $" + String.format("%.2f", amount));

        // Simulate payment processing
        System.out.print("\nAuthorizing");
        this.authorizationCode = String.format("%06d", (int)(Math.random() * 1_000_000));

        System.out.println();

        System.out.println(" Card payment authorized!");
        System.out.println("Authorization Code: " + this.authorizationCode);
        return true;
    }

    @Override
    public String getPaymentReceipt(double amount) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("\n").append("=".repeat(40)).append("\n");
        receipt.append("         PAYMENT RECEIPT\n");
        receipt.append("=".repeat(40)).append("\n");
        receipt.append("Payment Method: ").append(cardType.getName()).append("\n");
        receipt.append("Date: ").append(LocalDateTime.now().format(formatter)).append("\n");
        receipt.append("-".repeat(40)).append("\n");
        receipt.append("Cardholder:     ").append(cardHolderName).append("\n");
        receipt.append("Card:           ").append(getMaskedCardNumber()).append("\n");
        receipt.append("Amount:         $").append(String.format("%7.2f", amount)).append("\n");
        receipt.append("Auth Code:      ").append(this.authorizationCode).append("\n");
        receipt.append("-".repeat(40)).append("\n");
        receipt.append("Status: APPROVED\n");
        receipt.append("=".repeat(40)).append("\n");
        return receipt.toString();
    }

    @Override
    public String getPaymentMethodName() {
        return cardType.getName();
    }

    @Override
    public boolean validatePayment(double amount) {
        // Validate card number length
        if (cardNumber.length() < 13 || cardNumber.length() > 19) {
            System.out.println(" Invalid card number length");
            return false;
        }

        // Validate CVV
        if (cvv == null || !cvv.matches("\\d{3,4}")) {
            System.out.println(" Invalid CVV (must be 3-4 digits)");
            return false;
        }

        // Validate expiry date
        if (!isValidExpiryDate(expiryDate)) {
            System.out.println(" Card has expired or invalid expiry date (use MM/YY format)");
            return false;
        }

        // Validate cardholder name
        if (cardHolderName == null || cardHolderName.trim().length() < 3) {
            System.out.println(" Invalid cardholder name");
            return false;
        }

        // Validate amount
        if (amount <= 0) {
            System.out.println(" Invalid amount");
            return false;
        }

        // Check transaction limit
        if (amount > cardType.getTransactionLimit()) {
            System.out.println(" Amount exceeds card limit of $" +
                    String.format("%.2f", cardType.getTransactionLimit()));
            return false;
        }

        return true;
    }

    private boolean isValidExpiryDate(String expiry) {
        if (expiry == null || !expiry.matches("\\d{2}/\\d{2}")) {
            return false;
        }

        try {
            String[] parts = expiry.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = 2000 + Integer.parseInt(parts[1]);

            if (month < 1 || month > 12) {
                return false;
            }

            YearMonth expiryYearMonth = YearMonth.of(year, month);
            return !expiryYearMonth.isBefore(YearMonth.now());
        } catch (Exception e) {
            return false;
        }
    }

    private CardType detectCardType(String number) {
        if (number.startsWith("1")) {
            return CardType.VISA;
        } else if (number.startsWith("2")) {
            return CardType.MASTERCARD;
        }
        return CardType.DEBIT;
    }

    private String getMaskedCardNumber() {
        if (cardNumber.length() >= 4) {
            return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
        }
        return "****";
    }

    private String generateAuthCode() {
        return String.format("%06d", (int)(Math.random() * 1000000));
    }

    public CardType getCardType() {
        return cardType;
    }
}