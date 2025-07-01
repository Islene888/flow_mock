// src/main/java/com/ella/flow_mock/exception/GlobalExceptionHandler.java
package com.ella.flow_mock.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleBizException(RuntimeException ex) {
        // 业务异常一律返回 400
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }
}
