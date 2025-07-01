package com.ella.flow_mock.exception;

import com.ella.flow_mock.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleBizException(RuntimeException ex) {
        // 返回统一结构，code=1 或其它业务约定的错误码
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(1, ex.getMessage()));
    }

    // 可以加：兜底的Exception捕获
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        // 兜底的未知错误，code=500
        return ResponseEntity
                .status(500)
                .body(ApiResponse.error(500, "服务器开小差了~ " + ex.getMessage()));
    }
}
