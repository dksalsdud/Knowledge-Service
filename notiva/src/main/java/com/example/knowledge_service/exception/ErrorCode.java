package com.example.knowledge_service.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 도메인별 에러 코드 정의
 * - 북마크, 노트, 유저 등 확장 가능
 * - 상태코드, 메시지, 도메인 명시
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    
    // 공통
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 잘못되었습니다."),

    // 유저 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),

    // 노트 관련
    NOTE_NOT_FOUND(HttpStatus.NOT_FOUND, "노트를 찾을 수 없습니다."),
    NOTE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "노트에 접근할 권한이 없습니다."),

    //투두리스트 관련
    TODOLIST_NOT_FOUND(HttpStatus.NOT_FOUND, "투두리스트를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
