package com.example.knowledge_service.note.dto;


import com.example.knowledge_service.note.domain.NoteVisibility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 사용자가 입력하는 정보들을 담아 옮기는 용도
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoteDTO {

    private String title;

    private String content;

    private NoteVisibility visibility;
}
