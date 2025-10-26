package com.example.knowledge_service.todolist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.knowledge_service.todolist.dto.TodoListDTO;
import com.example.knowledge_service.todolist.service.TodoListService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/todo")
public class TodoListController {

    private final TodoListService todoListService;

    /**
     * 투두리스트 생성 처리
     */
    @PostMapping("/create")
    public String postTodoList(@RequestParam(value = "userId") Long userId, @ModelAttribute TodoListDTO todoListDTO) {
        
        todoListService.createTodoList(userId, todoListDTO);
        
        return "redirect:/Notiva";
    }
    
}
