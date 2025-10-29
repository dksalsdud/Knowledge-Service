package com.example.knowledge_service.todolist.domain;

import java.time.LocalDateTime;

import com.example.knowledge_service.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class TodoList {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 제목
    @Column(nullable = false, length = 100)
    private String title;

    // 완수 여부
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TodoComplete complete = TodoComplete.FAIL;

    // 시작 날짜
    @Column(nullable = false)
    private LocalDateTime startDay;

    // 끝나는 날짜
    @Column(nullable = false)
    private LocalDateTime endDay;

    /**
     * 투두리스트 작성다
     * 유저는 여러개의 투두리스트를 갖을 수 있음
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
