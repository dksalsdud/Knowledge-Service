package com.example.knowledge_service.bookmark.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.knowledge_service.bookmark.domain.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>{
    
    /**
     * 특정 사용자의 모든 북마크를 최신순으로 조회 (노트 정보 포함)
     * Fetch Join을 사용하여 N+1 문제 방지
     * 
     * @param userId 사용자 ID
     * @return 북마크 목록 (최신순)
     */
    @Query("SELECT b FROM Bookmark b " +
           "JOIN FETCH b.note n " +
           "JOIN FETCH n.user " +
           "WHERE b.user.id = :userId " +
           "ORDER BY b.createdAt DESC")
    List<Bookmark> findAllByUserIdWithNote(@Param("userId") Long userId);

    /**
     * 특정 사용자의 북마크 개수 조회
     * 
     * @param userId 사용자 ID
     * @return 북마크 개수
     */
    Long countByUser_Id(Long userId);

    /**
     * 특정 사용자가 특정 노트를 북마크했는지 확인
     * 
     * @param userId 사용자 ID
     * @param noteId 노트 ID
     * @return 북마크 존재 여부
     */
    boolean existsByUser_IdAndNote_Id(Long userId, Long noteId);

    /**
     * 특정 사용자의 특정 노트 북마크 조회
     * 
     * @param userId 사용자 ID
     * @param noteId 노트 ID
     * @return 북마크 엔티티
     */
    Optional<Bookmark> findByUser_IdAndNote_Id(Long userId, Long noteId);
}