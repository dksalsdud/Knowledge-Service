package com.example.knowledge_service.note.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.knowledge_service.note.domain.Note;

/**
 * 노트 데이터 접근을 담당하는 리포지토리
 */
public interface NoteRepository extends JpaRepository<Note, Long>{

    /**
     * 특정 사용자의 모든 노트를 최신순으로 조회
     * @param userId 사용자 ID
     * @return 노트 목록 (최신순 정렬)
     */
    List<Note> findAllByUser_IdOrderByUpdatedAtDesc(Long userId);
}
