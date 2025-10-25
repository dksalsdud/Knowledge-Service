package com.example.knowledge_service.note.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.knowledge_service.note.domain.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    
}
