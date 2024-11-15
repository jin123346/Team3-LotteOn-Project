package com.lotteon.dto.User;

public enum Grade {
    VVIP(5),
    VIP(4),
    GOLD(3),
    SILVER(2),
    FAMILY(1);

    private final int accumulationRate;

    Grade(int accumulationRate) {
        this.accumulationRate = accumulationRate;
    }

    public int getAccumulationRate() {
        return accumulationRate;
    }
}
