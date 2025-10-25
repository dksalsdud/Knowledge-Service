package com.example.knowledge_service.note.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
}
