package com.example.knowledge_service.bookmark.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.knowledge_service.bookmark.domain.Bookmark;

public interface BookmarkRepository extends JpaRepository<Long, Bookmark>{
    
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

    
}
