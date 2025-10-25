package com.example.knowledge_service.note.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    /**
     * 노트 공개 여부
     * 노트 검색에 띄울지 말지를 결정하는 컬럼
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NoteVisibility visibility;

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
     * 노트 방문수
     * 노트 페이지를 조회 할 때마다 숫자가 늘어난다
     * 최초 생성시 기본 값 0
     */
    @Column(nullable = false)
    private long viewCount = 0L;
}
