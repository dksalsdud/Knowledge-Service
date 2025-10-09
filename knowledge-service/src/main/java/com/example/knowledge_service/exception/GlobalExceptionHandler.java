package com.example.knowledge_service.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 전체 Controller 단에서 발생하는 예외를 공통으로 처리.
 * Thymeleaf 기반이라, JSON 응답이 아닌 redirect 방식으로 처리.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 우리가 정의한 AppException을 전역에서 처리한다.
     * 예: 서비스에서 throw new AppException(ErrorCode.NOTE_NOT_FOUND);
     */
    @ExceptionHandler(AppException.class)
    public String handleAppException(AppException ex, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        // FlashAttribute는 redirect 이후에도 1회성으로 전달되는 데이터
        redirectAttributes.addFlashAttribute("errorMessage", ex.getErrorCode().getMessage());

        String referer = request.getHeader("Referer");
        // redirect는 상황에 맞게 조정 가능 (여기선 홈으로)
        return "redirect:" + (referer != null ? referer : "/");
    }

    /**
     * 그 외 모든 예외 (NullPointerException 등)
     * 예기치 못한 서버 오류를 잡아 UI로 안내
     */
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        redirectAttributes.addFlashAttribute("errorMessage", "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}
