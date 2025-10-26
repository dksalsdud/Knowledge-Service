package com.example.knowledge_service.todolist.dto;

import java.time.LocalDateTime;

import com.example.knowledge_service.todolist.domain.TodoComplete;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoListDTO {

    private String title;
    
    private TodoComplete complete;

    private LocalDateTime startDay;

    private LocalDateTime endDay;
}
