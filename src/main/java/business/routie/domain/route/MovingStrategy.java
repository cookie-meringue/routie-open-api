package business.routie.domain.route;

public enum MovingStrategy {
    DRIVING,
    TRANSIT;

    public static MovingStrategy fromString(String strategy) {
        return switch (strategy.toUpperCase()) {
            case "DRIVING" -> DRIVING;
            case "TRANSIT" -> TRANSIT;
            default -> throw new IllegalArgumentException("Unknown moving strategy: " + strategy);
        };
    }
}
