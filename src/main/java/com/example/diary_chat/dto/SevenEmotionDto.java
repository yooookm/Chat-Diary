package com.example.diary_chat.dto;

import com.example.diary_chat.domain.Emotion;
import java.time.LocalDate;

public record SevenEmotionDto(Long userId, LocalDate date, Emotion emotion) {
}
