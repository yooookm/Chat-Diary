package com.example.diary_chat.exception;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {

    HttpStatus getHttpStatus();
    String getErrorMessage();
}
