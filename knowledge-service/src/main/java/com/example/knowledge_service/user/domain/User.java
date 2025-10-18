package com.example.knowledge_service.user.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.knowledge_service.bookmark.domain.Bookmark;
import com.example.knowledge_service.note.domain.Note;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String userPw;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String nickname;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private LocalDateTime createdAt;

     /**
     * 엔티티 저장 전 실행
     * 생성 시간을 현재 시간으로 초기화
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ✅ 필수: 사용자가 작성한 모든 노트
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<Note> notes = new ArrayList<>();

    // ✅ 필수: 사용자가 북마크한 모든 북마크
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<Bookmark> bookmarks = new ArrayList<>();
}
