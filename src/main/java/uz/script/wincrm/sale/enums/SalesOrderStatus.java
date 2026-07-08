package uz.script.wincrm.sale.enums;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public enum SalesOrderStatus {
    NEW,
    CONFIRMED,
    PROCESSING,
    DELIVERED,
    COMPLETED,
    CANCELLED;

    private static final Map<SalesOrderStatus, Set<SalesOrderStatus>> TRANSITIONS = new EnumMap<>(SalesOrderStatus.class);

    static {
        TRANSITIONS.put(NEW, EnumSet.of(CONFIRMED, CANCELLED));
        TRANSITIONS.put(CONFIRMED, EnumSet.of(PROCESSING, CANCELLED));
        TRANSITIONS.put(PROCESSING, EnumSet.of(DELIVERED, CANCELLED));
        TRANSITIONS.put(DELIVERED, EnumSet.of(COMPLETED, CANCELLED));
        TRANSITIONS.put(COMPLETED, EnumSet.noneOf(SalesOrderStatus.class));
        TRANSITIONS.put(CANCELLED, EnumSet.noneOf(SalesOrderStatus.class));
    }

    public boolean canTransitionTo(SalesOrderStatus target) {
        if (target == null) {
            return false;
        }
        return TRANSITIONS.getOrDefault(this, EnumSet.noneOf(SalesOrderStatus.class))
                .contains(target);
    }

    public boolean isFinal() {
        return this == COMPLETED || this == CANCELLED;
    }
}