package com.example.knowledge_service.exception;

import lombok.Getter;

/**
 * 서비스 전반에서 공통적으로 사용하는 커스텀 예외 클래스.
 * ErrorCode(에러 유형)과 함께 던져서 전역 예외 처리기에서 일관되게 처리하도록 한다.
 */
@Getter
public class AppException extends RuntimeException {
    
    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public AppException(ErrorCode errorCode, String detailMessage) {
        super(detailMessage);
        this.errorCode = errorCode;
    }
}
