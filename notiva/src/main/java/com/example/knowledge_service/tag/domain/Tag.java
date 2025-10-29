package com.example.knowledge_service.tag.domain;

import java.util.ArrayList;
import java.util.List;

import com.example.knowledge_service.notetag.domain.NoteTag;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class Tag {
    
    /**
     * 태그 고유 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 태그 이름
     */
    @Column(nullable = false, length = 50)
    private String tagName;

    /**
     * 태그 Cascadetype 추가
     * 노트가 생성되기 전 만들때 같이만들 수 있고 노트 삭제시 해당 태그들 삭제
     * 
     * 고아 객체 삭제 설정 추가
     * 노트와 연결이 끊어진 태그들은 DB에서 자동 삭제
     */
    @Builder.Default
    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoteTag> noteTags = new ArrayList<>();

}
