package com.example.diary_chat.exception.member;


import com.example.diary_chat.exception.BaseErrorCode;
import com.example.diary_chat.exception.CustomException;

public class MemberCustomException extends CustomException {

    public MemberCustomException(BaseErrorCode errorCode) {
        super(errorCode);
    }

    //회원가입 예외 처리

    public static class MissingInformationException extends MemberCustomException {
        public MissingInformationException() {
            super(MemberErrorCode.MISSING_INFORMATION);
        }
    }

    public static class DuplicatedNicknameException extends MemberCustomException {
        public DuplicatedNicknameException() {
            super(MemberErrorCode.DUPLICATE_NICKNAME);
        }
    }

    public static class InvalidNicknameLengthException extends MemberCustomException {
        public InvalidNicknameLengthException() {
            super(MemberErrorCode.INVALID_NICKNAME_LENGTH);
        }
    }

    public static class DuplicatedEmailException extends MemberCustomException {
        public DuplicatedEmailException() {
            super(MemberErrorCode.DUPLICATE_EMAIL);
        }
    }

    public static class InvalidEmailFormatException extends MemberCustomException {
        public InvalidEmailFormatException() {
            super(MemberErrorCode.INVALID_EMAIL_FORMAT);
        }
    }

    public static class PasswordTooShortException extends MemberCustomException {
        public PasswordTooShortException() {
            super(MemberErrorCode.PASSWORD_TOO_SHORT);
        }
    }

    //로그인 예외처리

    public static class EmailNotFoundException extends MemberCustomException {
        public EmailNotFoundException() {
            super(MemberErrorCode.EMAIL_NOT_FOUND);
        }
    }

    public static class IncorrectPasswordException extends MemberCustomException {
        public IncorrectPasswordException() {
            super(MemberErrorCode.INVALID_IN_PASSWORD);
        }
    }

}