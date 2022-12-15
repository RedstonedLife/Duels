package com.redstonedlife.duels.plugin.enums;

public enum DuelID {
    DIGIT_FOUR("4DIG"),
    DIGIT_SIX("6DIG"),
    HEX("HEX");

    String value;

    DuelID (String value) {
        this.value = value;
    }

    public static DuelID fromValue(String value) {
        return switch (value) {
            case "6DIG" -> DIGIT_SIX;
            case "HEX" -> HEX;
            default -> DIGIT_FOUR;
        };
    }
}
