package com.example.knowledge_service.note.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.knowledge_service.bookmark.domain.Bookmark;
import com.example.knowledge_service.user.domain.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 노트 엔티티
 * 사용자가 작성한 노트 정보를 저장하는 도메인 모델
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Note {
    
    /**
     * 노트 고유 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 노트 제목
     * 최대 100자까지 입력 가능
     */
    @Column(nullable = false, length = 100)
    private String title;

    /**
     * 노트 내용
     * 대용량 텍스트를 저장하기 위해 LONGTEXT 타입 사용
     */
    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    /**
     * 노트 생성 시간
     * 자동으로 현재 시간이 설정됨
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 노트 최종 수정 시간
     * 수정 시마다 자동으로 업데이트됨
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

     /**
     * 노트 작성자
     * User 엔티티와 다대일 관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

     /**
     * 엔티티 저장 전 실행
     * 생성 시간과 수정 시간을 현재 시간으로 초기화
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 엔티티 업데이트 전 실행
     * 수정 시간을 현재 시간으로 업데이트
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ✅ 필수: 사용자가 북마크한 모든 북마크
    @OneToMany(mappedBy = "note", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<Bookmark> bookmarks = new ArrayList<>();
}
