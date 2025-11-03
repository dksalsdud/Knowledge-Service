package com.example.knowledge_service.tag.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.knowledge_service.tag.domain.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>{
    
    /**
     * 태그 이름으로 태그 찾기
     */
    Optional<Tag> findByTagName(String tagName);
}
