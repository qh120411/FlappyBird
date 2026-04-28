public enum Difficulty {
    EASY("De", 108, 2, 195, 0.34, -8.2, 6.8, 0),
    MEDIUM("Trung binh", 94, 3, 170, 0.42, -8.8, 8.0, 0),
    HARD("Kho", 82, 4, 145, 0.50, -9.1, 9.2, 1);

    private final String label;
    private final int pipeDelay;
    private final int pipeSpeed;
    private final int pipeGap;
    private final double gravity;
    private final double flapVelocity;
    private final double maxFallSpeed;
    private final int wrongAnswerPenalty;

    Difficulty(
        String label,
        int pipeDelay,
        int pipeSpeed,
        int pipeGap,
        double gravity,
        double flapVelocity,
        double maxFallSpeed,
        int wrongAnswerPenalty
    ) {
        this.label = label;
        this.pipeDelay = pipeDelay;
        this.pipeSpeed = pipeSpeed;
        this.pipeGap = pipeGap;
        this.gravity = gravity;
        this.flapVelocity = flapVelocity;
        this.maxFallSpeed = maxFallSpeed;
        this.wrongAnswerPenalty = wrongAnswerPenalty;
    }

    public String getLabel() {
        return label;
    }

    public int getPipeDelay() {
        return pipeDelay;
    }

    public int getPipeSpeed() {
        return pipeSpeed;
    }

    public int getPipeGap() {
        return pipeGap;
    }

    public double getGravity() {
        return gravity;
    }

    public double getFlapVelocity() {
        return flapVelocity;
    }

    public double getMaxFallSpeed() {
        return maxFallSpeed;
    }

    public int getWrongAnswerPenalty() {
        return wrongAnswerPenalty;
    }

    public static Difficulty fromString(String value) {
        if (value != null) {
            for (Difficulty difficulty : values()) {
                if (difficulty.name().equalsIgnoreCase(value) || difficulty.label.equalsIgnoreCase(value)) {
                    return difficulty;
                }
            }
        }
        return MEDIUM;
    }

    @Override
    public String toString() {
        return label;
    }
}
