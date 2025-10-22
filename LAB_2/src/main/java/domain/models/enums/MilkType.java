package domain.models.enums;

public enum MilkType {
    WHOLE("Whole Milk", 0.0),
    PISTACHIO("Pistachio Milk", 0.9),
    ALMOND("Almond Milk", 0.75),
    OAT("Oat Milk", 0.75),
    SOY("Soy Milk", 0.50),
    COCONUT("Coconut Milk", 0.50);

    private final String displayName;
    private final double extraCharge;

    MilkType(String displayName, double extraCharge) {
        this.displayName = displayName;
        this.extraCharge = extraCharge;
    }

    public String getDisplayName() { return displayName; }
    public double getExtraCharge() { return extraCharge; }
}