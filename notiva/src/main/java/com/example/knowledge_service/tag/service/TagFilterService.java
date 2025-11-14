package com.example.knowledge_service.tag.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * TagFilerService
 * 정의 : 프론트단에서 보내는 태그들을 데이터베이스에 넣기 전 정렬화 작업
 * TagService에 의존성 주입되어 함수로 호출 및 사용
 */
@Service
@RequiredArgsConstructor
public class TagFilterService {
    
    /**
     * 전달된 태그 문자열 리스트를 정규화(Normalization) 하는 함수
     * - null 제거
     * - 앞뒤 공백 제거
     * - 연속 공백 제거
     * - 소문자 통일
     * - 빈 문자열 제거
     * - 중복 제거
     */
    public List<String> normalizeTags(List<String> rawTags) {
        if (rawTags == null || rawTags.isEmpty()) {
            return Collections.emptyList();
        }

        return rawTags.stream()
                .filter(Objects::nonNull) // null 제거
                .map(String::trim)        // 앞뒤 공백 제거
                .map(tag -> tag.replaceAll("\\s+", " ")) // 내부 공백 정리
                .map(String::toLowerCase) // 소문자 통일
                .filter(tag -> !tag.isEmpty()) // 빈 문자열 제거
                .distinct() // 중복 제거
                .toList();
    }
}
