package com.example.knowledge_service.note.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.knowledge_service.note.domain.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    /**
     * 특정 사용자의 모든 노트를 최신순으로 조회
     * @param userId 사용자 ID
     * @return 노트 목록 (최신순 정렬)
     */
    List<Note> findAllByUser_IdOrderByUpdatedAtDesc(Long userId);
}
