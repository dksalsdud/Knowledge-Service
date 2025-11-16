package com.example.knowledge_service.notetag.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NotaTagDTO {
    
    private Long noteId;

    private Long tagId;
}
