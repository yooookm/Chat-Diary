package com.example.diary_chat.dto;

import com.example.diary_chat.domain.Emotion;

public record EmotionStatisticsDto(Emotion emotion, Long count) {
}
