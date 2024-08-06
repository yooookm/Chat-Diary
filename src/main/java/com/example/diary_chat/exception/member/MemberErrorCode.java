package com.example.diary_chat.exception.member;

import com.example.diary_chat.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {

    //회원가입 예외처리
    MISSING_INFORMATION(HttpStatus.BAD_REQUEST, "정보를 모두 입력해주세요"),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "닉네임 중복"),
    INVALID_NICKNAME_LENGTH(HttpStatus.BAD_REQUEST, "닉네임은 2~10글자 사이여야 합니다"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이메일 중복"),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "유효한 이메일 형식이어야 합니다"),
    PASSWORD_TOO_SHORT(HttpStatus.BAD_REQUEST, "비밀번호를 6자리 이상으로 해주세요"),


    //로그인 예외처리
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 이메일"),
    INVALID_IN_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호 불일치");


    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getErrorMessage() {
        return message;
    }
}
