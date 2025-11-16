package com.example.knowledge_service.tag.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.knowledge_service.tag.domain.Tag;
import com.example.knowledge_service.tag.repository.TagRepository;

import lombok.RequiredArgsConstructor;

/**
 * TagService
 * 정의 : Tag 관련하여 CRUD 관리
 * 태그추가, 태그변경, 태그삭제, 태그가져오는 기능 구현
 * 태그 서비스 단을 노트서비스단에 의존성을 주입하여 사용하여 함수로 사용할 수 있도록 개발
 */
@Service
@RequiredArgsConstructor
public class TagService {

    private final TagFilterService tagFilterService;
    private final TagRepository tagRepository;

    /**
     * 태그 이름 목록을 받아서 DB에 없는 태그는 새로 만들고,
     * 최종적으로 Tag 엔티티 리스트를 리턴한다.
     */
    @Transactional
    public List<Tag> saveOrFindTags(List<String> tagNames) {

        // 1) 태그 이름 정규화
        List<String> normalized = tagFilterService.normalizeTags(tagNames);

        List<Tag> result = new ArrayList<>();

        for (String name : normalized) {
            Tag tag = tagRepository.findByTagName(name)
                    .orElseGet(() -> tagRepository.save(
                            Tag.builder().tagName(name).build()
                    ));
        
            result.add(tag);
        }
        return result;
    }
}
