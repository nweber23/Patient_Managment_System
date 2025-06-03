public enum PatientType {
    EMERGENCY(1, "[EMERGENCY]", Colors.RED, "Emergency Queue"),
    SENIOR(2, "[SENIOR]", Colors.ORANGE, "Senior Queue"),
    REGULAR(3, "[REGULAR]", Colors.BLUE, "Regular Queue");

    private final int priority;
    private final String icon;
    private final String color;
    private final String queueName;

    PatientType(int priority, String icon, String color, String queueName) {
        this.priority = priority;
        this.icon = icon;
        this.color = color;
        this.queueName = queueName;
    }

    public int getPriority() { return priority; }
    public String getIcon() { return icon; }
    public String getColor() { return color; }
    public String getQueueName() { return queueName; }
}