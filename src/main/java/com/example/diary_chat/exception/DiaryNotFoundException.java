package com.example.diary_chat.exception;

public class DiaryNotFoundException extends RuntimeException {
    public DiaryNotFoundException(String message) {
        super(message);
    }
}
