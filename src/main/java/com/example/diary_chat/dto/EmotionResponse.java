package com.example.diary_chat.dto;

import lombok.Getter;

@Getter
public class EmotionResponse {
    private String emotion;

    public EmotionResponse(String emotion) {
        this.emotion = emotion;
    }
}
