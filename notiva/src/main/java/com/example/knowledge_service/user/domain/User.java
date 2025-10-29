package com.example.knowledge_service.user.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // ✅ 유저가 삭제되면 관련된 노트도 모두 삭제됨
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<Note> notes = new ArrayList<>();

    /**
     * 엔티티 저장 전 실행
     * 생성 시간을 현재 시간으로 초기화
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
