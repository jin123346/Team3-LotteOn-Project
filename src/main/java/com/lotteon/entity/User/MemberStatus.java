package com.lotteon.entity.User;

public enum MemberStatus {
    ACTIVE("정상"),
    SUSPENDED("중지"),
    DORMANT("휴면"),
    WITHDRAWN("탈퇴");

    private final String displayName;

    MemberStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
