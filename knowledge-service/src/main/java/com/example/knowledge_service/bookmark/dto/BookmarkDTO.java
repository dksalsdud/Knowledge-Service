package com.example.knowledge_service.bookmark.dto;

import java.time.LocalDateTime;

import com.example.knowledge_service.bookmark.domain.Bookmark;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookmarkDTO {
    
    /**
     * 북마크 ID
     */
    private Long bookmarkId;
    
    /**
     * 노트 ID
     */
    private Long noteId;

    /**
     * 유저 ID
     */
    private Long userId;
    
    /**
     * 노트 제목
     */
    private String noteTitle;
    
    /**
     * 노트 내용 미리보기 (앞 100자)
     */
    private String notePreview;
    
    /**
     * 노트 작성자 이름
     */
    private String authorName;
    
    /**
     * 노트 작성 시간
     */
    private LocalDateTime noteCreatedAt;
    
    /**
     * 북마크 생성 시간
     */
    private LocalDateTime bookmarkedAt;

    /**
     * 엔티티를 DTO로 변환하는 정적 팩토리 메서드
     * @param bookmark 북마크 엔티티
     * @return 변환된 DTO
     */
    public static BookmarkDTO from(Bookmark bookmark) {
        String content = bookmark.getNote().getContent();
        String preview = content.length() > 100 
            ? content.substring(0, 100) + "..." 
            : content;
        
        return BookmarkDTO.builder()
                .bookmarkId(bookmark.getId())
                .noteId(bookmark.getNote().getId())
                .noteTitle(bookmark.getNote().getTitle())
                .notePreview(preview)
                .authorName(bookmark.getNote().getUser().getNickname())
                .noteCreatedAt(bookmark.getNote().getCreatedAt())
                .bookmarkedAt(bookmark.getCreatedAt())
                .build();
    }
}