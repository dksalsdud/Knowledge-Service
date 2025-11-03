package com.example.knowledge_service.tag.service;

import org.springframework.stereotype.Service;

import com.example.knowledge_service.tag.domain.Tag;
import com.example.knowledge_service.tag.repository.TagRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    
    /**
     * 태그 이름으로 태그 찾기 또는 생성
     * @param tagName 태그 이름
     * @return Tag 엔티티
     */
    @Transactional
    public Tag findOrCreateTag(String tagName) {
        // 공백 제거 및 소문자 변환
        String normalizedTagName = tagName.trim().toLowerCase();
        
        // 이미 존재하는 태그인지 확인
        return tagRepository.findByTagName(normalizedTagName)
                .orElseGet(() -> {
                    // 없으면 새로 생성
                    Tag newTag = Tag.builder()
                            .tagName(normalizedTagName)
                            .build();
                    return tagRepository.save(newTag);
                });
    }
}
