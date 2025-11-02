package com.example.knowledge_service.notetag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.knowledge_service.notetag.domain.NoteTag;

@Repository
public interface NoteTagRepository extends JpaRepository<NoteTag, Long> {
    
}
