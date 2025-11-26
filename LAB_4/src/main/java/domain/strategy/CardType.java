package domain.strategy;

public enum CardType {
    VISA("Visa", 10.0),
    MASTERCARD("Mastercard", 7.0),
    DEBIT("Debit Card", 100.0);

    private final String name;
    private final double transactionLimit;

    CardType(String name, double transactionLimit) {
        this.name = name;
        this.transactionLimit = transactionLimit;
    }

    public String getName() {
        return name;
    }

    public double getTransactionLimit() {
        return transactionLimit;
    }
}