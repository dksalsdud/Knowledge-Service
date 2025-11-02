package com.example.knowledge_service.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.knowledge_service.tag.domain.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>{
    
}
