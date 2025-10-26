package com.example.knowledge_service.todolist.service;

import org.springframework.stereotype.Service;

import com.example.knowledge_service.todolist.repository.TodoListRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoListService {

    private final TodoListRepository todoListRepository;
}
