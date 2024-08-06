package com.example.diary_chat.domain;

public enum Emotion {
    ANGRY("화남"),
    HAPPY("행복"),
    EMBARRASSED("당황"),
    SAD("슬픔"),
    NEUTRAL("보통"),
    EXCITED("즐거움"),
    ANXIOUS("불안"),
    TIRED("힘듦");

    private final String korean;

    Emotion(String korean) {
        this.korean = korean;
    }

    public static Emotion fromKorean(String korean) {
        return switch (korean) {
            case "화남" -> ANGRY;
            case "불안" -> ANXIOUS;
            case "행복" -> HAPPY;
            case "즐거움" -> EXCITED;
            case "당황" -> EMBARRASSED;
            case "힘듦" -> TIRED;
            case "슬픔" -> SAD;
            case "보통" -> NEUTRAL;
            default -> NEUTRAL;
        };
    }

    @Override
    public String toString() {
        return this.korean;
    }
}
