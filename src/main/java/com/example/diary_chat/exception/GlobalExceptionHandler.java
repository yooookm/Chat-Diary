package com.example.diary_chat.exception;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import jakarta.persistence.EntityNotFoundException;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

    // 입력값 유효성 검사 -> 여러 개의 에러 메세지를 한번에 반환
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // DB 관련 오류 처리
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("데이터 무결성 오류가 발생했습니다.");
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccess(DataAccessException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("데이터베이스 처리 중 오류가 발생했습니다.");
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<String> handleEmptyResultDataAccess(EmptyResultDataAccessException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("요청한 데이터가 존재하지 않습니다.");
    }

    // 요청한 Id에 해당하는 데이터가 없을 때
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("요청한 데이터가 존재하지 않습니다 : " + ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<String> handleForbiddenException(ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleException(Exception ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
//    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 날짜 형식입니다.");
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleSecurityException(SecurityException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization 헤더가 필요합니다.");
    }

    @ExceptionHandler(AmazonS3Exception.class)
    public ResponseEntity<String> handleAmazonS3Exception(AmazonS3Exception ex) {
        if ("NoSuchKey".equals(ex.getErrorCode())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("요청한 파일이 존재하지 않습니다.");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("S3 서비스 오류가 발생했습니다.");
    }

    @ExceptionHandler(DiaryNotFoundException.class)
    public ResponseEntity<String> handleDiaryNotFound(DiaryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("해당 날짜에 일기가 없습니다. 대화하기 페이지로 이동합니다.");
    }
}
