public class QueueConfig {
    // Queue capacity limits
    public static final int MAX_EMERGENCY_CAPACITY = 10;
    public static final int MAX_SENIOR_CAPACITY = 15;
    public static final int MAX_REGULAR_CAPACITY = 25;
    public static final int MAX_TOTAL_CAPACITY = 50;
    
    // Warning thresholds (percentage of capacity)
    public static final double WARNING_THRESHOLD = 0.8; // 80%
    public static final double CRITICAL_THRESHOLD = 0.95; // 95%
    
    public static int getMaxCapacityForType(PatientType type) {
        switch (type) {
            case EMERGENCY: return MAX_EMERGENCY_CAPACITY;
            case SENIOR: return MAX_SENIOR_CAPACITY;
            case REGULAR: return MAX_REGULAR_CAPACITY;
            default: return 0;
        }
    }
    
    public static String getCapacityStatus(int current, int max) {
        double percentage = (double) current / max;
        
        if (percentage >= CRITICAL_THRESHOLD) {
            return Colors.RED + Colors.BOLD + "CRITICAL" + Colors.RESET;
        } else if (percentage >= WARNING_THRESHOLD) {
            return Colors.YELLOW + Colors.BOLD + "WARNING" + Colors.RESET;
        } else {
            return Colors.GREEN + "NORMAL" + Colors.RESET;
        }
    }
    
    public static boolean isAtCapacity(int current, int max) {
        return current >= max;
    }
    
    public static boolean isNearCapacity(int current, int max) {
        return (double) current / max >= WARNING_THRESHOLD;
    }
}