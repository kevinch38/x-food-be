package com.enigma.x_food.shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> responseStatusException(ResponseStatusException e){
        CommonResponse commonResponse = CommonResponse.builder()
                .message(e.getReason())
                .statusCode(e.getRawStatusCode())
                .build();
        return ResponseEntity.status(e.getStatus()).body(commonResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> constraintViolationException(ConstraintViolationException e){
        CommonResponse commonResponse = CommonResponse.builder()
                .message(e.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResponse);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CommonResponse<T> {
        private String message;
        private Integer statusCode;
        private T data;
        private PagingResponse paging;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PagingResponse {
        private Integer totalPages;
        private Long count;
        private Integer page;
        private Integer size;
    }
}
