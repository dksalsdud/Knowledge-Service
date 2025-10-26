package com.example.knowledge_service.todolist.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    @Builder.Default
    private TodoComplete complete = TodoComplete.FAIL;

    // 시작 날짜
    @Column(nullable = false)
    private LocalDateTime startDay;

    // 끝나는 날짜
    @Column(nullable = false)
    private LocalDateTime endDay;
}
