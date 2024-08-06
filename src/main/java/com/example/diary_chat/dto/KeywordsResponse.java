package com.example.diary_chat.dto;

import lombok.Getter;

@Getter
public class KeywordsResponse {

    private String keyword1;
    private String keyword2;
    private String keyword3;

    public KeywordsResponse(String keyword1, String keyword2, String keyword3) {
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
        this.keyword3 = keyword3;
    }
}