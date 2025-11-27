package domain.strategy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MobilePaymentStrategy implements PaymentStrategy {
    private final String phoneNumber;
    private final String provider;
    private final String deviceId;
    private final boolean useBiometric;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public MobilePaymentStrategy(String phoneNumber, String provider, String deviceId) {
        this(phoneNumber, provider, deviceId, true);
    }

    public MobilePaymentStrategy(String phoneNumber, String provider, String deviceId, boolean useBiometric) {
        this.phoneNumber = phoneNumber;
        this.provider = provider;
        this.deviceId = deviceId;
        this.useBiometric = useBiometric;
    }

    @Override
    public boolean processPayment(double amount) {
        if (!validatePayment(amount)) {
            return false;
        }

        System.out.println("\n📱 Processing " + provider + " payment...");
        System.out.println("Phone: " + getMaskedPhoneNumber());
        System.out.println("Device: " + getMaskedDeviceId());
        System.out.println("Amount: $" + String.format("%.2f", amount));

        // Simulate sending to device
        System.out.println("\n Sending payment request to your device...");
        simulateDelay(1000);

        // Biometric or PIN authentication
        if (useBiometric) {
            System.out.println(" Please authenticate using fingerprint or Face ID");
        } else {
            System.out.println(" Please enter your device PIN");
        }

        System.out.print("Verifying");
        for (int i = 0; i < 3; i++) {
            simulateDelay(400);
            System.out.print(".");
        }
        System.out.println();

        System.out.println(" Payment authorized via " + provider + "!");
        System.out.println("Confirmation: " + generateConfirmationCode());

        return true;
    }

    @Override
    public String getPaymentReceipt(double amount) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("\n").append("=".repeat(40)).append("\n");
        receipt.append("         PAYMENT RECEIPT\n");
        receipt.append("=".repeat(40)).append("\n");
        receipt.append("Payment Method: ").append(provider).append("\n");
        receipt.append("Date: ").append(LocalDateTime.now().format(formatter)).append("\n");
        receipt.append("-".repeat(40)).append("\n");
        receipt.append("Phone:          ").append(getMaskedPhoneNumber()).append("\n");
        receipt.append("Device:         ").append(getMaskedDeviceId()).append("\n");
        receipt.append("Amount:         $").append(String.format("%7.2f", amount)).append("\n");
        receipt.append("Confirmation:   ").append(generateConfirmationCode()).append("\n");

        String authMethod = useBiometric ? "Biometric" : "PIN";
        receipt.append("Auth Method:    ").append(authMethod).append("\n");

        receipt.append("-".repeat(40)).append("\n");
        receipt.append("Status: COMPLETED\n");
        receipt.append("=".repeat(40)).append("\n");
        receipt.append("Receipt sent to ").append(getMaskedPhoneNumber()).append("\n");
        receipt.append("=".repeat(40)).append("\n");
        return receipt.toString();
    }

    @Override
    public String getPaymentMethodName() {
        return provider;
    }

    @Override
    public boolean validatePayment(double amount) {
        // Validate phone number (must be 10 digits)
        String digitsOnly = phoneNumber.replaceAll("[^0-9]", "");
        if (digitsOnly.length() != 10) {
            System.out.println(" Invalid phone number (must be 10 digits)");
            return false;
        }

        // Validate provider
        if (!isValidProvider(provider)) {
            System.out.println(" Unsupported payment provider");
            System.out.println("Supported: Apple Pay, Google Pay, Samsung Pay");
            return false;
        }

        // Validate device ID
        if (deviceId == null || deviceId.trim().length() < 8) {
            System.out.println(" Invalid device ID");
            return false;
        }

        // Validate amount
        if (amount <= 0) {
            System.out.println(" Invalid amount");
            return false;
        }

        // Check provider limit
        double limit = getProviderLimit(provider);
        if (amount > limit) {
            System.out.println(" Amount exceeds " + provider + " limit of $" +
                    String.format("%.2f", limit));
            return false;
        }

        return true;
    }

    private boolean isValidProvider(String provider) {
        if (provider == null) return false;

        String lower = provider.toLowerCase();
        return lower.contains("apple") ||
                lower.contains("google") ||
                lower.contains("samsung");
    }

    private double getProviderLimit(String provider) {
        String lower = provider.toLowerCase();
        if (lower.contains("apple") || lower.contains("google")) {
            return 5000.0;
        } else if (lower.contains("samsung")) {
            return 3000.0;
        }
        return 1000.0;
    }

    private String getMaskedPhoneNumber() {
        String digits = phoneNumber.replaceAll("[^0-9]", "");
        if (digits.length() >= 4) {
            return "(" + digits.substring(0, 3) + ") ***-" +
                    digits.substring(digits.length() - 4);
        }
        return "***-***-****";
    }

    private String getMaskedDeviceId() {
        if (deviceId != null && deviceId.length() > 8) {
            return deviceId.substring(0, 4) + "..." +
                    deviceId.substring(deviceId.length() - 4);
        }
        return "****...****";
    }

    private String generateConfirmationCode() {
        return String.format("%08d", (int)(Math.random() * 100000000));
    }

    private void simulateDelay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}