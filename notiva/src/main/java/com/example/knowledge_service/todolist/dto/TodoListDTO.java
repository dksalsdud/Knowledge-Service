package com.example.knowledge_service.todolist.dto;

import java.time.LocalDateTime;

import com.example.knowledge_service.todolist.domain.TodoComplete;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TodoListDTO {

    private String titile;
    
    private TodoComplete complete;

    private LocalDateTime startDay;

    private LocalDateTime endDay;
}
