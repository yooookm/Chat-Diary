package com.example.diary_chat.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatRequest {
    private String prompt;

    public ChatRequest(String prompt) {
        this.prompt = prompt;
    }
}
